// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.level.tile;

import com.mojang.ld22.entity.ItemEntity;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.entity.particle.TextParticle;
import com.mojang.ld22.entity.particle.SmashParticle;
import com.mojang.ld22.item.ToolType;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.gfx.Screen;

public class HurdleTile extends Tile
{
    public HurdleTile(final int id) {
        super(id);
    }
    
    @Override
    public void render(final Screen screen, final Level level, final int x, final int y) {
        final int col = Color.get(level.grassColor, 200, 531, 430);
        screen.render(x * 16 + 0, y * 16 + 0, 27, col, 0);
        screen.render(x * 16 + 8, y * 16 + 0, 28, col, 0);
        screen.render(x * 16 + 0, y * 16 + 8, 59, col, 0);
        screen.render(x * 16 + 8, y * 16 + 8, 60, col, 0);
    }
    
    @Override
    public boolean mayPass(final Level level, final int x, final int y, final Entity e) {
        return false;
    }
    
    @Override
    public boolean interact(final Level level, final int xt, final int yt, final Player player, final Item item, final int attackDir) {
        if (item instanceof ToolItem) {
            final ToolItem tool = (ToolItem)item;
            if (tool.type == ToolType.pickaxe && player.payStamina(6 - tool.level)) {
                this.hurt(level, xt, yt, 1);
                return true;
            }
        }
        return false;
    }
    
    public void hurt(final Level level, final int x, final int y, final int dmg) {
        final int damage = level.getData(x, y) + 1;
        level.add(new SmashParticle(x * 16 + 8, y * 16 + 8));
        level.add(new TextParticle(new StringBuilder().append(dmg).toString(), x * 16 + 8, y * 16 + 8, Color.get(-1, 500, 500, 500)));
        if (dmg > 0) {
            int count = this.random.nextInt(2);
            if (damage >= this.random.nextInt(10) + 3) {
                level.setTile(x, y, Tile.dirt, 0);
                count += 2;
            }
            else {
                level.setData(x, y, damage);
            }
            for (int i = 0; i < count; ++i) {
                level.add(new ItemEntity(new ResourceItem(Resource.wood), x * 16 + this.random.nextInt(10) + 3, y * 16 + this.random.nextInt(10) + 3));
            }
        }
    }
    
    @Override
    public void bumpedInto(final Level level, final int x, final int y, final Entity entity) {
    }
}
