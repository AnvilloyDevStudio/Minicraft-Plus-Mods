package minimods.mod.core.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import minicraft.screen.PauseDisplay;

@Mixin(PauseDisplay.class)
public class PausedDisplayMixin {
	public static void init() {};

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "minicraft.core.Game.setDisplay", ordinal = 3))
	public void constructorInvokeAssign(CallbackInfo ci) {
        System.out.println("An instance of SomeJavaFile has been created!");
		WorldMixin.commandWindow.close();
		WorldMixin.commandWindow = null;
    }
}
