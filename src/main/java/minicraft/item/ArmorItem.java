package minicraft.item;

import java.util.ArrayList;

import minicraft.entity.Direction;
import minicraft.entity.mob.Player;
import minicraft.gfx.Sprite;
import minicraft.level.Level;
import minicraft.level.tile.Tile;
import minicraftmodsapiinterface.ISprite;

public class ArmorItem extends StackableItem {
	public static ArrayList<Item> Instances = new ArrayList<>();
	
	protected static ArrayList<Item> getAllInstances() {
		
		
		return Instances;
	}
	
	static {
		new ArmorItem("Leather Armor", new Sprite(0, 9, 0), .3f, 1);
		new ArmorItem("Snake Armor", new Sprite(1, 9, 0), .4f, 2);
		new ArmorItem("Iron Armor", new Sprite(2, 9, 0), .5f, 3);
		new ArmorItem("Gold Armor", new Sprite(3, 9, 0), .7f, 4);
		new ArmorItem("Gem Armor", new Sprite(4, 9, 0), 1f, 5);
	}
	
	private final float armor;
	private final int staminaCost;
	public final int level;
	
	public ArmorItem(String name, Sprite sprite, float health, int level) { this(name, sprite, 1, health, level); }
	private ArmorItem(String name, ISprite sprite, int count, float health, int level) {
		super(name, sprite, count);
		this.armor = health;
		this.level = level;
		staminaCost = 9;
		Instances.add(this);
	}

	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, Direction attackDir) {
		boolean success = false;
		if (player.curArmor == null && player.payStamina(staminaCost)) {
			player.curArmor = this; // Set the current armor being worn to this.
			player.armor = (int) (armor * Player.maxArmor); // Armor is how many hits are left
			success = true;
		}
		
		return super.interactOn(success);
	}
	
	@Override
	public boolean interactsWithWorld() { return false; }
	
	public ArmorItem clone() {
		return new ArmorItem(getName(), sprite, count, armor, level);
	}
}
