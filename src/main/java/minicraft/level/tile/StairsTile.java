package minicraft.level.tile;

import minicraft.core.Game;
import minicraft.core.io.Sound;
import minicraft.gfx.Sprite;
import minicraft.item.PowerGloveItem;
import minicraft.level.Level;
import minicraftmodsapiinterface.*;

public class StairsTile extends Tile {
	private static Sprite down = new Sprite(21, 0, 2, 2, 1, 0);
	private static Sprite up = new Sprite(19, 0, 2, 2, 1, 0);
	
	protected StairsTile(String name, boolean leadsUp) {
		super(name, leadsUp ? up : down);
		maySpawn = false;
	}
	
	@Override
	public void render(IScreen screen, ILevel level, int x, int y) {
		sprite.render(screen, x * 16, y * 16, 0, DirtTile.dCol(((Level)level).depth));
	}

	@Override
	public boolean interact(ILevel level, int xt, int yt, IPlayer player, IItem item, IDirection attackDir) {
		super.interact(level, xt, yt, player, item, attackDir);

		// Makes it so you can remove the stairs if you are in creative and debug mode.
		if (item instanceof PowerGloveItem && Game.isMode("Creative") && Game.debug) {
			level.setTile(xt, yt, Tiles.get("Grass"));
			Sound.monsterHurt.play();
			return true;
		} else {
			return false;
		}
	}
}
