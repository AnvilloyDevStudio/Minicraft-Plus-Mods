// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.entity;

import com.mojang.ld22.item.Item;
import java.util.List;
import java.util.Collection;
import com.mojang.ld22.level.tile.Tile;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.level.Level;
import java.util.Random;

public class Entity
{
    protected final Random random;
    public int x;
    public int y;
    public int xr;
    public int yr;
    public boolean removed;
    public Level level;
    
    public Entity() {
        this.random = new Random();
        this.xr = 6;
        this.yr = 6;
    }
    
    public void render(final Screen screen) {
    }
    
    public void tick() {
    }
    
    public void remove() {
        this.removed = true;
    }
    
    public final void init(final Level level) {
        this.level = level;
    }
    
    public boolean intersects(final int x0, final int y0, final int x1, final int y1) {
        return this.x + this.xr >= x0 && this.y + this.yr >= y0 && this.x - this.xr <= x1 && this.y - this.yr <= y1;
    }
    
    public boolean blocks(final Entity e) {
        return false;
    }
    
    public void hurt(final Mob mob, final int dmg, final int attackDir) {
    }
    
    public void hurt(final Tile tile, final int x, final int y, final int dmg) {
    }
    
    public void hurt(final Fire fire, final int x, final int y, final int dmg) {
    }
    
    public boolean move(final int xa, final int ya) {
        if (xa != 0 || ya != 0) {
            boolean stopped = true;
            if (xa != 0 && this.move2(xa, 0)) {
                stopped = false;
            }
            if (ya != 0 && this.move2(0, ya)) {
                stopped = false;
            }
            if (!stopped) {
                final int xt = this.x >> 4;
                final int yt = this.y >> 4;
                this.level.getTile(xt, yt).steppedOn(this.level, xt, yt, this);
            }
            return !stopped;
        }
        return true;
    }
    
    protected boolean move2(final int xa, final int ya) {
        if (xa != 0 && ya != 0) {
            throw new IllegalArgumentException("Move2 can only move along one axis at a time!");
        }
        final int xto0 = this.x - this.xr >> 4;
        final int yto0 = this.y - this.yr >> 4;
        final int xto2 = this.x + this.xr >> 4;
        final int yto2 = this.y + this.yr >> 4;
        final int xt0 = this.x + xa - this.xr >> 4;
        final int yt0 = this.y + ya - this.yr >> 4;
        final int xt2 = this.x + xa + this.xr >> 4;
        final int yt2 = this.y + ya + this.yr >> 4;
        boolean blocked = false;
        for (int yt3 = yt0; yt3 <= yt2; ++yt3) {
            for (int xt3 = xt0; xt3 <= xt2; ++xt3) {
                if (xt3 < xto0 || xt3 > xto2 || yt3 < yto0 || yt3 > yto2) {
                    this.level.getTile(xt3, yt3).bumpedInto(this.level, xt3, yt3, this);
                    if (this.level.getTile(xt3, yt3).mayEat(this.level, xt3, yt3, this)) {
                        this.level.setTile(xt3, yt3, Tile.dirt, 0);
                    }
                    if (this.holdingOrb()) {
                        this.level.setTile(xt3, yt3, Tile.protoRock, 0);
                    }
                    if (this.holdingGreenOrb()) {
                        this.greenOrb(xt3, yt3);
                    }
                    if (this.level.getTile(xt3, yt3).canBurn(this.level, xt3, yt3, this)) {
                        this.level.setFire(xt3, yt3, 1);
                    }
                    if (!this.level.getTile(xt3, yt3).mayPass(this.level, xt3, yt3, this)) {
                        blocked = true;
                        return false;
                    }
                }
            }
        }
        if (blocked) {
            return false;
        }
        final List<Entity> wasInside = this.level.getEntities(this.x - this.xr, this.y - this.yr, this.x + this.xr, this.y + this.yr);
        final List<Entity> isInside = this.level.getEntities(this.x + xa - this.xr, this.y + ya - this.yr, this.x + xa + this.xr, this.y + ya + this.yr);
        for (int i = 0; i < isInside.size(); ++i) {
            final Entity e = isInside.get(i);
            if (e != this) {
                e.touchedBy(this);
            }
        }
        isInside.removeAll(wasInside);
        for (int i = 0; i < isInside.size(); ++i) {
            final Entity e = isInside.get(i);
            if (e != this) {
                if (e.blocks(this)) {
                    return false;
                }
            }
        }
        this.x += xa;
        this.y += ya;
        return true;
    }
    
    void greenOrb(final int xt, final int yt) {
        final int x = this.random.nextInt(13) + 1;
        switch (x) {
            case 1: {
                this.level.setTile(xt, yt, Tile.bPine, 0);
                break;
            }
            case 2: {
                this.level.setTile(xt, yt, Tile.treeSapling, 0);
                break;
            }
            case 3: {
                this.level.setTile(xt, yt, Tile.bElm, 0);
                break;
            }
            case 4: {
                this.level.setTile(xt, yt, Tile.bShroom, 0);
                break;
            }
            case 5: {
                this.level.setTile(xt, yt, Tile.flower, 0);
                break;
            }
            case 6: {
                this.level.setTile(xt, yt, Tile.grass, 0);
                break;
            }
            case 7: {
                this.level.setTile(xt, yt, Tile.cactusSapling, 0);
                break;
            }
            case 8: {
                this.level.setTile(xt, yt, Tile.wheat, 0);
                break;
            }
            case 9: {
                this.level.setTile(xt, yt, Tile.carrot, 0);
                break;
            }
            case 10: {
                this.level.setTile(xt, yt, Tile.pepper, 0);
                break;
            }
            case 11: {
                this.level.setTile(xt, yt, Tile.garlic, 0);
                break;
            }
            case 12: {
                this.level.setTile(xt, yt, Tile.radish, 0);
                break;
            }
            case 13: {
                this.level.setTile(xt, yt, Tile.longGrass, 0);
                break;
            }
            case 14: {
                this.level.setTile(xt, yt, Tile.turnip, 0);
                break;
            }
            case 15: {
                this.level.setTile(xt, yt, Tile.onion, 0);
                break;
            }
            case 16: {
                this.level.setTile(xt, yt, Tile.pepper, 0);
                break;
            }
            case 17: {
                this.level.setTile(xt, yt, Tile.tomato, 0);
                break;
            }
            case 18: {
                this.level.setTile(xt, yt, Tile.beet, 0);
                break;
            }
            case 19: {
                this.level.setTile(xt, yt, Tile.corn, 0);
                break;
            }
            case 20: {
                this.level.setTile(xt, yt, Tile.lettuce, 0);
                break;
            }
            case 21: {
                this.level.setTile(xt, yt, Tile.cabbage, 0);
                break;
            }
            case 22: {
                this.level.setTile(xt, yt, Tile.mustard, 0);
                break;
            }
            case 24: {
                this.level.setTile(xt, yt, Tile.squash, 0);
                break;
            }
            case 25: {
                this.level.setTile(xt, yt, Tile.bean, 0);
                break;
            }
            case 26: {
                this.level.setTile(xt, yt, Tile.pea, 0);
                break;
            }
            default: {
                this.level.setTile(xt, yt, Tile.grass, 0);
                break;
            }
        }
        this.level.setFire(xt, yt, 0);
    }
    
    protected void touchedBy(final Entity entity) {
    }
    
    public boolean isBlockableBy(final Mob mob) {
        return true;
    }
    
    public void touchItem(final ItemEntity itemEntity) {
    }
    
    public boolean canSwim() {
        return false;
    }
    
    public boolean canBurn() {
        return false;
    }
    
    public boolean burnsThings() {
        return false;
    }
    
    public boolean canDig() {
        return false;
    }
    
    public boolean canPass() {
        return false;
    }
    
    public boolean isPlayer() {
        return false;
    }
    
    public boolean canClimb() {
        return false;
    }
    
    public boolean canWalk() {
        return true;
    }
    
    public boolean isSwimmer() {
        return false;
    }
    
    public boolean eatsTruffles() {
        return false;
    }
    
    public boolean eatsTrees() {
        return false;
    }
    
    public boolean eatsRock() {
        return false;
    }
    
    public boolean eatsFood() {
        return false;
    }
    
    public boolean holdingOrb() {
        return false;
    }
    
    public boolean isOrb() {
        return false;
    }
    
    public boolean holdingGreenOrb() {
        return false;
    }
    
    public boolean holdingWhiteOrb() {
        return false;
    }
    
    public boolean isGreenOrb() {
        return false;
    }
    
    public boolean isWhiteOrb() {
        return false;
    }
    
    public boolean grazes() {
        return false;
    }
    
    public boolean interact(final Player player, final Item item, final int attackDir) {
        return item.interact(player, this, attackDir);
    }
    
    public boolean use(final Player player, final int attackDir) {
        return false;
    }
    
    public int getLightRadius() {
        return 0;
    }
}
