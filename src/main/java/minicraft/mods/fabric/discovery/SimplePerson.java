package minicraft.mods.fabric.discovery;

import net.fabricmc.loader.api.metadata.ContactInformation;
import net.fabricmc.loader.api.metadata.Person;

/**
 * Represents a simple implementation of person which is only identified by name.
 */
class SimplePerson implements Person {
	private final String name;

	SimplePerson(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public ContactInformation getContact() {
		return ContactInformation.EMPTY;
	}
}
