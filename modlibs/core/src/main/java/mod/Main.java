package mod;

import java.io.IOException;

import javax.imageio.ImageIO;

import minicraft.core.Game;
import minicraft.core.io.Sound;
import minicraft.entity.Direction;
import minicraft.entity.Entity;
import minicraft.entity.mob.Mob;
import minicraft.entity.mob.Player;
import minicraft.entity.particle.SmashParticle;
import minicraft.entity.particle.TextParticle;
import minicraft.gfx.Color;
import minicraft.gfx.Screen;
import minicraft.gfx.Sprite;
import minicraft.gfx.SpriteSheet;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.item.Recipe;
import minicraft.item.Recipes;
import minicraft.item.StackableItem;
import minicraft.item.TileItem;
import minicraft.item.ToolItem;
import minicraft.level.Level;
import minicraft.level.LevelGen;
import minicraft.level.tile.OreTile;
import minicraft.level.tile.Tile;
import minicraft.level.tile.Tiles;
import minicraft.level.tile.TreeTile;
import minicraft.level.tile.OreTile.OreType;
import minicraft.mod.compatibility.GraphicComp;

public class Main {
    public static void entry() {
        Items.add(new StackableItem("copper", null));
        Items.add(new StackableItem("copper ore", null));
        Recipes.furnaceRecipes.add(new Recipe("Copper_1", "Coal_1", "Copper Ore_4"));
        Tiles.add(43, new OreTile(new OreType("Copper", Items.get("copper ore"), null)));
        LevelGen.ModTileGen.TileGeneration copperGen = (map, data, layer, w, h, random) -> {
            int r = 2;
            for (int i = 0; i < w * h / 650; i++) {
                int x = random.nextInt(w);
                int y = random.nextInt(h);
                for (int j = 0; j < 35; j++) {
                    int xx = x + random.nextInt(4) - random.nextInt(4);
                    int yy = y + random.nextInt(4) - random.nextInt(4);
                    if (xx >= r && yy >= r && xx < w - r && yy < h - r) {
                        if (map[xx + yy * w] == 7) { // id of "Rock" == 7
                            map[xx + yy * w] = (short) 43;
                        }
                    }
                }
            }
        };
        new LevelGen.ModTileGen(-1, copperGen);
        new LevelGen.ModTileGen(-2, copperGen);
        new LevelGen.ModTileGen(-3, copperGen);
        Tiles.add(44, new TreeTile("High Tree") {
            public void render(Screen screen, Level level, int x, int y) {
                new Sprite(0, 30, 2, 2, 1).render(screen, x << 4, y << 4);
            }
        
            @Override
            public boolean hurt(Level level, int x, int y, Mob source, int dmg, Direction attackDir) {
                hurt(level, x, y, dmg);
                return true;
            }
        
            public void hurt(Level level, int x, int y, int dmg) {
                if (random.nextInt(250) == 0)
                    level.dropItem(x * 16 + 8, y * 16 + 8, Items.get("Apple"));
                
                int damage = level.getData(x, y) + dmg;
                int treeHealth = 30;
                if (Game.isMode("Creative")) dmg = damage = treeHealth;
                
                level.add(new SmashParticle(x*16, y*16));
                Sound.monsterHurt.play();
        
                level.add(new TextParticle("" + dmg, x * 16 + 8, y * 16 + 8, Color.RED));
                if (damage >= treeHealth) {
                    level.dropItem(x * 16 + 8, y * 16 + 8, 2, 5, Items.get("Wood"));
                    level.dropItem(x * 16 +  8, y * 16 + 8, 0, 2, Items.get("Acorn"));
                    level.setTile(x, y, Tiles.get("Grass"));
                } else {
                    level.setData(x, y, damage);
                }
            }
        });
        new LevelGen.ModTileGen(0, (map, data, layer, w, h, random) -> {
            for (int i = 0; i < w * h / 200; i++) {
                int x = random.nextInt(w);
                int y = random.nextInt(h);
                for (int j = 0; j < 200; j++) {
                    int xx = x + random.nextInt(15) - random.nextInt(15);
                    int yy = y + random.nextInt(15) - random.nextInt(15);
                    if (xx >= 0 && yy >= 0 && xx < w && yy < h) {
                        if (map[xx + yy * w] == 8) { // id of "Tree" == 8
                            map[xx + yy * w] = (short) 44;
                        }
                    }
                }
            }
        });
        Tiles.add(46, new Tile("redstone", (Sprite)null) {
            @Override
            public boolean mayPass(Level level, int x, int y, Entity e) {
                return true;
            }
            @Override
            public void render(Screen screen, Level level, int x, int y) {
                Tiles.get(1).render(screen, level, x, y);
                try {
                    GraphicComp.spriteFromSpriteSheet(0, 0, 2, 2, new SpriteSheet(ImageIO.read(Main.class.getResourceAsStream(level.getData(x, y) == 0? "/resources/tile/Redstone.png": "/resources/tile/RedstoneOn.png")))).render(screen, x*16, y*16);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public boolean interact(Level level, int xt, int yt, Player player, Item item, Direction attackDir) {
                level.dropItem(xt*16+8, yt*16+8, Items.get("redstone"));
                level.setTile(xt, yt, Tiles.get("Dirt"));
                return true;
            }
            @Override
            public boolean hurt(Level level, int x, int y, Mob source, int dmg, Direction attackDir) {
                level.dropItem(x*16+8, y*16+8, Items.get("redstone"));
                level.setTile(x, y, Tiles.get("Dirt"));
                return true;
            }
        });
        Items.add(new TileItem("redstone", null, "redstone", "grass", "dirt"));
        Tiles.add(45, new OreTile(new OreType("Redstone", Items.get("redstone"), null)) {
            @Override
            public void render(Screen screen, Level level, int x, int y) {
                try {
                    Tiles.get(1).render(screen, level, x, y);
                    GraphicComp.spriteFromSpriteSheet(0, 0, 2, 2, new SpriteSheet(ImageIO.read(Main.class.getResourceAsStream("/resources/tile/RedstoneOre.png")))).render(screen, x * 16, y * 16);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Tiles.add(47, new Tile("Redstone signal transmitter", (Sprite)null) {
            @Override
            public boolean mayPass(Level level, int x, int y, Entity e) {
                return true;
            }
            @Override
            public void render(Screen screen, Level level, int x, int y) {
                Tiles.get(1).render(screen, level, x, y);
                try {
                    GraphicComp.spriteFromSpriteSheet(0, 0, 2, 2, new SpriteSheet(ImageIO.read(Main.class.getResourceAsStream("/resources/tile/RedstoneTransmitter.png")))).render(screen, x*16, y*16, level.getData(x, y));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public boolean tick(Level level, int xt, int yt) {
                Direction dir = Direction.getDirection(level.getData(xt, yt));
                int bx = xt-dir.getX();
                int by = yt-dir.getY();
                int fx = xt+dir.getX();
                int fy = yt+dir.getY();
                if (level.getTile(bx, by).name.equals("REDSTONE") && level.getTile(xt+dir.getX(), yt+dir.getY()).name.equals("REDSTONE")) {
                    if (level.getData(bx, by) == 1) level.setData(fx, fy, 1);
                    else level.setData(fx, fy, 0);
                }
                return true;
            }
            @Override
            public boolean interact(Level level, int xt, int yt, Player player, Item item, Direction attackDir) {
                level.dropItem(xt*16+8, yt*16+8, Items.get("Redstone signal transmitter"));
                level.setTile(xt, yt, Tiles.get("Dirt"));
                return true;
            }
            @Override
            public boolean hurt(Level level, int x, int y, Mob source, int dmg, Direction attackDir) {
                level.dropItem(x*16+8, y*16+8, Items.get("Redstone signal transmitter"));
                level.setTile(x, y, Tiles.get("Dirt"));
                return true;
            }
        });
        Items.add(new TileItem("Redstone signal transmitter", null, "Redstone signal transmitter", "grass", "dirt") {
            @Override
            public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, Direction attackDir) {
                System.out.println("Placed.");
                return false;
                // boolean r = super.interactOn(tile, level, xt, yt, player, attackDir);
                // if (r && level.getTile(xt, yt).name.equals("REDSTONE SIGNAL TRANSMITTER")) level.setData(xt, yt, attackDir.getDir());
                // return r;
            }
        });
        Items.get("Redstone signal transmitter").interactOn(null, null, 0, 0, null, null);
        Recipes.workbenchRecipes.add(new Recipe("Redstone signal transmitter_1", "redstone_2", "stone_5"));
        new LevelGen.ModTileGen(-2, (map, data, layer, w, h, random) -> {
            int r = 2;
            for (int i = 0; i < w * h / 1000; i++) {
                int x = random.nextInt(w);
                int y = random.nextInt(h);
                for (int j = 0; j < 15; j++) {
                    int xx = x + random.nextInt(4) - random.nextInt(4);
                    int yy = y + random.nextInt(4) - random.nextInt(4);
                    if (xx >= r && yy >= r && xx < w - r && yy < h - r) {
                        if (map[xx + yy * w] == 7) { // id of "Rock" == 7
                            map[xx + yy * w] = (short) 45;
                        }
                    }
                }
            }
        });
        Tiles.add(48, new Tile("Redstone switch", (Sprite)null) {
            @Override
            public boolean mayPass(Level level, int x, int y, Entity e) {
                return true;
            }
            @Override
            public void render(Screen screen, Level level, int x, int y) {
                Tiles.get(1).render(screen, level, x, y);
                try {
                    GraphicComp.spriteFromSpriteSheet(0, 0, 2, 2, new SpriteSheet(ImageIO.read(Main.class.getResourceAsStream(level.getData(x, y) == 0? "/resources/tile/RedstoneSwitch.png": "/resources/tile/RedstoneSwitchOn.png")))).render(screen, x*16, y*16);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public boolean tick(Level level, int xt, int yt) {
                int switchOn = level.getData(xt, yt);
                if (level.getTile(xt+1, yt).name.equals("REDSTONE")) level.setData(xt+1, yt, switchOn);
                if (level.getTile(xt-1, yt).name.equals("REDSTONE")) level.setData(xt-1, yt, switchOn);
                if (level.getTile(xt, yt+1).name.equals("REDSTONE")) level.setData(xt, yt+1, switchOn);
                if (level.getTile(xt, yt-1).name.equals("REDSTONE")) level.setData(xt, yt-1, switchOn);
                return true;
            }
            @Override
            public boolean interact(Level level, int xt, int yt, Player player, Item item, Direction attackDir) {
                if (item instanceof ToolItem) {
                    level.dropItem(xt*16+8, yt*16+8, Items.get("Redstone switch"));
                    level.setTile(xt, yt, Tiles.get("Dirt"));
                }
                else level.setData(xt, yt, level.getData(xt, yt)^1);
                return true;
            }
            @Override
            public boolean hurt(Level level, int x, int y, Mob source, int dmg, Direction attackDir) {
                level.dropItem(x*16+8, y*16+8, Items.get("Redstone switch"));
                level.setTile(x, y, Tiles.get("Dirt"));
                return true;
            }
        });
        Items.add(new TileItem("Redstone switch", null, "Redstone switch", "grass", "dirt"));
        Recipes.craftRecipes.add(new Recipe("Redstone switch_1", "redstone_1", "stone_2", "wood_1"));
    }
}
