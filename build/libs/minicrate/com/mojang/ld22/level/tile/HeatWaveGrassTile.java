// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.level.tile;

import com.mojang.ld22.entity.ItemEntity;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.sound.Sound;
import com.mojang.ld22.item.ToolType;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.gfx.Screen;

public class HeatWaveGrassTile extends Tile
{
    public HeatWaveGrassTile(final int id) {
        super(id);
        this.connectsToGrass = true;
    }
    
    @Override
    public void render(final Screen screen, final Level level, final int x, final int y) {
        final int col = Color.get(level.grassColor, level.grassColor, level.grassColor + 111, level.grassColor + 111);
        final int transitionColor = Color.get(level.grassColor - 111, level.grassColor, level.grassColor + 111, level.dirtColor);
        final boolean u = !level.getTile(x, y - 1).connectsToGrass;
        final boolean d = !level.getTile(x, y + 1).connectsToGrass;
        final boolean l = !level.getTile(x - 1, y).connectsToGrass;
        final boolean r = !level.getTile(x + 1, y).connectsToGrass;
        if (level.getFire(x, y) < 1) {
            if (!u && !l) {
                screen.render(x * 16 + 0, y * 16 + 0, 0, col, 0);
            }
            else {
                screen.render(x * 16 + 0, y * 16 + 0, (l ? 11 : 12) + (u ? 0 : 1) * 32, transitionColor, 0);
            }
            if (!u && !r) {
                screen.render(x * 16 + 8, y * 16 + 0, 1, col, 0);
            }
            else {
                screen.render(x * 16 + 8, y * 16 + 0, (r ? 13 : 12) + (u ? 0 : 1) * 32, transitionColor, 0);
            }
            if (!d && !l) {
                screen.render(x * 16 + 0, y * 16 + 8, 2, col, 0);
            }
            else {
                screen.render(x * 16 + 0, y * 16 + 8, (l ? 11 : 12) + (d ? 2 : 1) * 32, transitionColor, 0);
            }
            if (!d && !r) {
                screen.render(x * 16 + 8, y * 16 + 8, 3, col, 0);
            }
            else {
                screen.render(x * 16 + 8, y * 16 + 8, (r ? 13 : 12) + (d ? 2 : 1) * 32, transitionColor, 0);
            }
        }
        else if (this.random.nextBoolean()) {
            if (!u && !l) {
                screen.render(x * 16 + 1, y * 16 + 0, 0, col, 0);
            }
            else {
                screen.render(x * 16 + 1, y * 16 + 0, (l ? 11 : 12) + (u ? 0 : 1) * 32, transitionColor, 0);
            }
            if (!u && !r) {
                screen.render(x * 16 + 7, y * 16 + 0, 1, col, 0);
            }
            else {
                screen.render(x * 16 + 7, y * 16 + 0, (r ? 13 : 12) + (u ? 0 : 1) * 32, transitionColor, 0);
            }
            if (!d && !l) {
                screen.render(x * 16 + 1, y * 16 + 8, 2, col, 0);
            }
            else {
                screen.render(x * 16 + 1, y * 16 + 8, (l ? 11 : 12) + (d ? 2 : 1) * 32, transitionColor, 0);
            }
            if (!d && !r) {
                screen.render(x * 16 + 7, y * 16 + 8, 3, col, 0);
            }
            else {
                screen.render(x * 16 + 7, y * 16 + 8, (r ? 13 : 12) + (d ? 2 : 1) * 32, transitionColor, 0);
            }
        }
        else {
            if (!u && !l) {
                screen.render(x * 16 + 0, y * 16 + 0, 0, col, 0);
            }
            else {
                screen.render(x * 16 + 0, y * 16 + 0, (l ? 11 : 12) + (u ? 0 : 1) * 32, transitionColor, 0);
            }
            if (!u && !r) {
                screen.render(x * 16 + 8, y * 16 + 0, 1, col, 0);
            }
            else {
                screen.render(x * 16 + 8, y * 16 + 0, (r ? 13 : 12) + (u ? 0 : 1) * 32, transitionColor, 0);
            }
            if (!d && !l) {
                screen.render(x * 16 + 0, y * 16 + 8, 2, col, 0);
            }
            else {
                screen.render(x * 16 + 0, y * 16 + 8, (l ? 11 : 12) + (d ? 2 : 1) * 32, transitionColor, 0);
            }
            if (!d && !r) {
                screen.render(x * 16 + 8, y * 16 + 8, 3, col, 0);
            }
            else {
                screen.render(x * 16 + 8, y * 16 + 8, (r ? 13 : 12) + (d ? 2 : 1) * 32, transitionColor, 0);
            }
        }
    }
    
    @Override
    public void tick(final Level level, final int xt, final int yt) {
        if (this.random.nextInt(20) == 0) {
            level.setFire(xt, yt, 0);
        }
        if (this.random.nextInt(40) != 0) {
            return;
        }
        int xn = xt;
        int yn = yt;
        if (this.random.nextBoolean()) {
            xn += this.random.nextInt(2) * 2 - 1;
        }
        else {
            yn += this.random.nextInt(2) * 2 - 1;
        }
        if (level.getTile(xn, yn) == Tile.dirt) {
            level.setTile(xn, yn, this, 0);
            level.setFire(xn, yn, 0);
        }
    }
    
    @Override
    public boolean mayPass(final Level level, final int x, final int y, final Entity e) {
        return e.canWalk();
    }
    
    @Override
    public boolean interact(final Level level, final int xt, final int yt, final Player player, final Item item, final int attackDir) {
        if (item instanceof ToolItem) {
            final ToolItem tool = (ToolItem)item;
            if (tool.type == ToolType.shovel && player.payStamina(4 - tool.level)) {
                level.setTile(xt, yt, Tile.dirt, 0);
                Sound.monsterHurt.play();
                if (this.random.nextInt(5) == 0) {
                    level.add(new ItemEntity(new ResourceItem(Resource.wheatSeeds), xt * 16 + this.random.nextInt(10) + 3, yt * 16 + this.random.nextInt(10) + 3));
                    return true;
                }
            }
            if (tool.type == ToolType.hoe && player.payStamina(4 - tool.level)) {
                Sound.monsterHurt.play();
                if (this.random.nextInt(5) == 0) {
                    level.add(new ItemEntity(new ResourceItem(Resource.wheatSeeds), xt * 16 + this.random.nextInt(10) + 3, yt * 16 + this.random.nextInt(10) + 3));
                    return true;
                }
                level.setTile(xt, yt, Tile.farmland, 0);
                return true;
            }
        }
        return false;
    }
}
