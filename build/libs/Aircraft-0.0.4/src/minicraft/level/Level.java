// Package declaration
package minicraft.level;

// Default Java Libraries
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

// Game imports
import minicraft.core.Game;
import minicraft.core.Updater;
import minicraft.core.io.Settings;
import minicraft.core.io.Sound;
import minicraft.entity.ClientTickable;
import minicraft.entity.Entity;
import minicraft.entity.ItemEntity;
import minicraft.entity.Spark;
import minicraft.entity.Spark2;
import minicraft.entity.Spark3;
import minicraft.entity.furniture.Chest;
import minicraft.entity.furniture.DungeonChest;
import minicraft.entity.furniture.Spawner;
import minicraft.entity.mob.Chicken;
import minicraft.entity.mob.Cow;
import minicraft.entity.mob.Creeper;
import minicraft.entity.mob.DefenderMob;
import minicraft.entity.mob.EnemyMob;
import minicraft.entity.mob.FrostMob;
import minicraft.entity.mob.Goat;
import minicraft.entity.mob.GuiMan;
import minicraft.entity.mob.Knight;
import minicraft.entity.mob.Mob;
import minicraft.entity.mob.MobAi;
import minicraft.entity.mob.PassiveMob;
import minicraft.entity.mob.Phyg;
import minicraft.entity.mob.Pig;
import minicraft.entity.mob.Player;
import minicraft.entity.mob.RemotePlayer;
import minicraft.entity.mob.Sheep;
import minicraft.entity.mob.Sheepuff;
import minicraft.entity.mob.Skeleton;
import minicraft.entity.mob.SkyMob;
import minicraft.entity.mob.Slime;
import minicraft.entity.mob.Snake;
import minicraft.entity.mob.Zombie;
import minicraft.entity.mob.boss.AirWizard;
import minicraft.entity.mob.boss.AirWizardPhase3;
import minicraft.entity.mob.villager.Cleric;
import minicraft.entity.mob.villager.Golem;
import minicraft.entity.mob.villager.Librarian;
import minicraft.entity.mob.villager.OldGolem;
import minicraft.entity.mob.villager.VillagerMob;
import minicraft.entity.particle.Particle;
import minicraft.gfx.Point;
import minicraft.gfx.Rectangle;
import minicraft.gfx.Screen;
import minicraft.item.Item;
import minicraft.level.tile.Tile;
import minicraft.level.tile.Tiles;
import minicraft.level.tile.TorchTile;

//--------------------------------------------------------------------------------------------------------------------------------------------------

/* 
 * This class is in charge of managing events or functions related to the world (Level)
 * 
 *  - Level variables
 *  - Sky dungeon generation
 *  - Checks for entities (AirWizard, mobs and dungeon chests)
 *  - Random music system
 *  - Mob spawn system
 *  - Mob removal system
 *  - Dungeons generation
 *  - Village generation
 */

public class Level {
    private Random random = new Random();

    private static final String[] levelNames = {
        "Heaven",
        "Surface",
        "Iron",
        "Gold",
        "Lava",
        "Dungeon",
        "Hell"
    };

    public static String getLevelName(int depth) {
        return levelNames[-1 * depth + 1];
    }

    public static String getDepthString(int depth) {
        return "Level " + (depth < 0 ? "B" + (-depth) : depth);
    }

    private static final int MOB_SPAWN_FACTOR = 100; // the chance of a mob actually trying to spawn when trySpawn is
    // called equals: mobCount / maxMobCount * MOB_SPAWN_FACTOR. so, it
    // basically equals the chance, 1/number, of a mob spawning when
    // the mob cap is reached. I hope that makes sense...

    public int w;
    public int h;
    private long seed; // The used seed that was used to generate the world

    public byte[] tiles; // An array of all the tiles in the world.
    public byte[] data; // An array of the data of the tiles in the world. // ?

    public static int randomMusic; // used for the Random music system in the current level

    public final int depth; // Depth level of the level
    public int monsterDensity = 8; // Affects the number of monsters that are on the level, bigger the number the less monsters spawn.
    public int maxMobCount;
    public int chestCount;
    public int mobCount = 0;

    /**
     * I will be using this lock to avoid concurrency exceptions in entities and
     * sparks set
     */
    private final Object entityLock = new Object();
    private final Set < Entity > entities = java.util.Collections.synchronizedSet(new HashSet < > ()); // A list of all the entities in the world
    private final Set < Spark > sparks = java.util.Collections.synchronizedSet(new HashSet < > ()); // A list of all the sparks in the world
    private final Set < Spark2 > sparks2 = java.util.Collections.synchronizedSet(new HashSet < > ()); // A list of all the sparks2 in the world
    private final Set < Spark3 > sparks3 = java.util.Collections.synchronizedSet(new HashSet < > ()); // A list of all the sparks3 in the world
    private final Set < Player > players = java.util.Collections.synchronizedSet(new HashSet < > ()); // A list of all the players in the world
    private final List < Entity > entitiesToAdd = new ArrayList < > (); // entities that will be added to the level on next tick, are stored here. This is for the sake of multithreading, optimization. (hopefully)
    private final List < Entity > entitiesToRemove = new ArrayList < > (); // entities that will be removed from the level on next tick are stored here. This is for the sake of multithreading optimization. (hopefully)

    // creates a sorter for all the entities to be rendered.
	private static Comparator<Entity> spriteSorter = Comparator.comparingInt(e -> e.y);

    public Entity[] getEntitiesToSave() {
        Entity[] allEntities = new Entity[entities.size() + sparks.size() + sparks2.size() + sparks3.size() + entitiesToAdd.size()];
        Entity[] toAdd = entitiesToAdd.toArray(new Entity[entitiesToAdd.size()]);
        Entity[] current = getEntityArray();
        System.arraycopy(current, 0, allEntities, 0, current.length);
        System.arraycopy(toAdd, 0, allEntities, current.length, toAdd.length);

        return allEntities;
    }

    // This is a solely debug method I made, to make printing repetitive stuff easier,
    // Should be changed to accept prepend and entity, or a tile (as an Object),
    // It will get the coordinates and class name from the object, and will divide coords
    // by 16 if passed an entity.
    public void printLevelLoc(String prefix, int x, int y) {
        printLevelLoc(prefix, x, y, "");
    }

    public void printLevelLoc(String prefix, int x, int y, String suffix) {
        String levelName = getLevelName(depth);

        System.out.println(prefix + " on " + levelName + " level (" + x + "," + y + ")" + suffix);
    }

    public void printTileLocs(Tile t) {
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++)
                if (getTile(x, y).id == t.id)
                    printLevelLoc(t.name, x, y);
    }

    public void printEntityLocs(Class < ? extends Entity > c) {
        int numfound = 0;
        for (Entity entity: getEntityArray()) {
            if (c.isAssignableFrom(entity.getClass())) {
                printLevelLoc(entity.toString(), entity.x >> 4, entity.y >> 4);
                numfound++;
            }
        }

        System.out.println("Found " + numfound + " entities in level of depth " + depth);
    }

    private void updateMobCap() {
        maxMobCount = 150 + 150 * Settings.getIdx("diff");
        if (depth == 1)
            maxMobCount /= 2;
        if (depth == 0 || depth == -4)
            maxMobCount = maxMobCount * 2 / 3;
    }

    public Level(int w, int h, long seed, int level, Level parentLevel, boolean makeWorld) {
        depth = level;
        this.w = w;
        this.h = h;
        this.seed = seed;
        byte[][] maps; // Multidimensional array (an array within a array), used for the map

        if (level != -4 && level != 0)
            monsterDensity = 8;

        updateMobCap();

        if (!makeWorld) {
            int arrsize = w * h;
            tiles = new byte[arrsize];
            data = new byte[arrsize];
            return;
        }

        if (Game.debug)
            System.out.println("Making level " + level + "...");

        maps = LevelGen.createAndValidateMap(w, h, level);
        if (maps == null) {
            System.err.println("Level Gen ERROR: Returned maps array is null");
            return;
        }

        tiles = maps[0]; // assigns the tiles in the map
        data = maps[1]; // assigns the data of the tiles

        if (level < 0)
            generateSpawnerStructures();

        if (level == 0)
            generateVillages();

        if (parentLevel != null) { // If the level above this one is not null (aka, if this isn't the sky level)
            for (int y = 0; y < h; y++) { // Loop through height
                for (int x = 0; x < w; x++) { // Loop through width
                    if (parentLevel.getTile(x, y) == Tiles.get("Stairs Down")) { // If the tile in the level above the
                        // current one is a stairs down then...
                        if (level == -4) /// Make the obsidian wall formation around the stair in the dungeon level
                            Structure.dungeonGate.draw(this, x, y);

                        else if (level == 0) { // Surface
                            if (Game.debug)
                                System.out.println("Setting tiles around " + x + "," + y + " to hard rock");
                            setAreaTiles(x, y, 1, Tiles.get("Hard Rock"), 0); // Surround the sky stairs with hard rock
                        } else // Any other level, the up-stairs should have dirt on all sides.
                            setAreaTiles(x, y, 1, Tiles.get("dirt"), 0);

                        setTile(x, y, Tiles.get("Stairs Up")); // Set a stairs up tile in the same position on the
                        // current level
                    }
                }
            }

        } else { // This is the sky level
            boolean placedSkyDungeon = false;
            while (!placedSkyDungeon) {
                int x = random.nextInt(this.w - 7);
                int y = random.nextInt(this.h - 5);

                // Generate the sky dungeon (always in Sky grass tile)
                if (this.getTile(x - 3, y - 2) == Tiles.get("Sky grass") &&
                    this.getTile(x + 3, y - 2) == Tiles.get("Sky grass")) {
                    if (this.getTile(x - 3, y + 2) == Tiles.get("Sky grass") &&
                        this.getTile(x + 3, y + 2) == Tiles.get("Sky grass")) {
                        Structure.skyDungeon.draw(this, x, y);
                        placedSkyDungeon = true;
                    }
                }
            }
        }

        checkChestCount(false);

        checkAirWizard();

        if (Game.debug)
            printTileLocs(Tiles.get("Stairs Down"));
    }

    public Level(int w, int h, int level, Level parentLevel, boolean makeWorld) {
        this(w, h, 0, level, parentLevel, makeWorld);
    }

    /** Level which the world is contained in */
    public Level(int w, int h, int level, Level parentLevel) {
        this(w, h, level, parentLevel, true);
    }

    public long getSeed() {
        return seed;
    }

    public void checkAirWizard() {
        checkAirWizard(true);
    }

    private void checkAirWizard(boolean check) {
        if (depth == 1 && !AirWizard.beaten) { // Add the airwizard to the surface

            boolean found = false;
            if (check) {
                for (Entity e: entitiesToAdd)
                    if (e instanceof AirWizard)
                        found = true;
                for (Entity e: entities)
                    if (e instanceof AirWizard)
                        found = true;
            }

            if (!found) {
                AirWizard aw = new AirWizard(false);
                add(aw, w / 2, h / 2, true);
            }
        }
    }

    public void checkChestCount() {
        checkChestCount(true);
    }

    private void checkChestCount(boolean check) {
        // If the level is the dungeon, and we're not just loading the world...
        if (depth != -4)
            return;

        int numChests = 0;

        if (check) {
            for (Entity e: entitiesToAdd)
                if (e instanceof DungeonChest)
                    numChests++;
            for (Entity e: entities)
                if (e instanceof DungeonChest)
                    numChests++;
            if (Game.debug)
                System.out.println("Found " + numChests + " chests.");
        }

        /// Make DungeonChests!
        for (int i = numChests; i < 10 * (w / 128); i++) {
            DungeonChest d = new DungeonChest(true);
            boolean addedchest = false;
            while (!addedchest) { // Keep running until we successfully add a DungeonChest

                // Pick a random tile:
                int x2 = random.nextInt(16 * w) / 16;
                int y2 = random.nextInt(16 * h) / 16;

                if (getTile(x2, y2) == Tiles.get("Obsidian")) {
                    boolean xaxis = random.nextBoolean();
                    if (xaxis) {
                        for (int s = x2; s < w - s; s++) {
                            if (getTile(s, y2) == Tiles.get("Obsidian Wall")) {
                                d.x = s * 16 - 24;
                                d.y = y2 * 16 - 24;
                            }
                        }
                    } else { // y axis
                        for (int s = y2; s < y2 - s; s++) {
                            if (getTile(x2, s) == Tiles.get("Obsidian Wall")) {
                                d.x = x2 * 16 - 24;
                                d.y = s * 16 - 24;
                            }
                        }
                    }
                    if (d.x == 0 && d.y == 0) {
                        d.x = x2 * 16 - 8;
                        d.y = y2 * 16 - 8;
                    }
                    if (getTile(d.x / 16, d.y / 16) == Tiles.get("Obsidian Wall")) {
                        setTile(d.x / 16, d.y / 16, Tiles.get("Obsidian"));
                    }
                    add(d);
                    chestCount++;
                    addedchest = true;
                }
            }
        }
    }

    private void tickEntity(Entity entity) {
        if (entity == null)
            return;

        if (Game.hasConnectedClients() && entity instanceof Player && !(entity instanceof RemotePlayer)) {
            if (Game.debug)
                System.out.println("SERVER is removing regular player " + entity + " from level " + this);
            entity.remove();
        }
        if (Game.isValidServer() && entity instanceof Particle) {
            // there is no need to track this.
            if (Game.debug)
                System.out.println(
                    "SERVER warning: Found particle in entity list: " + entity + ". Removing from level " + this);
            entity.remove();
        }

        if (entity.isRemoved()) {
            remove(entity);
            return;
        }

        if (entity != Game.player) { // player is ticked separately, others are ticked on server
            if (!Game.isValidClient())
                entity.tick(); /// the main entity tick call.
            else if (entity instanceof ClientTickable)
                ((ClientTickable) entity).clientTick();
        }

        if (entity.isRemoved() || entity.getLevel() != this) {
            remove(entity);
            return;
        }

        if (Game.hasConnectedClients()) // this means it's a server
            Game.server.broadcastEntityUpdate(entity);
    }

    public void tick(boolean fullTick) {
        int count = 0;
        Iterator < Entity > entityAdderIt = entitiesToAdd.iterator();
        while (entityAdderIt.hasNext()) {
            Entity entity = entityAdderIt.next();
            boolean inLevel = entities.contains(entity);

            if (!inLevel) {
                if (Game.isValidServer())
                    Game.server.broadcastEntityAddition(entity, true);

                if (!Game.isValidServer() || !(entity instanceof Particle)) {
                    // if (Game.debug) printEntityStatus("Adding ", entity,
                    // "furniture.DungeonChest", "mob.AirWizard", "mob.Player");

                    synchronized(entityLock) {
                        if (entity instanceof Spark) {
                            sparks.add((Spark) entity);
                        } else {
                            entities.add(entity);
                            if (entity instanceof Player) {
                                players.add((Player) entity);
                            }
                        }

                        if (entity instanceof Spark2) {
                            sparks2.add((Spark2) entity);
                        } else {
                            entities.add(entity);
                            if (entity instanceof Player) {
                                players.add((Player) entity);
                            }
                        }

                        if (entity instanceof Spark3) {
                            sparks3.add((Spark3) entity);
                        } else {
                            entities.add(entity);
                            if (entity instanceof Player) {
                                players.add((Player) entity);
                            }
                        }

                    }
                }
            }
            entityAdderIt.remove();
        }

        // this play random music in game
        if (Settings.get("ambient").equals("Nice")) {

            randomMusic++;

            if (randomMusic >= 16000) {
                randomMusic = 0;

                // Surface
                if (random.nextInt(3) == 0 && depth == 0) { // Surface only
                    Sound.Theme_Surface.play();

                } else if (random.nextInt(3) == 1 && depth == 0 || depth == -1) { // Surface and underground
                    Sound.Theme_Cave.play();

                } else if (random.nextInt(3) == 2 && depth == 0) { // Surface only
                    Sound.Theme_Peaceful.play();

                } else if (random.nextInt(3) == 3 && depth == 0) { // Surface only
                    Sound.Theme_Peaceful.play();

                }

                // Cave
                if (random.nextInt(5) == 0 && depth == -1) { // Cave
                    Sound.Ambience1.play();

                } else if (random.nextInt(5) == 1 && depth == -1) { // Cave
                    Sound.Ambience2.play();

                } else if (random.nextInt(5) == 2 && depth == -1 || depth == -2) { // Cave and cavern
                    Sound.Ambience3.play();

                } else if (random.nextInt(5) == 3 && depth == -1 || depth == -2) { // Cave and cavern
                    Sound.Ambience4.play();

                } else if (random.nextInt(5) == 4 && depth == -2) { // Cavern
                    Sound.Theme_Cavern.play();

                } else if (random.nextInt(5) == 5 && depth == -2) { // Cavern
                    Sound.Theme_Cavern_drip.play();

                }

                // Sky
                if (random.nextInt(1) == 0 && depth == 1) { // Sky
                    Sound.Theme_Surface.play();

                } else if (random.nextInt(1) == 1 && depth == 1) { // Sky
                    Sound.Theme_Fall.play();

                }

            }
        }

        if (Settings.get("ambient").equals("Normal")) {
            /*
             * if (random.nextInt(256000) == 1) { }
             */
        }

        if (Settings.get("ambient").equals("Scary")) {
            if (random.nextInt(128000) == 1) {

                if (random.nextInt(8) == 0) {
                    Sound.Ambience1.play();
                }
                if (random.nextInt(8) == 2) {
                    Sound.Ambience2.play();
                }
                if (random.nextInt(8) == 4) {
                    Sound.Ambience3.play();
                }
                if (random.nextInt(8) == 6) {
                    Sound.Ambience4.play();
                }
                if (random.nextInt(8) == 8) {
                    Sound.Ambience5.play();
                }
            }
        }

        if (fullTick && (!Game.isValidServer() || getPlayers().length > 0)) {
            // this prevents any entity (or tile) tick action from happening on a server
            // level with no players.

            if (!Game.isValidClient()) {
                for (int i = 0; i < w * h / 50; i++) {
                    int xt = random.nextInt(w);
                    int yt = random.nextInt(w);
                    boolean notableTick = getTile(xt, yt).tick(this, xt, yt);
                    if (Game.isValidServer() && notableTick)
                        Game.server.broadcastTileUpdate(this, xt, yt);
                }
            }

            // entity loop

            for (Entity e: entities) {
                tickEntity(e);
                if (e instanceof Mob)
                    count++;
            }

            sparks.forEach(this::tickEntity);
            sparks2.forEach(this::tickEntity);
            sparks3.forEach(this::tickEntity);
        }

        while (count > maxMobCount) {
            Entity removeThis = (Entity) entities.toArray()[(random.nextInt(entities.size()))];
            if (removeThis instanceof MobAi) {
                // make sure there aren't any close players
				boolean playerClose = entityNearPlayer(removeThis);

                if (!playerClose) {
                    remove(removeThis);
                    count--;
                }
            }
        }

        while (entitiesToRemove.size() > 0) {
            Entity entity = entitiesToRemove.get(0);

            if (Game.isValidServer() && !(entity instanceof Particle) && entity.getLevel() == this)
                Game.server.broadcastEntityRemoval(entity, this, true);

            // if(Game.debug) printEntityStatus("Removing ", entity, "mob.Player");

            entity.remove(this); // this will safely fail if the entity's level doesn't match this one.

            if (entity instanceof Spark) {
                sparks.remove(entity);
            } else {
                entities.remove(entity);
            }

            if (entity instanceof Spark2) {
                sparks2.remove(entity);
            } else {
                entities.remove(entity);
            }

            if (entity instanceof Spark3) {
                sparks3.remove(entity);
            } else {
                entities.remove(entity);
            }

            if (entity instanceof Player)
                players.remove(entity);
            entitiesToRemove.remove(entity);
        }

        mobCount = count;

        if (Game.isValidServer() && players.size() == 0)
            return; // don't try to spawn any mobs when there's no player on the level, on a server.

        if (fullTick && count < maxMobCount && !Game.isValidClient())
            trySpawn();

    }
    

	public boolean entityNearPlayer(Entity entity) {
		for (Player player : players) {
			if (Math.abs(player.x - entity.x) < 128 && Math.abs(player.y - entity.y) < 76) {
				return true;
			}
		}
		return false;
	}

    /*
     * public void printEntityStatus(String entityMessage, Entity entity, String...
     * searching) { // "searching" can contain any number of class names I want to
     * print when found. String clazz = entity.getClass().getCanonicalName(); clazz
     * = clazz.substring(clazz.lastIndexOf(".")+1); for(String search: searching) {
     * try {
     * if(Class.forName("minicraft.entity."+search).isAssignableFrom(entity.getClass
     * ())) { if (clazz.equals("AirWizard")) clazz += ((AirWizard)entity).secondform
     * ? " II" : ""; printLevelLoc(Network.onlinePrefix()+entityMessage + clazz,
     * entity.x>>4, entity.y>>4, ": " + entity); break; } }
     * catch(ClassNotFoundException ex) { ex.printStackTrace(); } } }
     */

    public void dropItem(int x, int y, int mincount, int maxcount, Item...items) {
        dropItem(x, y, mincount + random.nextInt(maxcount - mincount + 1), items);
    }

    public void dropItem(int x, int y, int count, Item...items) {
        for (int i = 0; i < count; i++)
            dropItem(x, y, items);
    }

    public void dropItem(int x, int y, Item...items) {
        for (Item i: items)
            dropItem(x, y, i);
    }

    public ItemEntity dropItem(int x, int y, Item i) {

        if (Game.isValidClient()) {
            System.err.println("dropping item on client: " + i);
        }

        int ranx;
        int rany;

        do {
            ranx = x + random.nextInt(11) - 5;
            rany = y + random.nextInt(11) - 5;
        } while (ranx >> 4 != x >> 4 || rany >> 4 != y >> 4);
        ItemEntity ie = new ItemEntity(i, ranx, rany);
        add(ie);
        return ie;
    }

    public void renderBackground(Screen screen, int xScroll, int yScroll) {
        int xo = xScroll >> 4; // latches to the nearest tile coordinate
        int yo = yScroll >> 4;
        int w = (Screen.w) >> 4; // there used to be a "+15" as in below method
        int h = (Screen.h) >> 4;
        screen.setOffset(xScroll, yScroll);
        for (int y = yo; y <= h + yo; y++) {
            for (int x = xo; x <= w + xo; x++) {
                getTile(x, y).render(screen, this, x, y);
            }
        }
        screen.setOffset(0, 0);
    }

    public void renderSprites(Screen screen, int xScroll, int yScroll) {
        int xo = xScroll >> 4; // latches to the nearest tile coordinate
        int yo = yScroll >> 4;
        int w = (Screen.w + 15) >> 4;
        int h = (Screen.h + 15) >> 4;

        screen.setOffset(xScroll, yScroll);
        sortAndRender(screen, getEntitiesInTiles(xo, yo, xo + w, yo + h));

        screen.setOffset(0, 0);
    }

    public void renderLight(Screen screen, int xScroll, int yScroll, int brightness) {
        int xo = xScroll >> 4;
        int yo = yScroll >> 4;
        int w = (Screen.w + 15) >> 4;
        int h = (Screen.h + 15) >> 4;

        screen.setOffset(xScroll, yScroll);
        int r = 4;

        List < Entity > entities = getEntitiesInTiles(xo - r, yo - r, w + xo + r, h + yo + r);
        for (Entity e: entities) {
            int lr = e.getLightRadius();
            if (lr > 0)
                screen.renderLight(e.x - 1, e.y - 4, lr * brightness);
        }

        for (int y = yo - r; y <= h + yo + r; y++) {
            for (int x = xo - r; x <= w + xo + r; x++) {
                if (x < 0 || y < 0 || x >= this.w || y >= this.h)
                    continue;

                int lr = getTile(x, y).getLightRadius(this, x, y);
                if (lr > 0)
                    screen.renderLight(x * 16 + 8, y * 16 + 8, lr * brightness);
            }
        }
        screen.setOffset(0, 0);
    }

    private void sortAndRender(Screen screen, List < Entity > list) {
        list.sort(spriteSorter);
        for (int i = 0; i < list.size(); i++) {
            Entity e = list.get(i);
            if (e.getLevel() == this && !e.isRemoved())
                e.render(screen);
            else
                remove(e);
        }
    }

    public Tile getTile(int x, int y) {
        // if (x < 0 || y < 0 || x >= w || y >= h/* || (x + y * w) >= tiles.length*/)
        // return Tiles.get("rock");
        if (x < 0 || y < 0 || x >= w || y >= h /* || (x + y * w) >= tiles.length */ )
            return Tiles.get("connector tile");
        int id = tiles[x + y * w];
        if (id < 0)
            id += 256;
        return Tiles.get(id);
    }

    public void setTile(int x, int y, String tilewithdata) {
        if (!tilewithdata.contains("_")) {
            setTile(x, y, Tiles.get(tilewithdata));
            return;
        }
        String name = tilewithdata.substring(0, tilewithdata.indexOf("_"));
        int data = Tiles.get(name).getData(tilewithdata.substring(name.length() + 1));
        setTile(x, y, Tiles.get(name), data);
    }

    public void setTile(int x, int y, Tile t) {
        setTile(x, y, t, t.getDefaultData());
    }

    public void setTile(int x, int y, Tile t, int dataVal) {
        if (x < 0 || y < 0 || x >= w || y >= h)
            return;
        // if (Game.debug) printLevelLoc("setting tile from " +
        // Tiles.get(tiles[x+y*w]).name + " to " + t.name, x, y);

        if (Game.isValidClient() && !Game.isValidServer()) {
            System.out.println("Client requested a tile update for the " + t.name + " tile at " + x + "," + y);
        } else {
            tiles[x + y * w] = t.id;
            data[x + y * w] = (byte) dataVal;
        }

        if (Game.isValidServer())
            Game.server.broadcastTileUpdate(this, x, y);
    }

    public int getData(int x, int y) {
        if (x < 0 || y < 0 || x >= w || y >= h)
            return 0;
        return data[x + y * w] & 0xff;
    }

    public void setData(int x, int y, int val) {
        if (x < 0 || y < 0 || x >= w || y >= h)
            return;
        data[x + y * w] = (byte) val;
    }

    public void add(Entity e) {
        if (e == null)
            return;
        add(e, e.x, e.y);
    }

    public void add(Entity entity, int x, int y) {
        add(entity, x, y, false);
    }

    public void add(Entity entity, int x, int y, boolean tileCoords) {
        if (entity == null)
            return;
        if (tileCoords) {
            x = x * 16 + 8;
            y = y * 16 + 8;
        }
        entity.setLevel(this, x, y);

        entitiesToRemove.remove(entity); // to make sure the most recent request is satisfied.
        if (!entitiesToAdd.contains(entity))
            entitiesToAdd.add(entity);
    }

    public void remove(Entity e) {
        entitiesToAdd.remove(e);
        if (!entitiesToRemove.contains(e))
            entitiesToRemove.add(e);
    }

    private void trySpawn() {
        int spawnSkipChance = (int)(MOB_SPAWN_FACTOR * Math.pow(mobCount, 2) / Math.pow(maxMobCount, 2));
        if (spawnSkipChance > 0 && random.nextInt(spawnSkipChance) != 0)
            return; // hopefully will make mobs spawn a lot slower.

        boolean spawned = false;
        for (int i = 0; i < 30 && !spawned; i++) {

            int minLevel = 1;
            int maxLevel = 1;

            if (depth < 0) {
                maxLevel = (-depth) + ((Math.random() > 0.75 && -depth != 4) ? 1 : 0);
            }
            if (depth > 0) {
                minLevel = maxLevel = 4;
            }

            int lvl = random.nextInt(maxLevel - minLevel + 1) + minLevel;
            int rnd = random.nextInt(100);
            int nx = random.nextInt(w) * 16 + 8;
            int ny = random.nextInt(h) * 16 + 8;

            // System.out.println("trySpawn on level " + depth + " of lvl " + lvl + " mob w/
            // rand " + rnd + " at tile " + nx + "," + ny);

            // spawns the enemy mobs; first part prevents enemy mob spawn on surface and the sky on
            // first day, more or less.
            if (!Settings.get("diff").equals("Peaceful")) {
                if ((Updater.getTime() == Updater.Time.Night || depth != 0 && depth != 1) && EnemyMob.checkStartPos(this, nx, ny)) { // if night or underground, with a valid tile, spawn an enemy mob.
                    if (depth != -4) { // normal mobs
                        if (rnd <= 40)
                            add((new Slime(lvl)), nx, ny);
                        else if (rnd <= 75)
                            add((new Zombie(lvl)), nx, ny);
                        else if (rnd >= 85)
                            add((new OldGolem(lvl)), nx, ny);
                        else if (rnd >= 85)
                            add((new Skeleton(lvl)), nx, ny);
                        else
                            add((new Creeper(lvl)), nx, ny);

                    } else { // special dungeon mobs
                        if (rnd <= 40)
                            add((new Snake(lvl)), nx, ny);
                        else if (rnd <= 75)
                            add((new Knight(lvl)), nx, ny);
                        else if (rnd >= 85)
                            add((new Snake(lvl)), nx, ny);
                        else
                            add((new Knight(lvl)), nx, ny);
                    }

                    spawned = true;

                }
            } else {
                spawned = false;
            }

            if (depth == 0 && PassiveMob.checkStartPos(this, nx, ny)) {
                // Spawns the friendly mobs.
                if (rnd <= (Updater.getTime() == Updater.Time.Night ? 22 : 33)) {
                    add((new Cow()), nx, ny);
                }
                if (rnd <= (Updater.getTime() == Updater.Time.Night ? 22 : 33)) {
                    add((new Chicken()), nx, ny);

                } else if (rnd >= 68) {
                    add((new Pig()), nx, ny);

                } else {
                    add((new Sheep()), nx, ny);
                }

                spawned = true;
            }

            if (depth == 0 && VillagerMob.checkStartPos(this, nx, ny)) {
                // Spawns the villagers.
                if (rnd <= (Updater.getTime() == Updater.Time.Night ? 22 : 33)) {
                    add((new Librarian()), nx, ny);

                } else if (rnd >= 68) {
                    add((new Librarian()), nx, ny);

                } else {
                    add((new Librarian()), nx, ny);
                }
                if (rnd <= 75) {
                    add((new Librarian()), nx, ny);
                }

                spawned = true;
            }

            if (depth == 0 && VillagerMob.checkStartPos(this, nx, ny)) {
                // Spawns the villagers.
                if (rnd <= (Updater.getTime() == Updater.Time.Night ? 22 : 33)) {
                    add((new Cleric()), nx, ny);
                }
                if (rnd <= (Updater.getTime() == Updater.Time.Night ? 22 : 33)) {
                    add((new Cleric()), nx, ny);

                } else if (rnd >= 34) {
                    add((new Cleric()), nx, ny);

                } else {
                    add((new Cleric()), nx, ny);
                }

                spawned = true;

            }

            if (depth == 0 && DefenderMob.checkStartPos(this, nx, ny)) {
                // Spawns the golems.
                if (rnd <= (Updater.getTime() == Updater.Time.Night ? 22 : 33)) {
                    add((new Golem()), nx, ny);
                }
                if (rnd <= (Updater.getTime() == Updater.Time.Night ? 22 : 33)) {
                    add((new Golem()), nx, ny);

                } else if (rnd >= 34) {
                    add((new Golem()), nx, ny);

                } else {
                    add((new Golem()), nx, ny);
                }

                spawned = true;

            }

            if (depth == 0 && FrostMob.checkStartPos(this, nx, ny)) {
                // Spawns the villagers.
                if (rnd <= (Updater.getTime() == Updater.Time.Night ? 22 : 33)) {
                    add((new GuiMan()), nx, ny);
                } else {
                    add((new GuiMan()), nx, ny);
                }
                if (rnd <= 75) {
                    add((new Goat()), nx, ny);
                }

                spawned = true;
            }


            // This generates mobs from the sky, if the Air Wizard is not defeated
            // they will spawn hostile mobs, if instead, it is defeated they will
            // spawn peaceful mobs
            if (depth == 1 && SkyMob.checkStartPos(this, nx, ny)) {
                if (rnd <= (Updater.getTime() == Updater.Time.Night ? 22 : 33) && AirWizardPhase3.beaten == true) { // Spawns passive sky mobs.
                    add((new Phyg()), nx, ny);
                    add((new Sheepuff()), nx, ny);

                } else { // Spawns hostile sky mobs.
                    if (rnd <= 40)
                        add((new Slime(lvl)), nx, ny);
                    else if (rnd <= 75)
                        add((new Zombie(lvl)), nx, ny);
                    else if (rnd >= 85)
                        add((new OldGolem(lvl)), nx, ny);
                    else if (rnd >= 85)
                        add((new Skeleton(lvl)), nx, ny);
                    else
                        add((new Creeper(lvl)), nx, ny);
                }

                spawned = true;
            }

        }
    }

    public void removeAllEnemies() {
        for (Entity e: getEntityArray()) {
            if (e instanceof EnemyMob)
                if (!(e instanceof AirWizard) || Game.isMode("creative")) // don't remove the airwizard bosses! Unless in creative, since you can spawn more.
                    e.remove();
        }
    }

    public void clearEntities() {
        if (!Game.ISONLINE)
            entities.clear();
        else
            for (Entity e: getEntityArray())
                e.remove();
    }

    public Entity[] getEntityArray() {
        Entity[] entityArray = new Entity[entities.size() + sparks.size() + sparks2.size() + sparks3.size()];
        int index = 0;

        for (Entity entity: entities) {
            entityArray[index++] = entity;
        }
        for (Spark spark: sparks) {
            entityArray[index++] = spark;
        }

        for (Spark2 spark2: sparks2) {
            entityArray[index++] = spark2;
        }

        for (Spark3 spark3: sparks3) {
            entityArray[index++] = spark3;
        }

        return entityArray;
    }

    public List < Entity > getEntitiesInTiles(int xt, int yt, int radius) {
        return getEntitiesInTiles(xt, yt, radius, false);
    }

    @SafeVarargs
    public final List < Entity > getEntitiesInTiles(int xt, int yt, int radius, boolean includeGiven, Class < ? extends Entity > ...entityClasses) {
        return getEntitiesInTiles(xt - radius, yt - radius, xt + radius, yt + radius, includeGiven, entityClasses);
    }

    /**
     * Get entities in a certain area on the level.
     * 
     * @param xt0 Left
     * @param yt0 Top
     * @param xt1 Right
     * @param yt1 Bottom
     */
    public List < Entity > getEntitiesInTiles(int xt0, int yt0, int xt1, int yt1) {
        return getEntitiesInTiles(xt0, yt0, xt1, yt1, false);
    }

    /**
     * Get entities in a certain area on the level, and filter them by class.
     * 
     * @param xt0           Left
     * @param yt0           Top
     * @param xt1           Right
     * @param yt1           Bottom
     * @param includeGiven  If we should accept entities that match the provided
     *                      entityClasses. If false, we ignore the provided
     *                      entityClasses.
     * @param entityClasses Entities to accept.
     * @return A list of entities in the area.
     */
    @SafeVarargs
    public final List < Entity > getEntitiesInTiles(int xt0, int yt0, int xt1, int yt1, boolean includeGiven,
        Class < ? extends Entity > ...entityClasses) {
        List < Entity > contained = new ArrayList < > ();
        for (Entity e: getEntityArray()) {
            int xt = e.x >> 4;
            int yt = e.y >> 4;
            if (xt >= xt0 && xt <= xt1 && yt >= yt0 && yt <= yt1) {
                boolean matches = false;

                // Look through all entity classes to see if they match the current entity we
                // are at.
                for (int i = 0; !matches && i < entityClasses.length; i++)
                    // If the current entity and an entity class match.
                    matches = entityClasses[i].isAssignableFrom(e.getClass());

                // Add if the current entity matches an entity class and includeGiven is true.
                // If includeGiven is false, add if it doesn't match.
                if (matches == includeGiven)
                    contained.add(e);
            }
        }

        return contained;
    }

    public List < Entity > getEntitiesInRect(Rectangle area) {
        List < Entity > result = new ArrayList < > ();
        for (Entity e: getEntityArray()) {
            if (e.isTouching(area))
                result.add(e);
        }
        return result;
    }

    public List < Entity > getEntitiesInRect(Predicate < Entity > filter, Rectangle area) {
        List < Entity > result = new LinkedList < > ();
        for (Entity entity: entities) {
            if (filter.test(entity) && entity.isTouching(area)) {
                result.add(entity);
            }
        }
        return result;
    }

    /// finds all entities that are an instance of the given entity.
    public Entity[] getEntitiesOfClass(Class < ? extends Entity > targetClass) {
        ArrayList < Entity > matches = new ArrayList < > ();
        for (Entity e: getEntityArray()) {
            if (targetClass.isAssignableFrom(e.getClass()))
                matches.add(e);
        }

        return matches.toArray(new Entity[0]);
    }

    public Player[] getPlayers() {
        return players.toArray(new Player[players.size()]);
    }

    public Player getClosestPlayer(int x, int y) {
        Player[] players = getPlayers();
        if (players.length == 0)
            return null;

        Player closest = players[0];
        int xd = closest.x - x;
        int yd = closest.y - y;
        for (int i = 1; i < players.length; i++) {
            int curxd = players[i].x - x;
            int curyd = players[i].y - y;
            if (xd * xd + yd * yd > curxd * curxd + curyd * curyd) {
                closest = players[i];
                xd = curxd;
                yd = curyd;
            }
        }

        return closest;
    }

    public Point[] getAreaTilePositions(int x, int y, int r) {
        return getAreaTilePositions(x, y, r, r);
    }

    public Point[] getAreaTilePositions(int x, int y, int rx, int ry) {
        ArrayList < Point > local = new ArrayList < > ();
        for (int yp = y - ry; yp <= y + ry; yp++)
            for (int xp = x - rx; xp <= x + rx; xp++)
                if (xp >= 0 && xp < w && yp >= 0 && yp < h)
                    local.add(new Point(xp, yp));
        return local.toArray(new Point[local.size()]);
    }

    public Tile[] getAreaTiles(int x, int y, int r) {
        return getAreaTiles(x, y, r, r);
    }

    public Tile[] getAreaTiles(int x, int y, int rx, int ry) {
        ArrayList < Tile > local = new ArrayList < > ();

        for (Point p: getAreaTilePositions(x, y, rx, ry))
            local.add(getTile(p.x, p.y));

        return local.toArray(new Tile[local.size()]);
    }

    public void setAreaTiles(int xt, int yt, int r, Tile tile, int data) {
        setAreaTiles(xt, yt, r, tile, data, false);
    }

    public void setAreaTiles(int xt, int yt, int r, Tile tile, int data, boolean overwriteStairs) {
        for (int y = yt - r; y <= yt + r; y++) {
            for (int x = xt - r; x <= xt + r; x++) {
                if (overwriteStairs || (!getTile(x, y).name.toLowerCase().contains("stairs")))
                    setTile(x, y, tile, data);
            }
        }
    }

    public void setAreaTiles(int xt, int yt, int r, Tile tile, int data, String[] blacklist) {
        for (int y = yt - r; y <= yt + r; y++) {
            for (int x = xt - r; x <= xt + r; x++) {
                if (!Arrays.asList(blacklist).contains(getTile(x, y).name.toLowerCase()))
                    setTile(x, y, tile, data);
            }
        }
    }

    @FunctionalInterface
    public interface TileCheck {
        boolean check(Tile t, int x, int y);
    }

    public List < Point > getMatchingTiles(Tile search) {
        return getMatchingTiles((t, x, y) -> t.equals(search));
    }

    public List < Point > getMatchingTiles(Tile...search) {
        return getMatchingTiles((t, x, y) -> {
            for (Tile poss: search)
                if (t.equals(poss))
                    return true;
            return false;
        });
    }

    public List < Point > getMatchingTiles(TileCheck condition) {
        List < Point > matches = new ArrayList < > ();
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                if (condition.check(getTile(x, y), x, y))
                    matches.add(new Point(x, y));

        return matches;
    }

    public boolean isLight(int x, int y) {
        for (Tile t: getAreaTiles(x, y, 3))
            if (t instanceof TorchTile)
                return true;

        return false;
    }

    @SuppressWarnings("unused")
    private boolean noStairs(int x, int y) {
        return getTile(x, y) != Tiles.get("Stairs Down");
    }

    private void generateSpawnerStructures() {

        if (Game.debug)
            System.out.println("Trying to generate a spawner dungeon...");

        for (int i = 0; i < 18 / -depth * (w / 128); i++) {

            /// for generating spawner dungeons
            MobAi m;
            int r = random.nextInt(5);
            if (r == 1) {
                m = new Skeleton(-depth);
            } else if (r == 2 || r == 0) {
                m = new Slime(-depth);
            } else {
                m = new Zombie(-depth);
            }

            Spawner sp = new Spawner(m);
            int x3 = random.nextInt(16 * w) / 16;
            int y3 = random.nextInt(16 * h) / 16;
            if (getTile(x3, y3) == Tiles.get("dirt")) {
                boolean xaxis2 = random.nextBoolean();

                if (xaxis2) {
                    for (int s2 = x3; s2 < w - s2; s2++) {
                        if (getTile(s2, y3) == Tiles.get("rock")) {
                            sp.x = s2 * 16 - 24;
                            sp.y = y3 * 16 - 24;
                        }
                    }
                } else {
                    for (int s2 = y3; s2 < y3 - s2; s2++) {
                        if (getTile(x3, s2) == Tiles.get("rock")) {
                            sp.x = x3 * 16 - 24;
                            sp.y = s2 * 16 - 24;
                        }
                    }
                }

                if (sp.x == 0 && sp.y == 0) {
                    sp.x = x3 * 16 - 8;
                    sp.y = y3 * 16 - 8;
                }

                if (getTile(sp.x / 16, sp.y / 16) == Tiles.get("rock")) {
                    setTile(sp.x / 16, sp.y / 16, Tiles.get("dirt"));
                }

                Structure.mobDungeonCenter.draw(this, sp.x / 16, sp.y / 16);

                if (getTile(sp.x / 16, sp.y / 16 - 4) == Tiles.get("dirt")) {
                    Structure.mobDungeonNorth.draw(this, sp.x / 16, sp.y / 16 - 5);
                }
                if (getTile(sp.x / 16, sp.y / 16 + 4) == Tiles.get("dirt")) {
                    Structure.mobDungeonSouth.draw(this, sp.x / 16, sp.y / 16 + 5);
                }
                if (getTile(sp.x / 16 + 4, sp.y / 16) == Tiles.get("dirt")) {
                    Structure.mobDungeonEast.draw(this, sp.x / 16 + 5, sp.y / 16);
                }
                if (getTile(sp.x / 16 - 4, sp.y / 16) == Tiles.get("dirt")) {
                    Structure.mobDungeonWest.draw(this, sp.x / 16 - 5, sp.y / 16);
                }

                add(sp);
                for (int rpt = 0; rpt < 2; rpt++) {
                    if (random.nextInt(2) != 0)
                        continue;
                    Chest c = new Chest();
                    int chance = -depth;

                    c.populateInvRandom("minidungeon", chance);

                    add(c, sp.x - 16, sp.y - 16);
                }
            }

        }
        if (Game.debug)
            System.out.println("Spawner dungeon generated!");
    }

    // --------------------------------------------------------------------------------------------------------------------------------------------------

    /*
     * Generates the villages on the level
     * 
     * Villages are somewhat limited in their generation...
     * 
     * In this list shows the probabilities of generating villages according to the
     * size of the world
     * 
     * 128 x 128 = 0 - 1 can be generated 256 x 256 = 2 - 3 can be generated 512 x
     * 512 = 3 - 4 can be generated 1024 x 1024 = 4 - 4 can be generated
     * 
     * Villages can only be generated on grass, they may be able to generate in the
     * middle of deserts .. but that is because the generation of the world is
     * somewhat uneven, so there may be grass in a desert, and the village can be
     * generated there
     * 
     * Previously the villages had no paths .. the first attempt was to make them in
     * the code .. but it was quite buggy :(, so I got bored and put them directly
     * in the structures file (structure.java)
     * 
     */

    private void generateVillages() {
        int lastVillageX = 8;
        int lastVillageY = 8;

        // Checks the spawn of villagers
        new Librarian();
        new Librarian();
        new Cleric();
        new Cleric();
        new Golem();

        if (Game.debug)
            System.out.println("Trying to generate a village...");

        // makes 2-8 villages based on world size
        for (int i = 0; i < w / 128 * 4; i++) {

            // tries 10 times for each one
            for (int t = 0; t < 10; t++) {

                int x = random.nextInt(w);
                int y = random.nextInt(h);

                // makes sure the village isn't to close to the previous village
                if (getTile(x, y) == Tiles.get("grass") &&
                    (Math.abs(x - lastVillageX) > 28 && Math.abs(y - lastVillageY) > 28)) {
                    lastVillageX = x;
                    lastVillageY = y;

                    // a number between 2 and 4
                    int numHouses = 1;

                    // loops for each house in the village
                    for (int hs = 0; hs < numHouses; hs++) {
                        boolean hasChest = random.nextBoolean();
                        boolean hasCrops = random.nextBoolean();

                        // basically just gets what offset this house should have from the center of the
                        // village
                        int xo = hs == 0 || hs == 3 ? -8 : 8;
                        int yo = hs < 2 ? -8 : 8;

                        xo += random.nextInt(8);
                        yo += random.nextInt(8);

                        if (hasCrops) {
                            Structure.villageCrops.draw(this, x + xo, y + yo);
                            new Librarian();
                        } else {
                            Structure.villageHouseNormal2.draw(this, x + xo, y + yo);
                            new Cleric();
                        }

                        // add a chest to some of the houses
                        if (hasChest) {
                            Chest c = new Chest();
                            c.populateInvRandom("villagehouse", 1);
                            add(c, (x + random.nextInt(1) + xo) << 1, (y + random.nextInt(1) + yo) << 1);
                        }
                    }

                    break;
                }
            }
        }

        if (Game.debug)
            System.out.println("Village generated!");
    }

    // --------------------------------------------------------------------------------------------------------------------------------------------------

    public String toString() {
        return "Level(depth=" + depth + ")";
    }
}