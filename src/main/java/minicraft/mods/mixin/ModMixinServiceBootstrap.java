package minicraft.mods.mixin;

import org.spongepowered.asm.service.IMixinServiceBootstrap;

public class ModMixinServiceBootstrap implements IMixinServiceBootstrap {
	@Override
	public String getName() {
		return "MiniMods";
	}

	@Override
	public String getServiceClassName() {
		return "minicraft.mods.mixin.ModMixinService";
	}

	@Override
	public void bootstrap() {
		// already done in Knot
	}
}
