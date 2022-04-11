package minicraft.core;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;

import minicraft.core.io.ClassLoader;

public class Mods extends Game {
    private Mods() {}
    public static ArrayList<Mods.Mod> Mods = new ArrayList<>();
    public static ArrayList<Method> ModTileUnderGens = new ArrayList<>();
    public static ArrayList<Method> ModTileTopGens = new ArrayList<>();
    public static void init() {}

    static {
        File[] mods = FileHandler.readModsFolder();
        if (mods == null) {
            Mods = null;
        } else {
            ClassLoader loader = new ClassLoader();
            for (int a = 0; a<mods.length; a++) {
                Pair<Class<?>, JSONObject> modP = loader.loadJar(mods[a]);
                Mod modObj = new Mod(modP.getLeft());
                modObj.Info = modP.getRight();
                if (modObj.Info.getString("name") == null) System.out.println("mod.json name not found.");
                if (modObj.Info.getString("description") == null) System.out.println("mod.json description not found.");
                Mods.add(modObj);
            }
        }
    }
    public static class Mod {
        public JSONObject Info;
        public Mod(Class<?> m) {
            try {
                m.getDeclaredMethod("entry").invoke(null, new Object[0]);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
