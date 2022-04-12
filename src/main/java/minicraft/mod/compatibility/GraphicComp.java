package minicraft.mod.compatibility;

import minicraft.gfx.Sprite;
import minicraft.gfx.SpriteSheet;
import minicraft.gfx.Sprite.Px;

public class GraphicComp {
	/**
	 * 	Creates a reference to an 8x8 sprite in a spritesheet. Specify the position and sheet of the sprite to create.
	 * @param sx X position of the sprite in spritesheet coordinates.
	 * @param sy Y position of the sprite in spritesheet coordinates.
	 * @param sheet What spritesheet to use.
	 */
	public static Sprite spriteFromSpriteSheet(int sx, int sy, SpriteSheet sheet) {
		return spriteFromSpriteSheet(sx, sy, 1, 1, sheet);
	}
	public static Sprite spriteFromSpriteSheet(int sx, int sy, int sw, int sh, SpriteSheet sheet) {
		return spriteFromSpriteSheet(sx, sy, sw, sh, sheet, 0);
	}
	
	public static Sprite spriteFromSpriteSheet(int sx, int sy, int sw, int sh, SpriteSheet sheet, int mirror) {
		return spriteFromSpriteSheet(sx, sy, sw, sh, sheet, mirror, false);
	}
	public static Sprite spriteFromSpriteSheet(int sx, int sy, int sw, int sh, SpriteSheet sheet, int mirror, boolean onepixel) {
		Px[][] spritePixels = new Px[sh][sw];
		for (int r = 0; r < sh; r++)
			for (int c = 0; c < sw; c++)
				spritePixels[r][c] = new Px(sx + (onepixel ? 0 : c), sy + (onepixel ? 0 : r), mirror, sheet);
        return new Sprite(spritePixels);
	}
	public static Sprite spriteFromSpriteSheet(int sx, int sy, int sw, int sh, int sheet, boolean onepixel, int[][] mirrors) {
		Px[][] spritePixels = new Px[sh][sw];
		for (int r = 0; r < sh; r++)
			for (int c = 0; c < sw; c++)
				spritePixels[r][c] = new Px(sx + (onepixel? 0 : c), sy + (onepixel ? 0 : r), mirrors[r][c], sheet);
                return new Sprite(spritePixels);
    }
}
