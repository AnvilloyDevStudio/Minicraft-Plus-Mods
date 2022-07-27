package minicraft.mods.fabric.discovery;

import net.fabricmc.loader.api.Version;

interface DomainObject {
	String getId();

	interface Mod extends DomainObject {
		Version getVersion();
	}
}
