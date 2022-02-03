package minicraft.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import minicraft.core.io.ClassLoader;
import minicraft.item.*;

public class Mods extends Game {
    private Mods() {}
    public final static InMod[] Mods;
    public final static ArrayList ToolTypes;
    static {
        File[] mods = FileHandler.readModsFolder();
        if (mods == null) {
            Mods = null;
            ToolTypes = null;
        } else {
            ClassLoader loader = new ClassLoader();
            List<InMod> externalMods = new ArrayList(Collections.<InMod>emptyList());
            for (int a = 0; a<mods.length; a++) {
                Class mod = loader.loadJar(mods[a]);
                Class[] modclasses = mod.getDeclaredClasses();
                InMod modinstance = new InMod();
                for (int b = 0; b<modclasses.length; b++) {
                    if (modclasses[b].getName() == "Items") {
                        Class<Item>[] items = (List<Class<Item>>)modclasses[b].getClasses();
                        modinstance.items = items.stream();
                    }
                }
                System.out.println(modinstance);
                externalMods.add(modinstance);
            }
            Mods = externalMods.toArray(new InMod[0]);
            for (int a = 0; a<Mods.length; a++) {
                for (int b = 0; b<Mods[a].items.length; b++) {
                    if (Mods[a].items[b].getType() == "")
                }
            }
        }
    }
    private static class InMod {
        public InMod() {}
        public Item[] items;
    }
}
