package minicraft.level.tile;

import minicraft.gfx.ConnectorSprite;
import minicraft.gfx.Sprite;
import minicraft.level.Level;
import minicraftmodsapiinterface.*;

public class HoleTile extends Tile {
	private static ConnectorSprite sprite = new ConnectorSprite(HoleTile.class, new Sprite(24, 6, 3, 3, 1, 3), new Sprite(27, 6, 2, 2, 1))
	{
		public boolean connectsTo(ITile tile, boolean isSide) {
			return tile.connectsToLiquid();
		}
	};
	
	protected HoleTile(String name) {
		super(name, sprite);
		Connections.set("sand", true);
		Connections.set("fluid", true);
	}
	
	public void render(IScreen screen, ILevel level, int x, int y) {
		sprite.sparse.color = DirtTile.dCol(((Level)level).depth);
		sprite.render(screen, level, x, y);
	}

	public boolean mayPass(ILevel level, int x, int y, IEntity e) {
		return e.canSwim();
	}
}
