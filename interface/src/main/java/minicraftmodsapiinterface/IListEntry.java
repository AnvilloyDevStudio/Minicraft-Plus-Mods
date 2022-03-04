package minicraftmodsapiinterface;

import java.util.Locale;

public abstract class IListEntry {
	
	public static final int COL_UNSLCT = IColor.GRAY;
	public static final int COL_SLCT = IColor.WHITE;
	
	private boolean selectable = true, visible = true;
	
	/**
	 * Ticks the entry. Used to handle input from the InputHandler
	 * @param input InputHandler used to get player input.
	 */
	public abstract void tick(IInputHandler input);

	public void render(IScreen screen, int x, int y, boolean isSelected, String contain, int containColor) {
		if (!visible) {
			return;
		}

		render(screen, x, y, isSelected);
		if (contain == null || contain.isEmpty()) {
			return;
		}

		String string = toString().toLowerCase(Locale.ENGLISH);
		contain = contain.toLowerCase(Locale.ENGLISH);

		IFont.drawColor(string.replaceAll(contain, IColor.toStringCode(containColor) + contain + IColor.WHITE_CODE), screen, x, y);
	}
	
	/**
	 * Renders the entry to the given screen.
	 * Coordinate origin is in the top left corner of the entry space.
	 * @param screen Screen to render the entry to
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param isSelected true if the entry is selected, false otherwise
	 */
	public void render(IScreen screen, int x, int y, boolean isSelected) {
		if (visible)
			IFont.draw(toString(), screen, x, y, getColor(isSelected));
	}
	
	/**
	 * Returns the current color depending on if the entry is selected.
	 * @param isSelected true if the entry is selected, false otherwise
	 * @return the current entry color
	 */
	public int getColor(boolean isSelected) { return isSelected ? COL_SLCT : COL_UNSLCT; }
	
	/**
	 * Calculates the width of the entry.
	 * @return the entry's width
	 */
	public int getWidth() {
		return IFont.textWidth(toString());
	}
	
	/**
	 * Calculates the height of the entry.
	 * @return the entry's height
	 */
	public static int getHeight() {
		return IFont.textHeight();
	}
	
	/**
	 * Determines if this entry can be selected.
	 * @return true if it is visible and can be selected, false otherwise.
	 */
	public final boolean isSelectable() { return selectable && visible; }
	
	/**
	 * Returns whether the entry is visible or not.
	 * @return true if the entry is visible, false otherwise
	 */
	public final boolean isVisible() { return visible; }
	
	/**
	 * Changes if the entry can be selected or not.
	 * @param selectable true if the entry can be selected, false if not
	 */
	public final void setSelectable(boolean selectable) { this.selectable = selectable; }
	
	/**
	 * Changes if the entry is visible or not.
	 * @param visible true if the entry should be visible, false if not
	 */
	public final void setVisible(boolean visible) { this.visible = visible; }
	
	@Override
	public abstract String toString();
}
