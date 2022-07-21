package minimods.mod.core.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import minicraft.core.World;
import minimods.mod.core.CommandWindow;

@Mixin(World.class)
public class WorldMixin {
	public static void init() {};

	public static CommandWindow commandWindow;

	@Inject(method = "initWorld", at = @At("TAIL"))
	public void initWorldTail(CallbackInfo ci) {
		commandWindow = new CommandWindow();
	}
}
