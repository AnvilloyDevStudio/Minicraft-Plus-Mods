// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.level.tile;

import com.mojang.ld22.entity.AirWizard;
import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.gfx.Screen;

public class InfiniteFallTile extends Tile
{
    public InfiniteFallTile(final int id) {
        super(id);
    }
    
    @Override
    public void render(final Screen screen, final Level level, final int x, final int y) {
    }
    
    @Override
    public void tick(final Level level, final int xt, final int yt) {
    }
    
    @Override
    public boolean mayPass(final Level level, final int x, final int y, final Entity e) {
        return e instanceof AirWizard;
    }
}
