// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.level.tile;

import com.mojang.ld22.entity.ItemEntity;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.entity.Mob;
import com.mojang.ld22.item.ToolType;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.gfx.Screen;

public class RadishTile extends Tile
{
    public RadishTile(final int id) {
        super(id);
    }
    
    @Override
    public void render(final Screen screen, final Level level, final int x, final int y) {
        final int age = level.getData(x, y);
        int col = Color.get(level.dirtColor - 121, level.dirtColor - 11, level.dirtColor, 50);
        int icon = age / 10;
        if (icon >= 3) {
            col = Color.get(level.dirtColor - 121, level.dirtColor - 11, 50 + icon * 100, 40 + (icon - 3) * 2 * 100);
            if (age == 50) {
                col = Color.get(555, 0, 50 + icon * 100, 40 + (icon - 3) * 2 * 100);
            }
            icon = 3;
        }
        screen.render(x * 16 + 0, y * 16 + 0, 109, col, 0);
        screen.render(x * 16 + 8, y * 16 + 0, 109, col, 0);
        screen.render(x * 16 + 0, y * 16 + 8, 109, col, 1);
        screen.render(x * 16 + 8, y * 16 + 8, 109, col, 1);
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
    public void tick(final Level level, final int xt, final int yt) {
        if (this.random.nextInt(2) == 0) {
            return;
        }
        final int age = level.getData(xt, yt);
        if (age < 50) {
            level.setData(xt, yt, age + 1);
        }
    }
    
    @Override
    public boolean mayPass(final Level level, final int x, final int y, final Entity e) {
        return e.canWalk();
    }
    
    @Override
    public boolean canBurn(final Level level, final int x, final int y, final Entity e) {
        return e.burnsThings();
    }
    
    @Override
    public boolean interact(final Level level, final int xt, final int yt, final Player player, final Item item, final int attackDir) {
        if (item instanceof ToolItem) {
            final ToolItem tool = (ToolItem)item;
            if (tool.type == ToolType.shovel && player.payStamina(4 - tool.level)) {
                level.setTile(xt, yt, Tile.dirt, 0);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void steppedOn(final Level level, final int xt, final int yt, final Entity entity) {
        if (this.random.nextInt(60) != 0) {
            return;
        }
        if (level.getData(xt, yt) < 2) {
            return;
        }
        this.harvest(level, xt, yt);
    }
    
    @Override
    public void hurt(final Level level, final int x, final int y, final Mob source, final int dmg, final int attackDir) {
        this.harvest(level, x, y);
    }
    
    private void harvest(final Level level, final int x, final int y) {
        final int age = level.getData(x, y);
        for (int count = this.random.nextInt(3), i = 0; i < count; ++i) {
            level.add(new ItemEntity(new ResourceItem(Resource.radishSeeds), x * 16 + this.random.nextInt(10) + 3, y * 16 + this.random.nextInt(10) + 3));
        }
        int count = 0;
        if (age == 50) {
            count = this.random.nextInt(3) + 2;
        }
        else if (age >= 40) {
            count = this.random.nextInt(2) + 1;
        }
        for (int i = 0; i < count; ++i) {
            level.add(new ItemEntity(new ResourceItem(Resource.radish), x * 16 + this.random.nextInt(10) + 3, y * 16 + this.random.nextInt(10) + 3));
        }
        level.setTile(x, y, Tile.dirt, 0);
    }
}
