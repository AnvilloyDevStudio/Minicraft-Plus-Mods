package minicraft.level.tile;

import minicraft.core.io.Sound;
import minicraft.entity.mob.Mob;
import minicraft.gfx.ConnectorSprite;
import minicraft.gfx.Sprite;
import minicraft.item.Items;
import minicraft.item.ToolItem;
import minicraft.level.Level;
import minicraftmodsapiinterface.*;

public class SandTile extends Tile {
	static Sprite steppedOn, normal = new Sprite(9, 6, 2, 2, 1);
	static {
		Sprite.Px[][] pixels = new Sprite.Px[2][2];
		pixels[0][0] = new Sprite.Px(9, 8, 0, 1);
		pixels[0][1] = new Sprite.Px(10, 6, 0, 1);
		pixels[1][0] = new Sprite.Px(9, 7, 0, 1);
		pixels[1][1] = new Sprite.Px(9, 8, 0, 1);
		steppedOn = new Sprite(pixels);
	}
	
	private ConnectorSprite sprite = new ConnectorSprite(SandTile.class, new Sprite(6, 6, 3, 3, 1, 3), normal)
	{
		public boolean connectsTo(ITile tile, boolean isSide) {
			if(!isSide) return true;
			return ((Tile)tile).Connections.get("sand");
		}
	};
	
	protected SandTile(String name) {
		super(name, (ConnectorSprite)null);
		csprite = sprite;
		Connections.set("sand", true);
		maySpawn = true;
	}
	
	public void render(IScreen screen, ILevel level, int x, int y) {
		boolean steppedOn = level.getData(x, y) > 0;
		
		if(steppedOn) csprite.full = SandTile.steppedOn;
		else csprite.full = SandTile.normal;

		csprite.sparse.color = DirtTile.dCol(((Level)level).depth);
		
		csprite.render(screen, level, x, y);
	}

	public boolean tick(ILevel level, int x, int y) {
		int damage = level.getData(x, y);
		if (damage > 0) {
			level.setData(x, y, damage - 1);
			return true;
		}
		return false;
	}

	public void steppedOn(ILevel level, int x, int y, IEntity entity) {
		if (entity instanceof Mob) {
			level.setData(x, y, 10);
		}
	}

	public boolean interact(ILevel level, int xt, int yt, IPlayer player, IItem item, IDirection attackDir) {
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type.name.equals("shovel")) {
				if (player.payStamina(4 - (tool.level.level-1)) && tool.payDurability()) {
					level.setTile(xt, yt, Tiles.get("Hole"));
					Sound.monsterHurt.play();
					level.dropItem(xt * 16 + 8, yt * 16 + 8, Items.get("Sand"));
					return true;
				}
			}
		}
		return false;
	}
}
