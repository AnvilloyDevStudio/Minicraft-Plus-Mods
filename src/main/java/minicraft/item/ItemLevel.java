package minicraft.item;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemLevel {
    public static HashMap<String, ItemLevel> Levels = new HashMap<>();
    public static ArrayList<ItemLevel> LevelInstances = new ArrayList<>();
    static {
        Levels.put("wood", new ItemLevel("wood", 1));
        Levels.put("rock", new ItemLevel("rock", 2));
        Levels.put("iron", new ItemLevel("iron", 3));
        Levels.put("gold", new ItemLevel("gold", 4));
        Levels.put("gem", new ItemLevel("gem", 5));
    }
    public String name;
    public int level;
    ItemLevel(String name, int level) {
        this.name = name.toLowerCase();
        this.level = level;
        LevelInstances.add(this);
    }
}
