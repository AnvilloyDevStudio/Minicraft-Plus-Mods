package minicraft.level.tile;

import minicraft.core.io.Sound;
import minicraft.gfx.ConnectorSprite;
import minicraft.gfx.Sprite;
import minicraft.item.Items;
import minicraft.item.ToolItem;
import minicraft.level.Level;
import minicraftmodsapiinterface.*;

public class GrassTile extends Tile {
	private static ConnectorSprite sprite = new ConnectorSprite(GrassTile.class, new Sprite(0, 6, 3, 3, 1, 3), new Sprite(3, 6, 2, 2, 1))
	{
		public boolean connectsTo(ITile tile, boolean isSide) {
			if(!isSide) return true;
			return ((Tile)tile).Connections.get("grass");
		}
	};
	
	protected GrassTile(String name) {
		super(name, sprite);
		csprite.sides = csprite.sparse;
		Connections.set("grass", true);
		maySpawn = true;
	}

	public boolean tick(ILevel level, int xt, int yt) {
		// TODO revise this method.
		if (random.nextInt(40) != 0) return false;
		
		int xn = xt;
		int yn = yt;
		
		if (random.nextBoolean()) xn += random.nextInt(2) * 2 - 1;
		else yn += random.nextInt(2) * 2 - 1;

		if (level.getTile(xn, yn) == Tiles.get("Dirt")) {
			level.setTile(xn, yn, this);
		}
		return false;
	}

	@Override
	public void render(IScreen screen, ILevel level, int x, int y) {
		sprite.sparse.color = DirtTile.dCol(((Level)level).depth);
		sprite.render(screen, level, x, y);
	}

	public boolean interact(ILevel level, int xt, int yt, IPlayer player, IItem item, IDirection attackDir) {
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type.name.equals("shovel")) {
				if (player.payStamina(4 - (tool.level.level-1)) && tool.payDurability()) {
					level.setTile(xt, yt, Tiles.get("Dirt"));
					Sound.monsterHurt.play();
					if (random.nextInt(5) == 0) { // 20% chance to drop Grass seeds
						level.dropItem(xt * 16 + 8, yt * 16 + 8, 1, Items.get("Grass Seeds"));
					}
					return true;
				}
			}
			if (tool.type.name.equals("hoe")) {
				if (player.payStamina(4 - (tool.level.level-1)) && tool.payDurability()) {
					level.setTile(xt, yt, Tiles.get("Dirt"));
					Sound.monsterHurt.play();
					if (random.nextInt(5) != 0) { // 80% chance to drop Wheat seeds
						level.dropItem(xt * 16 + 8, yt * 16 + 8, Items.get("Wheat Seeds"));
					}
					return true;
				}
			}
			if (tool.type.name.equals("pickaxe")) {
				if (player.payStamina(4 - (tool.level.level-1)) && tool.payDurability()) {
					level.setTile(xt, yt, Tiles.get("Path"));
					Sound.monsterHurt.play();
				}
			}
		}
		return false;
	}
}
