package minicraft.item;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemLevel {
    public static HashMap<String, ItemLevel> Levels = new HashMap<>();
    public static ArrayList<ItemLevel> LevelInstances = new ArrayList<>();
    static {
        new ItemLevel("wood", 1);
        new ItemLevel("rock", 2);
        new ItemLevel("iron", 3);
        new ItemLevel("gold", 4);
        new ItemLevel("gem", 5);
    }
    public String name;
    public int level;
    public ItemLevel(String name, int level) {
        this.name = name.toLowerCase();
        this.level = level;
        LevelInstances.add(this);
        Levels.put(name.toLowerCase(), this);
    }
}
