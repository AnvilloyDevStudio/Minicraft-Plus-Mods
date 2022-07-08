package minicraft.item;

import java.util.HashMap;

public class ToolType {
	public static HashMap<String, ToolType> types = new HashMap<>();

	static {
		types.put("shovel", new ToolType("shovel", 0, 34)); // If there's a second number, it specifies durability.
		types.put("hoe", new ToolType("hoe", 1, 30));
		types.put("sword", new ToolType("sword", 2, 52));
		types.put("pickaxe", new ToolType("pickaxe", 3, 38));
		types.put("axe", new ToolType("axe", 3, 34));
		types.put("bow", new ToolType("bow", 5, 30));
		types.put("claymore", new ToolType("claymore", 6, 44));
		types.put("shears", new ToolType("shears", 0, 42, true));
	}

	public final int xPos; // X Position of origin
	public final int yPos; // Y position of origin
	public final int durability;
	public final boolean noLevel;
	public final String name;

	/**
	 * Create a tool with four levels: wood, stone, iron, gold, and gem.
	 * All these levels are added automatically but sprites have to be added manually.
	 * Uses line 14 in the item spritesheet.
	 * @param xPos X position of the starting sprite in the spritesheet.
	 * @param dur Durabiltity of the tool.
	 */
	ToolType(String name, int xPos, int dur) {
		this.name = name;
		this.xPos = xPos;
		yPos = 13;
		durability = dur;
		noLevel = false;
	}

	/**
	 * Create a tool without a specified level.
	 * Uses line 13 in the items spritesheet.
	 * @param xPos X position of the sprite in the spritesheet.
	 * @param dur Durabiltity of the tool.
	 * @param noLevel If the tool has only one level.
	 */
	ToolType(String name, int xPos, int dur, boolean noLevel) {
		this.name = name;
		yPos = 12;
		this.xPos = xPos;
		durability = dur;
		this.noLevel = noLevel;
	}
}
