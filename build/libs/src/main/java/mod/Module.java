package mod;

import java.util.Random;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import minicraftmodsapiinterface.*;

public class Module {
    private Module() {}
    public static class Items {
        // public static class Al1{
        //     public static String itemtype = "Tool";
        //     public static String name = "Al1";
        //     public static String type = "Al1";
        //     public static int durability = 20;
        //     public static boolean noLevel = true;
        //     public static boolean attack = true;
        //     public static boolean spriteSheet = false;
        // }
        public static class CopperOre {
            public static String itemtype = "Stackable";
            public static String name = "copper ore";
            public static boolean spriteSheet = false;
        }
        // public static class Forge {
        //     public static String itemtype = "Tool";
        //     public static String name = "Forge";
        //     public static String type = "Forge";
        //     public static int durability = 100;
        //     public static boolean noLevel = true;
        //     public static boolean attack = true;
        //     public static boolean spriteSheet = false;
        // }
        public static class Copper {
            public static String itemtype = "Stackable";
            public static String name = "copper";
            public static boolean spriteSheet = false;
        }
    }
    public static class Recipes {
        public static class Copper {
            public static String creation = "Copper_1";
            public static String[] require = new String[] {"Coal_1", "Copper Ore_4"};
            public static String type = "furnace";
        }
    }
    public static class Tiles {
        public static class CopperOre {
            public static String name = "Copper";
            public static String tiletype = "Ore";
            public static String drop = "CopperOre";
            public static boolean spriteSheet = false;
            public static int id = 43;
            public static void tilegen(short[] map, Random random, int depth, int w, int h, Class<?> Tiles) {
                int r = 2;
                for (int i = 0; i < w * h / 650; i++) {
                    int x = random.nextInt(w);
                    int y = random.nextInt(h);
                    for (int j = 0; j < 35; j++) {
                        int xx = x + random.nextInt(4) - random.nextInt(4);
                        int yy = y + random.nextInt(4) - random.nextInt(4);
                        if (xx >= r && yy >= r && xx < w - r && yy < h - r) {
                            if (map[xx + yy * w] == 7) { // id of "Rock" == 7
                                map[xx + yy * w] = (short) id;
                            }
                        }
                    }
                }
            }
        }
        public static class HighTree {
            public static String name = "High Tree";
            public static boolean spriteSheet = false;
            public static String tiletype = "";
            public static int id = 44;
            private static Random random = new Random();
            public static class Options {
                public static Map<String, Boolean> Connections = Map.of("grass", true);
                public static void render(IScreen screen, ILevel level, int x, int y, ISprite sprite, Class<?> extra) {
                    try {
                        ITile grass =(ITile) Class.class.cast(extra.getDeclaredField("tiles").get(null)).getDeclaredMethod("get", String.class).invoke(null, "Grass");
                        grass.render(screen, level, x, y);
                        sprite.render(screen, x*16, y*16);
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {e.printStackTrace();}
                }
                public static boolean tick(ILevel level, int xt, int yt, Class<?> extra) {
                    int damage = level.getData(xt, yt);
                    if (damage > 0) {
                        level.setData(xt, yt, damage - 1);
                        return true;
                    }
                    return false;
                }
                public static boolean hurt(ILevel level, int x, int y, IMob source, int dmg, IDirection attackDir, Class<?> extra) {
                    hurt(level, x, y, dmg, extra);
                    return true;
                }
            	public static void hurt(ILevel level, int x, int y, int dmg, Class<?> extra) {
                    try {
                        Method itemget = Class.class.cast(extra.getDeclaredField("items").get(null)).getDeclaredMethod("get", String.class);
                        if (random.nextInt(250) == 0)
                        level.dropItem(x * 16 + 8, y * 16 + 8, (IItem)itemget.invoke(null, "Apple"));
                        
                        int damage = level.getData(x, y) + dmg;
                        int treeHealth = 30;
                        if ((boolean)Class.class.cast(extra.getDeclaredField("game").get(null)).getDeclaredMethod("isMode", String.class).invoke(null, "Creative")) dmg = damage = treeHealth;
                        
                        HashMap<String, Class<? extends IEntity>> Particles = ((HashMap)extra.getDeclaredField("particles").get(null));
                        level.add(Particles.get("SmashParticle").getDeclaredConstructor(int.class, int.class).newInstance(x*16, y*16));
                        Object monsterHurt = Class.class.cast(extra.getDeclaredField("sound").get(null)).getDeclaredField("monsterHurt").get(null);
                        monsterHurt.getClass().getDeclaredMethod("play", (Class[])null).invoke(monsterHurt, new Object[0]);
                
                        level.add(Particles.get("TextParticle").getDeclaredConstructor(String.class, int.class, int.class, int.class).newInstance("" + dmg, x * 16 + 8, y * 16 + 8, (1 << 24) + (255 << 16) + (0 << 8) + (0)));
                        if (damage >= treeHealth) {
                            level.dropItem(x * 16 + 8, y * 16 + 8, 1, 3, (IItem)itemget.invoke(null, "Wood"));
                            level.dropItem(x * 16 +  8, y * 16 + 8, 0, 2, (IItem)itemget.invoke(null, "Acorn"));
                            Method tileget = (Class.class.cast(extra.getDeclaredField("tiles").get(null)).getDeclaredMethod("get", String.class));
                            // level.getClass().getDeclaredMethod("setTile", int.class, int.class, tileget.invoke(null, "Grass")).invoke(x, y, tileget.invoke(null, "Grass"));
                            ITile grass = (ITile)tileget.invoke(null, "Grass");
                            level.setTile(x, y, grass);
                        } else {
                            level.setData(x, y, damage);
                        }
                    } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {e.printStackTrace();}
                }
            
            }
            public static void tilegen(short[] map, Random random, int w, int h, Class<?> Tiles) {
                try {
                    Method getTile = Tiles.getDeclaredMethod("get", String.class);
                    Object tree = getTile.invoke(null, "tree");
                    short treeid = (short)tree.getClass().getField("id").get(tree);
                    for (int i = 0; i < w * h / 200; i++) {
                        int x = random.nextInt(w);
                        int y = random.nextInt(h);
                        for (int j = 0; j < 200; j++) {
                            int xx = x + random.nextInt(15) - random.nextInt(15);
                            int yy = y + random.nextInt(15) - random.nextInt(15);
                            if (xx >= 0 && yy >= 0 && xx < w && yy < h) {
                                if (map[xx + yy * w] == treeid) {
                                    map[xx + yy * w] = (short)id;
                                }
                            }
                        }
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {e.printStackTrace();}
            }
        }
    }
}
