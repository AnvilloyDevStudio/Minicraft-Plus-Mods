package minicraft.mods;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.jar.Manifest;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.tinylog.Logger;

import minicraft.mods.mixin.ModClassDelegate;
import minicraft.mods.mixin.ModClassLoader;
import minicraft.mods.mixin.ModMixinBootstrap;

public class LoaderInitialization {
	private static ModClassDelegate classLoader;
	private static boolean unlocked;

	public static void main(String[] args) {
		// A copy from Game#main
		Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
			throwable.printStackTrace();

			StringWriter string = new StringWriter();
			PrintWriter printer = new PrintWriter(string);
			throwable.printStackTrace(printer);

			JTextArea errorDisplay = new JTextArea(string.toString());
			errorDisplay.setEditable(false);
			JScrollPane errorPane = new JScrollPane(errorDisplay);
			JOptionPane.showMessageDialog(null, errorPane, "An error has occurred", JOptionPane.ERROR_MESSAGE);

			System.exit(1);
		});

		parseArgs(args);
		Mods.init();

		Mods.launchGame(classLoader.getClassLoader(), args);
	}

	// A copy from Initailizer
	static void parseArgs(String[] args) {
		boolean debug = false;

		// Parses command line arguments
		String saveDir = Mods.systemGameDir;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("--debug")) {
				debug = true;
			} else if (args[i].equals("--savedir") && i+1 < args.length) {
				i++;
				saveDir = args[i];
			}
		}

		Mods.setDebug(debug);
		Mods.gameDir = saveDir + Mods.localGameDir;
		Mods.gameModsDir = saveDir + Mods.localGameDir + "/mods";
	}

	public static void init() {
		classLoader = new ModClassLoader().getDelegate();
		ClassLoader cl = classLoader.getClassLoader();

		try {
			addToClassPath(Path.of(LoaderInitialization.class.getProtectionDomain().getCodeSource().getLocation().toURI()));
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}

		GameTransformer.transform();

		Thread.currentThread().setContextClassLoader(cl);

		ModFindHander.findMods();

		ModMixinBootstrap.init();

		classLoader.initializeTransformers();

		try {
			addToClassPath(Path.of(LoaderInitialization.class.getProtectionDomain().getCodeSource().getLocation().toURI()));
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}

		unlocked = true;
	}

	public static void addToClassPath(Path path, String... allowedPrefixes) {
		Logger.debug("Adding " + path + " to classpath.");

		classLoader.setAllowedPrefixes(path, allowedPrefixes);
		classLoader.addCodeSource(path);
	}

	public static void setAllowedPrefixes(Path path, String... prefixes) {
		classLoader.setAllowedPrefixes(path, prefixes);
	}

	public static void setValidParentClassPath(Collection<Path> paths) {
		classLoader.setValidParentClassPath(paths);
	}

	public static boolean isClassLoaded(String name) {
		return classLoader.isClassLoaded(name);
	}

	public static Class<?> loadIntoTarget(String name) throws ClassNotFoundException {
		return classLoader.loadIntoTarget(name);
	}

	public static InputStream getResourceAsStream(String name) {
		return classLoader.getClassLoader().getResourceAsStream(name);
	}

	public static ClassLoader getTargetClassLoader() {
		ModClassDelegate classLoader = LoaderInitialization.classLoader;

		return classLoader != null ? classLoader.getClassLoader() : null;
	}

	public static byte[] getClassByteArray(String name, boolean runTransformers) throws IOException {
		if (!unlocked) throw new IllegalStateException("early getClassByteArray access");

		if (runTransformers) {
			return classLoader.getPreMixinClassBytes(name);
		} else {
			return classLoader.getRawClassBytes(name);
		}
	}

	public static Manifest getManifest(Path originPath) {
		return classLoader.getManifest(originPath);
	}

	public static boolean isDebug() {
		return Mods.debug;
	}
}
