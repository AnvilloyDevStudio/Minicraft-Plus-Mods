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

public class LongGrassTile extends Tile
{
    int toggleWave;
    
    public LongGrassTile(final int id) {
        super(id);
        this.toggleWave = 0;
        this.connectsToGrass = true;
    }
    
    @Override
    public void render(final Screen screen, final Level level, final int x, final int y) {
        LongGrassTile.grass.render(screen, level, x, y);
        int col = Color.get(-1, 130, 140, 250);
        if (this.toggleWave == 0) {
            screen.render(x * 16 + 0, y * 16 + 0, 113, col, 0);
            screen.render(x * 16 + 8, y * 16 + 0, 113, col, 0);
            screen.render(x * 16 + 0, y * 16 + 8, 113, col, 0);
            screen.render(x * 16 + 8, y * 16 + 8, 113, col, 0);
        }
        else {
            screen.render(x * 16 + 0, y * 16 + 0, 115, col, 0);
            screen.render(x * 16 + 8, y * 16 + 0, 115, col, 0);
            screen.render(x * 16 + 0, y * 16 + 8, 115, col, 0);
            screen.render(x * 16 + 8, y * 16 + 8, 115, col, 0);
        }
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
        if (this.random.nextInt(2000) == 0) {
            level.setTile(xt, yt, LongGrassTile.grass, 0);
            level.setFire(xt, yt, 0);
        }
        if (this.random.nextInt(20) == 1) {
            ++this.toggleWave;
            if (this.toggleWave > 1) {
                this.toggleWave = 0;
            }
        }
        if (level.getFire(xt, yt) > 0) {
            level.moreFire(xt, yt);
            if (level.getFire(xt, yt) > 20) {
                level.setTile(xt, yt, Tile.Scorched, 0);
                level.setFire(xt, yt, 0);
            }
            if (this.random.nextInt(4) == 0) {
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
        final int xn = xt;
        final int yn = yt;
        if (level.getTile(xn, yn) == Tile.dirt) {
            level.setTile(xn, yn, LongGrassTile.grass, 0);
            level.setFire(xn, yn, 0);
        }
    }
    
    @Override
    public void bumpedInto(final Level level, final int x, final int y, final Entity entity) {
        if (level.getFire(x, y) > 0) {
            entity.hurt(this, x, y, 1);
        }
    }
    
    @Override
    public boolean mayPass(final Level level, final int x, final int y, final Entity e) {
        return e.canWalk();
    }
    
    @Override
    public boolean mayEat(final Level level, final int x, final int y, final Entity e) {
        return e.grazes();
    }
    
    @Override
    public boolean canBurn(final Level level, final int x, final int y, final Entity e) {
        return e.burnsThings();
    }
    
    @Override
    public boolean flamable() {
        return true;
    }
    
    @Override
    public boolean interact(final Level level, final int xt, final int yt, final Player player, final Item item, final int attackDir) {
        if (item instanceof ToolItem) {
            final ToolItem tool = (ToolItem)item;
            if (tool.type == ToolType.shovel && player.payStamina(4 - tool.level)) {
                level.setTile(xt, yt, Tile.dirt, 0);
                level.setFire(xt, yt, 0);
                Sound.monsterHurt.play();
                if (this.random.nextInt(3) == 0) {
                    this.throwSeeds(level, xt, yt);
                    return true;
                }
            }
            if (tool.type == ToolType.hoe && player.payStamina(4 - tool.level)) {
                Sound.monsterHurt.play();
                if (this.random.nextInt(3) == 0) {
                    this.throwSeeds(level, xt, yt);
                    return true;
                }
                level.setTile(xt, yt, Tile.farmland, 0);
                level.setFire(xt, yt, 0);
                return true;
            }
            else if (tool.type == ToolType.lighter) {
                if (player.payStamina(4 - tool.level)) {
                    level.setFire(xt, yt, 1);
                    return true;
                }
                return true;
            }
            else if (tool.type == ToolType.sword && player.payStamina(4 - tool.level)) {
                Sound.monsterHurt.play();
                if (this.random.nextInt(3) == 0) {
                    this.throwSeeds(level, xt, yt);
                }
                level.add(new ItemEntity(new ResourceItem(Resource.straw), xt * 16 + this.random.nextInt(10) + 3, yt * 16 + this.random.nextInt(10) + 3));
                level.setTile(xt, yt, Tile.grass, 0);
                level.setFire(xt, yt, 0);
                return true;
            }
        }
        return false;
    }
    
    void throwSeeds(final Level level, final int xt, final int yt) {
        final int whatSeed = this.random.nextInt(16) + 1;
        switch (whatSeed) {
            case 1: {
                level.add(new ItemEntity(new ResourceItem(Resource.carrotSeed), xt * 16 + this.random.nextInt(10) + 3, yt * 16 + this.random.nextInt(10) + 3));
                break;
            }
            case 2: {
                level.add(new ItemEntity(new ResourceItem(Resource.wheatSeeds), xt * 16 + this.random.nextInt(10) + 3, yt * 16 + this.random.nextInt(10) + 3));
                break;
            }
            case 3: {
                level.add(new ItemEntity(new ResourceItem(Resource.turnipSeeds), xt * 16 + this.random.nextInt(10) + 3, yt * 16 + this.random.nextInt(10) + 3));
                break;
            }
            case 4: {
                level.add(new ItemEntity(new ResourceItem(Resource.radishSeeds), xt * 16 + this.random.nextInt(10) + 3, yt * 16 + this.random.nextInt(10) + 3));
                break;
            }
            case 5: {
                level.add(new ItemEntity(new ResourceItem(Resource.onionSeeds), xt * 16 + this.random.nextInt(10) + 3, yt * 16 + this.random.nextInt(10) + 3));
                break;
            }
            case 6: {
                level.add(new ItemEntity(new ResourceItem(Resource.garlicSeeds), xt * 16 + this.random.nextInt(10) + 3, yt * 16 + this.random.nextInt(10) + 3));
                break;
            }
            case 7: {
                level.add(new ItemEntity(new ResourceItem(Resource.pepperSeeds), xt * 16 + this.random.nextInt(10) + 3, yt * 16 + this.random.nextInt(10) + 3));
                break;
            }
            case 8: {
                level.add(new ItemEntity(new ResourceItem(Resource.tomatoSeeds), xt * 16 + this.random.nextInt(10) + 3, yt * 16 + this.random.nextInt(10) + 3));
                break;
            }
            case 9: {
                level.add(new ItemEntity(new ResourceItem(Resource.beetSeeds), xt * 16 + this.random.nextInt(10) + 3, yt * 16 + this.random.nextInt(10) + 3));
                break;
            }
            case 10: {
                level.add(new ItemEntity(new ResourceItem(Resource.mustardSeed), xt * 16 + this.random.nextInt(10) + 3, yt * 16 + this.random.nextInt(10) + 3));
                break;
            }
            case 11: {
                level.add(new ItemEntity(new ResourceItem(Resource.lettuceSeeds), xt * 16 + this.random.nextInt(10) + 3, yt * 16 + this.random.nextInt(10) + 3));
                break;
            }
            case 12: {
                level.add(new ItemEntity(new ResourceItem(Resource.cornSeeds), xt * 16 + this.random.nextInt(10) + 3, yt * 16 + this.random.nextInt(10) + 3));
                break;
            }
            case 13: {
                level.add(new ItemEntity(new ResourceItem(Resource.bean), xt * 16 + this.random.nextInt(10) + 3, yt * 16 + this.random.nextInt(10) + 3));
                break;
            }
            case 14: {
                level.add(new ItemEntity(new ResourceItem(Resource.pea), xt * 16 + this.random.nextInt(10) + 3, yt * 16 + this.random.nextInt(10) + 3));
                break;
            }
            case 15: {
                level.add(new ItemEntity(new ResourceItem(Resource.squashSeeds), xt * 16 + this.random.nextInt(10) + 3, yt * 16 + this.random.nextInt(10) + 3));
                break;
            }
            default: {
                level.add(new ItemEntity(new ResourceItem(Resource.cabbageSeed), xt * 16 + this.random.nextInt(10) + 3, yt * 16 + this.random.nextInt(10) + 3));
                break;
            }
        }
    }
}
