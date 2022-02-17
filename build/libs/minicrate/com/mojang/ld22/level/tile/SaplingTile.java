// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.level.tile;

import com.mojang.ld22.entity.Mob;
import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.gfx.Screen;

public class SaplingTile extends Tile
{
    private Tile onType;
    private Tile growsTo;
    
    public SaplingTile(final int id, final Tile onType, final Tile growsTo) {
        super(id);
        this.onType = onType;
        this.growsTo = growsTo;
        this.connectsToSand = onType.connectsToSand;
        this.connectsToGrass = onType.connectsToGrass;
        this.connectsToWater = onType.connectsToWater;
        this.connectsToLava = onType.connectsToLava;
    }
    
    @Override
    public void render(final Screen screen, final Level level, final int x, final int y) {
        this.onType.render(screen, level, x, y);
        int col = Color.get(10, 40, 50, -1);
        screen.render(x * 16 + 4, y * 16 + 4, 107, col, 0);
        if (level.getFire(x, y) > 0) {
            col = Color.get(-1, 530, 551, 554);
            final int flicker = this.random.nextInt(this.random.nextInt(3) + 1);
            if (this.random.nextBoolean()) {
                screen.render(x * 16 + 4, y * 16 + 4, flicker + 14 + 96, col, 0);
            }
            else {
                screen.render(x * 16 + 4, y * 16 + 4, flicker + 14 + 96, col, 1);
            }
        }
    }
    
    @Override
    public void tick(final Level level, final int x, final int y) {
        if (level.getFire(x, y) > 0) {
            level.moreFire(x, y);
            final int count = this.random.nextInt(25) + 1;
            if (count == 4) {
                level.setFire(x, y, 0);
            }
            if (level.getFire(x, y) > 50) {
                level.setTile(x, y, SaplingTile.grass, 0);
                level.setFire(x, y, 0);
            }
        }
        final int age = level.getData(x, y) + 1;
        if (age > 100) {
            level.setTile(x, y, this.growsTo, 0);
            level.setFire(x, y, 0);
        }
        else {
            level.setData(x, y, age);
        }
    }
    
    @Override
    public boolean canBurn(final Level level, final int x, final int y, final Entity e) {
        return e.burnsThings();
    }
    
    @Override
    public void hurt(final Level level, final int x, final int y, final Mob source, final int dmg, final int attackDir) {
        level.setTile(x, y, this.onType, 0);
    }
}
