package minicraft.mods.coremods.mixins;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import minicraft.core.Renderer;
import minicraft.gfx.Screen;
import minicraft.mods.coremods.ModProcedure;

@Mixin(Renderer.class)
public class RendererMixin {
	@Shadow
	private static Screen screen;

	@Inject(method = "renderGui()V", at = @At(value = "INVOKE", target = "Lminicraft/core/Renderer;renderDebugInfo()V", remap = false), remap = false)
	private static void renderGUIBeforeDebug(CallbackInfo ci) {
		ModProcedure.displays0.forEach(d -> d.render(screen));
	}

	@Inject(method = "renderDebugInfo()V", at = @At(value = "INVOKE",
		target = "Lminicraft/gfx/Font;drawParagraph(Ljava/util/List;Lminicraft/gfx/Screen;Lminicraft/gfx/FontStyle;I)V", remap = false), remap = false)
	private static void renderDebugTail(CallbackInfo ci) {
		ModProcedure.displays1.forEach(d -> d.render(screen));
	}

	@Inject(method = "render()V", at = @At(value = "JUMP", opcode = Opcodes.IFNE,
		remap = false, shift = At.Shift.BEFORE, ordinal = 0), remap = false)
	private static void renderBeforeNagger(CallbackInfo ci) {
		ModProcedure.displays2.forEach(d -> d.render(screen));
	}
}
