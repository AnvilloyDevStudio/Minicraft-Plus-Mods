// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.gfx;

public class Color
{
    public static int get(final int a, final int b, final int c, final int d) {
        return (get(d) << 24) + (get(c) << 16) + (get(b) << 8) + get(a);
    }
    
    public static int get(final int d) {
        if (d < 0) {
            return 255;
        }
        final int r = d / 100 % 10;
        final int g = d / 10 % 10;
        final int b = d % 10;
        return r * 36 + g * 6 + b;
    }
}
