package minicraft.screen.entry;

import minicraft.core.io.Localization;

public class BooleanEntry extends ArrayEntry<Boolean> {
	
	public BooleanEntry(String label, boolean initial) {
		super(label, true, new Boolean[] {true, false});
		
		setSelection(initial ? 0 : 1);
	}
	
	@Override
	public String toString() {
		return getLabel() + ": " + (getValue() ?
			Localization.getLocalized("On") :
			Localization.getLocalized("Off")
		);
	}
}
