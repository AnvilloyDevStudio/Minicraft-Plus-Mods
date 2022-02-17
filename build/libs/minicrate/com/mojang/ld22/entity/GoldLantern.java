// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Color;

public class GoldLantern extends Furniture
{
    public GoldLantern() {
        super("GoldLantern");
        this.col = Color.get(-1, 110, 330, 553);
        this.sprite = 5;
        this.xr = 3;
        this.yr = 2;
    }
    
    @Override
    public int getLightRadius() {
        return 14;
    }
}
