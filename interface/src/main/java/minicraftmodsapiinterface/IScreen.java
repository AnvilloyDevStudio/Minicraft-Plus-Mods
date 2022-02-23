package minicraftmodsapiinterface;

public interface IScreen {
    public void setSheet(ISpriteSheet itemSheet, ISpriteSheet tileSheet, ISpriteSheet entitySheet, ISpriteSheet guiSheet, ISpriteSheet skinsSheet);

	public void setSheet(ISpriteSheet itemSheet, ISpriteSheet tileSheet, ISpriteSheet entitySheet, ISpriteSheet guiSheet);
	
	/** Clears all the colors on the screen */
	public void clear(int color);
	
	public void render(int[] pixelColors);

	public void render(int xp, int yp, int tile, int bits);

	public void render(int xp, int yp, int tile, int bits, int sheet);

    public void render(int xp, int yp, int tile, int bits, int sheet, int whiteTint);

	/** This method takes care of assigning the correct spritesheet to assign to the sheet variable **/
    public void render(int xp, int yp, int tile, int bits, int sheet, int whiteTint, boolean fullbright);

    /** Renders an object from the sprite sheet based on screen coordinates, tile (ISpriteSheet location), colors, and bits (for mirroring). I believe that xp and yp refer to the desired position of the upper-left-most pixel. */
    public void render(int xp, int yp, int tile, int bits, ISpriteSheet sheet, int whiteTint, boolean fullbright);

	/** Sets the offset of the screen */
	public void setOffset(int xOffset, int yOffset);
		
	/** Overlays the screen with pixels */
    public void overlay(IScreen screen2, int currentLevel, int xa, int ya);

	public void renderLight(int x, int y, int r);

}
