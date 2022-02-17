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

public class ElmTile extends Tile
{
    public int isWand;
    public int isStaff;
    
    public ElmTile(final int id) {
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
            screen.render(x * 16 + 0, y * 16 + 0, 54, col, 0);
        }
        else {
            screen.render(x * 16 + 0, y * 16 + 0, 21, col, 0);
        }
        if (u && ur && r) {
            screen.render(x * 16 + 8, y * 16 + 0, 86, barkCol2, 0);
        }
        else {
            screen.render(x * 16 + 8, y * 16 + 0, 22, col, 0);
        }
        if (d && dl && l) {
            screen.render(x * 16 + 0, y * 16 + 8, 86, barkCol2, 0);
        }
        else {
            screen.render(x * 16 + 0, y * 16 + 8, 53, barkCol1, 0);
        }
        if (d && dr && r) {
            screen.render(x * 16 + 8, y * 16 + 8, 54, col, 0);
        }
        else {
            screen.render(x * 16 + 8, y * 16 + 8, 118, barkCol2, 0);
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
            if (level.getFire(xt, yt) > 20) {
                level.setTile(xt, yt, Tile.fire, 0);
                level.setFire(xt, yt, 1);
            }
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
            level.setTile(xn, yn, ElmTile.bElm, 0);
            level.setFire(xn, yn, 0);
        }
        else {
            if (this.random.nextInt(40) != 0) {
                return;
            }
            if (level.getTile(xn, yn) == Tile.grass) {
                level.setTile(xn, yn, ElmTile.bElm, 0);
                level.setFire(xn, yn, 0);
            }
        }
    }
    
    @Override
    public boolean mayEat(final Level level, final int x, final int y, final Entity e) {
        return e.eatsTrees();
    }
    
    @Override
    public boolean canBurn(final Level level, final int x, final int y, final Entity e) {
        return e.burnsThings();
    }
    
    @Override
    public boolean mayPass(final Level level, final int x, final int y, final Entity e) {
        return e.canClimb();
    }
    
    @Override
    public boolean flamable() {
        return true;
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
            else if (tool.type == ToolType.lighter) {
                if (player.payStamina(4 - tool.level)) {
                    level.setFire(xt, yt, 1);
                    return true;
                }
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void bumpedInto(final Level level, final int x, final int y, final Entity entity) {
        if (this.random.nextInt(100) == 1 && entity instanceof Player) {
            int noCenterX = this.random.nextInt(6);
            if (noCenterX > 3) {
                noCenterX += 4;
            }
            int noCenterY = this.random.nextInt(6);
            if (noCenterY > 3) {
                noCenterY = noCenterX + 4;
            }
            level.add(new ItemEntity(new ResourceItem(Resource.ginger), x * 16 + noCenterX + 3, y * 16 + noCenterY + 3));
        }
        if (level.getFire(x, y) > 0) {
            entity.hurt(this, x, y, 1);
        }
    }
    
    private void hurt(final Level level, final int x, final int y, final int dmg) {
        if (this.isWand == 0) {
            for (int count = (this.random.nextInt(3) == 0) ? 1 : 0, i = 0; i < count; ++i) {
                level.add(new ItemEntity(new ResourceItem(Resource.cherry), x * 16 + this.random.nextInt(20) + 3, y * 16 + this.random.nextInt(20) + 3));
            }
            final int damage = level.getData(x, y) + dmg;
            level.add(new SmashParticle(x * 16 + 8, y * 16 + 8));
            level.add(new TextParticle(new StringBuilder().append(dmg).toString(), x * 16 + 8, y * 16 + 8, Color.get(-1, 500, 500, 500)));
            if (damage >= 20) {
                for (int count2 = this.random.nextInt(4) + 1, j = 0; j < count2; ++j) {
                    level.add(new ItemEntity(new ResourceItem(Resource.cherry), x * 16 + this.random.nextInt(10) + 3, y * 16 + this.random.nextInt(10) + 3));
                }
                for (int count2 = this.random.nextInt(4) + 1, j = 0; j < count2; ++j) {
                    level.add(new ItemEntity(new ResourceItem(Resource.wood), x * 16 + this.random.nextInt(10) + 3, y * 16 + this.random.nextInt(10) + 3));
                }
                for (int count2 = this.random.nextInt(this.random.nextInt(3) + 1), j = 0; j < count2; ++j) {
                    level.add(new ItemEntity(new ResourceItem(Resource.elmSeed), x * 16 + this.random.nextInt(10) + 3, y * 16 + this.random.nextInt(10) + 3));
                }
                level.setTile(x, y, Tile.grass, 0);
                level.setFire(x, y, 0);
            }
            else {
                level.setData(x, y, damage);
            }
        }
        else {
            level.setTile(x, y, Tile.shroom, 0);
            level.setFire(x, y, 0);
            this.isWand = 0;
        }
        if (this.isStaff == 1) {
            level.setTile(x, y, Tile.stairsUp, 0);
            level.setFire(x, y, 0);
            this.isStaff = 0;
        }
    }
}
