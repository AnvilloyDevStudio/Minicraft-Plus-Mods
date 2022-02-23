package minicraft.level.tile;

import minicraft.core.io.Sound;
import minicraft.entity.mob.Mob;
import minicraft.gfx.Sprite;
import minicraft.item.ToolItem;
import minicraftmodsapiinterface.*;

public class LavaBrickTile extends Tile {
	private static Sprite sprite = new Sprite(19, 2, 2, 2, 1);
	
	protected LavaBrickTile(String name) {
		super(name, sprite);
	}
	
	public boolean interact(ILevel level, int xt, int yt, IPlayer player, IItem item, IDirection attackDir) {
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type.name.equals("pickaxe")) {
				if (player.payStamina(4 - (tool.level.level-1)) && tool.payDurability()) {
					level.setTile(xt, yt, Tiles.get("Lava"));
					Sound.monsterHurt.play();
					return true;
				}
			}
		}
		return false;
	}

	public void bumpedInto(ILevel level, int x, int y, IEntity entity) {
		if(entity instanceof Mob)
			((Mob)entity).hurt(this, x, y, 3);
	}

	public boolean mayPass(ILevel level, int x, int y, IEntity e) { return e.canWool(); }
}
