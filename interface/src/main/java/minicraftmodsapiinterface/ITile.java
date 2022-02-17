package minicraftmodsapiinterface;

import java.util.ArrayList;
import java.util.HashMap;

public interface ITile {
    public static interface TileConnections {
		public ArrayList<Short> getTiles(String name);
		public Boolean get(String name);
		public void set(String name, boolean value);
	}
	
	/** This method is used by tiles to specify the default "data" they have in a level's data array.
		Used for starting health, color/type of tile, etc. */
	// At least, that was the idea at first...
	public int getDefaultData();
	
	/** Render method, used in sub-classes */
	public void render(IScreen screen, ILevel level, int x, int y);
	
	public boolean maySpawn();
	
	/** Returns if the player can walk on it, overrides in sub-classes  */
	public boolean mayPass(ILevel level, int x, int y, IEntity e);

	/** Gets the light radius of a tile, Bigger number = bigger circle */
	public int getLightRadius(ILevel level, int x, int y);

	/**
	 * Hurt the tile with a specified amount of damage.
	 * @param level The level this happened on.
	 * @param x X pos of the tile.
	 * @param y Y pos of the tile.
	 * @param source The mob that damaged the tile.
	 * @param dmg Damage to taken.
	 * @param attackDir The direction of the player hitting.
	 * @return If the damage was applied.
	 */
	public boolean hurt(ILevel level, int x, int y, IMob source, int dmg, IDirection attackDir);

	/**
	 * Hurt the tile with a specified amount of damage.
	 * @param level The level this happened on.
	 * @param x X position of the tile.
	 * @param y Y position of the tile.
	 * @param dmg The damage taken.
	 */
	public void hurt(ILevel level, int x, int y, int dmg);
	
	/** What happens when you run into the tile (ex: run into a cactus) */
	public void bumpedInto(ILevel level, int xt, int yt, IEntity entity);
	
	/** Update method */
	public boolean tick(ILevel level, int xt, int yt);
	
	/** What happens when you are inside the tile (ex: lava) */
	public void steppedOn(ILevel level, int xt, int yt, IEntity entity);

	/**
	 * Called when you hit an item on a tile (ex: Pickaxe on rock).
	 * @param level The level the player is on.
	 * @param xt X position of the player in tile coordinates (32x per tile).
	 * @param yt Y position of the player in tile coordinates (32px per tile).
	 * @param player The player who called this method.
	 * @param item The item the player is currently holding.
	 * @param attackDir The direction of the player attacking.
	 * @return Was the operation successful?
	 */
	public boolean interact(ILevel level, int xt, int yt, IPlayer player, IItem item, IDirection attackDir);
	
	/** Sees if the tile connects to a fluid. */
	public boolean connectsToLiquid();
	
	public int getData(String data);
	
	public boolean matches(int thisData, String tileInfo);
	
	public String getName(int data);
	
}
