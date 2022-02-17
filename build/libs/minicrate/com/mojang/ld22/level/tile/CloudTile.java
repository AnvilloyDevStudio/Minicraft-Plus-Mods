// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.level.tile;

import com.mojang.ld22.entity.ItemEntity;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.item.ToolType;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.gfx.Screen;

public class CloudTile extends Tile
{
    public CloudTile(final int id) {
        super(id);
    }
    
    @Override
    public void render(final Screen screen, final Level level, final int x, final int y) {
        final int col = Color.get(444, 444, 555, 555);
        final int transitionColor = Color.get(333, 444, 555, -1);
        final boolean u = level.getTile(x, y - 1) == Tile.infiniteFall;
        final boolean d = level.getTile(x, y + 1) == Tile.infiniteFall;
        final boolean l = level.getTile(x - 1, y) == Tile.infiniteFall;
        final boolean r = level.getTile(x + 1, y) == Tile.infiniteFall;
        final boolean ul = level.getTile(x - 1, y - 1) == Tile.infiniteFall;
        final boolean dl = level.getTile(x - 1, y + 1) == Tile.infiniteFall;
        final boolean ur = level.getTile(x + 1, y - 1) == Tile.infiniteFall;
        final boolean dr = level.getTile(x + 1, y + 1) == Tile.infiniteFall;
        if (!u && !l) {
            if (!ul) {
                screen.render(x * 16 + 0, y * 16 + 0, 17, col, 0);
            }
            else {
                screen.render(x * 16 + 0, y * 16 + 0, 7, transitionColor, 3);
            }
        }
        else {
            screen.render(x * 16 + 0, y * 16 + 0, (l ? 6 : 5) + (u ? 2 : 1) * 32, transitionColor, 3);
        }
        if (!u && !r) {
            if (!ur) {
                screen.render(x * 16 + 8, y * 16 + 0, 18, col, 0);
            }
            else {
                screen.render(x * 16 + 8, y * 16 + 0, 8, transitionColor, 3);
            }
        }
        else {
            screen.render(x * 16 + 8, y * 16 + 0, (r ? 4 : 5) + (u ? 2 : 1) * 32, transitionColor, 3);
        }
        if (!d && !l) {
            if (!dl) {
                screen.render(x * 16 + 0, y * 16 + 8, 20, col, 0);
            }
            else {
                screen.render(x * 16 + 0, y * 16 + 8, 39, transitionColor, 3);
            }
        }
        else {
            screen.render(x * 16 + 0, y * 16 + 8, (l ? 6 : 5) + (d ? 0 : 1) * 32, transitionColor, 3);
        }
        if (!d && !r) {
            if (!dr) {
                screen.render(x * 16 + 8, y * 16 + 8, 19, col, 0);
            }
            else {
                screen.render(x * 16 + 8, y * 16 + 8, 40, transitionColor, 3);
            }
        }
        else {
            screen.render(x * 16 + 8, y * 16 + 8, (r ? 4 : 5) + (d ? 0 : 1) * 32, transitionColor, 3);
        }
    }
    
    @Override
    public boolean mayPass(final Level level, final int x, final int y, final Entity e) {
        return true;
    }
    
    @Override
    public boolean interact(final Level level, final int xt, final int yt, final Player player, final Item item, final int attackDir) {
        if (item instanceof ToolItem) {
            final ToolItem tool = (ToolItem)item;
            if (tool.type == ToolType.shovel && player.payStamina(5)) {
                for (int count = this.random.nextInt(2) + 1, i = 0; i < count; ++i) {
                    level.add(new ItemEntity(new ResourceItem(Resource.cloud), xt * 16 + this.random.nextInt(10) + 3, yt * 16 + this.random.nextInt(10) + 3));
                }
                return true;
            }
        }
        return false;
    }
}
