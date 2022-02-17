// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.entity;

import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.menu.Menu;
import com.mojang.ld22.menu.conversation.TrainingMenu;
import com.mojang.ld22.crafting.Crafting;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;

public class Bob extends Mob
{
    private int xa;
    private int ya;
    private int lvl;
    private int randomWalkTime;
    private int seekingYou;
    Player player;
    
    public Bob(final int lvl) {
        this.randomWalkTime = 0;
        this.seekingYou = 1;
        this.lvl = lvl;
        this.x = this.random.nextInt(1024);
        this.y = this.random.nextInt(1024);
        final int n = 500;
        this.maxHealth = n;
        this.health = n;
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.level.player != null && this.randomWalkTime == 0) {
            final int xd = this.level.player.x - this.x;
            final int yd = this.level.player.y - this.y;
            if (xd * xd + yd * yd < 5000) {
                this.seekingYou = 1;
            }
            if (this.seekingYou == 1) {
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
                else if (xd * xd + yd * yd < 5000) {
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
            }
            else {
                this.seekingYou = 1;
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
        int xt = 8;
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
        final int yo = this.y - 10;
        int col = Color.get(-1, 110, 114, 532);
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
    public boolean use(final Player player, final int attackDir) {
        player.game.setMenu(new TrainingMenu(Crafting.bobSales, player));
        return true;
    }
    
    protected void touchedBy(final Entity entity, final Player player) {
        if (entity instanceof Player) {
            this.seekingYou = 0;
        }
    }
    
    @Override
    protected void die() {
        super.die();
        for (int count = this.random.nextInt(100) + 1, i = 0; i < count; ++i) {
            this.level.add(new ItemEntity(new ResourceItem(Resource.goop), this.x + this.random.nextInt(11) - 5, this.y + this.random.nextInt(11) - 5));
            this.level.gatesOfHellOpen = 1;
        }
        if (this.level.player != null) {
            final Player player = this.level.player;
            player.score -= 150;
        }
    }
}