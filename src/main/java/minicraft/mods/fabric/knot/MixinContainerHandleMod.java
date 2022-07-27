package minicraft.mods.fabric.knot;

import java.util.Collection;
import java.util.Collections;

import org.spongepowered.asm.launch.platform.container.IContainerHandle;

public class MixinContainerHandleMod implements IContainerHandle {
	@Override
	public String getAttribute(String name) {
		return null;
	}

	@Override
	public Collection<IContainerHandle> getNestedContainers() {
		return Collections.emptyList();
	}
}
