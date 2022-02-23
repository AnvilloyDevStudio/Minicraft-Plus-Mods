package minicraft.level.tile;

import minicraft.gfx.ConnectorSprite;
import minicraft.gfx.Sprite;
import minicraft.level.Level;
import minicraftmodsapiinterface.*;

public class LavaTile extends Tile {
	private ConnectorSprite sprite = new ConnectorSprite(LavaTile.class, new Sprite(12, 9, 3, 3, 1, 3), Sprite.dots(0))
	{
		public boolean connectsTo(ITile tile, boolean isSide) {
			return ((Tile)tile).Connections.get("fluid");
		}
	};
	
	protected LavaTile(String name) {
		super(name, (ConnectorSprite)null);
		super.csprite = sprite;
		Connections.set("sand", true);
		Connections.set("fluid", true);
	}
	
	public void render(IScreen screen, ILevel level, int x, int y) {
		long seed = (tickCount + (x / 2 - y) * 4311) / 10 * 54687121l + x * 3271612l + y * 3412987161l;
		sprite.full = Sprite.randomDots(seed, 1);
		sprite.sparse.color = DirtTile.dCol(((Level)level).depth);
		sprite.render(screen, level, x, y);
	}
	
	public boolean mayPass(ILevel level, int x, int y, IEntity e) {
		return e.canSwim();
	}

	public boolean tick(ILevel level, int xt, int yt) {
		int xn = xt;
		int yn = yt;

		if (random.nextBoolean()) xn += random.nextInt(2) * 2 - 1;
		else yn += random.nextInt(2) * 2 - 1;

		if (level.getTile(xn, yn) == Tiles.get("hole")) {
			level.setTile(xn, yn, this);
		}
		return false;
	}

	public int getLightRadius(ILevel level, int x, int y) {
		return 6;
	}
}
