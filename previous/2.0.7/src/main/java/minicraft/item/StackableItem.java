package minicraft.item;

import java.util.ArrayList;
import java.util.HashMap;

import minicraft.core.Game;
import minicraft.core.io.Localization;
import minicraft.gfx.Sprite;

// Some items are direct instances of this class; those instances are the true "items", like stone, wood, wheat, or coal; you can't do anything with them besides use them to make something else.

public class StackableItem extends Item {
	private static ArrayList<Item> Instances = new ArrayList<>();
	public static HashMap<String, StackableItem> StackableItems = new HashMap<>();

	protected static ArrayList<Item> getAllInstances() {
	
		
		return Instances;
	}

	static {
		new StackableItem("Wood", new Sprite(1, 0, 0));
		new StackableItem("Stone", new Sprite(2, 0, 0));
		new StackableItem("Leather", new Sprite(8, 0, 0));
		new StackableItem("Wheat", new Sprite(6, 0, 0));
		new StackableItem("Key", new Sprite(0, 4, 0));
		new StackableItem("arrow", new Sprite(0, 2, 0));
		new StackableItem("string", new Sprite(1, 4, 0));
		new StackableItem("Coal", new Sprite(2, 4, 0));
		new StackableItem("Iron Ore", new Sprite(3, 4, 0));
		new StackableItem("Lapis", new Sprite(4, 4, 0));
		new StackableItem("Gold Ore", new Sprite(5, 4, 0));
		new StackableItem("Iron", new Sprite(6, 4, 0));
		new StackableItem("Gold", new Sprite(7, 4, 0));
		new StackableItem("Rose", new Sprite(5, 0, 0));
		new StackableItem("GunPowder", new Sprite(8, 4, 0));
		new StackableItem("Slime", new Sprite(9, 4, 0));
		new StackableItem("glass", new Sprite(10, 4, 0));
		new StackableItem("cloth", new Sprite(11, 4, 0));
		new StackableItem("gem", new Sprite(12, 4, 0));
		new StackableItem("Scale", new Sprite(13, 4, 0));
		new StackableItem("Shard", new Sprite(14, 4, 0));

	}
	
	public int count;
	//public int maxCount = 100; // TODO I want to implement this later.
	
	public StackableItem(String name, Sprite sprite) {
		super(name, sprite);
		count = 1;
		Instances.add(this);
		StackableItems.put(name, this);
	}
	protected StackableItem(String name, Sprite sprite, int count) {
		this(name, (Sprite)sprite);
		this.count = count;
		Instances.add(this);
		StackableItems.put(name, this);
	}
	
	public boolean stacksWith(Item other) { return other instanceof StackableItem && other.getName().equals(getName()); }
	
	// This is used by (most) subclasses, to standardize the count decrement behavior. This is not the normal interactOn method.
	protected boolean interactOn(boolean subClassSuccess) {
		if (subClassSuccess && !Game.isMode("creative"))
			count--;
		return subClassSuccess;
	}
	
	/** Called to determine if this item should be removed from an inventory. */
	@Override
	public boolean isDepleted() {
		return count <= 0;
	}
	
	@Override
	public StackableItem clone() {
		return new StackableItem(getName(), sprite, count);
	}
	
	@Override
	public String toString() {
		return super.toString() + "-Stack_Size:"+count;
	}
	
	public String getData() {
		return getName() + "_" + count;
	}
	
	@Override
	public String getDisplayName() {
		String amt = (Math.min(count, 999)) + " ";
		return " " + amt + Localization.getLocalized(getName());
	}
}
