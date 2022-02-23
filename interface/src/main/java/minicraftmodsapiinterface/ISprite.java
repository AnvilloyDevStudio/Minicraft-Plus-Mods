package minicraftmodsapiinterface;

import java.util.Random;

public interface ISprite {
	
	public int color = -1;
	// spritePixels is arranged so that the pixels are in their correct positions relative to the top left of the full sprite. This means that their render positions are built-in to the array.

	public int getPos();
	public java.awt.Dimension getSize();

	public void render(IScreen screen, int x, int y);
	public void render(IScreen screen, int x, int y, int mirror);
	public void render(IScreen screen, int x, int y, int mirror, int whiteTint);

	public void renderRow(int r, IScreen screen, int x, int y);
	public void renderRow(int r, IScreen screen, int x, int y, int mirror);
	public void renderRow(int r, IScreen screen, int x, int y, int mirror, int whiteTint);
	
	public static class Px {
		protected int sheetPos, mirror, spriteSheetNum;
		protected ISpriteSheet spriteSheet;

		public Px(int sheetX, int sheetY, int mirroring, int sheet) {
			// pixelX and pixelY are the relative positions each pixel should have relative to the top-left-most pixel of the sprite.
			sheetPos = sheetX + 32 * sheetY;
			mirror = mirroring;
			this.spriteSheetNum = sheet;
		}

		public Px(int sheetX, int sheetY, int mirroring, ISpriteSheet sheet) {
			// pixelX and pixelY are the relative positions each pixel should have relative to the top-left-most pixel of the sprite.
			sheetPos = sheetX + 32 * sheetY;
			mirror = mirroring;
			this.spriteSheet = sheet;
		}

		public String toString() {
			return "SpritePixel:x=" + (sheetPos%32) + ";y=" + (sheetPos/32) + ";mirror=" + mirror;
		}
	}

}
