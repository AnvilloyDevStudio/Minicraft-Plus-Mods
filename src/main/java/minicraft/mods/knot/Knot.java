/*
 * Copyright 2016 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package minicraft.mods.knot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.jar.Manifest;

import minicraft.mods.FabricLauncherBase;
import minicraft.mods.FabricLoaderImpl;
import minicraft.mods.FabricMixinBootstrap;
import minicraft.mods.game.MiniModsGameProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.impl.FormattedException;
import net.fabricmc.loader.impl.entrypoint.EntrypointUtils;
import net.fabricmc.loader.impl.game.GameProvider;
import net.fabricmc.loader.impl.util.LoaderUtil;
import net.fabricmc.loader.impl.util.SystemProperties;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;

public final class Knot extends FabricLauncherBase {
	protected Map<String, Object> properties = new HashMap<>();

	private KnotClassLoaderInterface classLoader;
	private boolean isDevelopment;
	private EnvType envType;
	private final List<Path> classPath = new ArrayList<>();
	private GameProvider provider;
	private boolean unlocked;

	public static void launch(String[] args, EnvType type) {
		setupUncaughtExceptionHandler();

		try {
			Knot knot = new Knot(type);
			ClassLoader cl = knot.init(args);

			if (knot.provider == null) {
				throw new IllegalStateException("Game provider was not initialized! (Knot#init(String[]))");
			}

			knot.provider.launch(cl);
		} catch (FormattedException e) {
			handleFormattedException(e);
		}
	}

	public Knot(EnvType type) {
		this.envType = type;
	}

	protected ClassLoader init(String[] args) {
		setProperties(properties);

		// configure fabric vars
		if (envType == null) {
			String side = System.getProperty(SystemProperties.SIDE);
			if (side == null) throw new RuntimeException("Please specify side or use a dedicated Knot!");

			switch (side.toLowerCase(Locale.ROOT)) {
			case "client":
				envType = EnvType.CLIENT;
				break;
			case "server":
				envType = EnvType.SERVER;
				break;
			default:
				throw new RuntimeException("Invalid side provided: must be \"client\" or \"server\"!");
			}
		}

		System.setProperty(SystemProperties.SKIP_MC_PROVIDER, "true");
		for (String arg : args) {
			if (arg.toLowerCase().equals("--debug")) {
				System.setProperty(SystemProperties.LOG_LEVEL, "TRACE");
				System.setProperty(SystemProperties.DEVELOPMENT, "true");
			}
		}

		classPath.clear();

		List<String> missing = null;
		List<String> unsupported = null;

		for (String cpEntry : System.getProperty("java.class.path").split(File.pathSeparator)) {
			if (cpEntry.equals("*") || cpEntry.endsWith(File.separator + "*")) {
				if (unsupported == null) unsupported = new ArrayList<>();
				unsupported.add(cpEntry);
				continue;
			}

			Path path = Paths.get(cpEntry);

			if (!Files.exists(path)) {
				if (missing == null) missing = new ArrayList<>();
				missing.add(cpEntry);
				continue;
			}

			classPath.add(LoaderUtil.normalizeExistingPath(path));
		}

		if (unsupported != null) Log.warn(LogCategory.KNOT, "Knot does not support wildcard class path entries: %s - the game may not load properly!", String.join(", ", unsupported));
		if (missing != null) Log.warn(LogCategory.KNOT, "Class path entries reference missing files: %s - the game may not load properly!", String.join(", ", missing));

		// provider = createGameProvider(args);
		provider = new MiniModsGameProvider();
		provider.locateGame(this, args);
		Log.info(LogCategory.GAME_PROVIDER, "Loading %s %s with Fabric Loader %s", provider.getGameName(), provider.getRawGameVersion(), FabricLoaderImpl.VERSION);

		isDevelopment = Boolean.parseBoolean(System.getProperty(SystemProperties.DEVELOPMENT, "false"));

		// Setup classloader
		// TODO: Provide KnotCompatibilityClassLoader in non-exclusive-Fabric pre-1.13 environments?
		boolean useCompatibility = provider.requiresUrlClassLoader() || Boolean.parseBoolean(System.getProperty("fabric.loader.useCompatibilityClassLoader", "false"));
		classLoader = KnotClassLoaderInterface.create(useCompatibility, isDevelopment(), envType, provider);
		ClassLoader cl = classLoader.getClassLoader();

		provider.initialize(this);

		Thread.currentThread().setContextClassLoader(cl);

		FabricLoaderImpl loader = FabricLoaderImpl.INSTANCE;
		loader.setGameProvider(provider);
		loader.load();
		loader.freeze();

		FabricLoaderImpl.INSTANCE.loadAccessWideners();

		FabricMixinBootstrap.init(getEnvironmentType(), loader);
		FabricLauncherBase.finishMixinBootstrapping();

		classLoader.initializeTransformers();

		provider.unlockClassPath(this);
		unlocked = true;

		try {
			EntrypointUtils.invoke("preLaunch", PreLaunchEntrypoint.class, PreLaunchEntrypoint::onPreLaunch);
		} catch (RuntimeException e) {
			throw new FormattedException("A mod crashed on startup!", e);
		}

		return cl;
	}

	@Override
	public String getTargetNamespace() {
		// TODO: Won't work outside of Yarn
		return isDevelopment ? "named" : "intermediary";
	}

	@Override
	public List<Path> getClassPath() {
		return classPath;
	}

	@Override
	public void addToClassPath(Path path, String... allowedPrefixes) {
		Log.debug(LogCategory.KNOT, "Adding " + path + " to classpath.");

		classLoader.setAllowedPrefixes(path, allowedPrefixes);
		classLoader.addCodeSource(path);
	}

	@Override
	public void setAllowedPrefixes(Path path, String... prefixes) {
		classLoader.setAllowedPrefixes(path, prefixes);
	}

	@Override
	public void setValidParentClassPath(Collection<Path> paths) {
		classLoader.setValidParentClassPath(paths);
	}

	@Override
	public EnvType getEnvironmentType() {
		return envType;
	}

	@Override
	public boolean isClassLoaded(String name) {
		return classLoader.isClassLoaded(name);
	}

	@Override
	public Class<?> loadIntoTarget(String name) throws ClassNotFoundException {
		return classLoader.loadIntoTarget(name);
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		return classLoader.getClassLoader().getResourceAsStream(name);
	}

	@Override
	public ClassLoader getTargetClassLoader() {
		KnotClassLoaderInterface classLoader = this.classLoader;

		return classLoader != null ? classLoader.getClassLoader() : null;
	}

	@Override
	public byte[] getClassByteArray(String name, boolean runTransformers) throws IOException {
		if (!unlocked) throw new IllegalStateException("early getClassByteArray access");

		if (runTransformers) {
			return classLoader.getPreMixinClassBytes(name);
		} else {
			return classLoader.getRawClassBytes(name);
		}
	}

	@Override
	public Manifest getManifest(Path originPath) {
		return classLoader.getManifest(originPath);
	}

	@Override
	public boolean isDevelopment() {
		return isDevelopment;
	}

	@Override
	public String getEntrypoint() {
		return provider.getEntrypoint();
	}

	public static void main(String[] args) {
		new Knot(null).init(args);
	}

	static {
		LoaderUtil.verifyNotInTargetCl(Knot.class);
	}
}
