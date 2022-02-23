package minicraft.level.tile;

import minicraft.core.io.Sound;
import minicraft.gfx.ConnectorSprite;
import minicraft.gfx.Sprite;
import minicraft.item.Items;
import minicraft.item.ToolItem;
import minicraftmodsapiinterface.*;

public class CloudTile extends Tile {
	private static ConnectorSprite sprite = new ConnectorSprite(CloudTile.class, new Sprite(0, 22, 3, 3, 1, 3), new Sprite(3, 24, 2, 2, 1, 3), new Sprite(3, 22, 2, 2, 1))
	{
		public boolean connectsTo(ITile tile, boolean isSide) {
			return tile != Tiles.get("Infinite Fall");
		}
	};
	
	protected CloudTile(String name) {
		super(name, sprite);
	}

	public boolean mayPass(ILevel level, int x, int y, IEntity e) {
		return true;
	}

	public boolean interact(ILevel level, int xt, int yt, IPlayer player, IItem item, IDirection attackDir) {
		// We don't want the tile to break when attacked with just anything, even in creative mode
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type.name.equals("shovel") && player.payStamina(5)) {
				level.setTile(xt, yt, Tiles.get("Infinite Fall")); // Would allow you to shovel cloud, I think.
				Sound.monsterHurt.play();
				level.dropItem(xt * 16 + 8, yt * 16 + 8, 1, 3, Items.get("Cloud"));
				return true;
			}
		}
		return false;
	}
}
