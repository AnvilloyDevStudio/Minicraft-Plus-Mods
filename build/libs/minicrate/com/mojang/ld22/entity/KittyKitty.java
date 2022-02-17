// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;

public class KittyKitty extends Mob
{
    private int xa;
    private int ya;
    private int lvl;
    private int randomWalkTime;
    private int kittyColor;
    
    public KittyKitty(final int lvl) {
        this.randomWalkTime = 0;
        this.kittyColor = this.random.nextInt(4);
        this.lvl = lvl;
        this.x = this.random.nextInt(1024);
        this.y = this.random.nextInt(1024);
        final int n = lvl * lvl + 20;
        this.maxHealth = n;
        this.health = n;
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.level.player != null && this.randomWalkTime == 0 && this.kittyColor == 1) {
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
        int xt = 16;
        final int yt = 24;
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
        int col = Color.get(-1, 222, 0, 555);
        if (this.kittyColor == 1) {
            col = Color.get(-1, 111, 540, 555);
        }
        if (this.kittyColor == 2) {
            col = Color.get(-1, 111, 444, 555);
        }
        if (this.kittyColor == 3) {
            col = Color.get(-1, 111, 333, 555);
        }
        if (this.hurtTime > 0) {
            col = Color.get(-1, 555, 555, 555);
        }
        screen.render(xo + 8 * flip1, yo + 0, xt + yt * 32, col, flip1);
        screen.render(xo + 8 - 8 * flip1, yo + 0, xt + 1 + yt * 32, col, flip1);
        screen.render(xo + 8 * flip2, yo + 8, xt + (yt + 1) * 32, col, flip2);
        screen.render(xo + 8 - 8 * flip2, yo + 8, xt + 1 + (yt + 1) * 32, col, flip2);
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
    public boolean canClimb() {
        return true;
    }
    
    @Override
    public boolean eatsFood() {
        return true;
    }
    
    @Override
    protected void touchedBy(final Entity entity) {
        switch (this.kittyColor) {
            case 0: {
                if (entity instanceof Player) {
                    entity.hurt(this, this.lvl + 1, this.dir);
                    break;
                }
                break;
            }
            case 1: {
                if (entity instanceof Player) {
                    break;
                }
                entity.hurt(this, this.lvl + 1, this.dir);
                break;
            }
            case 2: {
                entity.hurt(this, this.lvl + 1, this.dir);
                break;
            }
            default: {
                if (entity instanceof Player) {
                    entity.hurt(this, 1, this.dir);
                    break;
                }
                if (entity instanceof Sheepish) {
                    entity.hurt(this, 100, this.dir);
                    break;
                }
                if (entity instanceof Turducken) {
                    entity.hurt(this, 100, this.dir);
                    break;
                }
                if (entity instanceof MonkeyBoy) {
                    entity.hurt(this, 100, this.dir);
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    protected void die() {
        super.die();
        if (this.level.player != null) {
            final Player player = this.level.player;
            player.score += 5 * this.lvl;
        }
    }
}
