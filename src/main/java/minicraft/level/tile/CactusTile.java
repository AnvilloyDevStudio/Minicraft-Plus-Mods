package minicraft.level.tile;

import minicraft.core.Game;
import minicraft.core.io.Settings;
import minicraft.core.io.Sound;
import minicraft.entity.mob.Mob;
import minicraft.entity.particle.SmashParticle;
import minicraft.entity.particle.TextParticle;
import minicraft.gfx.Color;
import minicraft.gfx.Sprite;
import minicraft.item.Items;
import minicraftmodsapiinterface.*;

public class CactusTile extends Tile {
	private static Sprite sprite = new Sprite(6, 0, 2, 2, 1);
	
	protected CactusTile(String name) {
		super(name, sprite);
		Connections.set("sand", true);
	}

	public boolean mayPass(ILevel level, int x, int y, IEntity e) {
		return false;
	}

	public boolean hurt(ILevel level, int x, int y, IMob source, int dmg, IDirection attackDir) {
		int damage = level.getData(x, y) + dmg;
		int cHealth = 10;
		if (Game.isMode("creative")) dmg = damage = cHealth;
		level.add(new SmashParticle(x * 16, y * 16));
		level.add(new TextParticle("" + dmg, x * 16 + 8, y * 16 + 8, Color.RED));
		
		if (damage >= cHealth) {
			//int count = random.nextInt(2) + 2;
			level.setTile(x, y, Tiles.get("sand"));
			Sound.monsterHurt.play();
			level.dropItem(x * 16 + 8, y * 16 + 8, 2, 4, Items.get("Cactus"));
		} else {
			level.setData(x, y, damage);
		}
		return true;
	}

	@Override
	public void render(IScreen screen, ILevel level, int x, int y) {
		Tiles.get("Sand").render(screen, level, x, y);

		sprite.render(screen, x << 4, y << 4);
	}

	public void bumpedInto(ILevel level, int x, int y, IEntity entity) {
		if(!(entity instanceof Mob)) return;
		Mob m = (Mob) entity;
		if (Settings.get("diff").equals("Easy")) {
			m.hurt(this, x, y, 1);
		}
		if (Settings.get("diff").equals("Normal")) {
			m.hurt(this, x, y, 1);
		}
		if (Settings.get("diff").equals("Hard")) {
			m.hurt(this, x, y, 2);
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
}
