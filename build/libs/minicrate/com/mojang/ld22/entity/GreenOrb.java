// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Color;

public class GreenOrb extends Furniture
{
    public GreenOrb() {
        super("GreenOrb");
        this.col = Color.get(-1, 8, 24, 151);
        this.sprite = 7;
        this.xr = 3;
        this.yr = 2;
    }
    
    @Override
    public int getLightRadius() {
        return 7;
    }
    
    @Override
    public boolean isGreenOrb() {
        return true;
    }
}
