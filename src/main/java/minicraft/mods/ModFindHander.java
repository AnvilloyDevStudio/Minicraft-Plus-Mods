package minicraft.mods;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

import org.tinylog.Logger;

public class ModFindHander {
	static void findMods() {
		File[] files = readModsFolder();
		if (files.length == 0) {
			ModLoadingHandler.secondaryPro = new ModLoadingHandler.Progress(1);
			ModLoadingHandler.secondaryPro.cur = 1;
			ModLoadingHandler.secondaryPro.text = "No Mods";
			Logger.info("No mods found.");
		} else {
			ModLoadingHandler.secondaryPro = new ModLoadingHandler.Progress(files.length);
			for (File file : files) {
				ModLoadingHandler.secondaryPro.text = "Found: " + file.getName();
				try (JarFile jar = new JarFile(file)) {
					URLClassLoader child = new URLClassLoader(
						new URL[] {file.toURI().toURL()},
						ModFindHander.class.getClassLoader()
					);

					try {
						Mods.mods.add(new ModContainer(jar, child));
					} catch (RuntimeException e) {
						throw new RuntimeException("Unable to load mod: " + file.getName(), e);
					}
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}

				ModLoadingHandler.secondaryPro.cur++;
			}
		}

		ModLoadingHandler.secondaryPro.text = "Adding Paths";
		for (ModContainer mod : Mods.mods) {
			LoaderInitialization.addToClassPath(mod.jarPath);
		}
	}

	private static File[] readModsFolder() {
		if (!new File(Mods.gameModsDir).exists())
			new File(Mods.gameModsDir).mkdirs();

		return new File(Mods.gameModsDir).listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".jar");
			}
		});
	}
}
