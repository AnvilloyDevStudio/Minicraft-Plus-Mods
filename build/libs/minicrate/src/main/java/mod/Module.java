package mod;

import java.util.Random;

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
    // public static class Tiles {
    //     // public static class CopperOre {
    //     //     public static String name = "Copper";
    //     //     public static String tiletype = "Ore";
    //     //     public static String drop = "CopperOre";
    //     //     public static boolean spriteSheet = false;
    //     //     public static int id = 43;
    //     //     public static void tilegen(short[] map, Random random, int depth, int w, int h) {
    //     //         int r = 2;
    //     //         for (int i = 0; i < w * h / 650; i++) {
    //     //             int x = random.nextInt(w);
    //     //             int y = random.nextInt(h);
    //     //             for (int j = 0; j < 35; j++) {
    //     //                 int xx = x + random.nextInt(4) - random.nextInt(4);
    //     //                 int yy = y + random.nextInt(4) - random.nextInt(4);
    //     //                 if (xx >= r && yy >= r && xx < w - r && yy < h - r) {
    //     //                     if (map[xx + yy * w] == 7) { // id of "Rock" == 7
    //     //                         map[xx + yy * w] = id;
    //     //                     }
    //     //                 }
    //     //             }
    //     //         }
    //     //     }
    //     // }
    // }
}
