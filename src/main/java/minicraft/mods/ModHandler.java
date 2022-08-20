package minicraft.mods;

import java.lang.reflect.InvocationTargetException;

import org.spongepowered.asm.service.MixinService;

public class ModHandler {
	/** Loading mods by entry. (Post-Init) */
	public static void initMods() {
		ModLoadingHandler.toFront();
		MixinService.getService().getLogger("ModHandler").debug("Start loading mods from entrypoint static method #entry().");
		ModContainer[] mods = Mods.mods.stream().filter(m -> m.entryClass != null).toArray(ModContainer[]::new);
		ModLoadingHandler.secondaryPro = new ModLoadingHandler.Progress(mods.length);
		for (ModContainer mod : mods) {
			ModLoadingHandler.secondaryPro.text = mod.metadata.name;
			if (mod.entryClass != null) try {
				mod.entryClass.getDeclaredMethod("entry").invoke(null, new Object[0]);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				throw new RuntimeException(e);
			}

			ModLoadingHandler.secondaryPro.cur++;
		}

		ModLoadingHandler.secondaryPro.text = "Completed";
		ModLoadingHandler.secondaryPro = null;
		ModLoadingHandler.overallPro.text = "Completd";
		try { // Wait a bit.
			Thread.sleep(100);
		} catch (InterruptedException e) {}
		ModLoadingHandler.closeWindow();
	}

	/** Loading mods by preInit with PreInit phase. */
	public static void preInitPhaseMods() {
		MixinService.getService().getLogger("ModHandler").debug("Start loading mods from preInitpoint static method #preInit().");
		ModContainer[] mods = Mods.mods.stream().filter(m -> m.preInitClass != null).toArray(ModContainer[]::new);
		ModLoadingHandler.secondaryPro = new ModLoadingHandler.Progress(mods.length);
		for (ModContainer mod : mods) {
			ModLoadingHandler.secondaryPro.text = mod.metadata.name;
			try {
				mod.preInitClass.getDeclaredMethod("preInit").invoke(null, new Object[0]);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				throw new RuntimeException(e);
			}

			ModLoadingHandler.secondaryPro.cur++;
		}
	}

	/** Loading mods by init with Init phase. */
	public static void initPhaseMods() {
		MixinService.getService().getLogger("ModHandler").debug("Start loading mods from initpoint static method #init().");
		ModContainer[] mods = Mods.mods.stream().filter(m -> m.initClass != null).toArray(ModContainer[]::new);
		ModLoadingHandler.secondaryPro = new ModLoadingHandler.Progress(mods.length);
		for (ModContainer mod : mods) {
			ModLoadingHandler.secondaryPro.text = mod.metadata.name;
			try {
				mod.initClass.getDeclaredMethod("init").invoke(null, new Object[0]);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				throw new RuntimeException(e);
			}

			ModLoadingHandler.secondaryPro.cur++;
		}
	}
}
