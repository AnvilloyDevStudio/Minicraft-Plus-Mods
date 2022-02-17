// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.entity;

import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;

public class HellBat extends Mob
{
    private int xa;
    private int ya;
    private int randomWalkTime;
    private int attackDelay;
    private int attackTime;
    private int attackType;
    
    public HellBat() {
        this.randomWalkTime = 0;
        this.attackDelay = 0;
        this.attackTime = 0;
        this.attackType = 0;
        this.x = this.random.nextInt(1024);
        this.y = this.random.nextInt(1024);
        final int n = 150;
        this.maxHealth = n;
        this.health = n;
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.attackDelay > 0) {
            this.dir = (this.attackDelay - 45) / 4 % 4;
            this.dir = this.dir * 2 % 4 + this.dir / 2;
            if (this.attackDelay < 45) {
                this.dir = 0;
            }
            --this.attackDelay;
            if (this.attackDelay == 0) {
                this.attackType = 0;
                this.attackTime = 30;
            }
            return;
        }
        if (this.attackTime > 0) {
            --this.attackTime;
            final double dir = this.attackTime * 0.25 * (this.attackTime % 2 * 2 - 1);
            final double speed = 0.7 + this.attackType * 0.2;
            this.level.add(new BatZap(this, Math.cos(dir) * speed, Math.sin(dir) * speed));
            return;
        }
        if (this.level.player != null && this.randomWalkTime == 0) {
            final int xd = this.level.player.x - this.x;
            final int yd = this.level.player.y - this.y;
            if (xd * xd + yd * yd < 1024) {
                this.xa = 0;
                this.ya = 0;
                if (xd < 0) {
                    this.xa = 1;
                }
                if (xd > 0) {
                    this.xa = -1;
                }
                if (yd < 0) {
                    this.ya = 1;
                }
                if (yd > 0) {
                    this.ya = -1;
                }
            }
            else {
                this.xa = 0;
            }
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
        final int speed2 = (this.tickTime % 4 != 0) ? 1 : 0;
        if (!this.move(this.xa * speed2, this.ya * speed2) || this.random.nextInt(100) == 0) {
            this.randomWalkTime = 30;
            this.xa = this.random.nextInt(3) - 1;
            this.ya = this.random.nextInt(3) - 1;
        }
        if (this.randomWalkTime > 0) {
            --this.randomWalkTime;
            if (this.level.player != null && this.randomWalkTime == 0) {
                final int xd2 = this.level.player.x - this.x;
                final int yd2 = this.level.player.y - this.y;
                if (this.random.nextInt(4) == 0 && xd2 * xd2 + yd2 * yd2 < 2500 && this.attackDelay == 0 && this.attackTime == 0) {
                    this.attackDelay = 120;
                }
            }
        }
    }
    
    @Override
    protected void doHurt(final int damage, final int attackDir) {
        super.doHurt(damage, attackDir);
        if (this.attackDelay == 0 && this.attackTime == 0) {
            this.attackDelay = 120;
        }
    }
    
    @Override
    public void render(final Screen screen) {
        int xt = 16;
        final int yt = 35;
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
        int col1 = Color.get(-1, 100, 500, 555);
        int col2 = Color.get(-1, 100, 500, 532);
        if (this.health < 200) {
            if (this.tickTime / 3 % 2 == 0) {
                col1 = Color.get(-1, 500, 100, 555);
                col2 = Color.get(-1, 500, 100, 532);
            }
        }
        else if (this.health < 1000 && this.tickTime / 5 % 4 == 0) {
            col1 = Color.get(-1, 500, 100, 555);
            col2 = Color.get(-1, 500, 100, 532);
        }
        if (this.hurtTime > 0) {
            col1 = Color.get(-1, 555, 555, 555);
            col2 = Color.get(-1, 555, 555, 555);
        }
        screen.render(xo + 8 * flip1, yo + 0, xt + yt * 32, col1, flip1);
        screen.render(xo + 8 - 8 * flip1, yo + 0, xt + 1 + yt * 32, col1, flip1);
        screen.render(xo + 8 * flip2, yo + 8, xt + (yt + 1) * 32, col2, flip2);
        screen.render(xo + 8 - 8 * flip2, yo + 8, xt + 1 + (yt + 1) * 32, col2, flip2);
    }
    
    @Override
    public boolean canClimb() {
        return true;
    }
    
    @Override
    public boolean canWalk() {
        return true;
    }
    
    @Override
    public boolean canPass() {
        return true;
    }
    
    @Override
    public boolean canDig() {
        return true;
    }
    
    @Override
    public boolean canSwim() {
        return true;
    }
    
    @Override
    public boolean isSwimmer() {
        return true;
    }
    
    @Override
    protected void touchedBy(final Entity entity) {
        if (entity instanceof Player) {
            entity.hurt(this, 3, this.dir);
        }
    }
    
    @Override
    protected void die() {
        super.die();
        for (int count = this.random.nextInt(2) + 1, i = 0; i < count; ++i) {
            this.level.add(new ItemEntity(new ResourceItem(Resource.cloth), this.x + this.random.nextInt(11) - 5, this.y + this.random.nextInt(11) - 5));
        }
        if (this.random.nextInt(3) == 0) {
            this.level.add(new ItemEntity(new ResourceItem(Resource.bread), this.x + this.random.nextInt(11) - 5, this.y + this.random.nextInt(11) - 5));
        }
        if (this.random.nextInt(5) == 0) {
            this.level.add(new ItemEntity(new ResourceItem(Resource.goldIngot), this.x + this.random.nextInt(11) - 5, this.y + this.random.nextInt(11) - 5));
        }
        if (this.level.player != null) {
            final Player player = this.level.player;
            player.score += 500;
        }
    }
}
