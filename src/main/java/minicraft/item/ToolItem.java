package minicraft.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import minicraft.core.Game;
import minicraft.core.io.Localization;
import minicraft.entity.Entity;
import minicraft.entity.mob.Mob;
import minicraft.gfx.Sprite;

public class ToolItem extends Item {

	protected static ArrayList<Item> getAllInstances() {
		ArrayList<Item> items = new ArrayList<>();

		for (ToolType tool : ToolType.types.values()) {
			if (!tool.noLevel) {
				items.add(new ToolItem(tool, "Wood"));
				items.add(new ToolItem(tool, "Rock"));
				items.add(new ToolItem(tool, "Iron"));
				items.add(new ToolItem(tool, "Gold"));
				items.add(new ToolItem(tool, "Gem"));
			} else {
				items.add(new ToolItem(tool));
			}
		}

		return items;
	}

	private Random random = new Random();

	public static HashMap<String, Integer> LEVELS = new HashMap<String, Integer>() {{
		put("Wood", 0);
		put("Rock", 1);
		put("Iron", 2);
		put("Gold", 3);
		put("Gem", 4);
	}};

	public ToolType type; // Type of tool (Sword, hoe, axe, pickaxe, shovel)
	public int level; // Level of said tool
	public String levelName;
	public int dur; // The durability of the tool
	private int damage; // The damage of the tool

	/** Tool Item, requires a tool type (ToolType.Sword, ToolType.Axe, ToolType.Hoe, etc) and a level (0 = wood, 2 = iron, 4 = gem, etc) */
	public ToolItem(ToolType type, String level) {
		super(level + " " + type.name, new Sprite(type.xPos, type.yPos + LEVELS.get(level), 0));

		this.type = type;
		this.level = LEVELS.get(level);
		this.levelName = level;
		this.damage = this.level * 5 + 10;

		dur = type.durability * (this.level + 1); // Initial durability fetched from the ToolType
	}

	public ToolItem(ToolType type) {
		super(type.name, new Sprite(type.xPos, type.yPos, 0));

		this.type = type;
		dur = type.durability;
	}

	/** Gets the name of this tool (and it's type) as a display string. */
	@Override
	public String getDisplayName() {
		if (!type.noLevel) return " " + Localization.getLocalized(levelName) + " " + Localization.getLocalized(type.toString());
		else return " " + Localization.getLocalized(type.toString());
	}

	public boolean isDepleted() {
		return dur <= 0 && type.durability > 0;
	}

	/** You can attack mobs with tools. */
	public boolean canAttack() {
		return !type.name.equals("shears");
	}

	public boolean payDurability() {
		if (dur <= 0) return false;
		if (!Game.isMode("creative")) dur--;
		return true;
	}

	public int getDamage() {
		return random.nextInt(5) + damage;
	}

	/** Gets the attack damage bonus from an item/tool (sword/axe) */
	public int getAttackDamageBonus(Entity e) {
		if (!payDurability())
			return 0;

		if (e instanceof Mob) {
			if (type.name.equals("axe")) {
				return (level + 1) * 2 + random.nextInt(4); // Wood axe damage: 2-5; gem axe damage: 10-13.
			} else if (type.name.equals("sword")) {
				return (level + 1) * 3 + random.nextInt(2 + level * level); // Wood: 3-5 damage; gem: 15-32 damage.
			} else if (type.name.equals("claymore")) {
				return (level + 1) * 3 + random.nextInt(4 + level * level * 3); // Wood: 3-6 damage; gem: 15-66 damage.
			} else if (type.name.equals("pickaxe"))
				return (level + 1) + random.nextInt(2); // Wood: 3-6 damage; gem: 15-66 damage.
			return 1;
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
	public int hashCode() { return type.name.hashCode() + level; }

	public ToolItem clone() {
		ToolItem ti;
		if (type.noLevel) {
			ti = new ToolItem(type);
		} else {
			ti = new ToolItem(type, levelName);
		}
		ti.dur = dur;
		return ti;
	}
}
