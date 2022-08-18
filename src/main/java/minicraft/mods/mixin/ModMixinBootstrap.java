package minicraft.mods.mixin;

import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import minicraft.mods.LoaderInitialization;
import minicraft.mods.ModContainer;
import minicraft.mods.Mods;

public class ModMixinBootstrap {
	public static void init() {
		System.setProperty("mixin.bootstrapService", ModMixinServiceBootstrap.class.getName());
		System.setProperty("mixin.service", ModMixinService.class.getName());

		MixinBootstrap.init();

		// Load coremods Mixin config here
		Mixins.addConfigurations("mixins.json");

		for (ModContainer mod : Mods.mods) {
			for (String config : mod.mixinConfig.mixins) {
				try {
					Mixins.addConfiguration(config);
				} catch (Throwable t) {
					throw new RuntimeException(String.format("Error creating Mixin config %s for mod %s", config, mod.metadata.modId), t);
				}
			}
		}

		// Goto default phase
		try {
			Method m = MixinEnvironment.class.getDeclaredMethod("gotoPhase", MixinEnvironment.Phase.class);
			m.setAccessible(true);
			m.invoke(null, MixinEnvironment.Phase.INIT);
			m.invoke(null, MixinEnvironment.Phase.DEFAULT);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
