package minicraft.level.tile;

import minicraft.core.Game;
import minicraft.entity.Arrow;
import minicraft.entity.mob.AirWizard;
import minicraft.entity.mob.Player;
import minicraft.gfx.Sprite;
import minicraftmodsapiinterface.*;

public class InfiniteFallTile extends Tile {
	
	protected InfiniteFallTile(String name) {
		super(name, (Sprite)null);
	}

	public void render(IScreen screen, ILevel level, int x, int y) {}

	public boolean tick(ILevel level, int xt, int yt) { return false; }

	public boolean mayPass(ILevel level, int x, int y, IEntity e) {
		return e instanceof AirWizard || e instanceof Arrow || e instanceof Player && ( ((Player) e).suitOn || Game.isMode("creative") );
	}
}
