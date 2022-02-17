package minicraftmodsapiinterface;

import java.util.List;

public interface IEntity {
	public abstract void render(IScreen screen); // Used to render the entity on screen.
	
	public abstract void tick(); // Used to update the entity.
	
	/**
	 * Returns true if the entity is removed from the level, otherwise false.
	 * @return removed
	 */
	public boolean isRemoved();
	
	/**
	 * Returns the level which this entity belongs in.
	 * @return level
	 */
	public ILevel getLevel();
		
	/** Returns true if this entity is found in the rectangle specified by given two coordinates. */
	public boolean isTouching(IRectangle area);
	
	/** Returns if this entity stops other solid entities from moving. */
	public boolean isSolid(); // Most entities are solid
	
	/** Determines if the given entity should prevent this entity from moving. */
	public boolean blocks(IEntity e);
	
	public boolean canSwim(); // Determines if the entity can swim (extended in sub-classes)
	public boolean canWool(); // This, strangely enough, determines if the entity can walk on wool; among some other things..?
	
	public int getLightRadius(); // Used for lanterns... and player? that might be about it, though, so idk if I want to put it here.
	
	/**
	 * Interacts with the entity this method is called on
	 * @param player The player attacking
	 * @param item The item the player attacked with
	 * @param attackDir The direction to interact
	 * @return If the interaction was successful
	 */
	public boolean interact(IPlayer player, IItem item, IDirection attackDir);
	
	/** Moves an entity horizontally and vertically. Returns whether entity was unimpeded in it's movement.  */
	public boolean move(int xd, int yd);
	
	/**
	 * Moves the entity a long only one direction.
	 * If xd != 0 then ya should be 0.
	 * If xd = 0 then ya should be != 0.
	 * Will throw exception otherwise.
	 * @param xd Horizontal move.
	 * @param yd Vertical move.
	 * @return true if the move was successful, false if not.
	 */

	/** This exists as a way to signify that the entity has been removed through player action and/or world action; basically, it's actually gone, not just removed from a level because it's out of range or something. Calls to this method are used to, say, drop items. */
	public void die();
	
	/** Removes the entity from the level. */
	public void remove();
	
	/** This should ONLY be called by the Level class. To properly remove an entity from a level, use level.remove(entity) */
	public void remove(ILevel level);
	
	/** This should ONLY be called by the Level class. To properly add an entity to a level, use level.add(entity) */
	public void setLevel(ILevel level, int x, int y);
	
	public boolean isWithin(int tileRadius, IEntity other);
	
	/**
	 * I think this is used to update a entity over a network.
	 * The server will send a correction of this entity's state
	 * which will then be updated.
	 * @param deltas A string representation of the new entity state.
	 */
	public void update(String deltas);
	
	/**
	 * Returns a string representation of this entity.
	 * @param fetchAll true if all variables should be returned, false if only the ones who have changed should be returned.
	 * @return Networking string representation of this entity.
	 */
	public String getUpdates(boolean fetchAll);
	
	/**
	 * Determines what has been updated and only return that.
	 * @return String representation of all the variables which has changed since last time.
	 */
	public String getUpdates();
	
	/// This marks the entity as having a new state to fetch.
	public void flushUpdates();
	
	public String toString();
}
