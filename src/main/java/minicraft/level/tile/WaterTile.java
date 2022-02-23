package minicraft.level.tile;

import minicraft.gfx.ConnectorSprite;
import minicraft.gfx.Sprite;
import minicraft.level.Level;
import minicraftmodsapiinterface.*;

public class WaterTile extends Tile {
	private ConnectorSprite sprite = new ConnectorSprite(WaterTile.class, new Sprite(12, 6, 3, 3, 1, 3), Sprite.dots(/*Color.get(005, 105, 115, 115)*/ 0))
	{
		public boolean connectsTo(ITile tile, boolean isSide) {
			return ((Tile)tile).Connections.get("fluid");
		}
	};
	
	protected WaterTile(String name) {
		super(name, (ConnectorSprite)null);
		csprite = sprite;
		Connections.set("fluid", true);
	}
	
	public void render(IScreen screen, ILevel level, int x, int y) {
		long seed = (tickCount + (x / 2 - y) * 4311) / 10 * 54687121l + x * 3271612l + y * 3412987161l;
		sprite.full = Sprite.randomDots(seed, 0);
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

		if (level.getTile(xn, yn) == Tiles.get("Hole")) {
			level.setTile(xn, yn, this);
		}
		
		// These set only the non-diagonally adjacent lava tiles to obsidian
		for (int x = -1; x < 2; x++) {
			if (level.getTile(xt + x, yt) == Tiles.get("Lava"))
				level.setTile(xt + x, yt, Tiles.get("Obsidian"));
		}
		for (int y = -1; y < 2; y++) {
			if (level.getTile(xt, yt + y) == Tiles.get("lava"))
				level.setTile(xt, yt + y, Tiles.get("Obsidian"));
		}
		return false;
	}
}
