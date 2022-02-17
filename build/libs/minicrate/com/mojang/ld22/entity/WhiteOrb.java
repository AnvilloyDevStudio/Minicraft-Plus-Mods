// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Color;

public class WhiteOrb extends Furniture
{
    public WhiteOrb() {
        super("WhiteOrb");
        this.col = Color.get(-1, 110, 445, 555);
        this.sprite = 7;
        this.xr = 3;
        this.yr = 2;
    }
    
    @Override
    public int getLightRadius() {
        return 12;
    }
    
    @Override
    public boolean isWhiteOrb() {
        return true;
    }
}
