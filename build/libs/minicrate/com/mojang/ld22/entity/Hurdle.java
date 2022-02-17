// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Color;

public class Hurdle extends Furniture
{
    public Hurdle() {
        super("Hurdle");
        this.col = Color.get(-1, 200, 531, 430);
        this.sprite = 14;
        this.xr = 3;
        this.yr = 2;
        this.remove();
    }
    
    @Override
    public int getLightRadius() {
        return 0;
    }
    
    @Override
    protected void touchedBy(final Entity entity) {
    }
}
