package minicraft.mod;

import java.util.ArrayList;

import minicraft.core.Mods;
import minicraft.item.*;


public abstract class ModItem /*extends Item*/ {
    public static void init() {}
    private static final ArrayList<Item> ModItems;
    public static ArrayList<ToolType> ToolTypes;
    static {
        ArrayList<Mods.Mod.Item> items = Mods.Items;
        ArrayList<Item> moditems = new ArrayList<>();
        for (Mods.Mod.Item item : items) {
            switch (item.itemtype) {
                case "Tool":
                    moditems.add(item.toToolItem());
                    break;
                case "Stackable":
                    moditems.add(item.toStackableItem());
                    break;
            }
        }
        ModItems = moditems;
    }
    public static class ModItemLevel {
        public int level;
        public String name;
        ModItemLevel(String name, int level) {
            this.name = name;
            this.level = level;
        }
    }
    public static ArrayList<Item> getAllInstances() {
        return ModItems;
    }
}
