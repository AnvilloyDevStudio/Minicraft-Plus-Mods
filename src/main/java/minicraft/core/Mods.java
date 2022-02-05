package minicraft.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;

import minicraft.core.io.ClassLoader;
import minicraft.item.*;

public class Mods extends Game {
    private Mods() {}
    public static ArrayList<JSONObject> Mods = new ArrayList<>();
    public static ArrayList<Item> Items = new ArrayList<>();
    public static void init() {}

    static {
        File[] mods = FileHandler.readModsFolder();
        if (mods == null) {
            Mods = null;
        } else {
            ClassLoader loader = new ClassLoader();
            for (int a = 0; a<mods.length; a++) {
                Mod modObj = new Mod();
                Pair<Class, JSONObject> modP = loader.loadJar(mods[a]);
                modObj.Info = modP.getRight();
                Object mod = modP.getLeft();
                Class[] modclasses = mod.getClass().getDeclaredClasses();
                for (int b = 0; b<modclasses.length; b++) {
                    if (modclasses[b].getName() == "Items") {
                        // Object[] items = modclasses[b];
                    }
                }
                if (modObj.Info.getString("name") == null) System.out.println("mod.json name not found.");
                if (modObj.Info.getString("description") == null) System.out.println("mod.json description not found.");
                Mods.add(modObj.Info);
            }
        }
    }
    private static class Mod {
        public JSONObject Info;
    }
}
