package minicraft.mods.coremods.mixins;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import minicraft.core.Game;
import minicraft.core.Updater;
import minicraft.mods.coremods.ModProcedure;

@Mixin(Updater.class)
public class UpdaterMixin extends Game {
	@Inject(method = "tick()V", at = @At(value = "FIELD", target = "paused:Z", opcode = Opcodes.PUTSTATIC, ordinal = 4,
		remap = false, shift = At.Shift.AFTER), remap = false)
	private static void tickNotPaused() {
		ModProcedure.tickables0.forEach(t -> t.tick(input));
	}

	@Inject(method = "tick()V", at = @At(value = "FIELD", target = "paused:Z", opcode = Opcodes.PUTSTATIC, ordinal = 3,
		remap = false, shift = At.Shift.AFTER), remap = false)
	private static void tickPaused() {
		ModProcedure.tickables1.forEach(t -> t.tick(input));
	}

	@Inject(method = "tick()V", at = @At(value = "FIELD", target = "input:Lminicraft/core/io/InputHandler;", opcode = Opcodes.GETSTATIC, ordinal = 4,
		remap = false, shift = At.Shift.AFTER), remap = false)
	private static void tickFocused() {
		ModProcedure.tickables2.forEach(t -> t.tick(input));
	}
}
