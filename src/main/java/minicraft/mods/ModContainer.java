package minicraft.mods;

import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import org.json.JSONArray;
import org.json.JSONObject;

public class ModContainer {
	public final Class<?> entryClass;
	public final Manifest manifest;
	public final ModMetadata metadata;
	public final ModMixinConfig mixinConfig;
	public final Path jarPath;

	ModContainer(JarFile jar, URLClassLoader child) {
		try {
			metadata = new ModMetadata(new JSONObject(new String(jar.getInputStream(jar.getEntry("mod.json")).readAllBytes())));
			entryClass = Class.forName(metadata.entrypoint, false, child);
			manifest = jar.getManifest();

			jarPath = Path.of(jar.getName());

			ZipEntry mixinEntry;
			if ((mixinEntry = jar.getEntry("mixins.json")) != null)
				mixinConfig = new ModMixinConfig(new JSONObject(new String(jar.getInputStream(mixinEntry).readAllBytes())));
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
		public final String author;
		private final String entrypoint;

		private ModMetadata(JSONObject json) {
			modId = json.getString("id");
			name = json.getString("name");
			description = json.getString("description");
			author = json.getString("author");
			entrypoint = json.getString("entrypoint");
		}
	}

	public static class ModMixinConfig {
		public final String[] mixins;

		private ModMixinConfig(JSONObject json) {
			JSONArray array = json.getJSONArray("mixins");
			mixins = new String[array.length()];
			for (int i = 0; i < array.length(); i++) {
				mixins[i] = array.getString(i);
			}
		}
	}
}
