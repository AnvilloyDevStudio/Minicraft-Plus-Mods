package minicraft.level.tile;

import minicraft.entity.mob.Mob;
import minicraft.gfx.ConnectorSprite;
import minicraft.gfx.Sprite;
import minicraftmodsapiinterface.*;

/// This class is for tiles WHILE THEY ARE EXPLODING
public class ExplodedTile extends Tile {
	private static ConnectorSprite sprite = new ConnectorSprite(ExplodedTile.class, new Sprite(6, 22, 3, 3, 1, 3), new Sprite(9, 22, 2, 2, 1))
	{
		public boolean connectsTo(ITile tile, boolean isSide) {
			return !isSide || tile.connectsToLiquid();
		}
	};
	
	protected ExplodedTile(String name) {
		super(name, sprite);
		Connections.set("sand", true);
		Connections.set("fluid", true);
	}
	
	public void steppedOn(ILevel level, int x, int y, IEntity entity) {
		if (entity instanceof Mob)
			((Mob)entity).hurt(this, x, y, 50);
	}
	
	public boolean mayPass(ILevel level, int x, int y, IEntity e) {
		return true;
	}
}
