// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.entity;

import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;

public class BurningMan extends Mob
{
    private int xa;
    private int ya;
    private int lvl;
    private int randomWalkTime;
    
    public BurningMan(final int lvl) {
        this.randomWalkTime = 0;
        this.lvl = lvl;
        this.x = this.random.nextInt(1024);
        this.y = this.random.nextInt(1024);
        final int n = lvl * lvl * 10;
        this.maxHealth = n;
        this.health = n;
        if (lvl == 3) {
            this.health = 1;
        }
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
        }
        final int speed = 1;
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
        int xt = 0;
        final int yt = 14;
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
        final int flicker = this.random.nextInt(this.random.nextInt(3) + 1);
        int col = Color.get(-1, 501 + flicker * 10, 512 + flicker * 10, 520 + flicker * 10);
        if (this.hurtTime > 0) {
            col = Color.get(-1, 555, 555, 555);
        }
        screen.render(xo + 8 * flip1, yo + 0, xt + yt * 32, col, flip1);
        screen.render(xo + 8 - 8 * flip1, yo + 0, xt + 1 + yt * 32, col, flip1);
        screen.render(xo + 8 * flip2, yo + 8, xt + (yt + 1) * 32, col, flip2);
        screen.render(xo + 8 - 8 * flip2, yo + 8, xt + 1 + (yt + 1) * 32, col, flip2);
    }
    
    @Override
    public boolean canWalk() {
        return true;
    }
    
    @Override
    protected void touchedBy(final Entity entity) {
        entity.hurt(this, this.lvl + 2, this.dir);
    }
    
    @Override
    public int getLightRadius() {
        final int r = (this.random.nextInt(10) + this.random.nextInt(10) + this.random.nextInt(10) + this.random.nextInt(10)) / 4 + 1;
        return r;
    }
    
    @Override
    protected void die() {
        super.die();
        for (int count = this.random.nextInt(2) + 1, i = 0; i < count; ++i) {
            this.level.add(new ItemEntity(new ResourceItem(Resource.coal), this.x + this.random.nextInt(11) - 5, this.y + this.random.nextInt(11) - 5));
        }
        if (this.random.nextInt(3) == 0) {
            this.level.add(new ItemEntity(new ResourceItem(Resource.dirt), this.x + this.random.nextInt(11) - 5, this.y + this.random.nextInt(11) - 5));
        }
        if (this.level.player != null) {
            final Player player = this.level.player;
            player.score += 50 * this.lvl;
        }
    }
}
