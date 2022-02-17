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

public class ShroomTile extends Tile
{
    public int isWand;
    public int isStaff;
    
    public ShroomTile(final int id) {
        super(id);
        this.connectsToGrass = true;
    }
    
    @Override
    public void render(final Screen screen, final Level level, final int x, final int y) {
        int col = Color.get(110, 130, 251, level.grassColor);
        final int barkCol1 = Color.get(110, 130, 434, level.grassColor);
        final int barkCol2 = Color.get(10, 30, 323, level.grassColor);
        final boolean u = level.getTile(x, y - 1) == this;
        final boolean l = level.getTile(x - 1, y) == this;
        final boolean r = level.getTile(x + 1, y) == this;
        final boolean d = level.getTile(x, y + 1) == this;
        final boolean ul = level.getTile(x - 1, y - 1) == this;
        final boolean ur = level.getTile(x + 1, y - 1) == this;
        final boolean dl = level.getTile(x - 1, y + 1) == this;
        final boolean dr = level.getTile(x + 1, y + 1) == this;
        if (u && ul && l) {
            screen.render(x * 16 + 0, y * 16 + 0, 58, col, 0);
        }
        else {
            screen.render(x * 16 + 0, y * 16 + 0, 25, col, 0);
        }
        if (u && ur && r) {
            screen.render(x * 16 + 8, y * 16 + 0, 90, barkCol2, 0);
        }
        else {
            screen.render(x * 16 + 8, y * 16 + 0, 26, col, 0);
        }
        if (d && dl && l) {
            screen.render(x * 16 + 0, y * 16 + 8, 90, barkCol2, 0);
        }
        else {
            screen.render(x * 16 + 0, y * 16 + 8, 57, barkCol1, 0);
        }
        if (d && dr && r) {
            screen.render(x * 16 + 8, y * 16 + 8, 58, col, 0);
        }
        else {
            screen.render(x * 16 + 8, y * 16 + 8, 122, barkCol2, 0);
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
        final int damage = level.getData(xt, yt);
        if (damage > 0) {
            level.setData(xt, yt, damage - 1);
        }
        if (level.getFire(xt, yt) > 0) {
            level.moreFire(xt, yt);
            if (this.random.nextInt(5) == 0) {
                int xq = xt - 1;
                int yq = yt - 1;
                if (level.getTile(xq, yq).flamable()) {
                    level.setFire(xq, yq, 1);
                }
                xq = xt;
                if (level.getTile(xq, yq).flamable()) {
                    level.setFire(xq, yq, 1);
                }
                xq = xt + 1;
                if (level.getTile(xq, yq).flamable()) {
                    level.setFire(xq, yq, 1);
                }
                yq = yt;
                if (level.getTile(xq, yq).flamable()) {
                    level.setFire(xq, yq, 1);
                }
                yq = yt + 1;
                if (level.getTile(xq, yq).flamable()) {
                    level.setFire(xq, yq, 1);
                }
                xq = xt;
                if (level.getTile(xq, yq).flamable()) {
                    level.setFire(xq, yq, 1);
                }
                xq = xt - 1;
                if (level.getTile(xq, yq).flamable()) {
                    level.setFire(xq, yq, 1);
                }
                yq = yt;
                if (level.getTile(xq, yq).flamable()) {
                    level.setFire(xq, yq, 1);
                }
            }
            if (level.getFire(xt, yt) > 10) {
                level.setTile(xt, yt, Tile.dirt, 0);
                level.setFire(xt, yt, 0);
            }
        }
        if (this.random.nextInt(10) != 0) {
            return;
        }
        if (level.getFire(xt, yt) != 0) {
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
            level.setTile(xn, yn, ShroomTile.bShroom, 0);
            level.setFire(xn, yn, 0);
        }
        if (level.getTile(xn, yn) == Tile.grass) {
            level.setTile(xn, yn, ShroomTile.bShroom, 0);
            level.setFire(xn, yn, 0);
        }
    }
    
    @Override
    public boolean mayEat(final Level level, final int x, final int y, final Entity e) {
        return e.eatsTruffles();
    }
    
    @Override
    public boolean mayPass(final Level level, final int x, final int y, final Entity e) {
        return e.canClimb();
    }
    
    @Override
    public boolean canBurn(final Level level, final int x, final int y, final Entity e) {
        return e.burnsThings();
    }
    
    @Override
    public int getLightRadius(final Level level, final int x, final int y) {
        return 5;
    }
    
    @Override
    public void bumpedInto(final Level level, final int x, final int y, final Entity entity) {
        if (level.getFire(x, y) > 0) {
            entity.hurt(this, x, y, 1);
        }
    }
    
    @Override
    public void hurt(final Level level, final int x, final int y, final Mob source, final int dmg, final int attackDir) {
        this.hurt(level, x, y, dmg);
    }
    
    @Override
    public boolean flamable() {
        return true;
    }
    
    @Override
    public boolean interact(final Level level, final int xt, final int yt, final Player player, final Item item, final int attackDir) {
        this.isWand = 0;
        if (item instanceof ToolItem) {
            final ToolItem tool = (ToolItem)item;
            if (tool.type == ToolType.wand) {
                this.isWand = 1;
            }
            if (tool.type == ToolType.staff) {
                this.isStaff = 1;
            }
            if (tool.type == ToolType.lighter) {
                if (player.payStamina(4 - tool.level)) {
                    level.setFire(xt, yt, 1);
                    return true;
                }
                return true;
            }
            else if (tool.type == ToolType.axe) {
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
                level.add(new ItemEntity(new ResourceItem(Resource.fungus), x * 16 + this.random.nextInt(20) + 3, y * 16 + this.random.nextInt(20) + 3));
            }
            final int damage = level.getData(x, y) + dmg;
            level.add(new SmashParticle(x * 16 + 8, y * 16 + 8));
            level.add(new TextParticle(new StringBuilder().append(dmg).toString(), x * 16 + 8, y * 16 + 8, Color.get(-1, 500, 500, 500)));
            if (damage >= 10) {
                for (int count2 = this.random.nextInt(4) + 1, j = 0; j < count2; ++j) {
                    level.add(new ItemEntity(new ResourceItem(Resource.fungus), x * 16 + this.random.nextInt(10) + 3, y * 16 + this.random.nextInt(10) + 3));
                }
                for (int count2 = this.random.nextInt(4) + 1, j = 0; j < count2; ++j) {
                    level.add(new ItemEntity(new ResourceItem(Resource.fungus), x * 16 + this.random.nextInt(10) + 3, y * 16 + this.random.nextInt(10) + 3));
                }
                for (int count2 = this.random.nextInt(this.random.nextInt(3) + 1), j = 0; j < count2; ++j) {
                    level.add(new ItemEntity(new ResourceItem(Resource.dirt), x * 16 + this.random.nextInt(10) + 3, y * 16 + this.random.nextInt(10) + 3));
                }
                level.setTile(x, y, Tile.grass, 0);
                level.setFire(x, y, 0);
            }
            else {
                level.setData(x, y, damage);
            }
        }
        else {
            level.setTile(x, y, Tile.tree, 0);
            level.setFire(x, y, 0);
            this.isWand = 0;
        }
        if (this.isStaff == 1) {
            level.setTile(x, y, Tile.fire, 0);
            level.setFire(x, y, 0);
            this.isStaff = 0;
        }
    }
}
