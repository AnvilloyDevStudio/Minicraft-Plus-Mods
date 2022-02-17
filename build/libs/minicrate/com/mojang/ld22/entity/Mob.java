// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.entity;

import com.mojang.ld22.level.Level;
import com.mojang.ld22.sound.Sound;
import com.mojang.ld22.entity.particle.TextParticle;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.level.tile.Tile;

public class Mob extends Entity
{
    protected int walkDist;
    protected int dir;
    public int hurtTime;
    protected int xKnockback;
    protected int yKnockback;
    public int maxHealth;
    public int health;
    public int swimTimer;
    public int tickTime;
    public static int playerShape;
    public int lifespan;
    int xHeading;
    int yHeading;
    
    static {
        Mob.playerShape = 0;
    }
    
    public Mob() {
        this.walkDist = 0;
        this.dir = 0;
        this.hurtTime = 0;
        this.maxHealth = 10;
        this.health = this.maxHealth;
        this.swimTimer = 0;
        this.tickTime = 0;
        this.lifespan = 50;
        final int n = 8;
        this.y = n;
        this.x = n;
        this.xr = 4;
        this.yr = 3;
    }
    
    @Override
    public void tick() {
        ++this.tickTime;
        if (this.level.getTile(this.x >> 4, this.y >> 4) == Tile.lava) {
            this.hurt(this, 4, this.dir ^ 0x1);
        }
        if (this.health <= 0) {
            this.die();
        }
        if (this.hurtTime > 0) {
            --this.hurtTime;
        }
    }
    
    protected void die() {
        this.remove();
    }
    
    @Override
    public boolean move(final int xa, final int ya) {
        if (this.isSwimming() && this.swimTimer++ % 2 == 0) {
            return !this.isSwimmer();
        }
        if (this.xKnockback < 0) {
            this.move2(-1, 0);
            ++this.xKnockback;
        }
        if (this.xKnockback > 0) {
            this.move2(1, 0);
            --this.xKnockback;
        }
        if (this.yKnockback < 0) {
            this.move2(0, -1);
            ++this.yKnockback;
        }
        if (this.yKnockback > 0) {
            this.move2(0, 1);
            --this.yKnockback;
        }
        if (this.hurtTime > 0) {
            return true;
        }
        if (xa != 0 || ya != 0) {
            ++this.walkDist;
            if (xa < 0) {
                this.dir = 2;
            }
            if (xa > 0) {
                this.dir = 3;
            }
            if (ya < 0) {
                this.dir = 1;
            }
            if (ya > 0) {
                this.dir = 0;
            }
            this.xHeading = xa;
            this.yHeading = ya;
        }
        return super.move(xa, ya);
    }
    
    public int xHeading() {
        return this.xHeading;
    }
    
    public int yHeading() {
        return this.yHeading;
    }
    
    protected boolean isSwimming() {
        final Tile tile = this.level.getTile(this.x >> 4, this.y >> 4);
        return !this.isSwimmer() && (tile == Tile.water || tile == Tile.lava);
    }
    
    @Override
    public boolean holdingOrb() {
        return false;
    }
    
    @Override
    public boolean holdingGreenOrb() {
        return false;
    }
    
    @Override
    public boolean blocks(final Entity e) {
        return e.isBlockableBy(this);
    }
    
    @Override
    public void hurt(final Tile tile, final int x, final int y, final int damage) {
        final int attackDir = this.dir ^ 0x1;
        this.doHurt(damage, attackDir);
    }
    
    @Override
    public void hurt(final Mob mob, final int damage, final int attackDir) {
        this.doHurt(damage, attackDir);
    }
    
    public void heal(final int heal) {
        if (this.hurtTime > 0) {
            return;
        }
        this.level.add(new TextParticle(new StringBuilder().append(heal).toString(), this.x, this.y, Color.get(-1, 50, 50, 50)));
        this.health += heal;
        if (this.health > this.maxHealth) {
            this.health = this.maxHealth;
        }
    }
    
    protected void doHurt(final int damage, final int attackDir) {
        if (this.hurtTime > 0) {
            return;
        }
        if (this.level.player != null) {
            final int xd = this.level.player.x - this.x;
            final int yd = this.level.player.y - this.y;
            if (xd * xd + yd * yd < 6400) {
                Sound.monsterHurt.play();
            }
        }
        this.level.add(new TextParticle(new StringBuilder().append(damage).toString(), this.x, this.y, Color.get(-1, 500, 500, 500)));
        this.health -= damage;
        if (attackDir == 0) {
            this.yKnockback = 6;
        }
        if (attackDir == 1) {
            this.yKnockback = -6;
        }
        if (attackDir == 2) {
            this.xKnockback = -6;
        }
        if (attackDir == 3) {
            this.xKnockback = 6;
        }
        this.hurtTime = 10;
    }
    
    public boolean findStartPos(final Level level) {
        final int x = this.random.nextInt(level.w);
        final int y = this.random.nextInt(level.h);
        final int xx = x * 16 + 8;
        final int yy = y * 16 + 8;
        if (level.player != null) {
            final int xd = level.player.x - xx;
            final int yd = level.player.y - yy;
            if (xd * xd + yd * yd < 6400) {
                return false;
            }
        }
        final int r = level.monsterDensity * 16;
        if (level.getEntities(xx - r, yy - r, xx + r, yy + r).size() > 0) {
            return false;
        }
        if (level.getTile(x, y).mayPass(level, x, y, this)) {
            this.x = xx;
            this.y = yy;
            return true;
        }
        return false;
    }
}
