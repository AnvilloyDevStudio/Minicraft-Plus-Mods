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
			Logger.info("No mods found.");
		} else {
			for (File file : files) {
				try (JarFile jar = new JarFile(file)) {
					URLClassLoader child = new URLClassLoader(
						new URL[] {file.toURI().toURL()},
						ModFindHander.class.getClassLoader()
					);

					Mods.mods.add(new ModContainer(jar, child));
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			}
		}

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
