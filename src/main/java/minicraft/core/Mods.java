package minicraft.core;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;

import minicraft.core.io.ClassLoader;
import minicraft.entity.furniture.*;
import minicraft.entity.mob.*;
import minicraft.gfx.MobSprite;
import minicraft.gfx.Screen;
import minicraft.gfx.Sprite;
import minicraft.gfx.SpriteSheet;
import minicraft.item.*;
import minicraft.level.Level;
import minicraft.level.tile.*;
import minicraft.level.tile.farming.ModPlant;
import minicraft.level.tile.farming.Plant;
import minicraft.mod.ModLoadAssets;

public class Mods extends Game {
    private Mods() {}
    public static ArrayList<Mods.Mod> Mods = new ArrayList<>();
    public static ArrayList<Mod.Item> Items = new ArrayList<>();
	public static ArrayList<Mod.Recipe> Recipes = new ArrayList<>();
    public static ArrayList<Mod.Entity> Entities = new ArrayList<>();    
    public static ArrayList<Mod.Tile> Tiles = new ArrayList<>();    
    public static ArrayList<Method> ModTileGens = new ArrayList<>();
    public static void init() {}

    static {
        File[] mods = FileHandler.readModsFolder();
        if (mods == null) {
            Mods = null;
        } else {
            ClassLoader loader = new ClassLoader();
            for (int a = 0; a<mods.length; a++) {
                Pair<Pair<Class<?>, Mod.Resources>, JSONObject> modP = loader.loadJar(mods[a]);
                Class<?> mod = modP.getLeft().getLeft();
                Mod modObj = new Mod(mod, modP.getLeft().getRight());
                modObj.Info = modP.getRight();
                if (modObj.Info.getString("name") == null) System.out.println("mod.json name not found.");
                if (modObj.Info.getString("description") == null) System.out.println("mod.json description not found.");
                Mods.add(modObj);

            }
            ModLoadAssets.init();
        }
    }
    public static class Mod {
        public JSONObject Info;
        public HashMap<String, Mod.Item> moditems = new HashMap<>();
        public HashMap<String, Mod.Recipe> modrecipes = new HashMap<>();
        public HashMap<String, Mod.Entity> modentities = new HashMap<>();
        public HashMap<String, Mod.Tile> modtiles = new HashMap<>();
        public HashMap<String, Class<?>> modtileentities = new HashMap<>();
        public static class Item {
            public String name;
            public String itemtype;
            public String[] itype;
            public int durability;
            public boolean noLevel;
            public int[] tooltypelvl;
            public boolean attack;
            public String ename;
            public int feed;
            public int cost;
            public boolean spriteSheet;
            public int[] sprite;
            private Resources resources;
            private Mod mod;
            public SpriteSheet findSpriteSheet() {
                if (spriteSheet) return resources.ItemsSheet;
                else {
                    SpriteSheet img = resources.ItemImages.get(name+".png");
                    if (img == null) {
                        sprite = new int[] {0, 31};
                        return resources.Sheets[0];
                    }
                    else return img;
                }
            }
            Item(Mod mod, Class<?> Obj, Resources res) {
                resources = res;
                this.mod = mod;
                try {
                    name = (String)Obj.getDeclaredField("name").get(null); // Item name
                    itemtype = (String)Obj.getDeclaredField("itemtype").get(null); // Item type
                    try{itype = (String[])Obj.getDeclaredField("type").get(null);} catch (NoSuchFieldException e) {itype = new String[0];}; // Item level
                    try{ename = (String)Obj.getDeclaredField("ename").get(null);} catch (NoSuchFieldException e) {ename = null;}; // Other name (Always Entity)
                    try{durability = (Integer)Obj.getDeclaredField("durability").get(null);} catch (NoSuchFieldException e) {durability = 1;};
                    try{feed = (Integer)Obj.getDeclaredField("feed").get(null);} catch (NoSuchFieldException e) {feed = 1;};
                    try{cost = (Integer)Obj.getDeclaredField("cost").get(null);} catch (NoSuchFieldException e) {cost = 1;};
                    try{noLevel = (boolean)Obj.getDeclaredField("noLevel").get(null);} catch (NoSuchFieldException e) {noLevel = true;};
                    try{tooltypelvl = (int[])Obj.getDeclaredField("level").get(null);} catch (NoSuchFieldException e) {tooltypelvl = new int[0];} // Item level level number
                    try{attack = (boolean)Obj.getDeclaredField("attack").get(null);} catch (NoSuchFieldException e) {attack = true;} // canAttack
                    spriteSheet = (boolean)Obj.getDeclaredField("spriteSheet").get(null); // false or ignore if sprite is separated from items.png
                    if (itype.length!=tooltypelvl.length) new Crash(new Crash.CrashData("ModLoad", "", "Lengths of Item type and level are difference", Obj.getName()));
                    try{sprite = (int[])Obj.getDeclaredField("sprite").get(null);} catch (NoSuchFieldException e) {sprite = new int[] {0, 0};} // xPos, yPos
                } catch (IllegalArgumentException | NullPointerException
                        | SecurityException | NoSuchFieldException | IllegalAccessException e) {
					if (e instanceof NoSuchFieldException) new Crash(new Crash.CrashData("ModLoad", Crash.getStackTrace(e), "Missing Item Field", Obj.getName()));
					else if (e instanceof IllegalArgumentException) new Crash(new Crash.CrashData("ModLoad", Crash.getStackTrace(e), "Illegal Item Field", Obj.getName()));
					else if (e instanceof IllegalAccessException || e instanceof NullPointerException) new Crash(new Crash.CrashData("ModLoad", Crash.getStackTrace(e), "Invalid Item Field", Obj.getName()));
					else e.printStackTrace();
				}
            }
            public ToolItem toToolItem(boolean instance, int index) {
                ToolType toolType = ToolType.Types.get(name.toLowerCase());
                if (toolType==null) toolType = new ToolType(name, resources.getSprite(findSpriteSheet(), sprite[0], sprite[1]), durability, attack, noLevel);
                if (!noLevel) {
                    ItemLevel itemLevel = ItemLevel.Levels.containsKey(itype[index])? ItemLevel.Levels.get(itype[index]): new ItemLevel(itype[index], tooltypelvl[index]);
                    return new ToolItem(toolType, itemLevel);
                }
                else return new ToolItem(toolType);
            }
            public StackableItem toStackableItem() {
                return new StackableItem(name, resources.getSprite(findSpriteSheet(), sprite[0], sprite[1]));
            }
            public FurnitureItem toFurnitureItem() {
                return new FurnitureItem(mod.modentities.get(ename).toFurniture());
            }
            public FoodItem toFoodItem() {
                return new FoodItem(name, resources.getSprite(findSpriteSheet(), sprite[0], sprite[1]), 1, feed, cost);
            }
            public BucketItem toBucketItem() {
                return new BucketItem(new BucketItem.Fill(name, mod.modtiles.get(ename).toTile(), resources.getSprite(findSpriteSheet(), sprite[0], sprite[1])));
            }
            public ArmorItem toArmorItem(int index) {
                return new ArmorItem(name, resources.getSprite(findSpriteSheet(), sprite[0], sprite[1]), durability, tooltypelvl[index]);
            }
        }
		public static class Recipe {
			public String creation;
			public String[] req;
			public String type;
            private Mod mod;
			public Recipe(Mod mod, Class<?> Obj) {
                this.mod = mod;
                try {
					creation = (String)Obj.getDeclaredField("creation").get(null);
					req = (String[])Obj.getDeclaredField("require").get(null); // Array of String
					try{type = (String)Obj.getDeclaredField("type").get(null);} catch (NoSuchFieldException e) {type = "workbench";} // Type of Recipe
                } catch (IllegalArgumentException | NullPointerException
                        | SecurityException | NoSuchFieldException | IllegalAccessException e) {
					if (e instanceof NoSuchFieldException) new Crash(new Crash.CrashData("ModLoad", Crash.getStackTrace(e), "Missing Recipe Field", Obj.getName()));
					else if (e instanceof IllegalArgumentException) new Crash(new Crash.CrashData("ModLoad", Crash.getStackTrace(e), "Illegal Recipe Field", Obj.getName()));
					else if (e instanceof IllegalAccessException || e instanceof NullPointerException) new Crash(new Crash.CrashData("ModLoad", Crash.getStackTrace(e), "Invalid Recipe Field", Obj.getName()));
					else e.printStackTrace();
				}
			}
			public minicraft.item.Recipe toRecipe() {
				return new minicraft.item.Recipe(creation, req);
			}
		}
        public static class Entity {
            public String name;
            public String entitytype;
            public String type;
            public int xr;
            public int yr;
            public String title;
            public int light;
            public int health;
            public boolean isFactor;
            public int detectDist;
            public int lifetime;
            public int rwTime;
            public int rwChance;
            public boolean spriteSheet;
            public int[] sprite;
            private Resources resources;
            private Mod mod;
            public SpriteSheet findSpriteSheet() {
                if (spriteSheet) return resources.EntitiesSheet;
                else {
                    SpriteSheet img = resources.EntityImages.get(name+".png");
                    if (img == null) {
                        sprite = new int[] {30, 30};
                        return resources.Sheets[2];
                    }
                    else return img;
                }
            }
            public Entity(Mod mod, Class<?> Obj, Resources res) {
                resources = res;
                this.mod = mod;
                try {
                    name = (String)Obj.getDeclaredField("name").get(null); // Entity name
                    entitytype = (String)Obj.getDeclaredField("entitytype").get(null); // Entity type
                    try{type = (String)Obj.getDeclaredField("type").get(null);} catch (NoSuchFieldException e) {type = null;}; // Entity type for more
                    try{title = (String)Obj.getDeclaredField("title").get(null);} catch (NoSuchFieldException e) {title = null;}; // Entity type for more (entity title)
                    try{xr = (Integer)Obj.getDeclaredField("xr").get(null);} catch (NoSuchFieldException e) {xr = 3;} // Entity x Radius
                    try{yr = (Integer)Obj.getDeclaredField("yr").get(null);} catch (NoSuchFieldException e) {yr = 3;} // Entity y Radius
                    try{light = (Integer)Obj.getDeclaredField("light").get(null);} catch (NoSuchFieldException e) {light = 0;} // Lantern light
                    try{health = (Integer)Obj.getDeclaredField("health").get(null);} catch (NoSuchFieldException e) {health = 3;} // Entity health
                    try{detectDist = (Integer)Obj.getDeclaredField("detectDist").get(null);} catch (NoSuchFieldException e) {detectDist = 10;} // Enemy detect distance
                    try{lifetime = (Integer)Obj.getDeclaredField("lifetime").get(null);} catch (NoSuchFieldException e) {lifetime = 60 * Updater.normSpeed;} // Entity lifetime
                    try{rwTime = (Integer)Obj.getDeclaredField("rwTime").get(null);} catch (NoSuchFieldException e) {rwTime = 45;} // Entity random walk time
                    try{rwChance = (Integer)Obj.getDeclaredField("rwChance").get(null);} catch (NoSuchFieldException e) {rwChance = 40;} // Entity random walk chance
                    try{isFactor = (boolean)Obj.getDeclaredField("health").get(null);} catch (NoSuchFieldException e) {isFactor = true;} // Entity isFactor
                    spriteSheet = (boolean)Obj.getDeclaredField("spriteSheet").get(null); // false or ignore if sprite is separated from entities.png
                    try{sprite = (int[])Obj.getDeclaredField("sprite").get(null);} catch (NoSuchFieldException e) {sprite = new int[] {0, 0};} // xPos, yPos
                } catch (IllegalArgumentException | NullPointerException
                        | SecurityException | NoSuchFieldException | IllegalAccessException e) {
					if (e instanceof NoSuchFieldException) new Crash(new Crash.CrashData("ModLoad", Crash.getStackTrace(e), "Missing Entity Field", Obj.getName()));
					else if (e instanceof IllegalArgumentException) new Crash(new Crash.CrashData("ModLoad", Crash.getStackTrace(e), "Illegal Entity Field", Obj.getName()));
					else if (e instanceof IllegalAccessException || e instanceof NullPointerException) new Crash(new Crash.CrashData("ModLoad", Crash.getStackTrace(e), "Invalid Entity Field", Obj.getName()));
					else e.printStackTrace();
				}
            }
            public MobSprite[] compileSpriteList(int sheetX, int sheetY, int width, int height, int mirror, int number) {
                MobSprite[] sprites = new MobSprite[number];
                for (int i = 0; i < sprites.length; i++)
                    sprites[i] = new MobSprite(sheetX + width * i, sheetY, width, height, mirror, findSpriteSheet());
                
                return sprites;
            }
        	public MobSprite[][] compileMobSpriteAnimations(int sheetX, int sheetY) {
                MobSprite[][] sprites = new MobSprite[4][2];
                // dir numbers: 0=down, 1=up, 2=left, 3=right.
                /// On the spritesheet, most mobs have 4 sprites there, first facing down, then up, then right 1, then right 2. The first two get flipped to animate them, but the last two get flipped to change direction.
                
                // Contents: down 1, up 1, right 1, right 2
                MobSprite[] set1 = MobSprite.compileSpriteList(sheetX, sheetY, 2, 2, 0, 4);
                
                // Contents: down 2, up 2, left 1, left 2
                MobSprite[] set2 = MobSprite.compileSpriteList(sheetX, sheetY, 2, 2, 1, 4);
                
                // Down
                sprites[0][0] = set1[0];
                sprites[0][1] = set2[0];
                
                // Up
                sprites[1][0] = set1[1];
                sprites[1][1] = set2[1];
                
                // Left
                sprites[2][0] = set2[2];
                sprites[2][1] = set2[3];
                
                // Right
                sprites[3][0] = set1[2];
                sprites[3][1] = set1[3];
                
                return sprites;
            }        
            public Furniture toFurniture() {
                switch (type) {
                    case "Crafter":
                        return new Crafter(new Crafter.Type(name, resources.getSprite2(findSpriteSheet(), sprite[0], sprite[1]), xr, yr, minicraft.item.Recipes.modRecipes.get(name)));
                    case "Lantern":
                        return new Lantern(new Lantern.Type(name, title, light, resources.getSprite2(findSpriteSheet(), sprite[0], sprite[1])));
                    case "Spawner":
                        return new Spawner(mod.modentities.get(title).toMobAi(0));
                    default:
                        return new Furniture(name, resources.getSprite2(findSpriteSheet(), sprite[0], sprite[1]), xr, yr);
                }
            }
            public MobAi toMobAi(int lvl) {
                switch (entitytype) {
                    case "Enemy":
                        return toEnemyMob(lvl);
                    default: // Passive
                        return toPassiveMob();
                }
            }
            public EnemyMob toEnemyMob(int lvl) {
                MobSprite[][][] sprites;
                if (type == "basic") {
                    sprites = new MobSprite[4][4][2];
                    for (int i = 0; i < 4; i++) {
                        MobSprite[][] list = compileMobSpriteAnimations(sprite[0], sprite[1] + (i * 2));
                        sprites[i] = list;
                    }
                } else {
                    sprites = new MobSprite[4][1][2];
                    for (int i = 0; i < 4; i++) {
                        MobSprite[] list = compileSpriteList(sprite[0], sprite[1] + (i * 2), 2, 2, 0, 2);
                        sprites[i][0] = list;
                    }
                }
                return new EnemyMob(lvl, sprites, health, isFactor, detectDist, lifetime, rwTime, rwChance);
            }
            public PassiveMob toPassiveMob() {
                return new PassiveMob(compileMobSpriteAnimations(sprite[0], sprite[1]), health);
            }
        }
        public static class Tile {
            public String name;
            public String tiletype;
            public String drop;
            public String seed;
            public int id;
            public boolean spriteSheet;
            public int[] sprite;
            private Resources resources;
            private Mod mod;
            public SpriteSheet findSpriteSheet() {
                if (spriteSheet) return resources.TilesSheet;
                else {
                    SpriteSheet img = resources.TileImages.get(name+".png");
                    if (img == null) {
                        sprite = new int[] {0, 30};
                        return resources.Sheets[1];
                    }
                    else return img;
                }
            }
            Tile(Mod mod, Class<?> Obj, Resources res) {
                resources = res;
                this.mod = mod;
                try {
                    name = (String)Obj.getDeclaredField("name").get(null); // Tile name
                    tiletype = (String)Obj.getDeclaredField("tiletype").get(null);
                    try{drop = (String)Obj.getDeclaredField("drop").get(null);} catch (NoSuchFieldException e) {drop = null;} // xPos, yPos
                    try{seed = (String)Obj.getDeclaredField("seed").get(null);} catch (NoSuchFieldException e) {seed = null;} // xPos, yPos
                    try{id = (Integer)Obj.getDeclaredField("id").get(null);} catch (NoSuchFieldException e) {id = -1;} // xPos, yPos
                    spriteSheet = (boolean)Obj.getDeclaredField("spriteSheet").get(null); // false or ignore if sprite is separated from tiles.png
                    try{sprite = (int[])Obj.getDeclaredField("sprite").get(null);} catch (NoSuchFieldException e) {sprite = new int[] {0, 0};} // xPos, yPos
                    try{ModTileGens.add(Obj.getDeclaredMethod("tilegen", short[].class,java.util.Random.class,int.class,int.class,int.class));} catch (NoSuchMethodException e) {}
                    if (id == -1) new Crash(new Crash.CrashData("ModLoad", "", "Invalid Tile ID: -1", Obj.getName()));
                } catch (IllegalArgumentException | NullPointerException
                        | SecurityException | NoSuchFieldException | IllegalAccessException e) {
					if (e instanceof NoSuchFieldException) new Crash(new Crash.CrashData("ModLoad", Crash.getStackTrace(e), "Missing Tile Field", Obj.getName()));
					else if (e instanceof IllegalArgumentException) new Crash(new Crash.CrashData("ModLoad", Crash.getStackTrace(e), "Illegal Tile Field", Obj.getName()));
					else if (e instanceof IllegalAccessException || e instanceof NullPointerException) new Crash(new Crash.CrashData("ModLoad", Crash.getStackTrace(e), "Invalid Tile Field", Obj.getName()));
					else e.printStackTrace();
				}
            }
            public minicraft.level.tile.Tile toTile() {
                return new ModTile(name, resources.getSprite2(findSpriteSheet(), sprite[0], sprite[1]));
            }
            public OreTile toOreTile() {
                return new OreTile(new OreTile.OreType(name, mod.moditems.get(drop).toStackableItem(), resources.getSprite2(findSpriteSheet(), sprite[0], sprite[1])));
            }
            public Plant toPlant() {
                return new ModPlant(name, seed, drop, sprite[0], sprite[1], findSpriteSheet());
            }
        }
        public Resources Resources;
        public static class Resources {
            private SpriteSheet BytestoSpriteSheet(byte[] bytes) throws IOException {return new SpriteSheet(ImageIO.read(new ByteArrayInputStream(bytes)));}
            public SpriteSheet ItemsSheet;
            public HashMap<String, SpriteSheet> ItemImages = new HashMap<>();
            public SpriteSheet EntitiesSheet;
            public HashMap<String, SpriteSheet> EntityImages = new HashMap<>();
            public SpriteSheet TilesSheet;
            public HashMap<String, SpriteSheet> TileImages = new HashMap<>();
            public Manifest manifest;
            public SpriteSheet[] Sheets = new SpriteSheet[] {
                new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("/resources/textures/items.png"))),
                new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("/resources/textures/tiles.png"))),
                new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("/resources/textures/entities.png"))),
                new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("/resources/textures/gui.png"))),
                new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("/resources/textures/skins.png")))
            };
            public Sprite getSprite(SpriteSheet sheet, int x, int y) {
                return new Sprite(new Sprite.Px[][]{new Sprite.Px[]{new Sprite.Px(x, y, 0, sheet)}});
            }
            public Sprite getSprite2(SpriteSheet sheet, int x, int y) {
                return new Sprite(new Sprite.Px[][]{
                    new Sprite.Px[]{new Sprite.Px(x, y, 0, sheet), new Sprite.Px(x+1, y, 0, sheet)},
                    new Sprite.Px[]{new Sprite.Px(x, y+1, 0, sheet), new Sprite.Px(x+1, y+1, 0, sheet)}
                });
            }
            public Resources(JarFile jar, Manifest man) throws IOException {
                manifest = man;
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (entry.getName().startsWith("resources/")) {
                        if (entry.getName().equals("resources/items.png")) ItemsSheet = BytestoSpriteSheet(jar.getInputStream(entry).readAllBytes());
                        if (!entry.isDirectory()&&entry.getName().startsWith("resources/item/")&&entry.getName().endsWith(".png")) {
                            ItemImages.put(entry.getName().substring(15), BytestoSpriteSheet(jar.getInputStream(entry).readAllBytes()));
                        }
                        if (entry.getName().equals("resources/entities.png")) EntitiesSheet = BytestoSpriteSheet(jar.getInputStream(entry).readAllBytes());
                        if (!entry.isDirectory()&&entry.getName().startsWith("resources/entity/")&&entry.getName().endsWith(".png")) {
                            EntityImages.put(entry.getName().substring(15), BytestoSpriteSheet(jar.getInputStream(entry).readAllBytes()));
                        }
                        if (entry.getName().equals("resources/tiles.png")) TilesSheet = BytestoSpriteSheet(jar.getInputStream(entry).readAllBytes());
                        if (!entry.isDirectory()&&entry.getName().startsWith("resources/tile/")&&entry.getName().endsWith(".png")) {
                            TileImages.put(entry.getName().substring(15), BytestoSpriteSheet(jar.getInputStream(entry).readAllBytes()));
                        }
                    }
                }
            }
        }
        public Mod(Class<?> m, Resources res) {
            Resources = res;
            for (Class<?> class1: m.getDeclaredClasses()) {
                if (class1.getSimpleName().equals("Items")) {
                    for (Class<?> class2 : class1.getDeclaredClasses()) {
                        Mod.Item item = new Mod.Item(this, class2, Resources);
                        Items.add(item);
                        moditems.put(class2.getSimpleName(), item);
                    }
                } else if (class1.getSimpleName().equals("Recipes")) {
                    for (Class<?> class2 : class1.getDeclaredClasses()) {
                        Mod.Recipe recipe = new Mod.Recipe(this, class2);
                        Recipes.add(recipe);
                        modrecipes.put(class2.getSimpleName(), recipe);
                    }
                } else if (class1.getSimpleName().equals("Entities")) {
                    for (Class<?> class2 : class1.getDeclaredClasses()) {
                        Mod.Entity entity = new Mod.Entity(this, class2, Resources);
                        Entities.add(entity);
                        modentities.put(class2.getSimpleName(), entity);
                    }
                } else if (class1.getSimpleName().equals("Tiles")) {
                    for (Class<?> class2 : class1.getDeclaredClasses()) {
                        Mod.Tile tile = new Mod.Tile(this, class2, Resources);
                        Tiles.add(tile);
                        modtiles.put(class2.getSimpleName(), tile);
                    }
                } else if (class1.getSimpleName().equals("TileEntities")) {
                    for (Class<?> class2 : class1.getDeclaredClasses()) {
                        modtileentities.put(class2.getSimpleName(), class2);
                    }
                }
            }
        }
    }
}
