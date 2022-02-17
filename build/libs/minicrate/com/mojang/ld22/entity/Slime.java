// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;

public class Slime extends Mob
{
    private int xa;
    private int ya;
    private int jumpTime;
    private int lvl;
    
    public Slime(final int lvl) {
        this.jumpTime = 0;
        this.lvl = lvl;
        this.x = this.random.nextInt(1024);
        this.y = this.random.nextInt(1024);
        final int n = lvl * lvl * 5;
        this.maxHealth = n;
        this.health = n;
    }
    
    @Override
    public void tick() {
        super.tick();
        final int speed = 1;
        if ((!this.move(this.xa * speed, this.ya * speed) || this.random.nextInt(40) == 0) && this.jumpTime <= -10) {
            this.xa = this.random.nextInt(3) - 1;
            this.ya = this.random.nextInt(3) - 1;
            if (this.level.player != null) {
                final int xd = this.level.player.x - this.x;
                final int yd = this.level.player.y - this.y;
                if (xd * xd + yd * yd < 2500) {
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
            if (this.xa != 0 || this.ya != 0) {
                this.jumpTime = 10;
            }
        }
        --this.jumpTime;
        if (this.jumpTime == 0) {
            final int n = 0;
            this.ya = n;
            this.xa = n;
        }
    }
    
    @Override
    protected void die() {
        super.die();
        for (int count = this.random.nextInt(6) + 1, i = 0; i < count; ++i) {
            this.level.add(new ItemEntity(new ResourceItem(Resource.goop), this.x + this.random.nextInt(11) - 5, this.y + this.random.nextInt(11) - 5));
        }
        if (this.level.player != null) {
            final Player player = this.level.player;
            player.score += 25 * this.lvl;
        }
    }
    
    @Override
    public void render(final Screen screen) {
        int xt = 0;
        final int yt = 18;
        if (this.health > this.maxHealth) {
            xt += 4;
        }
        else {
            xt = 0;
        }
        final int xo = this.x - 8;
        int yo = this.y - 11;
        if (this.jumpTime > 0) {
            xt += 2;
            yo -= 4;
        }
        int col = Color.get(-1, 10, 252, 555);
        if (this.lvl == 2) {
            col = Color.get(-1, 100, 522, 555);
        }
        if (this.lvl == 3) {
            col = Color.get(-1, 111, 444, 555);
        }
        if (this.lvl == 4) {
            col = Color.get(-1, 0, 111, 224);
        }
        if (this.hurtTime > 0) {
            col = Color.get(-1, 555, 555, 555);
        }
        screen.render(xo + 0, yo + 0, xt + yt * 32, col, 0);
        screen.render(xo + 8, yo + 0, xt + 1 + yt * 32, col, 0);
        screen.render(xo + 0, yo + 8, xt + (yt + 1) * 32, col, 0);
        screen.render(xo + 8, yo + 8, xt + 1 + (yt + 1) * 32, col, 0);
    }
    
    @Override
    public boolean canWalk() {
        return true;
    }
    
    @Override
    public int getLightRadius() {
        final int r = (this.random.nextInt(3) + this.random.nextInt(3) + this.random.nextInt(3) + this.random.nextInt(3)) / 4 + 1;
        return r;
    }
    
    @Override
    protected void touchedBy(final Entity entity) {
        if (entity instanceof Player) {
            entity.hurt(this, this.lvl, this.dir);
            Slime.playerShape = 6;
        }
    }
    
    @Override
    public void touchItem(final ItemEntity itemEntity) {
        itemEntity.Obsorb(this);
    }
}
