package minicraftmodsapiinterface;

import java.util.List;
import java.util.function.Predicate;

public interface ILevel {
    // public int w, h; // Width and height of the level

	// public short[] tiles; // An array of all the tiles in the world.
	// public short[] data; // An array of the data of the tiles in the world.
	
	// public final int depth; // Depth level of the level
	// public int monsterDensity; // Affects the number of monsters that are on the level, bigger the number the less monsters spawn.
	// public int maxMobCount;
	// public int chestCount;
	// public int mobCount;
    public IEntity[] getEntitiesToSave();
	
	/// This is a solely debug method I made, to make printing repetitive stuff easier.
	// Should be changed to accept prepend and entity, or a tile (as an Object). It will get the coordinates and class name from the object, and will divide coords by 16 if passed an entity.
	public void printLevelLoc(String prefix, int x, int y);
	public void printLevelLoc(String prefix, int x, int y, String suffix);
	
	public void printTileLocs(ITile t);
	public void printEntityLocs(Class<? extends IEntity> c);
	
	public long getSeed();

	public void checkAirWizard();
	
	public void checkChestCount();

	public void tick(boolean fullTick);
	
	public void printEntityStatus(String entityMessage, IEntity entity, String... searching);
	
	public void dropItem(int x, int y, int mincount, int maxcount, IItem... items);
	public void dropItem(int x, int y, int count, IItem... items);
	public void dropItem(int x, int y, IItem... items);
	public IItemEntity dropItem(int x, int y, IItem i);

	public void renderBackground(IScreen screen, int xScroll, int yScroll);
	
	public void renderSprites(IScreen screen, int xScroll, int yScroll);

	public void renderLight(IScreen screen, int xScroll, int yScroll, int brightness);
	
	public ITile getTile(int x, int y);
	
	public void setTile(int x, int y, String tilewithdata);
	
	public void setTile(int x, int y, ITile t);
	
	public void setTile(int x, int y, ITile t, int dataVal);
	
	public int getData(int x, int y);
	
	public void setData(int x, int y, int val);
	
	public void add(IEntity e);
	public void add(IEntity entity, int x, int y);
	public void add(IEntity entity, int x, int y, boolean tileCoords);
	
	public void remove(IEntity e);
	
	public void removeAllEnemies();
	
	public void clearEntities();
	
	public IEntity[] getEntityArray();
	
	public List<IEntity> getEntitiesInTiles(int xt, int yt, int radius);
	
	public List<IEntity> getEntitiesInTiles(int xt, int yt, int radius, boolean includeGiven, Class<? extends IEntity>... entityClasses);

	/**
	 * Get entities in a certain area on the level.
	 * @param xt0 Left
	 * @param yt0 Top
	 * @param xt1 Right
	 * @param yt1 Bottom
	 */
	public List<IEntity> getEntitiesInTiles(int xt0, int yt0, int xt1, int yt1);

	/**
	 * Get entities in a certain area on the level, and filter them by class.
	 * @param xt0 Left
	 * @param yt0 Top
	 * @param xt1 Right
	 * @param yt1 Bottom
	 * @param includeGiven If we should accept entities that match the provided entityClasses. If false, we ignore the provided entityClasses.
	 * @param entityClasses Entities to accept.
	 * @return A list of entities in the area.
	 */
	public List<IEntity> getEntitiesInTiles(int xt0, int yt0, int xt1, int yt1, boolean includeGiven, Class<? extends IEntity>... entityClasses);
	
	public List<IEntity> getEntitiesInRect(IRectangle area);

	public List<IEntity> getEntitiesInRect(Predicate<IEntity> filter, IRectangle area);
	
	/// Finds all entities that are an instance of the given entity.
	public IEntity[] getEntitiesOfClass(Class<? extends IEntity> targetClass);
	
	public IPlayer[] getPlayers();
	
	public IPlayer getClosestPlayer(int x, int y);
	
	public IPoint[] getAreaTilePositions(int x, int y, int r);
	public IPoint[] getAreaTilePositions(int x, int y, int rx, int ry);
	
	public ITile[] getAreaTiles(int x, int y, int r);
	public ITile[] getAreaTiles(int x, int y, int rx, int ry);
	
	public void setAreaTiles(int xt, int yt, int r, ITile tile, int data);
	public void setAreaTiles(int xt, int yt, int r, ITile tile, int data, boolean overwriteStairs);

	public void setAreaTiles(int xt, int yt, int r, ITile tile, int data, String[] blacklist);
	
	@FunctionalInterface
	public interface TileCheck {
		boolean check(ITile t, int x, int y);
	}
	
	public List<IPoint> getMatchingTiles(ITile search);
	public List<IPoint> getMatchingTiles(ITile... search);
	public List<IPoint> getMatchingTiles(TileCheck condition);
	
	public boolean isLight(int x, int y);	

}
