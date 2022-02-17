// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.level.tile;

import com.mojang.ld22.sound.Sound;
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

public class FireTile extends Tile
{
    public FireTile(final int id) {
        super(id);
    }
    
    @Override
    public void render(final Screen screen, final Level level, final int x, final int y) {
        int col = Color.get(level.dirtColor, level.dirtColor, level.dirtColor - 111, level.dirtColor - 111);
        screen.render(x * 16 + 0, y * 16 + 0, 0, col, 0);
        screen.render(x * 16 + 8, y * 16 + 0, 1, col, 0);
        screen.render(x * 16 + 0, y * 16 + 8, 2, col, 0);
        screen.render(x * 16 + 8, y * 16 + 8, 3, col, 0);
        col = Color.get(-1, 530, 551, 554);
        final int flicker = this.random.nextInt(this.random.nextInt(3) + 1);
        final int flicker2 = this.random.nextInt(this.random.nextInt(5) + 2);
        final int flicker3 = this.random.nextInt(this.random.nextInt(5) + 2);
        final int flicker4 = this.random.nextInt(this.random.nextInt(5) + 2);
        final int flicker5 = this.random.nextInt(this.random.nextInt(5) + 2);
        if (this.random.nextBoolean()) {
            screen.render(x * 16 + 4, y * 16 + 4, flicker + 14 + 96, col, 1);
            screen.render(x * 16 + flicker2, y * 16 + flicker3, flicker + 14 + 96, col, 1);
            screen.render(x * 16 + flicker4, y * 16 + flicker5, flicker + 14 + 96, col, 1);
        }
        else {
            screen.render(x * 16 + 4, y * 16 + 4, flicker + 14 + 96, col, 0);
            screen.render(x * 16 + flicker2, y * 16 + flicker3, flicker + 14 + 96, col, 1);
            screen.render(x * 16 + flicker4, y * 16 + flicker5, flicker + 14 + 96, col, 1);
        }
    }
    
    public int getLightRadius() {
        final int r = (this.random.nextInt(9) + this.random.nextInt(9) + this.random.nextInt(9) + this.random.nextInt(9)) / 4 + 1;
        return r;
    }
    
    @Override
    public boolean mayPass(final Level level, final int x, final int y, final Entity e) {
        return e.canWalk();
    }
    
    @Override
    public void bumpedInto(final Level level, final int x, final int y, final Entity entity) {
        if (level.getFire(x, y) > 0) {
            entity.hurt(this, x, y, 1);
        }
    }
    
    @Override
    public boolean interact(final Level level, final int xt, final int yt, final Player player, final Item item, final int attackDir) {
        if (item instanceof ToolItem) {
            final ToolItem tool = (ToolItem)item;
            if (tool.type == ToolType.shovel && player.payStamina(4 - tool.level)) {
                level.setTile(xt, yt, Tile.hole, 0);
                level.add(new ItemEntity(new ResourceItem(Resource.dirt), xt * 16 + this.random.nextInt(10) + 3, yt * 16 + this.random.nextInt(10) + 3));
                Sound.monsterHurt.play();
                return true;
            }
            if (tool.type == ToolType.hoe && player.payStamina(4 - tool.level)) {
                level.setTile(xt, yt, Tile.farmland, 0);
                Sound.monsterHurt.play();
                return true;
            }
            if (tool.type == ToolType.staff && player.payStamina(4 - tool.level)) {
                level.setTile(xt, yt, Tile.flower, 0);
                Sound.monsterHurt.play();
                return true;
            }
            if (tool.type == ToolType.wand && player.payStamina(4 - tool.level)) {
                level.setTile(xt, yt, Tile.grass, 0);
                Sound.monsterHurt.play();
                return true;
            }
        }
        return false;
    }
    
    public boolean burnsThings() {
        return true;
    }
    
    @Override
    public int getLightRadius(final Level level, final int x, final int y) {
        final int r = (this.random.nextInt(10) + this.random.nextInt(10) + this.random.nextInt(10) + this.random.nextInt(10) + 1) / 8 + 1 + (31 - level.getFire(x, y)) / 10;
        return r;
    }
    
    @Override
    public void tick(final Level level, final int xt, final int yt) {
        if (level.getFire(xt, yt) == 0) {
            level.setTile(xt, yt, Tile.Scorched, 0);
        }
        level.moreFire(xt, yt);
        if (level.getFire(xt, yt) > 20) {
            int count = this.random.nextInt(2) + 1;
            if (count == 1) {
                level.add(new ItemEntity(new ResourceItem(Resource.coal), xt * 16 + this.random.nextInt(5) + 4, yt * 16 + this.random.nextInt(5) + 4));
            }
            count = this.random.nextInt(4) + 1;
            if (count == 1) {
                level.add(new ItemEntity(new ResourceItem(Resource.dirt), xt * 16 + this.random.nextInt(5) + 4, yt * 16 + this.random.nextInt(5) + 4));
            }
            count = this.random.nextInt(8) + 1;
            if (count == 1) {
                level.add(new ItemEntity(new ResourceItem(Resource.glass), xt * 16 + this.random.nextInt(5) + 4, yt * 16 + this.random.nextInt(5) + 4));
            }
            level.setTile(xt, yt, Tile.dirt, 0);
            level.setFire(xt, yt, 0);
        }
        if (this.random.nextInt(10) == 0) {
            int xq = xt - 1;
            int yq = yt - 1;
            if (level.getTile(xq, yq).flamable()) {
                level.moreFire(xq, yq);
            }
            xq = xt;
            if (level.getTile(xq, yq).flamable()) {
                level.moreFire(xq, yq);
            }
            xq = xt + 1;
            if (level.getTile(xq, yq).flamable()) {
                level.moreFire(xq, yq);
            }
            yq = yt;
            if (level.getTile(xq, yq).flamable()) {
                level.moreFire(xq, yq);
            }
            yq = yt + 1;
            if (level.getTile(xq, yq).flamable()) {
                level.moreFire(xq, yq);
            }
            xq = xt;
            if (level.getTile(xq, yq).flamable()) {
                level.moreFire(xq, yq);
            }
            xq = xt - 1;
            if (level.getTile(xq, yq).flamable()) {
                level.moreFire(xq, yq);
            }
            yq = yt;
            if (level.getTile(xq, yq).flamable()) {
                level.moreFire(xq, yq);
            }
        }
    }
}
