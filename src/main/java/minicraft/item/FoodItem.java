package minicraft.item;

import java.util.ArrayList;

import minicraft.entity.Direction;
import minicraft.entity.mob.Player;
import minicraft.gfx.Sprite;
import minicraft.level.Level;
import minicraft.level.tile.Tile;


public class FoodItem extends StackableItem {
	public static ArrayList<Item> Instances = new ArrayList<>();
	
	protected static ArrayList<Item> getAllInstances() {

		return Instances;
	}
	
	static {
		new FoodItem("Baked Potato", new Sprite(19, 0, 0), 1);
		new FoodItem("Apple", new Sprite(16, 0, 0), 1);
		new FoodItem("Raw Pork", new Sprite(10, 0, 0), 1);
		new FoodItem("Raw Fish", new Sprite(14, 0, 0), 1);
		new FoodItem("Raw Beef", new Sprite(12, 0, 0), 1);
		new FoodItem("Bread", new Sprite(7, 0, 0), 2);
		new FoodItem("Cooked Fish", new Sprite(15, 0, 0), 3);
		new FoodItem("Cooked Pork", new Sprite(11, 0, 0), 3);
		new FoodItem("Steak", new Sprite(13, 0, 0), 3);
		new FoodItem("Gold Apple", new Sprite(17, 0, 0), 10);
	}
	
	private int feed; // The amount of hunger the food "satisfies" you by.
	private int staminaCost; // The amount of stamina it costs to consume the food.
	
	private FoodItem(String name, Sprite sprite, int feed) { this(name, sprite, 1, feed); }
	private FoodItem(String name, Sprite sprite, int count, int feed) { this(name, sprite, 1, feed, 5); }
	public FoodItem(String name, Sprite sprite, int count, int feed, int cost) {
		super(name, sprite, count);
		this.feed = feed;
		staminaCost = cost;
		Instances.add(this);
	}
	
	/** What happens when the player uses the item on a tile */
	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player Iplayer, Direction attackDir) {
		boolean success = false;
		Player player = (Player) Iplayer;
		if (count > 0 && player.hunger < Player.maxHunger && player.payStamina(staminaCost)) { // If the player has hunger to fill, and stamina to pay...
			player.hunger = Math.min(player.hunger + feed, Player.maxHunger); // Restore the hunger
			success = true;
		}
		
		return super.interactOn(success);
	}
	
	@Override
	public boolean interactsWithWorld() { return false; }
	
	public FoodItem clone() {
		return new FoodItem(getName(), sprite, count, feed);
	}
}
