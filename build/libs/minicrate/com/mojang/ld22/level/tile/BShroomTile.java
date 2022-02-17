// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.level.tile;

import com.mojang.ld22.entity.Mob;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.gfx.Screen;

public class BShroomTile extends Tile
{
    private Tile onType;
    
    public BShroomTile(final int id) {
        super(id);
        this.onType = BShroomTile.grass;
        this.connectsToGrass = true;
    }
    
    @Override
    public void render(final Screen screen, final Level level, final int x, final int y) {
        this.onType.render(screen, level, x, y);
        final int col = Color.get(10, 40, 50, -1);
        screen.render(x * 16 + 4, y * 16 + 4, 108, col, 0);
    }
    
    @Override
    public void tick(final Level level, final int x, final int y) {
        final int age = level.getData(x, y) + 1;
        if (age > 25) {
            level.setTile(x, y, BShroomTile.shroom, 0);
            level.setFire(x, y, 0);
        }
        else {
            level.setData(x, y, age);
        }
    }
    
    @Override
    public int getLightRadius(final Level level, final int x, final int y) {
        return 1;
    }
    
    @Override
    public void hurt(final Level level, final int x, final int y, final Mob source, final int dmg, final int attackDir) {
        level.setTile(x, y, BShroomTile.grass, 0);
        level.setFire(x, y, 0);
    }
}
