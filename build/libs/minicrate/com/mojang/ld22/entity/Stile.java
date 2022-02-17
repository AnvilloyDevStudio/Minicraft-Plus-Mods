// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.entity;

import com.mojang.ld22.level.Level;
import com.mojang.ld22.gfx.Color;

public class Stile extends Furniture
{
    public Stile() {
        super("Stile");
        this.col = Color.get(-1, 200, 320, 531);
        this.sprite = 12;
        this.xr = 3;
        this.yr = 2;
    }
    
    public boolean mayPass(final Level level, final int x, final int y, final Entity e) {
        return e.isPlayer();
    }
    
    @Override
    public int getLightRadius() {
        return 6;
    }
    
    @Override
    public boolean blocks(final Entity e) {
        return !e.isPlayer();
    }
    
    @Override
    protected void touchedBy(final Entity entity) {
    }
}
