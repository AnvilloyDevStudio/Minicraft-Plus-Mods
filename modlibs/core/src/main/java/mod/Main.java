package mod;

import minicraft.core.Game;
import minicraft.core.io.Sound;
import minicraft.entity.Direction;
import minicraft.entity.mob.Mob;
import minicraft.entity.particle.SmashParticle;
import minicraft.entity.particle.TextParticle;
import minicraft.gfx.Color;
import minicraft.gfx.Screen;
import minicraft.gfx.Sprite;
import minicraft.item.Items;
import minicraft.item.Recipe;
import minicraft.item.Recipes;
import minicraft.item.StackableItem;
import minicraft.level.Level;
import minicraft.level.LevelGen;
import minicraft.level.tile.OreTile;
import minicraft.level.tile.Tiles;
import minicraft.level.tile.TreeTile;
import minicraft.level.tile.OreTile.OreType;

public class Main {
    public static void entry() {
        Items.add(new StackableItem("copper", null));
        Items.add(new StackableItem("copper ore", null));
        Recipes.furnaceRecipes.add(new Recipe("Copper_1", "Coal_1", "Copper Ore_4"));
        Tiles.add(43, new OreTile(new OreType("Copper", Items.get("copper ore"), new Sprite(0, 30, 2, 2, 1))));
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
                    level.dropItem(x * 16 + 8, y * 16 + 8, 1, 3, Items.get("Wood"));
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
    }
}
