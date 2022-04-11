package minicraft.core.io;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;

public class ClassLoader {
    public ClassLoader() {}
    public Pair<Pair<Class<?>, Manifest>, JSONObject> loadJar(File jarf) {
        URLClassLoader child;
        try {
            JarFile jar = new JarFile(jarf);
			JSONObject modInfo = new JSONObject(new String(jar.getInputStream(jar.getEntry("mod.json")).readAllBytes()));
            // jar.getJarEntry("mod/")
            jar.close();
            child = new URLClassLoader(
                new URL[] {jarf.toURI().toURL()},
                getClass().getClassLoader()
            );
            Class<?> classToLoad = Class.forName("mod.Main", true, child);
            return Pair.of(Pair.of(classToLoad, jar.getManifest()), modInfo);
            // Method method = classToLoad.getDeclaredMethod("myMethod");
            // Object instance = classToLoad.getDeclaredConstructor().newInstance();
            // Object result = method.invoke(instance);
        } catch (ClassNotFoundException | SecurityException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
