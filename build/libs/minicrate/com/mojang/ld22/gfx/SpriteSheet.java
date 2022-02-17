// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.gfx;

import java.awt.image.BufferedImage;

public class SpriteSheet
{
    public int width;
    public int height;
    public int[] pixels;
    
    public SpriteSheet(final BufferedImage image) {
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.pixels = image.getRGB(0, 0, this.width, this.height, null, 0, this.width);
        for (int i = 0; i < this.pixels.length; ++i) {
            this.pixels[i] = (this.pixels[i] & 0xFF) / 64;
        }
    }
}
