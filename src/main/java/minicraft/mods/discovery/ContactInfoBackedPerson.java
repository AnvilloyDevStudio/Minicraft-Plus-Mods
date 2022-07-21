package minicraft.mods.discovery;

import net.fabricmc.loader.api.metadata.ContactInformation;

final class ContactInfoBackedPerson extends SimplePerson {
	private final ContactInformation contact;

	ContactInfoBackedPerson(String name, ContactInformation contact) {
		super(name);
		this.contact = contact;
	}

	@Override
	public ContactInformation getContact() {
		return this.contact;
	}
}
