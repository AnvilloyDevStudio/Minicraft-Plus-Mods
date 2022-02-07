package minicraft.item;

import java.util.ArrayList;
import java.util.Random;

import minicraft.core.Game;
import minicraft.core.io.Localization;
import minicraft.entity.Entity;
import minicraft.entity.mob.Mob;
import minicraft.gfx.Sprite;

public class ToolItem extends Item {
	
	public static ArrayList<Item> Instances = new ArrayList<>();

	protected static ArrayList<Item> getAllInstances() {

		// for (ToolType tool : ToolType.values()) {
		// 	if (!tool.noLevel) {
		// 		for (int lvl = 0; lvl <= 4; lvl++)
		// 			items.add(new ToolItem(tool, lvl));
		// 	} else {
		// 		items.add(new ToolItem(tool));
		// 	}
		// }
		
		return Instances;
	}
	
	private Random random = new Random();
	
	public ToolType type; // Type of tool (Sword, hoe, axe, pickaxe, shovel)
	public ItemLevel level = ItemLevel.Levels.get("wood"); // Level of said tool
	public int dur; // The durability of the tool
	
	/** Tool Item, requires a tool type (ToolType.Sword, ToolType.Axe, ToolType.Hoe, etc) and a level (0 = wood, 2 = iron, 4 = gem, etc) */
	public ToolItem(ToolType type, ItemLevel level) {
		super(level.name + " " + type.name, type.sprite!=null? type.sprite: new Sprite(type.xPos, type.yPos + level.level-1, 0));
		
		this.type = type;
		this.level = level;
		
		dur = type.durability * level.level; // Initial durability fetched from the ToolType
	}

	public ToolItem(ToolType type) {
		super(type.name, type.sprite!=null? type.sprite: new Sprite(type.xPos, type.yPos, 0));

		this.type = type;
		dur = type.durability;
	}

	static {
		for (ToolType type : ToolType.TypeInstances) 
			if (!type.noLevel)
				for (ItemLevel lvl : ToolType.TypeLevels.get(type.name))
					Instances.add(new ToolItem(type, lvl));
			else Instances.add(new ToolItem(type));
	}
	
	/** Gets the name of this tool (and it's type) as a display string. */
	@Override
	public String getDisplayName() {
		if (!type.noLevel) return " " + Localization.getLocalized(level.name) + " " + Localization.getLocalized(type.toString());
		else return " " + Localization.getLocalized(type.toString());
	}
	
	public boolean isDepleted() {
		return dur <= 0 && type.durability > 0;
	}
	
	/** You can attack mobs with tools. */
	public boolean canAttack() {
		return type.attack;
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
			if (type.name.equals("axe")) {
				return level.level * 2 + random.nextInt(4); // Wood axe damage: 2-5; gem axe damage: 10-13.
			}
			if (type.name.equals("sword")) {
				return level.level * 3 + random.nextInt(2 + (level.level-1) * (level.level-1)); // Wood: 3-5 damage; gem: 15-32 damage.
			}
			if (type.name.equals("claymore")) {
				return level.level * 3 + random.nextInt(4 + (level.level-1) * (level.level-1) * 3); // Wood: 3-6 damage; gem: 15-66 damage.
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
	public int hashCode() { return type.name.hashCode() + (level.level-1); }
	
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
