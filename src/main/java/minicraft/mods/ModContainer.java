package minicraft.mods;

import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.json.JSONException;
import org.json.JSONObject;
import org.tinylog.Logger;

public class ModContainer {
	public final Class<?> entryClass;
	public final Class<?> preInitClass;
	public final Class<?> initClass;
	public final Manifest manifest;
	public final ModMetadata metadata;
	public final String mixinConfig;
	public final Path jarPath;

	ModContainer(JarFile jar, URLClassLoader child) {
		try {
			metadata = new ModMetadata(new JSONObject(new String(LoaderUtils.readStringFromInputStream(jar.getInputStream(jar.getEntry("mod.json"))))));
			if (!metadata.entrypoint.isEmpty()) {
				Class<?> clazz = Class.forName(metadata.entrypoint, false, child);
				boolean valid = false;
				try {
					clazz.getDeclaredMethod("entry");
					valid = true;
				} catch (NoSuchMethodException | SecurityException e) {
					Logger.warn("Method #entry is unable to get in {}.", clazz.getSimpleName(), e);
				} finally {
					if (valid)
						entryClass = clazz;
					else
						entryClass = null;
				}
			} else
				entryClass = null;

			if (!metadata.preInitpoint.isEmpty()) {
				Class<?> clazz = Class.forName(metadata.preInitpoint, false, child);
				boolean valid = false;
				try {
					clazz.getDeclaredMethod("preInit");
					valid = true;
				} catch (NoSuchMethodException | SecurityException e) {
					Logger.warn("Method #preInit is unable to get in {}.", clazz.getSimpleName(), e);
				} finally {
					if (valid)
						preInitClass = clazz;
					else
						preInitClass = null;
				}
			} else
				preInitClass = null;

			if (!metadata.initpoint.isEmpty()) {
				Class<?> clazz = Class.forName(metadata.initpoint, false, child);
				boolean valid = false;
				try {
					clazz.getDeclaredMethod("init");
					valid = true;
				} catch (NoSuchMethodException | SecurityException e) {
					Logger.warn("Method #init is unable to get in {}.", clazz.getSimpleName(), e);
				} finally {
					if (valid)
						initClass = clazz;
					else
						initClass = null;
				}
			} else
				initClass = null;

			manifest = jar.getManifest();

			jarPath = Paths.get(jar.getName());

			if (jar.getEntry(metadata.modId + ".mixins.json") != null)
				mixinConfig = metadata.modId + ".mixins.json";
			else
				mixinConfig = null;
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException("Unable to load mod file: " + jar.getName(), e);
		}
	}

	public static class ModMetadata {
		public final String modId;
		public final String name;
		public final String description;
		public final String version;
		public final String author;
		public final String entrypoint;
		public final String preInitpoint;
		public final String initpoint;

		private ModMetadata(JSONObject json) throws JSONException {
			modId = json.getString("id");
			if (modId.isEmpty()) throw new JSONException("modId cannot be empty.");

			name = json.getString("name");
			description = json.optString("description");
			version = json.optString("version", "1.0.0");
			author = json.optString("author");
			entrypoint = json.optString("entrypoint");
			preInitpoint = json.optString("preInitpoint");
			initpoint = json.optString("initpoint");
		}
	}
}
