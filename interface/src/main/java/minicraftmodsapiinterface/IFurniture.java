package minicraftmodsapiinterface;

public interface IFurniture {
    public IFurniture clone();

	public void tick();
	
	/** Draws the furniture on the screen. */
	public void render(IScreen screen);
	
	/** Called when the player presses the MENU key in front of this. */
	public boolean use(IPlayer player);
	
	public boolean blocks(IEntity e);
	
	/**
	 * Used in PowerGloveItem.java to let the user pick up furniture.
	 * @param player The player picking up the furniture.
	 */
	public boolean interact(IPlayer player, IItem item, IDirection attackDir);

	/**
	 * Tries to let the player push this furniture.
	 * @param player The player doing the pushing.
	 */
	public void tryPush(IPlayer player);
	
	public boolean canWool();
	

}
