package mod;

import java.util.Random;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

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
                public static void render(Object screen, Object level, int x, int y, Object sprite, Class<?> extra) {
                    try {
                        Object grass = Class.class.cast(extra.getDeclaredField("tiles").get(null)).getDeclaredMethod("get", String.class).invoke(null, "Grass");
                        System.out.println(grass.getClass().getDeclaredMethod("render", screen.getClass(), level.getClass(), int.class, int.class));
                        System.out.println(sprite.getClass().getDeclaredMethod("render", screen.getClass(), int.class, int.class));
                        grass.getClass().getDeclaredMethod("render", screen.getClass(), level.getClass(), int.class, int.class).invoke(grass, screen, level, x, y);
                        sprite.getClass().getDeclaredMethod("render", screen.getClass(), int.class, int.class).invoke(sprite, screen, x*16, y*16);
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {e.printStackTrace();}
                }
                public static boolean tick(Object level, int xt, int yt, Class<?> extra) {
                    try {
                        int damage = (int)level.getClass().getDeclaredMethod("getData", int.class, int.class).invoke(level, xt, yt);
                        if (damage > 0) {
                            level.getClass().getDeclaredMethod("setData", int.class, int.class, int.class).invoke(level, xt, yt, damage - 1);
                            return true;
                        }
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {e.printStackTrace();}
                    return false;
                }
                public static boolean hurt(Object level, int x, int y, Object source, int dmg, Object attackDir, Class<?> extra) {
                    hurt(level, x, y, dmg, extra);
                    return true;
                }
            	public static void hurt(Object level, int x, int y, int dmg, Class<?> extra) {
                    try {
                        Method itemget = Class.class.cast(extra.getDeclaredField("items").get(null)).getDeclaredMethod("get", String.class);
                        Method dropItem = level.getClass().getDeclaredMethod("dropItem", int.class, int.class, int.class, int.class, itemget.invoke(null, "Apple").getClass().arrayType());
                        if (random.nextInt(250) == 0)
                        level.getClass().getDeclaredMethod("dropItem", int.class, int.class, itemget.invoke(null, "Apple").getClass()).invoke(level, x * 16 + 8, y * 16 + 8, itemget.invoke(null, "Apple"));
                        
                        int damage = (int)level.getClass().getDeclaredMethod("getData", int.class, int.class).invoke(level, x, y) + dmg;
                        int treeHealth = 30;
                        if ((boolean)Class.class.cast(extra.getDeclaredField("game")).getDeclaredMethod("isMode", String.class).invoke(null, "Creative")) dmg = damage = treeHealth;
                        
                        HashMap<String, Class<?>> Particles = ((HashMap)extra.getDeclaredField("particles").get(null));
                        Method leveladd = level.getClass().getDeclaredMethod("add", Particles.get("SmashParticle").getSuperclass().getSuperclass());
                        leveladd.invoke(Particles.get("SmashParticle").getDeclaredConstructor(int.class, int.class).newInstance(x*16, y*16));
                        Object monsterHurt = Class.class.cast(extra.getDeclaredField("sound")).getDeclaredField("monsterHurt").get(null);
                        monsterHurt.getClass().getDeclaredMethod("play", (Class[])null).invoke(monsterHurt, new Object[0]);
                
                        leveladd.invoke(Particles.get("TextParticle").getDeclaredConstructor(String.class, int.class, int.class, int.class).newInstance("" + dmg, x * 16 + 8, y * 16 + 8, (1 << 24) + (255 << 16) + (0 << 8) + (0)));
                        if (damage >= treeHealth) {
                            dropItem.invoke(level, x * 16 + 8, y * 16 + 8, 1, 3, itemget.invoke(null, "Wood"));
                            dropItem.invoke(level, x * 16 +  8, y * 16 + 8, 0, 2, itemget.invoke(null, "Acorn"));
                            Method tileget = (Class.class.cast(extra.getDeclaredField("tiles").get(null)).getDeclaredMethod("get", String.class));
                            // level.getClass().getDeclaredMethod("setTile", int.class, int.class, tileget.invoke(null, "Grass")).invoke(x, y, tileget.invoke(null, "Grass"));
                            Object grass = tileget.invoke(null, "Grass");
                            Class<?> clazz = level.getClass();
                            Method method = clazz.getDeclaredMethod("setTile", int.class, int.class, grass.getClass());
                            method.invoke(x, y, grass);
                        } else {
                            level.getClass().getDeclaredMethod("setData", int.class, int.class, int.class).invoke(level, x, y, damage);
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
