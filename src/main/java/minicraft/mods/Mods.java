package minicraft.mods;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.jar.Manifest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.tinylog.Logger;

import minicraft.core.Game;
import minicraft.core.Renderer;
import minicraft.core.io.FileHandler;
import minicraft.core.io.InputHandler;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.mods.ModClassLoader.ModContainer;
import minicraft.mods.knot.MixinServiceKnot;
import minicraft.mods.knot.MixinServiceKnotBootstrap;
import minicraft.saveload.Version;
import minicraft.screen.Display;

public class Mods extends Game {
    public static ArrayList<Mods.Mod> Mods = new ArrayList<>();
    public static final Version VERSION = new Version("0.4.0");
    public static ArrayList<Display> guiDisplays = new ArrayList<>();
	public static ArrayList<Item> registeredItems = new ArrayList<>();

	public static void registerItem(Item item) {
		registeredItems.add(item);
		Items.add(item);
	}

    public static void init() {}

    static {
        // File[] mods = FileHandler.readModsFolder();
        // if (mods != null) {
		// 	if (mods.length == 0) {
		// 		Logger.info("No mod detected.");
		// 	} else {
		// 		ModClassLoader loader = new ModClassLoader();
		// 		for (int a = 0; a<mods.length; a++) {
		// 			ModContainer modContainer = loader.loadJar(mods[a]);
		// 			Mod mod = new Mod(modContainer);
		// 			Mods.add(mod);
		// 		}
		// 		int count = 0;
		// 		boolean success = true;
		// 		for (int i = 0; i<Mods.size(); i++) {
		// 			if (count > Mods.size()*Mods.size()) {
		// 				System.out.println("Dependency structure too complex.");
		// 				success = false;
		// 				break;
		// 			}
		// 			int index = i;
		// 			JSONArray deps = Mods.get(i).info.optJSONArray("depencencies");
		// 			if (deps != null) {
		// 				for (int j = 0; j<deps.length(); j++) {
		// 					String n = deps.getString(j);
		// 					int jdx = Mods.indexOf(Mods.stream().filter(m -> m.info.getString("name").equals(n)).findAny().orElse(null));
		// 					if (jdx == -1) {
		// 						System.out.println("Dependency not found: "+deps.getString(j));
		// 						success = false;
		// 						break;
		// 					}
		// 					index = jdx;
		// 				}
		// 				Mods.add(index, Mods.remove(i));
		// 			}
		// 			count++;
		// 		}
		// 		if (success) {
		// 			Mods.forEach(m -> m.loadEntry());

		// 			System.setProperty("mixin.bootstrapService", MixinServiceKnotBootstrap.class.getName());
		// 			System.setProperty("mixin.service", MixinServiceKnot.class.getName());

		// 			MixinBootstrap.init();
		// 		}
		// 	}
        // } else Logger.info("Unable to list mods.");
    }

    public static void renderGui() {
        guiDisplays.forEach(m -> m.render(Renderer.screen));
    }
    public static void tick(InputHandler input) {
        guiDisplays.forEach(m -> m.tick(input));
    }

    public static class Mod {
        public final JSONObject info;
        public final Manifest manifest;
        private final Class<?> mainClass;
        public Mod(ModContainer mod) {
            manifest = mod.manifest;
            mainClass = mod.entryClass;
			info = mod.modInfo;
			if (info.getString("name") == null) System.out.println("mod.json name not found.");
			if (info.getString("description") == null) System.out.println("mod.json description not found.");
        }

        public void loadEntry() {
            try {
                mainClass.getDeclaredMethod("entry").invoke(null, new Object[0]);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
