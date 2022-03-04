package mod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import minicraftmodsapiinterface.*;

public class Module {
    private Module() {}
    public static class Items {
        public static class Wand {
            public static String itemtype = "Tool";
            public static String name = "Wand";
            public static String[] type = { "Wood", "Rock", "Iron", "Gold", "Gem", "Dark", "Mega", "Red", "Green", "Blue", "Spell" };
            public static int durability = 20;
            public static boolean noLevel = false;
            public static boolean attack = true;
            public static boolean spriteSheet = false;
        }
        // public static class CopperOre {
        //     public static String itemtype = "Stackable";
        //     public static String name = "copper ore";
        //     public static boolean spriteSheet = false;
        // }
        // public static class Copper {
        //     public static String itemtype = "Stackable";
        //     public static String name = "copper";
        //     public static boolean spriteSheet = false;
        // }
    }
    public static class ItemLevels {
        public static class Dark {
            public static String name = "Dark";
            public static int level = 6;
        }
        public static class Mega {
            public static String name = "Mega";
            public static int level = 7;
        }
        public static class Red {
            public static String name = "Red";
            public static int level = 8;
        }
        public static class Green {
            public static String name = "Green";
            public static int level = 9;
        }
        public static class Blue {
            public static String name = "Blue";
            public static int level = 9;
        }
        public static class Spell {
            public static String name = "Spell";
            public static int level = 9;
        }
    }
    // public static class Recipes {
    //     // public static class Copper {
    //     //     public static String creation = "Copper_1";
    //     //     public static String[] require = new String[] {"Coal_1", "Copper Ore_4"};
    //     //     public static String type = "furnace";
    //     // }
    // }
    public static class Tiles {
        public static class BeanTile {
            public static String name = "Bean";
            public static boolean spriteSheet = false;
            public static String tiletype = "Plant";
            public static int id = 44;
            private static Random random = new Random();
            public static class Options {
                public static Map<String, Boolean> Connections = Map.of("grass", true);
                // public static void render(IScreen screen, ILevel level, int x, int y, ISprite sprite, Class<?> extra) {
                //     try {
                //         ITile grass =(ITile) Class.class.cast(extra.getDeclaredField("tiles").get(null)).getDeclaredMethod("get", String.class).invoke(null, "Grass");
                //         grass.render(screen, level, x, y);
                //         sprite.render(screen, x*16, y*16);
                //     } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {e.printStackTrace();}
                // }
                public static boolean tick(ILevel level, int xt, int yt, Class<?> extra) {
                    Method itemget = null;
                    Method tileget = null;
                    try {
                        itemget = Class.class.cast(extra.getDeclaredField("items").get(null)).getDeclaredMethod("get", String.class);
                        tileget = Class.class.cast(extra.getDeclaredField("tiles").get(null)).getDeclaredMethod("get", String.class);
                    } catch (NoSuchMethodException | SecurityException | IllegalArgumentException
                            | IllegalAccessException | NoSuchFieldException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    if (random.nextInt(100) == 0) {
                        try {
                            level.dropItem(xt * 16 + random.nextInt(6) + 5, yt * 16 + random.nextInt(6) + 5, (IItem)itemget.invoke(null, "Bean"));
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    int damage = level.getData(xt, yt);
                    if (damage > 0) {
                        level.setData(xt, yt, damage - 1);
                    }
                    // if (level.getFire(xt, yt) > 0) {
                    //     level.moreFire(xt, yt);
                    //     if (level.getFire(xt, yt) > 20) {
                    //         level.setTile(xt, yt, Tile.fire, 0);
                    //         level.setFire(xt, yt, 1);
                    //     }
                    // }
                    if (random.nextInt(40) != 0) {
                        return true;
                    }
                    int xn = xt;
                    int yn = yt;
                    if (random.nextBoolean()) {
                        xn += random.nextInt(2) * 2 - 1;
                    }
                    else {
                        yn += random.nextInt(2) * 2 - 1;
                    }
                    if (level.getTile(xn, yn).getName(0).equals("dirt")) {
                        try {
                            level.setTile(xn, yn, (ITile)tileget.invoke(null, "Bean"), 0);
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        // level.setFire(xn, yn, 0);
                    }
                    else {
                        if (random.nextInt(40) != 0) {
                            return true;
                        }
                        try {
                            if (level.getTile(xn, yn).getName(0).equals("grass")) {
                                level.setTile(xn, yn, (ITile)tileget.invoke(null, "Bean"), 0);
                                // level.setFire(xn, yn, 0);
                            }
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    if (damage > 0) {
                        level.setData(xt, yt, damage - 1);
                        return true;
                    }
                    return false;
                }
                public void hurt(ILevel level, int x, int y, IMob source, int dmg, IDirection attackDir, Class<?> extra) {
                    hurt(level, x, y, dmg, extra);
                }
                
                public boolean interact(ILevel level, int xt, int yt, IPlayer player, IItem item, IDirection attackDir) {
                    if (item.getClass().getName().contains("Tool")) {
                        // if (item.type == ToolType.wand) {
                        //     this.isWand = 1;
                        // }
                        // if (tool.type == ToolType.staff) {
                        //     this.isStaff = 1;
                        // }
                        // if (tool.type == ToolType.axe) {
                        //     if (player.payStamina(4 - tool.level)) {
                        //         this.hurt(level, xt, yt, this.random.nextInt(10) + tool.level * 5 + 20);
                        //         return true;
                        //     }
                        //     return true;
                        // }
                    }
                    return false;
                }
                private void hurt(ILevel level, int x, int y, int dmg, Class<?> extra) {
                    // if (this.isWand == 0) {
                    //     final int damage = level.getData(x, y) + dmg;
                    //     level.add(new SmashParticle(x * 16 + 8, y * 16 + 8));
                    //     level.add(new TextParticle(new StringBuilder().append(dmg).toString(), x * 16 + 8, y * 16 + 8, Color.get(-1, 500, 500, 500)));
                    //     if (damage >= 5) {
                    //         for (int count = this.random.nextInt(2) + 1, i = 0; i < count; ++i) {
                    //             level.add(new ItemEntity(new ResourceItem(Resource.bean), x * 16 + this.random.nextInt(10) + 3, y * 16 + this.random.nextInt(10) + 3));
                    //         }
                    //         level.setTile(x, y, Tile.dirt, 0);
                    //         level.setFire(x, y, 0);
                    //     }
                    //     else {
                    //         level.setData(x, y, damage);
                    //     }
                    // }
                    // else {
                    //     level.setTile(x, y, Tile.pea, 0);
                    //     level.setFire(x, y, 0);
                    //     this.isWand = 0;
                    // }
                    // if (this.isStaff == 1) {
                    //     level.setTile(x, y, Tile.corn, 0);
                    //     level.setFire(x, y, 0);
                    //     this.isStaff = 0;
                    // }
                }            
            }
        }
        // public static class CopperOre {
        //     public static String name = "Copper";
        //     public static String tiletype = "Ore";
        //     public static String drop = "CopperOre";
        //     public static boolean spriteSheet = false;
        //     public static int id = 43;
        //     public static void tilegen(short[] map, Random random, int depth, int w, int h) {
        //         int r = 2;
        //         for (int i = 0; i < w * h / 650; i++) {
        //             int x = random.nextInt(w);
        //             int y = random.nextInt(h);
        //             for (int j = 0; j < 35; j++) {
        //                 int xx = x + random.nextInt(4) - random.nextInt(4);
        //                 int yy = y + random.nextInt(4) - random.nextInt(4);
        //                 if (xx >= r && yy >= r && xx < w - r && yy < h - r) {
        //                     if (map[xx + yy * w] == 7) { // id of "Rock" == 7
        //                         map[xx + yy * w] = id;
        //                     }
        //                 }
        //             }
        //         }
        //     }
        // }
    }
}
