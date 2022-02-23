package minicraft.level.tile;

import minicraft.core.io.Sound;
import minicraft.gfx.ConnectorSprite;
import minicraft.gfx.Sprite;
import minicraft.item.Items;
import minicraft.item.ToolItem;
import minicraftmodsapiinterface.*;

public class FlowerTile extends Tile {
	private static final Sprite flowerSprite = new Sprite(3, 8, 1);
	
	protected FlowerTile(String name) {
		super(name, (ConnectorSprite)null);
		Connections.set("grass", true);
		maySpawn = true;
	}

	public boolean tick(ILevel level, int xt, int yt) {
		// TODO revise this method.
		if (random.nextInt(30) != 0) return false; // Skips every 31 tick.

		int xn = xt;
		int yn = yt;

		if (random.nextBoolean()) xn += random.nextInt(2) * 2 - 1;
		else yn += random.nextInt(2) * 2 - 1;

		if (level.getTile(xn, yn) == Tiles.get("Dirt")) {
			level.setTile(xn, yn, Tiles.get("Grass"));
		}
		return false;
	}
	
	public void render(IScreen screen, ILevel level, int x, int y) {
		Tiles.get("Grass").render(screen, level, x, y);
		
		int data = level.getData(x, y);
		int shape = (data / 16) % 2;
		
		x = x << 4;
		y = y << 4;
		
		flowerSprite.render(screen, x + 8 * shape, y);
		flowerSprite.render(screen, x + 8 * (shape == 0 ? 1 : 0), y + 8);
	}

	public boolean interact(ILevel level, int x, int y, IPlayer player, IItem item, IDirection attackDir) {
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type.name.equals("shovel")) {
				if (player.payStamina(2 - (tool.level.level-1)) && tool.payDurability()) {
					level.setTile(x, y, Tiles.get("Grass"));
					Sound.monsterHurt.play();
					level.dropItem(x * 16 + 8, y * 16 + 8, Items.get("Flower"));
					level.dropItem(x * 16 + 8, y * 16 + 8, Items.get("Rose"));
					return true;
				}
			}
		}
		return false;
	}

	public boolean hurt(ILevel level, int x, int y, IMob source, int dmg, IDirection attackDir) {
		level.dropItem(x *16 + 8, y * 16 + 8, 0, 1, Items.get("Flower"));
		level.dropItem(x *16 + 8, y * 16 + 8, 0, 1, Items.get("Rose"));
		level.setTile(x, y, Tiles.get("Grass"));
		return true;
	}
}
