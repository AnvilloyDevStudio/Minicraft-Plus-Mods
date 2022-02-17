// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.level.tile;

import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.item.ToolType;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.gfx.Screen;

public class FarmTile extends Tile
{
    public FarmTile(final int id) {
        super(id);
    }
    
    @Override
    public void render(final Screen screen, final Level level, final int x, final int y) {
        final int col = Color.get(level.dirtColor - 121, level.dirtColor - 11, level.dirtColor, level.dirtColor + 111);
        screen.render(x * 16 + 0, y * 16 + 0, 34, col, 1);
        screen.render(x * 16 + 8, y * 16 + 0, 34, col, 0);
        screen.render(x * 16 + 0, y * 16 + 8, 34, col, 0);
        screen.render(x * 16 + 8, y * 16 + 8, 34, col, 1);
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
    public void tick(final Level level, final int xt, final int yt) {
        final int age = level.getData(xt, yt);
        if (age < 5) {
            level.setData(xt, yt, age + 1);
        }
    }
    
    @Override
    public boolean mayPass(final Level level, final int x, final int y, final Entity e) {
        return e.canWalk();
    }
    
    @Override
    public void steppedOn(final Level level, final int xt, final int yt, final Entity entity) {
        if (this.random.nextInt(60) != 0) {
            return;
        }
        if (level.getData(xt, yt) < 5) {
            return;
        }
        level.setTile(xt, yt, Tile.dirt, 0);
    }
}
