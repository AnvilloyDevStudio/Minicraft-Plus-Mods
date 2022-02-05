package minicraft.core;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;

import minicraft.core.io.ClassLoader;

public class Mods extends Game {
    private Mods() {}
    public static ArrayList<JSONObject> Mods = new ArrayList<>();
    public static ArrayList<Mod.Item> Items = new ArrayList<>();
    public static void init() {}

    static {
        File[] mods = FileHandler.readModsFolder();
        if (mods == null) {
            Mods = null;
        } else {
            ClassLoader loader = new ClassLoader();
            for (int a = 0; a<mods.length; a++) {
                Mod modObj = new Mod();
                Pair<Pair<Class<?>, Enumeration<URL>>, JSONObject> modP = loader.loadJar(mods[a]);
                modObj.Info = modP.getRight();
                Object mod = modP.getLeft().getLeft();
                modObj.Resources = new Mod.Resources(modP.getLeft().getRight());
                Class<?>[] modclasses = mod.getClass().getDeclaredClasses();
                for (int b = 0; b<modclasses.length; b++) {
                    if (modclasses[b].getName() == "Items") {
                        Class<?>[] itemsC = modclasses[b].getDeclaredClasses();
                        for (int c = 0; c<itemsC.length; c++) {
                            Items.add(new Mod.Item(itemsC[c]));
                        }
                    }
                }
                if (modObj.Info.getString("name") == null) System.out.println("mod.json name not found.");
                if (modObj.Info.getString("description") == null) System.out.println("mod.json description not found.");
                Mods.add(modObj.Info);
            }
        }
    }
    public static class Mod {
        public JSONObject Info;
        public static class Item {
            public String name;
            public String itemtype;
            public String tooltype;
            public int durability;
            Item(Class<?> Obj) {
                try {
                    name = (String)PropertyUtils.getProperty(Obj, "name");
                    itemtype = (String)PropertyUtils.getProperty(Obj, "itemtype");
                    tooltype = (String)PropertyUtils.getProperty(Obj, "type");
                    durability = (Integer)PropertyUtils.getProperty(Obj, "durability");
                } catch (IllegalArgumentException | IllegalAccessException
                        | SecurityException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        public Resources Resources;
        public static class Resources {
            Resources(Enumeration<URL> urls) {
                for (Enumeration<URL> a = urls; a.hasMoreElements();) System.out.println(a.nextElement());
            }
        }
    }
}
