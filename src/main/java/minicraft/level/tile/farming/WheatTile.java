package minicraft.level.tile.farming;

import minicraft.level.tile.Tiles;
import minicraftmodsapiinterface.*;

public class WheatTile extends Plant {

	public WheatTile(String name) {
		super(name);
	}

	@Override
	public void render(IScreen screen, ILevel level, int x, int y) {
		int age = level.getData(x, y);
		int icon = age / (maxAge / 5);

		Tiles.get("Farmland").render(screen, level, x, y);

		screen.render(x * 16 + 0, y * 16 + 0, 13 + 0 * 32 + icon, 0, 1);
		screen.render(x * 16 + 8, y * 16 + 0, 13 + 0 * 32 + icon, 0, 1);
		screen.render(x * 16 + 0, y * 16 + 8, 13 + 0 * 32 + icon, 1, 1);
		screen.render(x * 16 + 8, y * 16 + 8, 13 + 0 * 32 + icon, 1, 1);
	}
}
