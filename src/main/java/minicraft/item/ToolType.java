package minicraft.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ToolType {
	public static ArrayList<ToolType> TypeInstances = new ArrayList<>();
	public static HashMap<String, ToolType> Types = new HashMap<>();
	public static HashMap<String, ArrayList<ItemLevel>> TypeLevels = new HashMap<>();
	static {
		ArrayList<ItemLevel> defaultLevels = new ArrayList<>(List.of(
			ItemLevel.Levels.get("wood"),
			ItemLevel.Levels.get("rock"),
			ItemLevel.Levels.get("iron"),
			ItemLevel.Levels.get("gold"),
			ItemLevel.Levels.get("gem")
		));
		Types.put("shovel", new ToolType("shovel", 0, 24));
		TypeLevels.put("shovel", defaultLevels);
		Types.put("hoe", new ToolType("hoe", 1, 20));
		TypeLevels.put("hoe", defaultLevels);
		Types.put("sword", new ToolType("sword", 2, 42));
		TypeLevels.put("sword", defaultLevels);
		Types.put("pickaxe", new ToolType("pickaxe", 3, 28));
		TypeLevels.put("pickaxe", defaultLevels);
		Types.put("axe", new ToolType("axe", 4, 24));
		TypeLevels.put("axe", defaultLevels);
		Types.put("bow", new ToolType("bow", 5, 20));
		TypeLevels.put("bow", defaultLevels);
		Types.put("claymore", new ToolType("claymore", 6, 34));
		TypeLevels.put("claymore", defaultLevels);
		Types.put("shear", new ToolType("shear", 0, 42, true));
		TypeLevels.put("shear", new ArrayList<ItemLevel>());
	}

	public final String name;
	public final int xPos; // X Position of origin
	public final int yPos; // Y position of origin
	public final int durability;
	public final boolean noLevel;

	/**
	 * Create a tool with four levels: wood, stone, iron, gold, and gem.
	 * All these levels are added automatically but sprites have to be added manually.
	 * Uses line 14 in the item spritesheet.
	 * @param xPos X position of the starting sprite in the spritesheet.
	 * @param dur Durabiltity of the tool.
	 */
	ToolType(String name, int xPos, int dur) {this(name, xPos, 13, dur);}
	ToolType(String name, int xPos, int yPos, int dur) {this(name, xPos, yPos, dur, false);}
	ToolType(String name, int xPos, int dur, boolean noLevel) {this(name, xPos, 12, dur, noLevel);}
	/**
	 * Create a tool without a specified level.
	 * Uses line 13 in the items spritesheet.
	 * @param xPos X position of the sprite in the spritesheet.
	 * @param dur Durabiltity of the tool.
	 * @param noLevel If the tool has only one level.
	 */
	ToolType(String name, int xPos, int yPos, int dur, boolean noLevel) {
		name = name.toLowerCase();
		this.name = name;
		this.yPos = yPos;
		this.xPos = xPos;
		durability = dur;
		this.noLevel = noLevel;
		TypeInstances.add(this);
		if (Types.containsKey(name)) new Exception("Repeated ToolType: "+name).printStackTrace();
		Types.put(name, this);
		TypeLevels.put(name, new ArrayList<ItemLevel>());

	}
}
