// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.level.tile;

import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.gfx.Screen;

public class StairsTile extends Tile
{
    private boolean leadsUp;
    
    public StairsTile(final int id, final boolean leadsUp) {
        super(id);
        this.leadsUp = leadsUp;
    }
    
    @Override
    public void render(final Screen screen, final Level level, final int x, final int y) {
        final int color = Color.get(level.dirtColor, 0, 333, 444);
        int xt = 0;
        if (this.leadsUp) {
            xt = 2;
        }
        screen.render(x * 16 + 0, y * 16 + 0, xt + 64, color, 0);
        screen.render(x * 16 + 8, y * 16 + 0, xt + 1 + 64, color, 0);
        screen.render(x * 16 + 0, y * 16 + 8, xt + 96, color, 0);
        screen.render(x * 16 + 8, y * 16 + 8, xt + 1 + 96, color, 0);
    }
    
    @Override
    public boolean mayPass(final Level level, final int x, final int y, final Entity e) {
        return e.canWalk();
    }
}
