package minicraft.level.tile;

import minicraft.core.Game;
import minicraft.core.io.Sound;
import minicraft.entity.Entity;
import minicraft.entity.particle.SmashParticle;
import minicraft.entity.particle.TextParticle;
import minicraft.gfx.Color;
import minicraft.gfx.ConnectorSprite;
import minicraft.gfx.Screen;
import minicraft.item.Items;
import minicraft.item.ToolItem;
import minicraftmodsapiinterface.IDirection;
import minicraftmodsapiinterface.IItem;
import minicraftmodsapiinterface.ILevel;
import minicraftmodsapiinterface.IMob;
import minicraftmodsapiinterface.IPlayer;

public class TreeTile extends Tile {
	
	protected TreeTile(String name) {
		super(name, (ConnectorSprite)null);
		Connections.set("grass", true);
	}
	
	public void render(Screen screen, ILevel level, int x, int y) {
		Tiles.get("Grass").render(screen, level, x, y);
		
		boolean u = level.getTile(x, y - 1) == this;
		boolean l = level.getTile(x - 1, y) == this;
		boolean r = level.getTile(x + 1, y) == this;
		boolean d = level.getTile(x, y + 1) == this;
		boolean ul = level.getTile(x - 1, y - 1) == this;
		boolean ur = level.getTile(x + 1, y - 1) == this;
		boolean dl = level.getTile(x - 1, y + 1) == this;
		boolean dr = level.getTile(x + 1, y + 1) == this;

		if (u && ul && l) {
			screen.render(x * 16 + 0, y * 16 + 0, 1 + 1 * 32, 0, 1);
		} else {
			screen.render(x * 16 + 0, y * 16 + 0, 0 + 0 * 32, 0, 1);
		}
		if (u && ur && r) {
			screen.render(x * 16 + 8, y * 16 + 0, 1 + 2 * 32, 0, 1);
		} else {
			screen.render(x * 16 + 8, y * 16 + 0, 1 + 0 * 32, 0, 1);
		}
		if (d && dl && l) {
			screen.render(x * 16 + 0, y * 16 + 8, 1 + 2 * 32, 0, 1);
		} else {
			screen.render(x * 16 + 0, y * 16 + 8, 0 + 1 * 32, 0, 1);
		}
		if (d && dr && r) {
			screen.render(x * 16 + 8, y * 16 + 8, 1 + 1 * 32, 0, 1);
		} else {
			screen.render(x * 16 + 8, y * 16 + 8, 1 + 3 * 32, 0, 1);
		}
	}

	public boolean tick(ILevel level, int xt, int yt) {
		int damage = level.getData(xt, yt);
		if (damage > 0) {
			level.setData(xt, yt, damage - 1);
			return true;
		}
		return false;
	}

	public boolean mayPass(ILevel level, int x, int y, Entity e) {
		return false;
	}
	
	@Override
	public boolean hurt(ILevel level, int x, int y, IMob source, int dmg, IDirection attackDir) {
		hurt(level, x, y, dmg);
		return true;
	}
	
	@Override
	public boolean interact(ILevel level, int xt, int yt, IPlayer player, IItem item, IDirection attackDir) {
		if(Game.isMode("Creative"))
			return false; // Go directly to hurt method
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type.name.equals("axe")) {
				if (player.payStamina(4 - (tool.level.level-1)) && tool.payDurability()) {
					hurt(level, xt, yt, random.nextInt(10) + (tool.level.level-1) * 5 + 10);
					return true;
				}
			}
		}
		return false;
	}

	public void hurt(ILevel level, int x, int y, int dmg) {
		if (random.nextInt(100) == 0)
			level.dropItem(x * 16 + 8, y * 16 + 8, Items.get("Apple"));
		
		int damage = level.getData(x, y) + dmg;
		int treeHealth = 20;
		if (Game.isMode("Creative")) dmg = damage = treeHealth;
		
		level.add(new SmashParticle(x*16, y*16));
		Sound.monsterHurt.play();

		level.add(new TextParticle("" + dmg, x * 16 + 8, y * 16 + 8, Color.RED));
		if (damage >= treeHealth) {
			level.dropItem(x * 16 + 8, y * 16 + 8, 1, 3, Items.get("Wood"));
			level.dropItem(x * 16 +  8, y * 16 + 8, 0, 2, Items.get("Acorn"));
			level.setTile(x, y, Tiles.get("Grass"));
		} else {
			level.setData(x, y, damage);
		}
	}
}
