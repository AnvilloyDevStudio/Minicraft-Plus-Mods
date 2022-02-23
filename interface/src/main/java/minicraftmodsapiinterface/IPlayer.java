package minicraftmodsapiinterface;

import java.util.HashMap;

public interface IPlayer {
    public int getMultiplier();
	
	public void addMultiplier(int value);
	
	public void tickMultiplier();
	
	public int getScore();
	public void setScore(int score);
	public void addScore(int points);
	
	/**
	 * Adds a new potion effect to the player.
	 * @param type Type of potion.
	 * @param duration How long the effect lasts.
	 */
	public void addPotionEffect(IPotionType type, int duration);
	
	/**
	 * Adds a potion effect to the player.
	 * @param type Type of effect.
	 */
	public void addPotionEffect(IPotionType type);
	
	/**
	 * Returns all the potion effects currently affecting the player.
	 * @return all potion effects on the player.
	 */
	public HashMap<IPotionType, Integer> getPotionEffects();
	
	public void tick();
	
	/**
	 * Removes an held item and places it back into the inventory.
	 * Looks complicated to so it can handle the powerglove.
	 */
	public void resolveHeldItem();

	/**
	 * This method is called when we press the attack button.
	 */

	public void render(IScreen screen);

	/** What happens when the player interacts with a itemEntity */
	public void pickupItem(IItemEntity itemEntity);

	// The player can swim.
	public boolean canSwim();

	// Can walk on wool tiles..? quickly..?
	public boolean canWool();

	/**
	 * Finds a starting position for the player.
	 * @param level ILevel which the player wants to start in.
	 * @param spawnSeed Spawnseed.
	 */
	public void findStartPos(ILevel level, long spawnSeed);

	/**
	 * Finds the starting position for the player in a level.
	 * @param level The level.
	 */
	public void findStartPos(ILevel level);
	public void findStartPos(ILevel level, boolean setSpawn);

	/**
	 * Finds a location where the player can respawn in a given level.
	 * @param level The level.
	 * @return true
	 */
	public boolean respawn(ILevel level);

	/**
	 * Uses an amount of stamina to do an action.
	 * @param cost How much stamina the action requires.
	 * @return true if the player had enough stamina, false if not.
	 */
	public boolean payStamina(int cost);

	/**
	 * Gets the player's light radius underground
	 */
	public int getLightRadius();

	/** What happens when the player dies */
	public void die();

	public void hurt(ITnt tnt, int dmg);

	/** Hurt the player.
	 * @param damage How much damage to do to player.
	 * @param attackDir What direction to attack.
	 */
	public void hurt(int damage, IDirection attackDir);

	public void remove();

	public String getPlayerData();

	public IInventory getInventory();

}
