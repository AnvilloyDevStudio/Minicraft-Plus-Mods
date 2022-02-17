// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.entity;

import com.mojang.ld22.level.tile.Tile;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.gfx.Color;

public class Fire extends Furniture
{
    public Fire() {
        super("Fire");
        this.col = Color.get(-1, 530, 551, 554);
        this.sprite = 9;
        this.xr = 3;
        this.yr = 2;
    }
    
    public void bumpedInto(final Level level, final int x, final int y, final Entity entity) {
        entity.hurt(this, x, y, 1);
        level.setTile(x, y, Tile.fire, 0);
        level.setFire(x, y, 1);
        entity.remove();
    }
    
    @Override
    public int getLightRadius() {
        return 12;
    }
}
