package minicraft.level.tile;

import minicraft.core.Game;
import minicraft.core.io.Sound;
import minicraft.entity.particle.SmashParticle;
import minicraft.entity.particle.TextParticle;
import minicraft.gfx.Color;
import minicraft.gfx.ConnectorSprite;
import minicraft.gfx.Sprite;
import minicraft.item.Items;
import minicraft.item.ToolItem;
import minicraft.level.Level;
import minicraftmodsapiinterface.*;

public class HardRockTile extends Tile {
	// Theoretically the full sprite should never be used, so we can use a placeholder
	private static ConnectorSprite sprite = new ConnectorSprite(HardRockTile.class, new Sprite(18, 9, 3, 3, 1, 3), new Sprite(21, 10, 2, 2, 1, 3), Sprite.missingTexture(2, 2));
	
	protected HardRockTile(String name) {
		super(name, sprite);
	}
	
	public boolean mayPass(ILevel level, int x, int y, IEntity e) {
		return false;
	}

	public boolean hurt(ILevel level, int x, int y, IMob source, int dmg, IDirection attackDir) {
		hurt(level, x, y, 0);
		return true;
	}

	public boolean interact(ILevel level, int xt, int yt, IPlayer player, IItem item, IDirection attackDir) {
		if(Game.isMode("Creative"))
			return false; // Go directly to hurt method
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type.name.equals("pickaxe") && tool.level.level-1 == 4) {
				if (player.payStamina(4 - (tool.level.level-1)) && tool.payDurability()) {
					hurt(level, xt, yt, random.nextInt(10) + (tool.level.level-1) * 5 + 10);
					return true;
				}
			} else {
				Game.notifications.add("Gem Pickaxe Required.");
			}
		}
		return false;
	}

	public void hurt(ILevel level, int x, int y, int dmg) {
		int damage = level.getData(x, y) + dmg;
		int hrHealth = 200;
		if (Game.isMode("Creative")) dmg = damage = hrHealth;
		level.add(new SmashParticle(x * 16, y * 16));
		Sound.monsterHurt.play();

		level.add(new TextParticle("" + dmg, x * 16 + 8, y * 16 + 8, Color.RED));
		if (damage >= hrHealth) {
			level.setTile(x, y, Tiles.get("dirt"));
			level.dropItem(x * 16 + 8, y * 16 + 8, 1, 3, Items.get("Stone"));
			level.dropItem(x * 16 + 8, y * 16 + 8, 0, 1, Items.get("Coal"));
		} else {
			level.setData(x, y, damage);
		}
	}

	@Override
	public void render(IScreen screen, ILevel level, int x, int y) {
		sprite.sparse.color = DirtTile.dCol(((Level)level).depth);
		super.render(screen, level, x, y);
	}

	public boolean tick(ILevel level, int xt, int yt) {
		int damage = level.getData(xt, yt);
		if (damage > 0) {
			level.setData(xt, yt, damage - 1);
			return true;
		}
		return false;
	}
}
