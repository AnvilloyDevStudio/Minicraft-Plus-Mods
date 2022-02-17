// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.level.tile;

import com.mojang.ld22.entity.Mob;
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

public class FlowerTile extends GrassTile
{
    public FlowerTile(final int id) {
        super(id);
        FlowerTile.tiles[id] = this;
        this.connectsToGrass = true;
    }
    
    @Override
    public void render(final Screen screen, final Level level, final int x, final int y) {
        super.render(screen, level, x, y);
        final int data = level.getData(x, y);
        final int shape = data / 16 % 2;
        final int flowerCol = Color.get(10, level.grassColor, 555, 440);
        if (shape == 0) {
            screen.render(x * 16 + 0, y * 16 + 0, 33, flowerCol, 0);
        }
        if (shape == 1) {
            screen.render(x * 16 + 8, y * 16 + 0, 33, flowerCol, 0);
        }
        if (shape == 1) {
            screen.render(x * 16 + 0, y * 16 + 8, 33, flowerCol, 0);
        }
        if (shape == 0) {
            screen.render(x * 16 + 8, y * 16 + 8, 33, flowerCol, 0);
        }
    }
    
    @Override
    public boolean mayPass(final Level level, final int x, final int y, final Entity e) {
        return e.canWalk();
    }
    
    @Override
    public boolean interact(final Level level, final int x, final int y, final Player player, final Item item, final int attackDir) {
        if (item instanceof ToolItem) {
            final ToolItem tool = (ToolItem)item;
            if (tool.type == ToolType.shovel && player.payStamina(4 - tool.level)) {
                if (this.random.nextInt(2) == 1) {
                    this.throwSeeds(level, x, y);
                }
                level.add(new ItemEntity(new ResourceItem(Resource.flower), x * 16 + this.random.nextInt(10) + 3, y * 16 + this.random.nextInt(10) + 3));
                level.add(new ItemEntity(new ResourceItem(Resource.flower), x * 16 + this.random.nextInt(10) + 3, y * 16 + this.random.nextInt(10) + 3));
                level.setTile(x, y, Tile.grass, 0);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void hurt(final Level level, final int x, final int y, final Mob source, final int dmg, final int attackDir) {
        for (int count = this.random.nextInt(2) + 1, i = 0; i < count; ++i) {
            level.add(new ItemEntity(new ResourceItem(Resource.flower), x * 16 + this.random.nextInt(10) + 3, y * 16 + this.random.nextInt(10) + 3));
        }
        if (this.random.nextInt(3) == 1) {
            this.throwSeeds(level, x, y);
        }
        level.setTile(x, y, Tile.grass, 0);
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
