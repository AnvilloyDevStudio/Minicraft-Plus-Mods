package minicraftmodsapiinterface;

public abstract class IScreen {
	public static int w = 288, h = 192;

    public abstract void setSheet(ISpriteSheet itemSheet, ISpriteSheet tileSheet, ISpriteSheet entitySheet, ISpriteSheet guiSheet, ISpriteSheet skinsSheet);

	public abstract void setSheet(ISpriteSheet itemSheet, ISpriteSheet tileSheet, ISpriteSheet entitySheet, ISpriteSheet guiSheet);
	
	/** Clears all the colors on the screen */
	public abstract void clear(int color);
	
	public abstract void render(int[] pixelColors);

	public abstract void render(int xp, int yp, int tile, int bits);

	public abstract void render(int xp, int yp, int tile, int bits, int sheet);

    public abstract void render(int xp, int yp, int tile, int bits, int sheet, int whiteTint);

	/** This method takes care of assigning the correct spritesheet to assign to the sheet variable **/
    public abstract void render(int xp, int yp, int tile, int bits, int sheet, int whiteTint, boolean fullbright);

    /** Renders an object from the sprite sheet based on screen coordinates, tile (ISpriteSheet location), colors, and bits (for mirroring). I believe that xp and yp refer to the desired position of the upper-left-most pixel. */
    public abstract void render(int xp, int yp, int tile, int bits, ISpriteSheet sheet, int whiteTint, boolean fullbright);

	/** Sets the offset of the screen */
	public abstract void setOffset(int xOffset, int yOffset);
		
	/** Overlays the screen with pixels */
    public abstract void overlay(IScreen screen2, int currentLevel, int xa, int ya);

	public abstract void renderLight(int x, int y, int r);

}
