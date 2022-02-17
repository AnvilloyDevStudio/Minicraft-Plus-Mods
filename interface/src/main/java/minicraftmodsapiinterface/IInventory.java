package minicraftmodsapiinterface;

import java.util.List;

public interface IInventory {
    public List<IItem> getItems();
	public void clearInv();
	public int invSize();

	/**
	 * Get one item in this inventory.
	 * @param idx The index of the item in the inventory's item array.
	 * @return The specified item.
	 */
	public IItem get(int idx);

	/**
	 * Remove an item in this inventory.
	 * @param idx The index of the item in the inventory's item array.
	 * @return The removed item.
	 */
	public IItem remove(int idx);
	
	public void addAll(IInventory other);
	
	/** Adds an item to the inventory */
	public void add(IItem item);
	
	/**
	 * Adds several copies of the same item to the end of the inventory.
	 * @param item IItem to be added.
	 * @param num Amount of items to add.
	 */
	public void add(IItem item, int num);
	
	/**
	 * Adds an item to a specific spot in the inventory.
	 * @param slot Index to place item at.
	 * @param item IItem to be added.
	 */
	public void add(int slot, IItem item);
	
	/** 
	 * Removes the item from the inventory entirely, whether it's a stack, or a lone item.
	 */
	public void removeItem(IItem i);
	
	/**
	 * Removes items from this inventory. Note, if passed a stackable item, this will only remove a max of count from the stack.
	 * @param given IItem to remove.
	 * @param count Max amount of the item to remove.
	 */
	public void removeItems(IItem given, int count);
	
	/** Returns the how many of an item you have in the inventory. */
	public int count(IItem given);
	
	/**
	 * Generates a string representation of all the items in the inventory which can be sent
	 * over the network.
	 * @return String representation of all the items in the inventory.
	 */
	public String getItemData();
	
	/**
	 * Replaces all the items in the inventory with the items in the string.
	 * @param items String representation of an inventory.
	 */
	public void updateInv(String items);
	
	/**
	 * Tries to add an item to the inventory.
	 * @param chance Chance for the item to be added.
	 * @param item IItem to be added.
	 * @param num How many of the item.
	 * @param allOrNothing if true, either all items will be added or none, if false its possible to add
	 * between 0-num items.
	 */
	public void tryAdd(int chance, IItem item, int num, boolean allOrNothing);
	public void tryAdd(int chance, IItem item, int num);
	public void tryAdd(int chance, IItem item);
	public void tryAdd(int chance, IToolType type, int lvl);
	
	/**
	 * Tries to add an Furniture to the inventory.
	 * @param chance Chance for the item to be added.
	 * @param type Type of furniture to add.
	 */
	public void tryAdd(int chance, IFurniture type);

}
