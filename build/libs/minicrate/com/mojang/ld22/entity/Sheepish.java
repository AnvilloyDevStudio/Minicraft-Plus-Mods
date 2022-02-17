// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.entity;

import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;

public class Sheepish extends Mob
{
    private int xa;
    private int ya;
    private int lvl;
    private int randomWalkTime;
    private int milkable;
    private int tame;
    
    public Sheepish(final int lvl) {
        this.randomWalkTime = 0;
        this.tame = 0;
        this.lvl = lvl;
        this.x = this.random.nextInt(1024);
        this.y = this.random.nextInt(1024);
        final int n = lvl * lvl * 30;
        this.maxHealth = n;
        this.health = n;
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.level.player != null && this.randomWalkTime == 0) {
            final int xd = this.level.player.x - this.x;
            final int yd = this.level.player.y - this.y;
            if (xd * xd + yd * yd < 2500) {
                this.xa = 0;
                this.ya = 0;
                if (xd < 0) {
                    this.xa = -1;
                }
                if (xd > 0) {
                    this.xa = 1;
                }
                if (yd < 0) {
                    this.ya = -1;
                }
                if (yd > 0) {
                    this.ya = 1;
                }
            }
            else {
                this.milkable = 1;
            }
        }
        final int speed = this.tickTime & 0x1;
        if (!this.move(this.xa * speed, this.ya * speed) || this.random.nextInt(200) == 0) {
            this.randomWalkTime = 60;
            this.xa = (this.random.nextInt(3) - 1) * this.random.nextInt(2);
            this.ya = (this.random.nextInt(3) - 1) * this.random.nextInt(2);
        }
        if (this.randomWalkTime > 0) {
            --this.randomWalkTime;
        }
    }
    
    @Override
    public void render(final Screen screen) {
        int xt = 24;
        final int yt = 20;
        int flip1 = this.walkDist >> 3 & 0x1;
        int flip2 = this.walkDist >> 3 & 0x1;
        if (this.dir == 1) {
            xt += 2;
        }
        if (this.dir > 1) {
            flip1 = 0;
            flip2 = (this.walkDist >> 4 & 0x1);
            if (this.dir == 2) {
                flip1 = 1;
            }
            xt += 4 + (this.walkDist >> 3 & 0x1) * 2;
        }
        final int xo = this.x - 8;
        final int yo = this.y - 11;
        int col = Color.get(-1, 200, 422, 555);
        if (this.lvl == 2) {
            col = Color.get(-1, 200, 422, 555);
        }
        if (this.lvl == 3) {
            col = Color.get(-1, 200, 422, 555);
        }
        if (this.lvl == 4) {
            col = Color.get(-1, 200, 422, 555);
        }
        if (this.hurtTime > 0) {
            col = Color.get(-1, 555, 555, 555);
        }
        screen.render(xo + 8 * flip1, yo + 0, xt + yt * 32, col, flip1);
        screen.render(xo + 8 - 8 * flip1, yo + 0, xt + 1 + yt * 32, col, flip1);
        screen.render(xo + 8 * flip1, yo + 8, xt + (yt + 1) * 32, col, flip1);
        screen.render(xo + 8 - 8 * flip1, yo + 8, xt + 1 + (yt + 1) * 32, col, flip1);
    }
    
    @Override
    public boolean canPass() {
        return true;
    }
    
    @Override
    public boolean canWalk() {
        return true;
    }
    
    @Override
    public boolean grazes() {
        return true;
    }
    
    @Override
    public boolean eatsFood() {
        return true;
    }
    
    @Override
    protected void touchedBy(final Entity entity) {
        if (entity instanceof Player && this.milkable == 1) {
            this.milkable = 0;
            this.level.add(new ItemEntity(new ResourceItem(Resource.milk), this.x + this.random.nextInt(11) - 5, this.y + this.random.nextInt(11) - 5));
        }
        if (entity instanceof LandShark) {
            entity.hurt(this, this.lvl + 1, this.dir);
        }
    }
    
    @Override
    protected void die() {
        super.die();
        for (int count = this.random.nextInt(10) + 1, i = 0; i < count; ++i) {
            this.level.add(new ItemEntity(new ResourceItem(Resource.meat), this.x + this.random.nextInt(11) - 5, this.y + this.random.nextInt(11) - 5));
        }
        if (this.level.player != null) {
            final Player player = this.level.player;
            player.score += 50 * this.lvl;
        }
    }
}
