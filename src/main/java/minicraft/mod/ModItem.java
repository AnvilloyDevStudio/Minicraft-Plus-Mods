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
        public final int level;
        public int xPos; // X Position of origin
        public int yPos; // Y position of origin
        public final int durability;
        public final boolean noLevel;
        public ArrayList<ToolType.Type> types = new ArrayList<>();
    
        ToolType(String name, int level, int dur, boolean nolevel) {
            this.name = name;
            this.level = level;
            this.durability = dur;
            this.noLevel = nolevel;
        }
        public void addLevel(ToolType.Type type) {
            this.types.add(type);
        }
    }
    static {
        ArrayList<Item> items = Mods.Items;
        ArrayList<Item> moditems = new ArrayList<>();
        for (Item item : items) {
            switch (item.itemtype) {
                case "tool":
                    moditems.add(new ModToolItem(item.type));
            }
        }
    }
    public static class ModItemLevel {
        public int level;
        public String name;
        ModItemLevel(String name, int level) {
            
        }
    }
    public static class ModToolItem extends Item {
	
        protected static ArrayList<Item> getAllInstances() {
            ArrayList<Item> items = new ArrayList<>();
    
            for (ToolType tool : ToolTypes) {
                if (!tool.noLevel) {
                    for (int lvl = 0; lvl <= 4; lvl++)
                        items.add(new ModToolItem(tool, lvl));
                } else {
                    items.add(new ModToolItem(tool));
                }
            }
            
            return items;
        }
        
        private Random random = new Random();
        
        // public static final String[] LEVEL_NAMES = {"Wood", "Rock", "Iron", "Gold", "Gem"}; // The names of the different levels. A later level means a stronger tool.
        
        public ToolType type; // Type of tool (Sword, hoe, axe, pickaxe, shovel)
        public int level; // Level of said tool
        public int dur; // The durability of the tool
        
        /** Tool Item, requires a tool type (ToolType.Sword, ToolType.Axe, ToolType.Hoe, etc) and a level (0 = wood, 2 = iron, 4 = gem, etc) */
        // public ModToolItem(ToolType type, int level) {
        //     super(LEVEL_NAMES[level] + " " + type.name(), new Sprite(type.xPos, type.yPos + level, 0));
            
        //     this.type = type;
        //     this.level = level;
            
        //     dur = type.durability * (level + 1); // Initial durability fetched from the ToolType
        // }
    
        public ModToolItem(ToolType type, ) {
            super(type.name, new Sprite(type.xPos, type.yPos, 0));
    
            this.type = type;
            dur = type.durability;
        }

        public ModToolItem(ToolType type) {
            super(type.name, new Sprite(type.xPos, type.yPos, 0));
    
            this.type = type;
            dur = type.durability;
        }
        
        /** Gets the name of this tool (and it's type) as a display string. */
        @Override
        public String getDisplayName() {
            if (!type.noLevel) return " " + Localization.getLocalized(LEVEL_NAMES[level]) + " " + Localization.getLocalized(type.toString());
            else return " " + Localization.getLocalized(type.toString());
        }
        
        public boolean isDepleted() {
            return dur <= 0 && type.durability > 0;
        }
        
        /** You can attack mobs with tools. */
        public boolean canAttack() {
            return type != ToolType.Shear;
        }
        
        public boolean payDurability() {
            if (dur <= 0) return false;
            if (!Game.isMode("creative")) dur--;
            return true;
        }
        
        /** Gets the attack damage bonus from an item/tool (sword/axe) */
        public int getAttackDamageBonus(Entity e) {
            if (!payDurability())
                return 0;
            
            if (e instanceof Mob) {
                if (type == ToolType.Axe) {
                    return (level + 1) * 2 + random.nextInt(4); // Wood axe damage: 2-5; gem axe damage: 10-13.
                }
                if (type == ToolType.Sword) {
                    return (level + 1) * 3 + random.nextInt(2 + level * level); // Wood: 3-5 damage; gem: 15-32 damage.
                }
                if (type == ToolType.Claymore) {
                    return (level + 1) * 3 + random.nextInt(4 + level * level * 3); // Wood: 3-6 damage; gem: 15-66 damage.
                }
                return 1; // All other tools do very little damage to mobs.
            }
            
            return 0;
        }
        
        @Override
        public String getData() {
            return super.getData() + "_" + dur;
        }
        
        /** Sees if this item equals another. */
        @Override
        public boolean equals(Item item) {
            if (item instanceof ToolItem) {
                ToolItem other = (ToolItem) item;
                return other.type == type && other.level == level;
            }
            return false;
        }
        
        @Override
        public int hashCode() { return type.name().hashCode() + level; }
        
        public ToolItem clone() {
            ToolItem ti;
            if (type.noLevel) {
                ti = new ToolItem(type);
            } else {
                ti = new ToolItem(type, level);
            }
            ti.dur = dur;
            return ti;
        }
    }
}
