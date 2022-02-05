package minicraft.mod;

import java.util.ArrayList;
import java.util.Random;

import minicraft.core.Mods;
import minicraft.item.*;
import minicraft.core.io.Localization;
import minicraft.entity.Direction;
import minicraft.entity.mob.Player;
import minicraft.gfx.Font;
import minicraft.gfx.Screen;
import minicraft.gfx.Sprite;
import minicraft.level.Level;
import minicraft.level.tile.Tile;


import minicraft.core.Game;
import minicraft.core.io.Localization;
import minicraft.entity.Entity;
import minicraft.entity.mob.Mob;
import minicraft.gfx.Sprite;


public abstract class ModItem extends Item {
    public ModItem(String name) {
        super(name);
    }
    private static final ArrayList<Item> ModItems;
    public static ArrayList<ModItem.ToolType> ToolTypes;
    public static class ToolType {
        public static class Type {
            public final String name;
            public final int level;
            Type(String name, int level) {
                this.name = name;
                this.level = level;
            }
        }
        public final String name;
        public int xPos; // X Position of origin
        public int yPos; // Y position of origin
        public final int durability;
        public final boolean noLevel;
        public ArrayList<ModItemLevel> levels = new ArrayList<>();
        public ModItemLevel level; // The level when adding
        public static ArrayList<ToolType> types = new ArrayList<>();

        ToolType(String name, int dur, ModItemLevel level) {
            this.name = name;
            this.durability = dur;
            this.levels.add(level);
            this.level = level;
            this.noLevel = false;
            types.add(this);
        }
        ToolType(String name, int dur, ModItemLevel level, boolean nolevel) {
            this.name = name;
            this.durability = dur;
            this.levels.add(level);
            this.level = level;
            this.noLevel = nolevel;
            types.add(this);
        }
        public void addLevel(ModItemLevel level) {
            this.levels.add(level);
        }
    }
    static {
        ArrayList<Mods.Mod.Item> items = Mods.Items;
        ArrayList<Item> moditems = new ArrayList<>();
        for (Mods.Mod.Item item : items) {
            switch (item.itemtype) {
                case "tool":
                    moditems.add(new ModToolItem(item.itemtype, item.durability));
            }
        }
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
        return null;
    }
}
