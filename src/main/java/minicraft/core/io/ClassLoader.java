package minicraft.core.io;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoader {
    public ClassLoader() {}
    public Class loadJar(File jar) {
        URLClassLoader child;
        try {
            child = new URLClassLoader(
                new URL[] {jar.toURI().toURL()},
                this.getClass().getClassLoader()
            );
            Class classToLoad = Class.forName("com.Core.Module", true, child);
            return classToLoad;
            // Method method = classToLoad.getDeclaredMethod("myMethod");
            // Object instance = classToLoad.getDeclaredConstructor().newInstance();
            // Object result = method.invoke(instance);
        } catch (MalformedURLException | ClassNotFoundException | SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }
}
