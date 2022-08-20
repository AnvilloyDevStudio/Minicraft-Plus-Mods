package minicraft.mods.mixin;

import java.lang.reflect.Method;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import minicraft.mods.ModContainer;
import minicraft.mods.ModLoadingHandler;
import minicraft.mods.Mods;

public class ModMixinBootstrap {
	public static void init() {
		System.setProperty("mixin.bootstrapService", ModMixinServiceBootstrap.class.getName());
		System.setProperty("mixin.service", ModMixinService.class.getName());

		(ModLoadingHandler.secondaryPro = new ModLoadingHandler.Progress(Mods.mods.size())).text = "Initializing Mixin";
		MixinBootstrap.init();

		// Load coremods Mixin config here
		Mixins.addConfigurations("mixins.json");

		for (ModContainer mod : Mods.mods) {
			ModLoadingHandler.secondaryPro.cur++;
			if (mod.mixinConfig != null) try {
				ModLoadingHandler.secondaryPro.text = "Adding Mixin Config: " + mod.metadata.name;
				Mixins.addConfiguration(mod.mixinConfig);
			} catch (Throwable t) {
				throw new RuntimeException(String.format("Error creating Mixin config %s for mod %s", mod.mixinConfig, mod.metadata.modId), t);
			} else ModLoadingHandler.secondaryPro.text = null;
		}

		ModLoadingHandler.secondaryPro.text = "Mixin Configs Added";
	}

	/** Goto INIT Phase. */
	public static void goPhaseInit() {
		try {
			Method m = MixinEnvironment.class.getDeclaredMethod("gotoPhase", MixinEnvironment.Phase.class);
			m.setAccessible(true);
			m.invoke(null, MixinEnvironment.Phase.INIT);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/** Goto DEFAULT Phase. */
	public static void goPhaseDefault() {
		try {
			Method m = MixinEnvironment.class.getDeclaredMethod("gotoPhase", MixinEnvironment.Phase.class);
			m.setAccessible(true);
			m.invoke(null, MixinEnvironment.Phase.DEFAULT);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
