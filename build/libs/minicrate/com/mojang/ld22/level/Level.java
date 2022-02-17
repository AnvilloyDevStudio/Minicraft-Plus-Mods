// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.level;

import com.mojang.ld22.entity.Mob;
import com.mojang.ld22.entity.HellBat;
import com.mojang.ld22.entity.Muncher;
import com.mojang.ld22.entity.Bob;
import com.mojang.ld22.entity.BlueGirl;
import com.mojang.ld22.entity.Girl;
import com.mojang.ld22.entity.MonkeyBoy;
import com.mojang.ld22.entity.Ship;
import com.mojang.ld22.entity.SeaMonster;
import com.mojang.ld22.entity.LandShark;
import com.mojang.ld22.entity.Saurus;
import com.mojang.ld22.entity.Sheepish;
import com.mojang.ld22.entity.Turducken;
import com.mojang.ld22.entity.Chicky;
import com.mojang.ld22.entity.Piggy;
import com.mojang.ld22.entity.KittyKitty;
import com.mojang.ld22.entity.BlastMage;
import com.mojang.ld22.entity.BurningMan;
import com.mojang.ld22.entity.GhostGirl;
import com.mojang.ld22.entity.GirlZombie;
import com.mojang.ld22.entity.Skeleton;
import com.mojang.ld22.entity.Zombie;
import com.mojang.ld22.entity.Slime;
import java.util.Collections;
import java.util.Collection;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.entity.AirWizard;
import com.mojang.ld22.level.tile.Tile;
import com.mojang.ld22.level.levelgen.LevelGen;
import java.util.ArrayList;
import com.mojang.ld22.entity.Player;
import java.util.Comparator;
import com.mojang.ld22.entity.Entity;
import java.util.List;
import java.util.Random;

public class Level
{
    private Random random;
    public int w;
    public int h;
    public byte[] tiles;
    public byte[] data;
    public byte[] flame;
    public List<Entity>[] entitiesInTiles;
    public int grassColor;
    public int dirtColor;
    public int sandColor;
    private int depth;
    public int monsterDensity;
    public int gatesOfHellOpen;
    public List<Entity> entities;
    private Comparator<Entity> spriteSorter;
    private List<Entity> rowSprites;
    public Player player;
    
    public Level(final int w, final int h, final int level, final Level parentLevel) {
        this.random = new Random();
        this.flame = new byte[60000];
        this.grassColor = 141;
        this.dirtColor = 322;
        this.sandColor = 550;
        this.monsterDensity = 8;
        this.gatesOfHellOpen = 0;
        this.entities = new ArrayList<Entity>();
        this.spriteSorter = new Comparator<Entity>() {
            @Override
            public int compare(final Entity e0, final Entity e1) {
                if (e1.y < e0.y) {
                    return 1;
                }
                if (e1.y > e0.y) {
                    return -1;
                }
                return 0;
            }
        };
        this.rowSprites = new ArrayList<Entity>();
        if (level < 0) {
            this.dirtColor = 222;
        }
        this.depth = level;
        this.w = w;
        this.h = h;
        if (level == 1) {
            this.dirtColor = 444;
        }
        byte[][] maps;
        if (level == 0) {
            maps = LevelGen.createAndValidateTopMap(w, h);
        }
        else if (level < 0) {
            maps = LevelGen.createAndValidateUndergroundMap(w, h, -level);
            this.monsterDensity = 4;
        }
        else {
            maps = LevelGen.createAndValidateSkyMap(w, h);
            this.monsterDensity = 4;
        }
        this.tiles = maps[0];
        this.data = maps[1];
        if (parentLevel != null) {
            for (int y = 0; y < h; ++y) {
                for (int x = 0; x < w; ++x) {
                    if (parentLevel.getTile(x, y) == Tile.stairsDown) {
                        this.setTile(x, y, Tile.stairsUp, 0);
                        if (level == 0) {
                            this.setTile(x - 1, y, Tile.hardRock, 0);
                            this.setTile(x + 1, y, Tile.hardRock, 0);
                            this.setTile(x, y - 1, Tile.hardRock, 0);
                            this.setTile(x, y + 1, Tile.hardRock, 0);
                            this.setTile(x - 1, y - 1, Tile.hardRock, 0);
                            this.setTile(x - 1, y + 1, Tile.hardRock, 0);
                            this.setTile(x + 1, y - 1, Tile.hardRock, 0);
                            this.setTile(x + 1, y + 1, Tile.hardRock, 0);
                        }
                        else {
                            this.setTile(x - 1, y, Tile.dirt, 0);
                            this.setTile(x + 1, y, Tile.dirt, 0);
                            this.setTile(x, y - 1, Tile.dirt, 0);
                            this.setTile(x, y + 1, Tile.dirt, 0);
                            this.setTile(x - 1, y - 1, Tile.dirt, 0);
                            this.setTile(x - 1, y + 1, Tile.dirt, 0);
                            this.setTile(x + 1, y - 1, Tile.dirt, 0);
                            this.setTile(x + 1, y + 1, Tile.dirt, 0);
                        }
                    }
                }
            }
        }
        this.entitiesInTiles = new ArrayList[w * h];
        for (int i = 0; i < w * h; ++i) {
            this.entitiesInTiles[i] = new ArrayList<Entity>();
            this.flame[i] = 0;
        }
        if (level == 1) {
            final AirWizard aw = new AirWizard();
            aw.x = w * 8;
            aw.y = h * 8;
            this.add(aw);
        }
        this.stopFire();
    }
    
    public void renderBackground(final Screen screen, final int xScroll, final int yScroll) {
        final int xo = xScroll >> 4;
        final int yo = yScroll >> 4;
        final int w = screen.w + 15 >> 4;
        final int h = screen.h + 15 >> 4;
        screen.setOffset(xScroll, yScroll);
        for (int y = yo; y <= h + yo; ++y) {
            for (int x = xo; x <= w + xo; ++x) {
                this.getTile(x, y).render(screen, this, x, y);
            }
        }
        screen.setOffset(0, 0);
    }
    
    public void renderSprites(final Screen screen, final int xScroll, final int yScroll) {
        final int xo = xScroll >> 4;
        final int yo = yScroll >> 4;
        final int w = screen.w + 15 >> 4;
        final int h = screen.h + 15 >> 4;
        screen.setOffset(xScroll, yScroll);
        for (int y = yo; y <= h + yo; ++y) {
            for (int x = xo; x <= w + xo; ++x) {
                if (x >= 0 && y >= 0 && x < this.w) {
                    if (y < this.h) {
                        this.rowSprites.addAll(this.entitiesInTiles[x + y * this.w]);
                    }
                }
            }
            if (this.rowSprites.size() > 0) {
                this.sortAndRender(screen, this.rowSprites);
            }
            this.rowSprites.clear();
        }
        screen.setOffset(0, 0);
    }
    
    public void renderLight(final Screen screen, final int xScroll, final int yScroll) {
        final int xo = xScroll >> 4;
        final int yo = yScroll >> 4;
        final int w = screen.w + 15 >> 4;
        final int h = screen.h + 15 >> 4;
        screen.setOffset(xScroll, yScroll);
        for (int r = 4, y = yo - r; y <= h + yo + r; ++y) {
            for (int x = xo - r; x <= w + xo + r; ++x) {
                if (x >= 0 && y >= 0 && x < this.w) {
                    if (y < this.h) {
                        final List<Entity> entities = this.entitiesInTiles[x + y * this.w];
                        for (int i = 0; i < entities.size(); ++i) {
                            final Entity e = entities.get(i);
                            final int lr = e.getLightRadius();
                            if (lr > 0) {
                                screen.renderLight(e.x - 1, e.y - 4, lr * 8);
                            }
                        }
                        final int lr2 = this.getTile(x, y).getLightRadius(this, x, y);
                        if (lr2 > 0) {
                            screen.renderLight(x * 16 + 8, y * 16 + 8, lr2 * 8);
                        }
                    }
                }
            }
        }
        screen.setOffset(0, 0);
    }
    
    private void sortAndRender(final Screen screen, final List<Entity> list) {
        Collections.sort(list, this.spriteSorter);
        for (int i = 0; i < list.size(); ++i) {
            list.get(i).render(screen);
        }
    }
    
    public Tile getTile(final int x, final int y) {
        if (x < 0 || y < 0 || x >= this.w || y >= this.h) {
            return Tile.rock;
        }
        return Tile.tiles[this.tiles[x + y * this.w]];
    }
    
    public void setTile(final int x, final int y, final Tile t, final int dataVal) {
        if (x < 0 || y < 0 || x >= this.w || y >= this.h) {
            return;
        }
        this.tiles[x + y * this.w] = t.id;
        this.data[x + y * this.w] = (byte)dataVal;
    }
    
    public int getData(final int x, final int y) {
        if (x < 0 || y < 0 || x >= this.w || y >= this.h) {
            return 0;
        }
        return this.data[x + y * this.w] & 0xFF;
    }
    
    public void setData(final int x, final int y, final int val) {
        if (x < 0 || y < 0 || x >= this.w || y >= this.h) {
            return;
        }
        this.data[x + y * this.w] = (byte)val;
    }
    
    public int getFire(final int x, final int y) {
        if (x < 0 || y < 0 || x >= this.w || y >= this.h) {
            return 0;
        }
        return this.flame[x + y * this.w];
    }
    
    public void setFire(final int x, final int y, final int val) {
        if (x < 0 || y < 0 || x >= this.w || y >= this.h) {
            return;
        }
        this.flame[x + y * this.w] = (byte)val;
    }
    
    public void moreFire(final int x, final int y) {
        if (x < 0 || y < 0 || x >= this.w || y >= this.h) {
            return;
        }
        final byte[] flame = this.flame;
        final int n = x + y * this.w;
        ++flame[n];
    }
    
    public void add(final Entity entity) {
        if (entity instanceof Player) {
            this.player = (Player)entity;
        }
        entity.removed = false;
        this.entities.add(entity);
        entity.init(this);
        this.insertEntity(entity.x >> 4, entity.y >> 4, entity);
    }
    
    public void remove(final Entity e) {
        this.entities.remove(e);
        final int xto = e.x >> 4;
        final int yto = e.y >> 4;
        this.removeEntity(xto, yto, e);
    }
    
    private void insertEntity(final int x, final int y, final Entity e) {
        if (x < 0 || y < 0 || x >= this.w || y >= this.h) {
            return;
        }
        this.entitiesInTiles[x + y * this.w].add(e);
    }
    
    private void removeEntity(final int x, final int y, final Entity e) {
        if (x < 0 || y < 0 || x >= this.w || y >= this.h) {
            return;
        }
        this.entitiesInTiles[x + y * this.w].remove(e);
    }
    
    public void trySpawn(int count) {
        for (int i = 0; i < count; ++i) {
            int minLevel = 1;
            int maxLevel = 1;
            if (this.depth < 0) {
                maxLevel = -this.depth + 1;
            }
            if (this.depth > 0) {
                maxLevel = (minLevel = 4);
            }
            final int lvl = this.random.nextInt(maxLevel - minLevel + 1) + minLevel;
            int whatMonster;
            if (this.depth != 0) {
                whatMonster = this.random.nextInt(22) + 1;
            }
            else {
                whatMonster = this.random.nextInt(62) + 1;
            }
            Mob mob;
            if (whatMonster < 4) {
                mob = new Slime(lvl);
            }
            else if (whatMonster < 6) {
                mob = new Zombie(lvl);
            }
            else if (whatMonster < 10) {
                mob = new Skeleton(lvl);
            }
            else if (whatMonster < 14) {
                mob = new GirlZombie(lvl);
            }
            else if (whatMonster < 18) {
                mob = new GhostGirl(lvl);
            }
            else if (whatMonster < 20) {
                mob = new BurningMan(lvl);
            }
            else if (whatMonster < 21) {
                mob = new BlastMage();
            }
            else if (whatMonster < 25) {
                mob = new KittyKitty(lvl);
            }
            else if (whatMonster < 30) {
                mob = new Piggy(lvl);
            }
            else if (whatMonster < 34) {
                mob = new Chicky(lvl);
            }
            else if (whatMonster < 38) {
                mob = new Turducken(lvl);
            }
            else if (whatMonster < 42) {
                mob = new Sheepish(lvl);
            }
            else if (whatMonster < 44) {
                mob = new Saurus(lvl);
            }
            else if (whatMonster < 46) {
                mob = new LandShark(lvl);
            }
            else if (whatMonster < 47) {
                mob = new SeaMonster(lvl);
            }
            else if (whatMonster < 51) {
                mob = new Ship(lvl);
            }
            else if (whatMonster < 52) {
                mob = new MonkeyBoy(lvl);
            }
            else if (whatMonster < 54) {
                mob = new Girl(lvl);
            }
            else if (whatMonster < 56) {
                mob = new BlueGirl(lvl);
            }
            else if (whatMonster < 58) {
                mob = new Bob(lvl);
            }
            else if (whatMonster < 60) {
                mob = new Muncher(lvl);
            }
            else {
                mob = new Zombie(lvl);
            }
            if (mob.findStartPos(this)) {
                this.add(mob);
            }
            if (this.gatesOfHellOpen == 1) {
                count = this.random.nextInt(10) + 1;
                for (int i = 0; i < count; ++i) {
                    mob = new HellBat();
                    if (mob.findStartPos(this)) {
                        this.add(mob);
                    }
                    mob = new HellBat();
                    if (mob.findStartPos(this)) {
                        this.add(mob);
                    }
                    mob = new HellBat();
                    if (mob.findStartPos(this)) {
                        this.add(mob);
                    }
                    this.gatesOfHellOpen = 0;
                }
            }
        }
    }
    
    public int isSurface() {
        if (this.depth != 0) {
            return 0;
        }
        return 1;
    }
    
    public int whatDepth() {
        return this.depth;
    }
    
    public void stopFire() {
        for (int i = 0; i < this.w * this.h; ++i) {
            this.flame[i] = 0;
        }
    }
    
    public void tick() {
        this.trySpawn(1);
        for (int i = 0; i < this.w * this.h / 50; ++i) {
            final int xt = this.random.nextInt(this.w);
            final int yt = this.random.nextInt(this.w);
            this.getTile(xt, yt).tick(this, xt, yt);
        }
        for (int i = 0; i < this.entities.size(); ++i) {
            final Entity e = this.entities.get(i);
            final int xto = e.x >> 4;
            final int yto = e.y >> 4;
            e.tick();
            if (e.removed) {
                this.entities.remove(i--);
                this.removeEntity(xto, yto, e);
            }
            else {
                final int xt2 = e.x >> 4;
                final int yt2 = e.y >> 4;
                if (xto != xt2 || yto != yt2) {
                    this.removeEntity(xto, yto, e);
                    this.insertEntity(xt2, yt2, e);
                }
            }
        }
    }
    
    public List<Entity> getEntities(final int x0, final int y0, final int x1, final int y1) {
        final List<Entity> result = new ArrayList<Entity>();
        final int xt0 = (x0 >> 4) - 1;
        final int yt0 = (y0 >> 4) - 1;
        final int xt2 = (x1 >> 4) + 1;
        for (int yt2 = (y1 >> 4) + 1, y2 = yt0; y2 <= yt2; ++y2) {
            for (int x2 = xt0; x2 <= xt2; ++x2) {
                if (x2 >= 0 && y2 >= 0 && x2 < this.w) {
                    if (y2 < this.h) {
                        final List<Entity> entities = this.entitiesInTiles[x2 + y2 * this.w];
                        for (int i = 0; i < entities.size(); ++i) {
                            final Entity e = entities.get(i);
                            if (e.intersects(x0, y0, x1, y1)) {
                                result.add(e);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
