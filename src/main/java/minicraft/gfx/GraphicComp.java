package minicraft.gfx;

import minicraft.gfx.Sprite.Px;

import java.awt.Rectangle;

public class GraphicComp {
	public static class ModSprite extends Sprite {
		/**
			This class needs to store a list of similar segments that make up a sprite, just once for everything. There's usually four groups, but the components are:
				-spritesheet location (x, y)
				-mirror type

			That's it!
			The screen's render method only draws one 8x8 pixel of the spritesheet at a time, so the "sprite size" will be determined by how many repetitions of the above group there are.
		*/

		public ModSprite(int pos, SpriteSheet sheet) {
			this(pos%32, pos/32, 1, 1, sheet);
		}

		private ModPx[][] modSpritePixels;

		/**
		 * 	Creates a reference to an 8x8 sprite in a spritesheet. Specify the position and sheet of the sprite to create.
		 * @param sx X position of the sprite in spritesheet coordinates.
		 * @param sy Y position of the sprite in spritesheet coordinates.
		 * @param sheet What spritesheet to use.
		 */
		public ModSprite(int sx, int sy, SpriteSheet sheet) {
			this(sx, sy, 1, 1, sheet);
		}
		public ModSprite(int sx, int sy, int sw, int sh, SpriteSheet sheet) {
			this(sx, sy, sw, sh, sheet, 0);
		}

		public ModSprite(int sx, int sy, int sw, int sh, SpriteSheet sheet, int mirror) {
			this(sx, sy, sw, sh, sheet, mirror, false);
		}
		public ModSprite(int sx, int sy, int sw, int sh, SpriteSheet sheet, int mirror, boolean onepixel) {
			super(new Px[0][0]);
			sheetLoc = new Rectangle(sx, sy, sw, sh);

			modSpritePixels = new ModPx[sh][sw];
			for (int r = 0; r < sh; r++)
				for (int c = 0; c < sw; c++)
					modSpritePixels[r][c] = new ModPx(sx + (onepixel ? 0 : c), sy + (onepixel ? 0 : r), mirror, sheet);

			spritePixels = modSpritePixels;
		}
		public ModSprite(int sx, int sy, int sw, int sh, SpriteSheet sheet, boolean onepixel, int[][] mirrors) {
			super(new Px[0][0]);
			sheetLoc = new Rectangle(sx, sy, sw, sh);

			modSpritePixels = new ModPx[sh][sw];
			for (int r = 0; r < sh; r++)
				for (int c = 0; c < sw; c++)
					modSpritePixels[r][c] = new ModPx(sx + (onepixel? 0 : c), sy + (onepixel ? 0 : r), mirrors[r][c], sheet);

			spritePixels = modSpritePixels;
		}

		public ModSprite(ModPx[][] pixels) {
			super(pixels);
			modSpritePixels = pixels;
		}

		@Override
		public void renderRow(int r, Screen screen, int x, int y) {
			ModPx[] row = modSpritePixels[r];
			for (int c = 0; c < row.length; c++) { // Loop across through each column
				screen.render(x + c * 8, y, row[c].sheetPos, row[c].mirror, row[c].spriteSheet, this.color, false, 0); // Render the sprite pixel.
			}
		}
		@Override
		public void renderRow(int r, Screen screen, int x, int y, int mirror) {
			ModPx[] row = modSpritePixels[r];
			for (int c = 0; c < row.length; c++) { // Loop across through each column
				screen.render(x + c * 8, y, row[c].sheetPos, mirror, row[c].spriteSheet, this.color, false, 0); // Render the sprite pixel.
			}
		}
		@Override
		public void renderRow(int r, Screen screen, int x, int y, int mirror, int whiteTint) {
			ModPx[] row = modSpritePixels[r];
			for (int c = 0; c < row.length; c++) {
				screen.render(x + c * 8, y, row[c].sheetPos, (mirror != -1 ? mirror : row[c].mirror), row[c].spriteSheet, whiteTint, false, 0);
			}
		}

		@Override
		public void renderRow(int r, Screen screen, int x, int y, int mirror, int whiteTint, int color) {
			ModPx[] row = modSpritePixels[r];
			for (int c = 0; c < row.length; c++) {
				screen.render(x + c * 8, y, row[c].sheetPos, (mirror != -1 ? mirror : row[c].mirror), row[c].spriteSheet, whiteTint, false, color);
			}
		}

		@Override
		protected void renderPixel(int c, int r, Screen screen, int x, int y, int mirror, int whiteTint) {
			screen.render(x, y, modSpritePixels[r][c].sheetPos, mirror, modSpritePixels[r][c].spriteSheet, whiteTint, false, color);
		}

		public void renderMirrorred(int fullMirror, Screen screen, int x, int y) {
			int h = spritePixels.length;
			int w = spritePixels[0].length;
			boolean mirrorX = (fullMirror&2)>0;
			boolean mirrorY = (fullMirror&2)>0;
			for (int row = 0; row < h; row++) { // Loop down through each row
				int my = mirrorX? h-1-row: row;
				for (int col = 0; col<w; col++) {
					int mx = mirrorY? w-1-col: col;
					renderPixel(mx, my, screen, x+8*col, y+8*row);
				}
			}
		}
		public void renderMirrorred(int fullMirror, Screen screen, int x, int y, int mirror) {
			int h = spritePixels.length;
			int w = spritePixels[0].length;
			boolean mirrorX = (fullMirror&1)>0;
			boolean mirrorY = (fullMirror&2)>0;
			for (int row = 0; row < h; row++) { // Loop down through each row
				int my = mirrorX? h-1-row: row;
				for (int col = 0; col<w; col++) {
					int mx = mirrorY? w-1-col: col;
					renderPixel(mx, my, screen, x+8*col, y+8*row, mirror);
				}
			}
		}
		public void renderMirrorred(int fullMirror, Screen screen, int x, int y, int mirror, int whiteTint) {
			int h = spritePixels.length;
			int w = spritePixels[0].length;
			boolean mirrorX = (fullMirror&1)>0;
			boolean mirrorY = (fullMirror&2)>0;
			for (int row = 0; row < h; row++) { // Loop down through each row
				int my = mirrorX? h-1-row: row;
				for (int col = 0; col<w; col++) {
					int mx = mirrorY? w-1-col: col;
					renderPixel(mx, my, screen, x+8*col, y+8*row, mirror, whiteTint);
				}
			}
		}

		public static class ModPx extends Px {
			protected SpriteSheet spriteSheet;

			public ModPx(int sheetX, int sheetY, int mirroring, SpriteSheet sheet) {
				super(sheetX, sheetY, mirroring, 0);
				// pixelX and pixelY are the relative positions each pixel should have relative to the top-left-most pixel of the sprite.
				this.spriteSheet = sheet;
			}
		}
	}

	// The original rotated pixel rendering lowers the performance, so this will not be added.
	// public static class MirrorableSprite extends Sprite {
	// 	public void renderRotated(Screen screen, int x, int y, int rotation) {
	// 		int h = spritePixels.length;
	// 		int w = spritePixels[0].length;
	// 		for (int r = 0; r < h; r++) {
	// 			for (int c = 0; c < w; c++) {
	// 				int xp = c;
	// 				int yp = r;
	// 				if (rotation == 1) {
	// 					xp = w-1-c;
	// 					yp = h-1-r;
	// 				} else if (rotation == 2) {
	// 					xp = r;
	// 					yp = w-1-c;
	// 				} else if (rotation == 3) {
	// 					xp = h-1-r;
	// 					yp = c;
	// 				}
	// 				renderPixelRotated(xp, yp, screen, x+8*c, y+8*r, rotation);
	// 			}
	// 		}
	// 	}
	// 	public void renderPixelRotated(int c, int r, Screen screen, int x, int y) {
	// 		renderPixelRotated(c, r, screen, x, y, 0);
	// 	}
	// 	public void renderPixelRotated(int c, int r, Screen screen, int x, int y, int rotation) {
	// 		renderPixelRotated(c, r, screen, x, y, rotation, this.color);
	// 	}
	// 	public void renderPixelRotated(int c, int r, Screen screen, int x, int y, int rotation, int whiteTint) {
	// 		if (spritePixels[r][c].spriteSheet != null) {
	// 			GraphicComp.renderRotated(x, y, spritePixels[r][c].sheetPos, rotation, spritePixels[r][c].spriteSheet, whiteTint, false); // Render the sprite pixel.
	// 		} else {
	// 			// GraphicComp.renderRotated(x, y, spritePixels[r][c].sheetPos, mirror, spritePixels[r][c].spriteSheetNum, whiteTint);
	// 		}
	// 	}
	// }
    // /** Renders an object from the sprite sheet based on screen coordinates, tile (SpriteSheet location), colors, and bits (for mirroring). I believe that xp and yp refer to the desired position of the upper-left-most pixel. */
    // public static void renderRotated(int xp, int yp, int tile, int bits, SpriteSheet sheet, int whiteTint, boolean fullbright) {
	// 	Screen screen = Renderer.screen;
	// 	// xp and yp are originally in level coordinates, but offset turns them to screen coordinates.
	// 	xp -= screen.xOffset; //account for screen offset
	// 	yp -= screen.yOffset;

	// 	int xTile = tile % 32; // Gets x position of the spritesheet "tile"
	// 	int yTile = tile / 32; // Gets y position
	// 	int toffs = xTile * 8 + yTile * 8 * sheet.width; // Gets the offset of the sprite into the spritesheet pixel array, the 8's represent the size of the box. (8 by 8 pixel sprite boxes)

	// 	// THIS LOOPS FOR EVERY PIXEL
	// 	for (int y = 0; y < 8; y++) { // Loops 8 times (because of the height of the tile)
	// 		int ys = y; // Current y pixel
	// 		if (y + yp < 0 || y + yp >= Screen.h) continue; // If the pixel is out of bounds, then skip the rest of the loop.
	// 		for (int x = 0; x < 8; x++) { // Loops 8 times (because of the width of the tile)
	// 			if (x + xp < 0 || x + xp >= Screen.w) continue; // Skip rest if out of bounds.

	// 			int xs = x; // Current x pixel
	// 			ys = y;
	// 			if (bits == 1) {
	// 				xs = 7-x;
	// 				ys = 7-y;
	// 			} else if (bits == 2) {
	// 				xs = y;
	// 				ys = 7-x;
	// 			} else if (bits == 3) {
	// 				xs = 7-y;
	// 				ys = x;
	// 			}

	// 			int col = sheet.pixels[toffs + xs + ys * sheet.width]; // Gets the color of the current pixel from the value stored in the sheet.

	// 			boolean isTransparent = (col >> 24 == 0);

	// 			if (!isTransparent) {
	// 				int index = (x + xp) + (y + yp) * Screen.w;

	// 				if (whiteTint != -1 && col == 0x1FFFFFF) {
	// 					// If this is white, write the whiteTint over it
	// 					screen.pixels[index] = whiteTint & 0xFFFFFF;
	// 				} else {
	// 					// Inserts the colors into the image
	// 					if (fullbright) {
	// 						screen.pixels[index] = Color.WHITE;
	// 					} else {
	// 						screen.pixels[index] = col & 0xFFFFFF;
	// 					}
	// 				}
	// 			}
	// 		}
	// 	}
	// }
}
