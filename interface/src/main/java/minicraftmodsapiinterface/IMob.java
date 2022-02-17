package minicraftmodsapiinterface;

public interface IMob {
    public void tick();
	
	public boolean move(int xd, int yd); // Move the mob, overrides from Entity
	
	public void updatePlayers(int oldxt, int oldyt);
	
	/**
	 * Checks if this Mob is currently on a light tile; if so, the mob sprite is brightened.
	 * @return true if the mob is on a light tile, false if not.
	 */
	public boolean isLight();

	/**
	 * Checks if the mob is swimming (standing on a liquid tile).
	 * @return true if the mob is swimming, false if not.
	 */
	public boolean isSwimming();

	/**
	 * Do damage to the mob this method is called on.
	 * @param tile The tile that hurt the player
	 * @param x The x position of the mob
	 * @param y The x position of the mob
	 * @param damage The amount of damage to hurt the mob with
	 */
	public void hurt(ITile tile, int x, int y, int damage);

	/**
	 * Do damage to the mob this method is called on.
	 * @param mob The mob that hurt this mob
	 * @param damage The amount of damage to hurt the mob with
	 */
	public void hurt(IMob mob, int damage);

	/**
	 * Do damage to the mob this method is called on.
	 * @param mob The mob that hurt this mob
	 * @param damage The amount of damage to hurt the mob with
	 * @param attackDir The direction this mob was attacked from
	 */
	public void hurt(IMob mob, int damage, IDirection attackDir);
	
	public void hurt(ITnt tnt, int dmg);

	/**
	 * Restores health to this mob.
	 * @param heal How much health is restored.
	 */
	public void heal(int heal);
	
}
