package minicraft.core.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;

public class ClassLoader {
    public ClassLoader() {}
    public Pair<Class, JSONObject> loadJar(File jar) {
        URLClassLoader child;
        try {
            child = new URLClassLoader(
                new URL[] {jar.toURI().toURL()},
                this.getClass().getClassLoader()
            );
            Class classToLoad = Class.forName("mod.Module", true, child);
            InputStream is = child.getResourceAsStream("mod.json");
            if (is == null) System.out.println("mod.json not found.");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
                content.append(System.lineSeparator());
            }
            JSONObject modInfo = new JSONObject(content.toString());
            return Pair.of(classToLoad, modInfo);
            // Method method = classToLoad.getDeclaredMethod("myMethod");
            // Object instance = classToLoad.getDeclaredConstructor().newInstance();
            // Object result = method.invoke(instance);
        } catch (ClassNotFoundException | SecurityException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
