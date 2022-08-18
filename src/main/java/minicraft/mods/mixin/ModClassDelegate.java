package minicraft.mods.mixin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.cert.Certificate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.tinylog.Logger;

import minicraft.mods.GameTransformer;
import minicraft.mods.LoaderInitialization;
import minicraft.mods.Mods;

final public class ModClassDelegate {
	static final class Metadata {
		static final Metadata EMPTY = new Metadata(null, null);

		final Manifest manifest;
		final CodeSource codeSource;

		Metadata(Manifest manifest, CodeSource codeSource) {
			this.manifest = manifest;
			this.codeSource = codeSource;
		}
	}

	private static final ClassLoader PLATFORM_CLASS_LOADER = getPlatformClassLoader();

	private final Map<Path, Metadata> metadataCache = new ConcurrentHashMap<>();
	private final ModClassLoader classLoader;
	private final ClassLoader parentClassLoader;
	private IMixinTransformer mixinTransformer;
	private boolean transformInitialized = false;
	private volatile Set<Path> codeSources = Collections.emptySet();
	private volatile Set<Path> validParentCodeSources = Collections.emptySet();
	private final Map<Path, String[]> allowedPrefixes = new ConcurrentHashMap<>();
	private final Set<String> parentSourcedClasses = Collections.newSetFromMap(new ConcurrentHashMap<>());

	ModClassDelegate(ModClassLoader classLoader, ClassLoader parentClassLoader) {
		this.classLoader = classLoader;
		this.parentClassLoader = parentClassLoader;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public void initializeTransformers() {
		if (transformInitialized) throw new IllegalStateException("Cannot initialize KnotClassDelegate twice!");

		mixinTransformer = ModMixinService.getTransformer();

		if (mixinTransformer == null) {
			try { // reflective instantiation for older mixin versions
				@SuppressWarnings("unchecked")
				Constructor<IMixinTransformer> ctor = (Constructor<IMixinTransformer>) Class.forName("org.spongepowered.asm.mixin.transformer.MixinTransformer").getConstructor();
				ctor.setAccessible(true);
				mixinTransformer = ctor.newInstance();
			} catch (ReflectiveOperationException e) {
				Logger.debug("Can't create Mixin transformer through reflection (only applicable for 0.8-0.8.2): %s", e);

				// both lookups failed (not received through IMixinService.offer and not found through reflection)
				throw new IllegalStateException("mixin transformer unavailable?");
			}
		}

		transformInitialized = true;
	}

	private IMixinTransformer getMixinTransformer() {
		assert mixinTransformer != null;
		return mixinTransformer;
	}

	public void addCodeSource(Path path) {
		try {
			path = path.toRealPath();
		} catch (IOException e1) {
			throw new UncheckedIOException(e1);
		}

		synchronized (this) {
			Set<Path> codeSources = this.codeSources;
			if (codeSources.contains(path)) return;

			Set<Path> newCodeSources = new HashSet<>(codeSources.size() + 1, 1);
			newCodeSources.addAll(codeSources);
			newCodeSources.add(path);

			this.codeSources = newCodeSources;
		}

		try {
			classLoader.addUrlFwd(path.toUri().toURL());
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}

		Logger.info("added code source %s", path);
	}

	public void setAllowedPrefixes(Path codeSource, String... prefixes) {
		try {
			codeSource = codeSource.toRealPath();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}

		if (prefixes.length == 0) {
			allowedPrefixes.remove(codeSource);
		} else {
			allowedPrefixes.put(codeSource, prefixes);
		}
	}

	public void setValidParentClassPath(Collection<Path> paths) {
		Set<Path> validPaths = new HashSet<>(paths.size(), 1);

		for (Path path : paths) {
			try {
				validPaths.add(path.toRealPath());
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}

		this.validParentCodeSources = validPaths;
	}

	public Manifest getManifest(Path codeSource) {
		try {
			return getMetadata(codeSource.toRealPath()).manifest;
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public boolean isClassLoaded(String name) {
		synchronized (classLoader.getClassLoadingLockFwd(name)) {
			return classLoader.findLoadedClassFwd(name) != null;
		}
	}

	public Class<?> loadIntoTarget(String name) throws ClassNotFoundException {
		synchronized (classLoader.getClassLoadingLockFwd(name)) {
			Class<?> c = classLoader.findLoadedClassFwd(name);

			if (c == null) {
				c = tryLoadClass(name, true);

				if (c == null) {
					throw new ClassNotFoundException("can't find class "+name);
				} else {
					Logger.info("loaded class {} into target", name);
				}
			}

			classLoader.resolveClassFwd(c);

			return c;
		}
	}

	Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		synchronized (classLoader.getClassLoadingLockFwd(name)) {
			Class<?> c = classLoader.findLoadedClassFwd(name);

			if (c == null) {
				if (name.startsWith("java.")) { // fast path for java.** (can only be loaded by the platform CL anyway)
					c = PLATFORM_CLASS_LOADER.loadClass(name);
				} else {
					c = tryLoadClass(name, false); // try local load

					if (c == null) { // not available locally, try system class loader
						String fileName = name.replace('.', '/').concat(".class");
						URL url = parentClassLoader.getResource(fileName);

						if (url == null) { // no .class file
							try {
								c = PLATFORM_CLASS_LOADER.loadClass(name);
								Logger.info("loaded resources-less class {} from platform class loader");
							} catch (ClassNotFoundException e) {
								Logger.warn("can't find class {}", name);
								throw e;
							}
						} else if (!isValidParentUrl(url, fileName)) { // available, but restricted
							// The class would technically be available, but the game provider restricted it from being
							// loaded by setting validParentUrls and not including "url". Typical causes are:
							// - accessing classes too early (game libs shouldn't be used until Loader is ready)
							// - using jars that are only transient (deobfuscation input or pass-through installers)
							String msg = String.format("can't load class {} at {} as it hasn't been exposed to the game",
									name, getCodeSource(url, fileName));
							Logger.warn(msg);
							throw new ClassNotFoundException(msg);
						} else { // load from system cl
							if (Mods.logClassLoad) Logger.trace("loading class {} using the parent class loader", name);
							c = parentClassLoader.loadClass(name);
						}
					} else {
						if (Mods.logClassLoad) Logger.trace("loaded class {}", name);
					}
				}
			}

			if (resolve) {
				classLoader.resolveClassFwd(c);
			}

			return c;
		}
	}

	/**
	 * Check if an url is loadable by the parent class loader.
	 *
	 * <p>This handles explicit parent url whitelisting by {@link #validParentCodeSources} or shadowing by {@link #codeSources}
	 */
	private boolean isValidParentUrl(URL url, String fileName) {
		if (url == null) return false;
		if (!hasRegularCodeSource(url)) return true;

		Path codeSource = getCodeSource(url, fileName);
		Set<Path> validParentCodeSources = this.validParentCodeSources;

		if (validParentCodeSources != null) { // explicit whitelist (in addition to platform cl classes)
			return validParentCodeSources.contains(codeSource) || PLATFORM_CLASS_LOADER.getResource(fileName) != null;
		} else { // reject urls shadowed by this cl
			return !codeSources.contains(codeSource);
		}
	}

	Class<?> tryLoadClass(String name, boolean allowFromParent) throws ClassNotFoundException {
		if (name.startsWith("java.")) {
			return null;
		}

		if (!allowedPrefixes.isEmpty()) { // check prefix restrictions (allows exposing libraries partially during startup)
			String fileName = name.replace('.', '/').concat(".class");
			URL url = classLoader.getResource(fileName);

			if (url != null && hasRegularCodeSource(url)) {
				Path codeSource = getCodeSource(url, fileName);
				String[] prefixes = allowedPrefixes.get(codeSource);

				if (prefixes != null) {
					assert prefixes.length > 0;
					boolean found = false;

					for (String prefix : prefixes) {
						if (name.startsWith(prefix)) {
							found = true;
							break;
						}
					}

					if (!found) {
						String msg = "class "+name+" is currently restricted from being loaded";
						Logger.warn(msg);
						throw new ClassNotFoundException(msg);
					}
				}
			}
		}

		if (!allowFromParent && !parentSourcedClasses.isEmpty()) { // propagate loadIntoTarget behavior to its nested classes
			int pos = name.length();

			while ((pos = name.lastIndexOf('$', pos - 1)) > 0) {
				if (parentSourcedClasses.contains(name.substring(0, pos))) {
					allowFromParent = true;
					break;
				}
			}
		}

		byte[] input = getPostMixinClassByteArray(name, allowFromParent);
		if (input == null) return null;

		if (allowFromParent) {
			parentSourcedClasses.add(name);
		}

		ModClassDelegate.Metadata metadata = getMetadata(name);

		int pkgDelimiterPos = name.lastIndexOf('.');

		if (pkgDelimiterPos > 0) {
			// TODO: package definition stub
			String pkgString = name.substring(0, pkgDelimiterPos);

			if (classLoader.getPackageFwd(pkgString) == null) {
				try {
					classLoader.definePackageFwd(pkgString, null, null, null, null, null, null, null);
				} catch (IllegalArgumentException e) { // presumably concurrent package definition
					if (classLoader.getPackageFwd(pkgString) == null) throw e; // still not defined?
				}
			}
		}

		return classLoader.defineClassFwd(name, input, 0, input.length, metadata.codeSource);
	}

	private Metadata getMetadata(String name) {
		String fileName = name.replace('.', '/').concat(".class");
		URL url = classLoader.getResource(fileName);
		if (url == null || !hasRegularCodeSource(url)) return Metadata.EMPTY;

		return getMetadata(getCodeSource(url, fileName));
	}

	private Metadata getMetadata(Path codeSource) {
		return metadataCache.computeIfAbsent(codeSource, (Path path) -> {
			Manifest manifest = null;
			CodeSource cs = null;
			Certificate[] certificates = null;

			try (JarFile f = new JarFile(path.toFile())) {
				manifest = f.getManifest();
			} catch (IOException e) {
				if (LoaderInitialization.isDebug()) {
					Logger.warn("Failed to load manifest", e);
				}
			}

			if (cs == null) {
				try {
					cs = new CodeSource(path.toUri().toURL(), certificates);
				} catch (MalformedURLException e) {
					throw new RuntimeException(e);
				}
			}

			return new Metadata(manifest, cs);
		});
	}

	private byte[] getPostMixinClassByteArray(String name, boolean allowFromParent) {
		byte[] transformedClassArray = getPreMixinClassByteArray(name, allowFromParent);

		if (!transformInitialized || !canTransformClass(name)) {
			return transformedClassArray;
		}

		try {
			return getMixinTransformer().transformClassBytes(name, name, transformedClassArray);
		} catch (Throwable t) {
			String msg = String.format("Mixin transformation of %s failed", name);
			Logger.warn(msg, t);

			throw new RuntimeException(msg, t);
		}
	}

	public byte[] getPreMixinClassBytes(String name) {
		return getPreMixinClassByteArray(name, true);
	}

	/**
	 * Runs all the class transformers except mixin.
	 */
	private byte[] getPreMixinClassByteArray(String name, boolean allowFromParent) {
		// some of the transformers rely on dot notation
		name = name.replace('/', '.');

		if (!transformInitialized || !canTransformClass(name)) {
			try {
				return getRawClassByteArray(name, allowFromParent);
			} catch (IOException e) {
				throw new RuntimeException("Failed to load class file for '" + name + "'!", e);
			}
		}

		byte[] input = GameTransformer.getTransformed(name);

		if (input == null) {
			try {
				input = getRawClassByteArray(name, allowFromParent);
			} catch (IOException e) {
				throw new RuntimeException("Failed to load class file for '" + name + "'!", e);
			}
		}

		return input;
	}

	private static boolean canTransformClass(String name) {
		name = name.replace('/', '.');
		// Blocking Fabric Loader classes is no longer necessary here as they don't exist on the modding class loader
		return /* !"net.fabricmc.api.EnvType".equals(name) && !name.startsWith("net.fabricmc.loader.") && */ !name.startsWith("org.apache.logging.log4j");
	}

	public byte[] getRawClassBytes(String name) throws IOException {
		return getRawClassByteArray(name, true);
	}

	private byte[] getRawClassByteArray(String name, boolean allowFromParent) throws IOException {
		name = name.replace('.', '/').concat(".class");
		URL url = classLoader.findResourceFwd(name);

		if (url == null) {
			if (!allowFromParent) return null;

			url = parentClassLoader.getResource(name);

			if (!isValidParentUrl(url, name)) {
				Logger.info("refusing to load class %s at %s from parent class loader", name, getCodeSource(url, name));

				return null;
			}
		}

		try (InputStream inputStream = url.openStream()) {
			int a = inputStream.available();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream(a < 32 ? 32768 : a);
			byte[] buffer = new byte[8192];
			int len;

			while ((len = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, len);
			}

			return outputStream.toByteArray();
		}
	}

	private static boolean hasRegularCodeSource(URL url) {
		return url.getProtocol().equals("file") || url.getProtocol().equals("jar");
	}

	private static Path getCodeSource(URL url, String fileName) {
		try {
			return Paths.get(((JarURLConnection) url.openConnection()).getJarFileURL().toURI());
		} catch (URISyntaxException | IOException e) {
			throw new UndeclaredThrowableException(e);
		}
	}

	private static ClassLoader getPlatformClassLoader() {
		try {
			return (ClassLoader) ClassLoader.class.getMethod("getPlatformClassLoader").invoke(null); // Java 9+ only
		} catch (NoSuchMethodException e) {
			return new ClassLoader(null) { }; // fall back to boot cl
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	interface ClassLoaderAccess {
		void addUrlFwd(URL url);
		URL findResourceFwd(String name);

		Package getPackageFwd(String name);
		Package definePackageFwd(String name, String specTitle, String specVersion, String specVendor, String implTitle, String implVersion, String implVendor, URL sealBase) throws IllegalArgumentException;

		Object getClassLoadingLockFwd(String name);
		Class<?> findLoadedClassFwd(String name);
		Class<?> defineClassFwd(String name, byte[] b, int off, int len, CodeSource cs);
		void resolveClassFwd(Class<?> cls);
	}
}
