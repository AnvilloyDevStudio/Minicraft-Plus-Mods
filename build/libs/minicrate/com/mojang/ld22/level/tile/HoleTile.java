// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.level.tile;

import com.mojang.ld22.entity.ItemEntity;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.entity.particle.TextParticle;
import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.entity.particle.SmashParticle;
import com.mojang.ld22.item.ToolType;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.gfx.Screen;

public class HoleTile extends Tile
{
    public HoleTile(final int id) {
        super(id);
        this.connectsToSand = true;
        this.connectsToWater = true;
        this.connectsToLava = true;
    }
    
    @Override
    public void render(final Screen screen, final Level level, final int x, final int y) {
        final int col = Color.get(111, 111, 110, 110);
        final int transitionColor1 = Color.get(3, 111, level.dirtColor - 111, level.dirtColor);
        final int transitionColor2 = Color.get(3, 111, level.sandColor - 110, level.sandColor);
        final boolean u = !level.getTile(x, y - 1).connectsToLiquid();
        final boolean d = !level.getTile(x, y + 1).connectsToLiquid();
        final boolean l = !level.getTile(x - 1, y).connectsToLiquid();
        final boolean r = !level.getTile(x + 1, y).connectsToLiquid();
        final boolean su = u && level.getTile(x, y - 1).connectsToSand;
        final boolean sd = d && level.getTile(x, y + 1).connectsToSand;
        final boolean sl = l && level.getTile(x - 1, y).connectsToSand;
        final boolean sr = r && level.getTile(x + 1, y).connectsToSand;
        if (!u && !l) {
            screen.render(x * 16 + 0, y * 16 + 0, 0, col, 0);
        }
        else {
            screen.render(x * 16 + 0, y * 16 + 0, (l ? 14 : 15) + (u ? 0 : 1) * 32, (su || sl) ? transitionColor2 : transitionColor1, 0);
        }
        if (!u && !r) {
            screen.render(x * 16 + 8, y * 16 + 0, 1, col, 0);
        }
        else {
            screen.render(x * 16 + 8, y * 16 + 0, (r ? 16 : 15) + (u ? 0 : 1) * 32, (su || sr) ? transitionColor2 : transitionColor1, 0);
        }
        if (!d && !l) {
            screen.render(x * 16 + 0, y * 16 + 8, 2, col, 0);
        }
        else {
            screen.render(x * 16 + 0, y * 16 + 8, (l ? 14 : 15) + (d ? 2 : 1) * 32, (sd || sl) ? transitionColor2 : transitionColor1, 0);
        }
        if (!d && !r) {
            screen.render(x * 16 + 8, y * 16 + 8, 3, col, 0);
        }
        else {
            screen.render(x * 16 + 8, y * 16 + 8, (r ? 16 : 15) + (d ? 2 : 1) * 32, (sd || sr) ? transitionColor2 : transitionColor1, 0);
        }
    }
    
    @Override
    public boolean interact(final Level level, final int xt, final int yt, final Player player, final Item item, final int attackDir) {
        if (item instanceof ToolItem) {
            final ToolItem tool = (ToolItem)item;
            if (tool.type == ToolType.pickaxe && player.payStamina(4 - tool.level)) {
                this.hurt(level, xt, yt, this.random.nextInt(10) + tool.level * 5 + 10);
                return true;
            }
        }
        return false;
    }
    
    public void hurt(final Level level, final int x, final int y, final int dmg) {
        final int damage = level.getData(x, y) + dmg;
        level.add(new SmashParticle(x * 16 + 8, y * 16 + 8));
        level.add(new TextParticle(new StringBuilder().append(dmg).toString(), x * 16 + 8, y * 16 + 8, Color.get(-1, 500, 500, 500)));
        int count = this.random.nextInt(5) + 1;
        if (count == 4) {
            level.add(new ItemEntity(new ResourceItem(Resource.stone), x * 16 + this.random.nextInt(10) + 3, y * 16 + this.random.nextInt(10) + 3));
        }
        count = this.random.nextInt(2) + 1;
        if (count == 1) {
            level.add(new ItemEntity(new ResourceItem(Resource.dirt), x * 16 + this.random.nextInt(10) + 3, y * 16 + this.random.nextInt(10) + 3));
        }
        count = this.random.nextInt(5) + 1;
        if (count == 4) {
            level.add(new ItemEntity(new ResourceItem(Resource.coal), x * 16 + this.random.nextInt(10) + 3, y * 16 + this.random.nextInt(10) + 3));
        }
        count = this.random.nextInt(10) + 1;
        if (count == 4) {
            level.add(new ItemEntity(new ResourceItem(Resource.ironOre), x * 16 + this.random.nextInt(10) + 3, y * 16 + this.random.nextInt(10) + 3));
        }
        count = this.random.nextInt(15) + 1;
        if (count == 4) {
            level.add(new ItemEntity(new ResourceItem(Resource.goldOre), x * 16 + this.random.nextInt(10) + 3, y * 16 + this.random.nextInt(10) + 3));
        }
        count = this.random.nextInt(20) + 1;
        if (count == 4) {
            level.add(new ItemEntity(new ResourceItem(Resource.gem), x * 16 + this.random.nextInt(10) + 3, y * 16 + this.random.nextInt(10) + 3));
        }
        if (damage >= 50) {
            level.setTile(x, y, Tile.stairsDown, 0);
        }
        else {
            level.setData(x, y, damage);
        }
    }
    
    @Override
    public boolean mayPass(final Level level, final int x, final int y, final Entity e) {
        return e.canDig();
    }
}
