package minicraft.mods;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import org.json.JSONObject;

public class ModClassLoader {
    public ModClassLoader() {}
    public ModContainer loadJar(File jarf) {
        try {
            JarFile jar = new JarFile(jarf);
			URLClassLoader child = new URLClassLoader(
                new URL[] {jarf.toURI().toURL()},
                getClass().getClassLoader()
            );
			ModContainer mod;
			try {
				mod = new ModContainer(jar, child, jarf);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

            jar.close();
            return mod;
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

	public static class ModContainer {
		public final Class<?> entryClass;
		public final Manifest manifest;
		public final JSONObject modInfo;
		public final JSONObject mixinConf;

		ModContainer(JarFile jar, URLClassLoader child, File jarf) {
			try {
				modInfo = new JSONObject(new String(jar.getInputStream(jar.getEntry("mod.json")).readAllBytes()));
				ZipEntry mixinEntry;
				if ((mixinEntry = jar.getEntry("mixins.json")) != null) mixinConf = new JSONObject(new String(jar.getInputStream(mixinEntry).readAllBytes()));
				else mixinConf = null;

				manifest = jar.getManifest();

				String entry = modInfo.optString("entry");
				if (entry == null) throw new IOException("No entry detected in mod configuration file.");
				entryClass = Class.forName(entry, true, child);
			} catch (IOException | ClassNotFoundException e) {
				throw new RuntimeException("Unable to load mod: " + jarf.getName(), e);
			}
		}
	}
}
