package minicraftmodsapiinterface;

public interface IItem {
	public ISprite getSprite();
	public boolean used_pending = false; // This is for multiplayer, when an item has been used, and is pending server response as to the outcome, this is set to true so it cannot be used again unless the server responds that the item wasn't used. Which should basically replace the item anyway, soo... yeah. this never gets set back.


    public void renderHUD(IScreen screen, int x, int y, int fontColor);
	
	/** Determines what happens when the player interacts with a tile */
	public boolean interactOn(ITile tile, ILevel level, int xt, int yt, IPlayer player, IDirection attackDir);
	
	/** Returning true causes this item to be removed from the player's active item slot */
	public boolean isDepleted();
	
	/** Returns if the item can attack mobs or not */
	public boolean canAttack();

	/** Sees if an item equals another item */
	public boolean equals(IItem item);
	
	/** This returns a copy of this item, in all necessary detail. */
	public abstract IItem clone();
		
	/** Gets the necessary data to send over a connection. This data should always be directly input-able into Items.get() to create a valid item with the given properties. */
	public String getData();
	
	public String getName();
	
	// Returns the String that should be used to display this item in a menu or list. 
	public String getDisplayName();
	
	public boolean interactsWithWorld();

}
