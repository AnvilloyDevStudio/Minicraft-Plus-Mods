package minicraft.mods.knot;

import org.spongepowered.asm.service.IMixinServiceBootstrap;

public class MixinServiceKnotBootstrap implements IMixinServiceBootstrap {
	@Override
	public String getName() {
		return "Knot";
	}

	@Override
	public String getServiceClassName() {
		return "net.fabricmc.loader.impl.launch.knot.MixinServiceKnot";
	}

	@Override
	public void bootstrap() {
		// already done in Knot
	}
}
