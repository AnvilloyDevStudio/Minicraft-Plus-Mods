package minicraft.mods;

import java.lang.reflect.InvocationTargetException;

import org.spongepowered.asm.service.MixinService;

public class ModHandler {


	public static void initMods() {
		MixinService.getService().getLogger("ModHandler").debug("Start loading mods from entrypoint static method #entry().");
		for (ModContainer mod : Mods.mods) {
			try {
				mod.entryClass.getDeclaredMethod("entry").invoke(null, new Object[0]);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
