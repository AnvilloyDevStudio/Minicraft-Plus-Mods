// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Color;

public class IronLantern extends Furniture
{
    public IronLantern() {
        super("IronLantern");
        this.col = Color.get(-1, 222, 333, 555);
        this.sprite = 5;
        this.xr = 3;
        this.yr = 2;
    }
    
    @Override
    public int getLightRadius() {
        return 12;
    }
}
