// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.level.tile;

import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.gfx.Screen;

public class StoneTile extends Tile
{
    public StoneTile(final int id) {
        super(id);
    }
    
    @Override
    public void render(final Screen screen, final Level level, final int x, final int y) {
        final int rc1 = 111;
        final int rc2 = 333;
        final int rc3 = 555;
        screen.render(x * 16 + 0, y * 16 + 0, 32, Color.get(rc1, level.dirtColor, rc2, rc3), 0);
        screen.render(x * 16 + 8, y * 16 + 0, 32, Color.get(rc1, level.dirtColor, rc2, rc3), 0);
        screen.render(x * 16 + 0, y * 16 + 8, 32, Color.get(rc1, level.dirtColor, rc2, rc3), 0);
        screen.render(x * 16 + 8, y * 16 + 8, 32, Color.get(rc1, level.dirtColor, rc2, rc3), 0);
    }
    
    @Override
    public boolean mayPass(final Level level, final int x, final int y, final Entity e) {
        return e.canPass();
    }
}
