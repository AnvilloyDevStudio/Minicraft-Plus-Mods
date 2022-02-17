// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.entity;

import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;

public class SeaMonster extends Mob
{
    private int xa;
    private int ya;
    private int lvl;
    private int randomWalkTime;
    private int monsterFrame;
    private int monsterFrameDelay;
    private int monsterFrame2;
    private int monsterFrameDelay2;
    private int monsterFrame3;
    private int monsterFrameDelay3;
    
    public SeaMonster(final int lvl) {
        this.randomWalkTime = 0;
        this.monsterFrame = 0;
        this.monsterFrameDelay = 0;
        this.monsterFrame2 = 6;
        this.monsterFrameDelay2 = 10;
        this.monsterFrame3 = 11;
        this.monsterFrameDelay3 = 10;
        this.lvl = lvl;
        this.x = this.random.nextInt(1024);
        this.y = this.random.nextInt(1024);
        final int n = lvl * lvl * 200;
        this.maxHealth = n;
        this.health = n;
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.level.player != null && this.randomWalkTime == 0) {
            final int xd = this.level.player.x - this.x;
            final int yd = this.level.player.y - this.y;
            if (xd * xd + yd * yd < 3025) {
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
        final int xt = 16;
        final int yt = 28;
        final int xo = this.x - 8;
        final int yo = this.y - 11;
        if (this.level.player == null) {
            return;
        }
        if (this.level.player != null) {
            final int xd = this.level.player.x - this.x;
            final int n = this.level.player.y - this.y;
        }
        int col = Color.get(-1, 200, 232, 343);
        if (this.hurtTime > 0) {
            col = Color.get(-1, 555, 555, 555);
        }
        int waterColor = Color.get(-1, -1, 115, 335);
        if (this.tickTime / 8 % 2 == 0) {
            waterColor = Color.get(-1, 335, 5, 115);
        }
        if (this.monsterFrameDelay++ == 20) {
            ++this.monsterFrame;
            this.monsterFrameDelay = 0;
        }
        switch (this.monsterFrame) {
            case 0: {
                screen.render(xo + 6, yo + 2, xt + 1 + (yt + 0) * 32, col, 0);
                break;
            }
            case 1: {
                screen.render(xo + 6, yo + 2, xt + 0 + (yt + 0) * 32, col, 0);
                break;
            }
            case 2: {
                screen.render(xo + 6, yo + 2, xt + 0 + (yt + 1) * 32, col, 0);
                break;
            }
            case 3: {
                screen.render(xo + 6, yo + 2, xt + 0 + 2 + (yt + 1) * 32, col, 1);
                break;
            }
            case 4: {
                screen.render(xo + 6, yo + 2, xt + 0 + 2 + (yt + 0) * 32, col, 1);
                break;
            }
            case 5: {
                screen.render(xo + 6, yo + 2, xt + 1 + 2 + (yt + 0) * 32, col, 1);
                break;
            }
            case 6: {
                screen.render(xo + 6, yo + 2, xt + 0 + 2 + (yt + 0) * 32, col, 1);
                break;
            }
            case 7: {
                screen.render(xo + 0 - 4, yo + 10, 421, waterColor, 0);
                screen.render(xo + 8 - 4, yo + 10, 421, waterColor, 1);
                screen.render(xo + 6, yo + 2, xt + 0 + 4 + (yt + 1) * 32, col, 1);
                break;
            }
            case 8: {
                screen.render(xo + 0 - 4, yo + 10, 421, waterColor, 0);
                screen.render(xo + 8 - 4, yo + 10, 421, waterColor, 1);
                screen.render(xo + 6, yo + 2, xt + 0 + 4 + (yt + 1) * 32, col, 0);
                screen.render(xo + 0, yo + 8, xt + 1 + (yt + 1) * 32, col, 0);
                break;
            }
            case 9: {
                screen.render(xo + 0 - 4, yo + 10, 421, waterColor, 0);
                screen.render(xo + 8 - 4, yo + 10, 421, waterColor, 1);
                screen.render(xo + 6, yo + 2, xt + 0 + 4 + (yt + 0) * 32, col, 0);
                screen.render(xo + 0, yo + 8, xt + 3 + (yt + 1) * 32, col, 0);
                break;
            }
            case 10: {
                screen.render(xo + 0 - 4, yo + 10, 421, waterColor, 0);
                screen.render(xo + 8 - 4, yo + 10, 421, waterColor, 1);
                screen.render(xo + 6, yo + 2, xt + 1 + 4 + (yt + 0) * 32, col, 0);
                screen.render(xo + 0, yo + 8, xt + 5 + (yt + 1) * 32, col, 0);
                break;
            }
            case 11: {
                screen.render(xo + 0 - 4, yo + 10, 421, waterColor, 0);
                screen.render(xo + 8 - 4, yo + 10, 421, waterColor, 1);
                screen.render(xo + 6, yo + 2, xt + 0 + 4 + (yt + 0) * 32, col, 0);
                screen.render(xo + 0, yo + 9, xt + 7 + (yt + 1) * 32, col, 0);
                break;
            }
            case 12: {
                screen.render(xo + 0 - 4, yo + 10, 421, waterColor, 0);
                screen.render(xo + 8 - 4, yo + 10, 421, waterColor, 1);
                screen.render(xo + 6, yo + 2, xt + 0 + 4 + (yt + 1) * 32, col, 0);
                screen.render(xo + 0, yo + 8, xt + 5 + (yt + 1) * 32, col, 0);
                break;
            }
            case 13: {
                screen.render(xo + 0 - 4, yo + 10, 421, waterColor, 0);
                screen.render(xo + 8 - 4, yo + 10, 421, waterColor, 1);
                screen.render(xo + 6, yo + 2, xt + 0 + 6 + (yt + 1) * 32, col, 1);
                screen.render(xo + 0, yo + 8, xt + 3 + (yt + 1) * 32, col, 0);
                break;
            }
            case 14: {
                screen.render(xo + 0 - 4, yo + 10, 421, waterColor, 0);
                screen.render(xo + 8 - 4, yo + 10, 421, waterColor, 1);
                screen.render(xo + 6, yo + 2, xt + 0 + 6 + (yt + 0) * 32, col, 1);
                screen.render(xo + 0, yo + 8, xt + 1 + (yt + 1) * 32, col, 0);
                break;
            }
            case 15: {
                screen.render(xo + 0 - 4, yo + 10, 421, waterColor, 0);
                screen.render(xo + 8 - 4, yo + 10, 421, waterColor, 1);
                screen.render(xo + 6, yo + 3, xt + 1 + 6 + (yt + 0) * 32, col, 1);
                break;
            }
            case 16: {
                screen.render(xo + 6, yo + 3, xt + 0 + 6 + (yt + 0) * 32, col, 1);
                break;
            }
            case 17: {
                screen.render(xo + 6, yo + 3, xt + 0 + 6 + (yt + 1) * 32, col, 1);
                break;
            }
            case 18: {
                screen.render(xo + 6, yo + 3, xt + 0 + 4 + (yt + 1) * 32, col, 0);
                break;
            }
            case 19: {
                screen.render(xo + 6, yo + 3, xt + 0 + 4 + (yt + 0) * 32, col, 0);
                break;
            }
            case 20: {
                screen.render(xo + 6, yo + 3, xt + 1 + 4 + (yt + 0) * 32, col, 0);
                break;
            }
            case 21: {
                screen.render(xo + 6, yo + 3, xt + 0 + 4 + (yt + 0) * 32, col, 0);
                break;
            }
            case 22: {
                screen.render(xo + 6, yo + 3, xt + 0 + 4 + (yt + 1) * 32, col, 0);
                break;
            }
            case 23: {
                screen.render(xo + 6, yo + 3, xt + 0 + 4 + (yt + 1) * 32, col, 1);
                break;
            }
            case 24: {
                screen.render(xo + 6, yo + 3, xt + 0 + 4 + (yt + 0) * 32, col, 1);
                break;
            }
            case 25: {
                screen.render(xo + 6, yo + 3, xt + 1 + 4 + (yt + 0) * 32, col, 1);
                break;
            }
            case 26: {
                screen.render(xo + 6, yo + 3, xt + 0 + 4 + (yt + 0) * 32, col, 1);
                break;
            }
            case 27: {
                screen.render(xo + 6, yo + 3, xt + 0 + 4 + (yt + 1) * 32, col, 1);
                break;
            }
            case 28: {
                screen.render(xo + 6, yo + 3, xt + 0 + 4 + (yt + 1) * 32, col, 0);
                break;
            }
            case 29: {
                screen.render(xo + 6, yo + 3, xt + 0 + 2 + (yt + 0) * 32, col, 0);
                break;
            }
            default: {
                if (this.monsterFrame > 40) {
                    this.monsterFrame = 0;
                    break;
                }
                break;
            }
        }
        if (this.monsterFrameDelay2++ == 20) {
            ++this.monsterFrame2;
            this.monsterFrameDelay2 = 0;
        }
        switch (this.monsterFrame2) {
            case 0: {
                screen.render(xo + 0, yo + 2, xt + 1 + (yt + 0) * 32, col, 0);
                break;
            }
            case 1: {
                screen.render(xo + 0, yo + 2, xt + 0 + (yt + 0) * 32, col, 0);
                break;
            }
            case 2: {
                screen.render(xo + 0, yo + 2, xt + 0 + (yt + 1) * 32, col, 0);
                break;
            }
            case 3: {
                screen.render(xo + 0, yo + 2, xt + 0 + 2 + (yt + 1) * 32, col, 1);
                break;
            }
            case 4: {
                screen.render(xo + 0, yo + 2, xt + 0 + 2 + (yt + 0) * 32, col, 1);
                break;
            }
            case 5: {
                screen.render(xo + 0, yo + 2, xt + 1 + 2 + (yt + 0) * 32, col, 1);
                break;
            }
            case 6: {
                screen.render(xo + 0, yo + 2, xt + 0 + 2 + (yt + 0) * 32, col, 1);
                break;
            }
            case 7: {
                screen.render(xo + 0, yo + 2, xt + 0 + 4 + (yt + 1) * 32, col, 1);
                break;
            }
            case 8: {
                screen.render(xo + 0, yo + 2, xt + 0 + 4 + (yt + 1) * 32, col, 0);
                break;
            }
            case 9: {
                screen.render(xo + 0, yo + 2, xt + 0 + 4 + (yt + 0) * 32, col, 0);
                break;
            }
            case 10: {
                screen.render(xo + 0, yo + 2, xt + 1 + 4 + (yt + 0) * 32, col, 0);
                break;
            }
            case 11: {
                screen.render(xo + 0, yo + 2, xt + 0 + 4 + (yt + 0) * 32, col, 0);
                break;
            }
            case 12: {
                screen.render(xo + 0, yo + 2, xt + 0 + 4 + (yt + 1) * 32, col, 0);
                break;
            }
            case 13: {
                screen.render(xo + 0, yo + 2, xt + 0 + 6 + (yt + 1) * 32, col, 1);
                break;
            }
            case 14: {
                screen.render(xo + 0, yo + 2, xt + 0 + 6 + (yt + 0) * 32, col, 1);
                break;
            }
            case 15: {
                screen.render(xo + 0, yo + 2, xt + 1 + 6 + (yt + 0) * 32, col, 1);
                break;
            }
            case 16: {
                screen.render(xo + 0, yo + 2, xt + 0 + 6 + (yt + 0) * 32, col, 1);
                break;
            }
            case 17: {
                screen.render(xo + 0, yo + 2, xt + 0 + 6 + (yt + 1) * 32, col, 1);
                break;
            }
            case 18: {
                screen.render(xo + 0, yo + 2, xt + 0 + 4 + (yt + 1) * 32, col, 0);
                break;
            }
            case 19: {
                screen.render(xo + 0, yo + 2, xt + 0 + 4 + (yt + 0) * 32, col, 0);
                break;
            }
            case 20: {
                screen.render(xo + 0, yo + 2, xt + 1 + 4 + (yt + 0) * 32, col, 0);
                break;
            }
            case 21: {
                screen.render(xo + 0, yo + 2, xt + 0 + 4 + (yt + 0) * 32, col, 0);
                break;
            }
            case 22: {
                screen.render(xo + 0, yo + 2, xt + 0 + 4 + (yt + 1) * 32, col, 0);
                break;
            }
            case 23: {
                screen.render(xo + 0, yo + 2, xt + 0 + 4 + (yt + 1) * 32, col, 1);
                break;
            }
            case 24: {
                screen.render(xo + 0, yo + 2, xt + 0 + 4 + (yt + 0) * 32, col, 1);
                break;
            }
            case 25: {
                screen.render(xo + 0, yo + 2, xt + 1 + 4 + (yt + 0) * 32, col, 1);
                break;
            }
            case 26: {
                screen.render(xo + 0, yo + 2, xt + 0 + 4 + (yt + 0) * 32, col, 1);
                break;
            }
            case 27: {
                screen.render(xo + 0, yo + 2, xt + 0 + 4 + (yt + 1) * 32, col, 1);
                break;
            }
            case 28: {
                screen.render(xo + 0, yo + 2, xt + 0 + 4 + (yt + 1) * 32, col, 0);
                break;
            }
            case 29: {
                screen.render(xo + 0, yo + 2, xt + 0 + 2 + (yt + 0) * 32, col, 0);
                break;
            }
            default: {
                if (this.monsterFrame2 > 39) {
                    this.monsterFrame2 = 0;
                    break;
                }
                break;
            }
        }
        if (this.monsterFrameDelay3++ == 18) {
            ++this.monsterFrame3;
            this.monsterFrameDelay3 = 0;
        }
        switch (this.monsterFrame2) {
            case 0: {
                screen.render(xo - 6, yo + 4, xt + 1 + (yt + 0) * 32, col, 0);
                break;
            }
            case 1: {
                screen.render(xo - 6, yo + 4, xt + 0 + (yt + 0) * 32, col, 0);
                break;
            }
            case 2: {
                screen.render(xo - 6, yo + 4, xt + 0 + (yt + 1) * 32, col, 0);
                break;
            }
            case 3: {
                screen.render(xo - 6, yo + 4, xt + 0 + 2 + (yt + 1) * 32, col, 1);
                break;
            }
            case 4: {
                screen.render(xo - 6, yo + 4, xt + 0 + 2 + (yt + 0) * 32, col, 1);
                break;
            }
            case 5: {
                screen.render(xo - 6, yo + 4, xt + 1 + 2 + (yt + 0) * 32, col, 1);
                break;
            }
            case 6: {
                screen.render(xo - 6, yo + 4, xt + 0 + 2 + (yt + 0) * 32, col, 1);
                break;
            }
            case 7: {
                screen.render(xo - 6, yo + 4, xt + 0 + 4 + (yt + 1) * 32, col, 1);
                break;
            }
            case 8: {
                screen.render(xo - 6, yo + 4, xt + 0 + 4 + (yt + 1) * 32, col, 0);
                break;
            }
            case 9: {
                screen.render(xo - 6, yo + 4, xt + 0 + 4 + (yt + 0) * 32, col, 0);
                break;
            }
            case 10: {
                screen.render(xo - 6, yo + 4, xt + 1 + 4 + (yt + 0) * 32, col, 0);
                break;
            }
            case 11: {
                screen.render(xo - 6, yo + 4, xt + 0 + 4 + (yt + 0) * 32, col, 0);
                break;
            }
            case 12: {
                screen.render(xo - 6, yo + 4, xt + 0 + 4 + (yt + 1) * 32, col, 0);
                break;
            }
            case 13: {
                screen.render(xo - 6, yo + 4, xt + 0 + 6 + (yt + 1) * 32, col, 1);
                break;
            }
            case 14: {
                screen.render(xo - 6, yo + 4, xt + 0 + 6 + (yt + 0) * 32, col, 1);
                break;
            }
            case 15: {
                screen.render(xo - 6, yo + 4, xt + 1 + 6 + (yt + 0) * 32, col, 1);
                break;
            }
            case 16: {
                screen.render(xo - 6, yo + 4, xt + 0 + 6 + (yt + 0) * 32, col, 1);
                break;
            }
            case 17: {
                screen.render(xo - 6, yo + 4, xt + 0 + 6 + (yt + 1) * 32, col, 1);
                break;
            }
            case 18: {
                screen.render(xo - 6, yo + 4, xt + 0 + 4 + (yt + 1) * 32, col, 0);
                break;
            }
            case 19: {
                screen.render(xo - 6, yo + 4, xt + 0 + 4 + (yt + 0) * 32, col, 0);
                break;
            }
            case 20: {
                screen.render(xo - 6, yo + 4, xt + 1 + 4 + (yt + 0) * 32, col, 0);
                break;
            }
            case 21: {
                screen.render(xo - 6, yo + 4, xt + 0 + 4 + (yt + 0) * 32, col, 0);
                break;
            }
            case 22: {
                screen.render(xo - 6, yo + 4, xt + 0 + 4 + (yt + 1) * 32, col, 0);
                break;
            }
            case 23: {
                screen.render(xo - 6, yo + 4, xt + 0 + 4 + (yt + 1) * 32, col, 1);
                break;
            }
            case 24: {
                screen.render(xo - 6, yo + 4, xt + 0 + 4 + (yt + 0) * 32, col, 1);
                break;
            }
            case 25: {
                screen.render(xo - 6, yo + 4, xt + 1 + 4 + (yt + 0) * 32, col, 1);
                break;
            }
            case 26: {
                screen.render(xo - 6, yo + 4, xt + 0 + 4 + (yt + 0) * 32, col, 1);
                break;
            }
            case 27: {
                screen.render(xo - 6, yo + 4, xt + 0 + 4 + (yt + 1) * 32, col, 1);
                break;
            }
            case 28: {
                screen.render(xo - 6, yo + 4, xt + 0 + 4 + (yt + 1) * 32, col, 0);
                break;
            }
            case 29: {
                screen.render(xo - 6, yo + 4, xt + 0 + 2 + (yt + 0) * 32, col, 0);
                break;
            }
            default: {
                if (this.monsterFrame3 > 38) {
                    this.monsterFrame3 = 0;
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public boolean canWalk() {
        return false;
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
            entity.hurt(this, this.lvl + 1, this.dir);
        }
    }
    
    @Override
    protected void die() {
        super.die();
        for (int count = this.random.nextInt(10) + 1, i = 0; i < count; ++i) {
            this.level.add(new ItemEntity(new ResourceItem(Resource.squid), this.x + this.random.nextInt(11) - 5, this.y + this.random.nextInt(11) - 5));
        }
        if (this.random.nextInt(3) == 0) {
            this.level.add(new ItemEntity(new ResourceItem(Resource.silverIngot), this.x + this.random.nextInt(11) - 5, this.y + this.random.nextInt(11) - 5));
        }
        for (int count = this.random.nextInt(4) + 1, i = 0; i < count; ++i) {
            this.level.add(new ItemEntity(new ResourceItem(Resource.goldIngot), this.x + this.random.nextInt(11) - 5, this.y + this.random.nextInt(11) - 5));
        }
        if (this.level.player != null) {
            final Player player = this.level.player;
            player.score += 100 * this.lvl;
        }
    }
}
