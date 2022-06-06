package minicraft.core.io;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.AbstractMap;
import java.util.Map.Entry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.json.JSONObject;

public class ClassLoader {
    public ClassLoader() {}
    public Entry<Entry<Class<?>, Manifest>, JSONObject> loadJar(File jarf) {
        URLClassLoader child;
        try {
            JarFile jar = new JarFile(jarf);
			JSONObject modInfo = new JSONObject(new String(jar.getInputStream(jar.getEntry("mod.json")).readAllBytes()));
            Manifest manifest = jar.getManifest();
            jar.close();
            child = new URLClassLoader(
                new URL[] {jarf.toURI().toURL()},
                getClass().getClassLoader()
            );
            Class<?> classToLoad = Class.forName("mod.Main", true, child);
            return new AbstractMap.SimpleEntry<>(new AbstractMap.SimpleEntry<>(classToLoad, manifest), modInfo);
        } catch (ClassNotFoundException | SecurityException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
