// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Color;

public class GemLantern extends Furniture
{
    public GemLantern() {
        super("GemLantern");
        this.col = Color.get(-1, 101, 404, 545);
        this.sprite = 5;
        this.xr = 3;
        this.yr = 2;
    }
    
    @Override
    public int getLightRadius() {
        return 16;
    }
}
