package minicraftmodsapiinterface;

public interface IFontStyle {
	/// Actually draws the text.
	public void draw(String msg, IScreen screen);
	
	public void configureForParagraph(String[] para, int spacing);
	
	public void setupParagraphLine(String[] para, int line, int spacing);
	
	public void drawParagraphLine(String[] para, int line, int spacing, IScreen screen);
	
	/* -- All the font modifier methods are below. They all return the current FontStyle instance for chaining. -- */
	
	/** Sets the color of the text itself. */
	public IFontStyle setColor(int col);
	
	/** sets the x position of the text anchor. This causes the text to be left-justified, if alignment is reset. */
	public IFontStyle setXPos(int pos);
	public IFontStyle setXPos(int pos, boolean resetAlignment);
	/** sets the y position of the text anchor. This sets the y pos to be the top of the block, if alignment is reset. */
	public IFontStyle setYPos(int pos);
	public IFontStyle setYPos(int pos, boolean resetAlignment);
	
	public IFontStyle setAnchor(int x, int y);
	
	/** Sets the position of the text box relative to the anchor. */
	public IFontStyle setRelTextPos(IRelPos relPos);
	public IFontStyle setRelTextPos(IRelPos relPos, boolean setBoth);
	
	/** Sets the position of a paragraph of text relative to the anchor. */
	public IFontStyle setRelLinePos(IRelPos relPos);
	
	/** This enables text shadowing, and sets the shadow color and type. It is a convenience method that offers a preset for text outlines, and a single shadow in a standard direction. */
	public IFontStyle setShadowType(int color, boolean full);
	
	/** This is what acutally sets the values described above. It also allows custom shadows. */
	public IFontStyle setShadowType(int color, String type);
	
	public int getColor();
}
