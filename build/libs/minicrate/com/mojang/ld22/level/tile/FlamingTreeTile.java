// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.level.tile;

import com.mojang.ld22.entity.particle.TextParticle;
import com.mojang.ld22.entity.particle.SmashParticle;
import com.mojang.ld22.entity.ItemEntity;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.item.ToolType;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.entity.Mob;
import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.gfx.Screen;

public class FlamingTreeTile extends Tile
{
    public int isWand;
    public int isStaff;
    
    public FlamingTreeTile(final int id) {
        super(id);
        this.connectsToGrass = true;
    }
    
    @Override
    public void render(final Screen screen, final Level level, final int x, final int y) {
        int col = Color.get(10, 30, 151, level.grassColor);
        final int barkCol1 = Color.get(10, 30, 430, level.grassColor);
        final int barkCol2 = Color.get(10, 30, 320, level.grassColor);
        final boolean u = level.getTile(x, y - 1) == this;
        final boolean l = level.getTile(x - 1, y) == this;
        final boolean r = level.getTile(x + 1, y) == this;
        final boolean d = level.getTile(x, y + 1) == this;
        final boolean ul = level.getTile(x - 1, y - 1) == this;
        final boolean ur = level.getTile(x + 1, y - 1) == this;
        final boolean dl = level.getTile(x - 1, y + 1) == this;
        final boolean dr = level.getTile(x + 1, y + 1) == this;
        if (u && ul && l) {
            screen.render(x * 16 + 0, y * 16 + 0, 42, col, 0);
        }
        else {
            screen.render(x * 16 + 0, y * 16 + 0, 9, col, 0);
        }
        if (u && ur && r) {
            screen.render(x * 16 + 8, y * 16 + 0, 74, barkCol2, 0);
        }
        else {
            screen.render(x * 16 + 8, y * 16 + 0, 10, col, 0);
        }
        if (d && dl && l) {
            screen.render(x * 16 + 0, y * 16 + 8, 74, barkCol2, 0);
        }
        else {
            screen.render(x * 16 + 0, y * 16 + 8, 41, barkCol1, 0);
        }
        if (d && dr && r) {
            screen.render(x * 16 + 8, y * 16 + 8, 42, col, 0);
        }
        else {
            screen.render(x * 16 + 8, y * 16 + 8, 106, barkCol2, 0);
        }
        final int flaming = 1;
        if (flaming == 1) {
            final int flicker = this.random.nextInt(this.random.nextInt(2) + 1);
            col = Color.get(-1, 530, 551, 554);
            screen.render(x * 16 + 4, y * 16 + 4, flicker + 12 + 96, col, 0);
        }
    }
    
    @Override
    public void tick(final Level level, final int xt, final int yt) {
        final int damage = level.getData(xt, yt);
        if (damage > 0) {
            level.setData(xt, yt, damage - 1);
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
            level.setTile(xn, yn, FlamingTreeTile.treeSapling, 0);
        }
        else {
            if (this.random.nextInt(40) != 0) {
                return;
            }
            if (level.getTile(xn, yn) == Tile.grass) {
                level.setTile(xn, yn, FlamingTreeTile.treeSapling, 0);
            }
        }
    }
    
    @Override
    public boolean mayEat(final Level level, final int x, final int y, final Entity e) {
        return e.eatsTrees();
    }
    
    @Override
    public boolean mayPass(final Level level, final int x, final int y, final Entity e) {
        return e.canClimb();
    }
    
    @Override
    public void hurt(final Level level, final int x, final int y, final Mob source, final int dmg, final int attackDir) {
        this.hurt(level, x, y, dmg);
    }
    
    @Override
    public boolean interact(final Level level, final int xt, final int yt, final Player player, final Item item, final int attackDir) {
        this.isWand = 0;
        this.isStaff = 0;
        if (item instanceof ToolItem) {
            final ToolItem tool = (ToolItem)item;
            if (tool.type == ToolType.wand) {
                this.isWand = 1;
            }
            if (tool.type == ToolType.staff) {
                this.isStaff = 1;
            }
            if (tool.type == ToolType.axe) {
                if (player.payStamina(4 - tool.level)) {
                    this.hurt(level, xt, yt, this.random.nextInt(10) + tool.level * 5 + 20);
                    return true;
                }
                return true;
            }
        }
        return false;
    }
    
    private void hurt(final Level level, final int x, final int y, final int dmg) {
        if (this.isWand == 0) {
            for (int count = (this.random.nextInt(3) == 0) ? 1 : 0, i = 0; i < count; ++i) {
                level.add(new ItemEntity(new ResourceItem(Resource.apple), x * 16 + this.random.nextInt(20) + 3, y * 16 + this.random.nextInt(20) + 3));
            }
            final int damage = level.getData(x, y) + dmg;
            level.add(new SmashParticle(x * 16 + 8, y * 16 + 8));
            level.add(new TextParticle(new StringBuilder().append(dmg).toString(), x * 16 + 8, y * 16 + 8, Color.get(-1, 500, 500, 500)));
            if (damage >= 20) {
                for (int count2 = this.random.nextInt(4) + 1, j = 0; j < count2; ++j) {
                    level.add(new ItemEntity(new ResourceItem(Resource.apple), x * 16 + this.random.nextInt(10) + 3, y * 16 + this.random.nextInt(10) + 3));
                }
                for (int count2 = this.random.nextInt(4) + 1, j = 0; j < count2; ++j) {
                    level.add(new ItemEntity(new ResourceItem(Resource.wood), x * 16 + this.random.nextInt(10) + 3, y * 16 + this.random.nextInt(10) + 3));
                }
                for (int count2 = this.random.nextInt(this.random.nextInt(3) + 1), j = 0; j < count2; ++j) {
                    level.add(new ItemEntity(new ResourceItem(Resource.acorn), x * 16 + this.random.nextInt(10) + 3, y * 16 + this.random.nextInt(10) + 3));
                }
                level.setTile(x, y, Tile.grass, 0);
            }
            else {
                level.setData(x, y, damage);
            }
        }
        else {
            level.setTile(x, y, Tile.pine, 0);
            this.isWand = 0;
        }
        if (this.isStaff == 1) {
            level.setTile(x, y, Tile.lava, 0);
        }
    }
}
