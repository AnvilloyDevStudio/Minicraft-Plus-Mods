package minicraft.mods;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Mods {
	public static final ArrayList<ModContainer> mods = new ArrayList<>();

    // public static ArrayList<Mods.Mod> Mods = new ArrayList<>();
    public static final String MODSVERSION = "0.4.0";
    // public static ArrayList<Display> guiDisplays = new ArrayList<>();

	// TODO A copy from Game#VERSION, this needed to be updated when updating
	public static final String GAMEVERSION = "2.1.3";

	// TODO A copy from FileHandler, this may need to update from the original code when updating
	static final String OS;
	static final String localGameDir;
	static final String systemGameDir;
	static String gameDir;
	static String gameModsDir;
	static boolean debug;
	public static boolean logClassLoad = false;

	static {
		OS = System.getProperty("os.name").toLowerCase();
		String local = "playminicraft/mods/Minicraft_Plus";

		if (OS.contains("windows")) // windows
			systemGameDir = System.getenv("APPDATA");
		else {
			systemGameDir = System.getProperty("user.home");
			if (!OS.contains("mac"))
				local = "." + local; // linux
		}

		localGameDir = "/" + local;
		gameDir = systemGameDir + localGameDir;
		gameModsDir = systemGameDir + localGameDir + "/mods";
	}

	public final static String entrypoint = "minicraft.core.Game";

    public static void init() {
		LoaderInitialization.init();
	}

	static void setDebug(boolean debug) {
		Mods.debug = debug;
	}

	public static void launchGame(ClassLoader loader, String[] args) {
		try {
			Class<?> c = loader.loadClass(entrypoint);
			Method m = c.getMethod("main", String[].class);
			m.invoke(null, (Object) args);
		} catch(InvocationTargetException e) {
			throw new RuntimeException("Game has crashed", e.getCause());
		} catch(ReflectiveOperationException e) {
			throw new RuntimeException("Failed to start game", e);
		}
	}

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

    // public static void renderGui() {
    //     guiDisplays.forEach(m -> m.render(Renderer.screen));
    // }
    // public static void tick(InputHandler input) {
    //     guiDisplays.forEach(m -> m.tick(input));
    // }
}
