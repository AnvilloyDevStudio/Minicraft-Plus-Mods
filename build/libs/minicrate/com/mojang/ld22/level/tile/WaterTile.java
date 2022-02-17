// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.level.tile;

import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.gfx.Screen;
import java.util.Random;

public class WaterTile extends Tile
{
    private Random wRandom;
    
    public WaterTile(final int id) {
        super(id);
        this.wRandom = new Random();
        this.connectsToSand = true;
        this.connectsToWater = true;
    }
    
    @Override
    public void render(final Screen screen, final Level level, final int x, final int y) {
        this.wRandom.setSeed((WaterTile.tickCount + (x / 2 - y) * 4311) / 10 * 54687121L + x * 3271612L + y * 3412987161L);
        final int col = Color.get(5, 5, 115, 115);
        final int transitionColor1 = Color.get(3, 5, level.dirtColor - 111, level.dirtColor);
        final int transitionColor2 = Color.get(3, 5, level.sandColor - 110, level.sandColor);
        final boolean u = !level.getTile(x, y - 1).connectsToWater;
        final boolean d = !level.getTile(x, y + 1).connectsToWater;
        final boolean l = !level.getTile(x - 1, y).connectsToWater;
        final boolean r = !level.getTile(x + 1, y).connectsToWater;
        final boolean su = u && level.getTile(x, y - 1).connectsToSand;
        final boolean sd = d && level.getTile(x, y + 1).connectsToSand;
        final boolean sl = l && level.getTile(x - 1, y).connectsToSand;
        final boolean sr = r && level.getTile(x + 1, y).connectsToSand;
        if (!u && !l) {
            screen.render(x * 16 + 0, y * 16 + 0, this.wRandom.nextInt(4), col, this.wRandom.nextInt(4));
        }
        else {
            screen.render(x * 16 + 0, y * 16 + 0, (l ? 14 : 15) + (u ? 0 : 1) * 32, (su || sl) ? transitionColor2 : transitionColor1, 0);
        }
        if (!u && !r) {
            screen.render(x * 16 + 8, y * 16 + 0, this.wRandom.nextInt(4), col, this.wRandom.nextInt(4));
        }
        else {
            screen.render(x * 16 + 8, y * 16 + 0, (r ? 16 : 15) + (u ? 0 : 1) * 32, (su || sr) ? transitionColor2 : transitionColor1, 0);
        }
        if (!d && !l) {
            screen.render(x * 16 + 0, y * 16 + 8, this.wRandom.nextInt(4), col, this.wRandom.nextInt(4));
        }
        else {
            screen.render(x * 16 + 0, y * 16 + 8, (l ? 14 : 15) + (d ? 2 : 1) * 32, (sd || sl) ? transitionColor2 : transitionColor1, 0);
        }
        if (!d && !r) {
            screen.render(x * 16 + 8, y * 16 + 8, this.wRandom.nextInt(4), col, this.wRandom.nextInt(4));
        }
        else {
            screen.render(x * 16 + 8, y * 16 + 8, (r ? 16 : 15) + (d ? 2 : 1) * 32, (sd || sr) ? transitionColor2 : transitionColor1, 0);
        }
    }
    
    @Override
    public boolean mayPass(final Level level, final int x, final int y, final Entity e) {
        return e.canSwim();
    }
    
    @Override
    public void tick(final Level level, final int xt, final int yt) {
        int xn = xt;
        int yn = yt;
        if (this.random.nextBoolean()) {
            xn += this.random.nextInt(2) * 2 - 1;
        }
        else {
            yn += this.random.nextInt(2) * 2 - 1;
        }
        if (level.getTile(xn, yn) == Tile.hole) {
            level.setTile(xn, yn, this, 0);
        }
    }
}