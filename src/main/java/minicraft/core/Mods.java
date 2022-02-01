package minicraft.core;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Mods extends Game {
    private Mods() {}
    public final static Module Core;
    public final static Module[] Mods;
    static {
        File[] mods = FileHandler.readModsFolder();
        if (mods == null) {
            Core = null;
            Mods = null;
        } else {
            List<Module> externalMods = Collections.<Module>emptyList();
            Predicate<File> checkCore = (File f) -> (f.getName() == "core.jar");
            Stream<File> coreA = Arrays.stream(mods).filter(checkCore);
            if (coreA.count()>0) Core = FileHandler.readJarFile(coreA.findFirst()).getModule();
            else Core = null;
            for (int a = 0; a<mods.length; a++) {
                if (mods[a].getName() == "core.jar") continue;
                externalMods.add(FileHandler.readJarFile(mods[a]).getModule());
            }
            Mods = externalMods.toArray(new Module[0]);
        }
    }
    public void loadCore() {
        if (Core == null) return;
        
    }
}
