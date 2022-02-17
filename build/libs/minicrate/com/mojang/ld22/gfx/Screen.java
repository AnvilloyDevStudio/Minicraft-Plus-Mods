// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.gfx;

public class Screen
{
    public int xOffset;
    public int yOffset;
    public static final int BIT_MIRROR_X = 1;
    public static final int BIT_MIRROR_Y = 2;
    public final int w;
    public final int h;
    public int[] pixels;
    private SpriteSheet sheet;
    private int[] dither;
    
    public Screen(final int w, final int h, final SpriteSheet sheet) {
        this.dither = new int[] { 0, 8, 2, 10, 12, 4, 14, 6, 3, 11, 1, 9, 15, 7, 13, 5 };
        this.sheet = sheet;
        this.w = w;
        this.h = h;
        this.pixels = new int[w * h];
    }
    
    public void clear(final int color) {
        for (int i = 0; i < this.pixels.length; ++i) {
            this.pixels[i] = color;
        }
    }
    
    public void render(int xp, int yp, final int tile, final int colors, final int bits) {
        xp -= this.xOffset;
        yp -= this.yOffset;
        final boolean mirrorX = (bits & 0x1) > 0;
        final boolean mirrorY = (bits & 0x2) > 0;
        final int xTile = tile % 32;
        final int yTile = tile / 32;
        final int toffs = xTile * 8 + yTile * 8 * this.sheet.width;
        for (int y = 0; y < 8; ++y) {
            int ys = y;
            if (mirrorY) {
                ys = 7 - y;
            }
            if (y + yp >= 0) {
                if (y + yp < this.h) {
                    for (int x = 0; x < 8; ++x) {
                        if (x + xp >= 0) {
                            if (x + xp < this.w) {
                                int xs = x;
                                if (mirrorX) {
                                    xs = 7 - x;
                                }
                                final int col = colors >> this.sheet.pixels[xs + ys * this.sheet.width + toffs] * 8 & 0xFF;
                                if (col < 255) {
                                    this.pixels[x + xp + (y + yp) * this.w] = col;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void lineRender(int xp, int yp, final int tile, final int colors, final int bits, final int row) {
        xp -= this.xOffset;
        yp -= this.yOffset;
        final boolean mirrorX = (bits & 0x1) > 0;
        final boolean mirrorY = (bits & 0x2) > 0;
        final int xTile = tile % 32;
        final int yTile = tile / 32;
        final int toffs = xTile * 8 + yTile * 8 * this.sheet.width;
        for (int y = 0; y < 8; ++y) {
            int ys = y;
            if (mirrorY) {
                ys = 7 - y;
            }
            if (y + yp >= 0) {
                if (y + yp < this.h) {
                    for (int x = 0; x < 8; ++x) {
                        if (x + xp >= 0) {
                            if (x + xp < this.w) {
                                int xs = x;
                                if (mirrorX) {
                                    xs = 7 - x;
                                }
                                final int col = colors >> this.sheet.pixels[xs + ys * this.sheet.width + toffs] * 8 & 0xFF;
                                if (y == row && col < 255) {
                                    this.pixels[x + xp + (y + yp) * this.w] = col;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void setOffset(final int xOffset, final int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }
    
    public void overlay(final Screen screen2, final int xa, final int ya) {
        final int[] oPixels = screen2.pixels;
        int i = 0;
        for (int y = 0; y < this.h; ++y) {
            for (int x = 0; x < this.w; ++x) {
                if (oPixels[i] / 10 <= this.dither[(x + xa & 0x3) + (y + ya & 0x3) * 4]) {
                    this.pixels[i] = 0;
                }
                ++i;
            }
        }
    }
    
    public void renderLight(int x, int y, final int r) {
        x -= this.xOffset;
        y -= this.yOffset;
        int x2 = x - r;
        int x3 = x + r;
        int y2 = y - r;
        int y3 = y + r;
        if (x2 < 0) {
            x2 = 0;
        }
        if (y2 < 0) {
            y2 = 0;
        }
        if (x3 > this.w) {
            x3 = this.w;
        }
        if (y3 > this.h) {
            y3 = this.h;
        }
        for (int yy = y2; yy < y3; ++yy) {
            int yd = yy - y;
            yd *= yd;
            for (int xx = x2; xx < x3; ++xx) {
                final int xd = xx - x;
                final int dist = xd * xd + yd;
                if (dist <= r * r) {
                    final int br = 255 - dist * 255 / (r * r);
                    if (this.pixels[xx + yy * this.w] < br) {
                        this.pixels[xx + yy * this.w] = br;
                    }
                }
            }
        }
    }
}
