// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.level.tile;

import com.mojang.ld22.item.Item;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.entity.Mob;
import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.resource.Resource;
import java.util.Random;

public class Tile
{
    public static int tickCount;
    protected Random random;
    public static Tile[] tiles;
    public static Tile grass;
    public static Tile rock;
    public static Tile water;
    public static Tile flower;
    public static Tile tree;
    public static Tile dirt;
    public static Tile sand;
    public static Tile cactus;
    public static Tile cloudCactus;
    public static Tile hole;
    public static Tile pine;
    public static Tile elm;
    public static Tile shroom;
    public static Tile bPine;
    public static Tile bElm;
    public static Tile bShroom;
    public static Tile stile;
    public static Tile hurdle;
    public static Tile farmland;
    public static Tile turnip;
    public static Tile carrot;
    public static Tile radish;
    public static Tile onion;
    public static Tile garlic;
    public static Tile pepper;
    public static Tile stone;
    public static Tile tomato;
    public static Tile beet;
    public static Tile corn;
    public static Tile lettuce;
    public static Tile cabbage;
    public static Tile mustard;
    public static Tile squash;
    public static Tile bean;
    public static Tile pea;
    public static Tile treeSapling;
    public static Tile cactusSapling;
    public static Tile wheat;
    public static Tile lava;
    public static Tile stairsDown;
    public static Tile stairsUp;
    public static Tile infiniteFall;
    public static Tile cloud;
    public static Tile hardRock;
    public static Tile ironOre;
    public static Tile goldOre;
    public static Tile gemOre;
    public static Tile freshWater;
    public static Tile protoRock;
    public static Tile fire;
    public static Tile longGrass;
    public static Tile Scorched;
    public final byte id;
    public boolean connectsToGrass;
    public boolean connectsToSand;
    public boolean connectsToLava;
    public boolean connectsToWater;
    
    static {
        Tile.tickCount = 0;
        Tile.tiles = new Tile[253];
        Tile.grass = new GrassTile(0);
        Tile.rock = new RockTile(1);
        Tile.water = new WaterTile(2);
        Tile.flower = new FlowerTile(3);
        Tile.tree = new TreeTile(4);
        Tile.dirt = new DirtTile(5);
        Tile.sand = new SandTile(6);
        Tile.cactus = new CactusTile(7);
        Tile.cloudCactus = new CloudCactusTile(8);
        Tile.hole = new HoleTile(9);
        Tile.pine = new PineTile(10);
        Tile.elm = new ElmTile(11);
        Tile.shroom = new ShroomTile(12);
        Tile.bPine = new BPineTile(13);
        Tile.bElm = new BElmTile(14);
        Tile.bShroom = new BShroomTile(15);
        Tile.stile = new StileTile(16);
        Tile.hurdle = new HurdleTile(17);
        Tile.farmland = new FarmTile(18);
        Tile.turnip = new TurnipTile(19);
        Tile.carrot = new CarrotTile(20);
        Tile.radish = new RadishTile(21);
        Tile.onion = new OnionTile(22);
        Tile.garlic = new GarlicTile(23);
        Tile.pepper = new PepperTile(24);
        Tile.stone = new StoneTile(25);
        Tile.tomato = new TomatoTile(26);
        Tile.beet = new BeetTile(27);
        Tile.corn = new CornTile(28);
        Tile.lettuce = new LettuceTile(29);
        Tile.cabbage = new CabbageTile(30);
        Tile.mustard = new MustardTile(31);
        Tile.squash = new SquashTile(32);
        Tile.bean = new BeanTile(33);
        Tile.pea = new PeaTile(34);
        Tile.treeSapling = new SaplingTile(40, Tile.grass, Tile.tree);
        Tile.cactusSapling = new SaplingTile(41, Tile.sand, Tile.cactus);
        Tile.wheat = new WheatTile(42);
        Tile.lava = new LavaTile(43);
        Tile.stairsDown = new StairsTile(44, false);
        Tile.stairsUp = new StairsTile(45, true);
        Tile.infiniteFall = new InfiniteFallTile(36);
        Tile.cloud = new CloudTile(47);
        Tile.hardRock = new HardRockTile(48);
        Tile.ironOre = new OreTile(49, Resource.ironOre);
        Tile.goldOre = new OreTile(50, Resource.goldOre);
        Tile.gemOre = new OreTile(51, Resource.gem);
        Tile.freshWater = new FreshWaterTile(52);
        Tile.protoRock = new ProtoRockTile(53);
        Tile.fire = new FireTile(54);
        Tile.longGrass = new LongGrassTile(55);
        Tile.Scorched = new ScorchedTile(56);
    }
    
    public Tile(final int id) {
        this.random = new Random();
        this.connectsToGrass = false;
        this.connectsToSand = false;
        this.connectsToLava = false;
        this.connectsToWater = false;
        this.id = (byte)id;
        if (Tile.tiles[id] != null) {
            throw new RuntimeException("Duplicate tile ids!");
        }
        Tile.tiles[id] = this;
    }
    
    public void render(final Screen screen, final Level level, final int x, final int y) {
    }
    
    public boolean mayPass(final Level level, final int x, final int y, final Entity e) {
        return true;
    }
    
    public boolean canBurn(final Level level, final int x, final int y, final Entity e) {
        return true;
    }
    
    public boolean mayEat(final Level level, final int x, final int y, final Entity e) {
        return false;
    }
    
    public int getLightRadius(final Level level, final int x, final int y) {
        return 0;
    }
    
    public void hurt(final Level level, final int x, final int y, final Mob source, final int dmg, final int attackDir) {
    }
    
    public void bumpedInto(final Level level, final int xt, final int yt, final Entity entity) {
    }
    
    public void tick(final Level level, final int xt, final int yt) {
    }
    
    public void steppedOn(final Level level, final int xt, final int yt, final Entity entity) {
    }
    
    public boolean interact(final Level level, final int xt, final int yt, final Player player, final Item item, final int attackDir) {
        return false;
    }
    
    public boolean use(final Level level, final int xt, final int yt, final Player player, final int attackDir) {
        return false;
    }
    
    public boolean connectsToLiquid() {
        return this.connectsToWater || this.connectsToLava;
    }
    
    public boolean canBurn(final Level level, final int xt, final int yt, final Tile e) {
        return false;
    }
    
    public boolean flamable() {
        return false;
    }
}
