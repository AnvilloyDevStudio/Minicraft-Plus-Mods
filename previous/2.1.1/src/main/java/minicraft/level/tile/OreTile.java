package minicraft.level.tile;

import java.util.HashMap;

import minicraft.core.Game;
import minicraft.core.io.Sound;
import minicraft.entity.Direction;
import minicraft.entity.Entity;
import minicraft.entity.mob.Mob;
import minicraft.entity.mob.Player;
import minicraft.entity.particle.SmashParticle;
import minicraft.entity.particle.TextParticle;
import minicraft.gfx.Color;
import minicraft.gfx.Screen;
import minicraft.gfx.Sprite;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.item.ToolItem;
import minicraft.item.ToolType;
import minicraft.level.Level;
import minicraft.screen.AchievementsDisplay;

/// this is all the spikey stuff (except "cloud cactus")
public class OreTile extends Tile {
	private final OreType type;

	public static class OreType {
		public static HashMap<String, OreType> types = new HashMap<>();

		static {
			types.put("Iron", new OreType("Iron", Items.get("Iron Ore"), 0));
			types.put("Lapis", new OreType("Lapis", Items.get("Lapis"), 2));
			types.put("Gold", new OreType("Gold", Items.get("Gold Ore"), 4));
			types.put("Gem", new OreType("Gem", Items.get("Gem"), 6));
			types.put("Cloud", new OreType("Cloud", Items.get("Cloud Ore"), 8));
		}

		private final Item drop;
		public final int color;
		public final String name;
		public final Sprite sprite;

		OreType(String name, Item drop, int color) {
			this.name = name;
			this.drop = drop;
			this.color = color;
			sprite = null;
		}
		public OreType(String name, Item drop, Sprite color) {
			this.name = name;
			this.drop = drop;
			this.color = 0;
			sprite = color;
		}

		private Item getOre() {
			return drop.clone();
		}
    }

	public OreTile(OreType o) {
		super((o.name.equals("Lapis") ? "Lapis" : o.name.equals("Cloud") ? "Cloud Cactus" : o.name + " Ore"), o.sprite == null ? new Sprite(22 + o.color, 2, 2, 2, 1) : o.sprite);
        this.type = o;
	}

	public void render(Screen screen, Level level, int x, int y) {
		sprite.color = DirtTile.dCol(level.depth);
		sprite.render(screen, x * 16, y * 16);
	}

	public boolean mayPass(Level level, int x, int y, Entity e) {
		return false;
	}

	public boolean hurt(Level level, int x, int y, Mob source, int dmg, Direction attackDir) {
		hurt(level, x, y, 0);
		return true;
	}

	public boolean interact(Level level, int xt, int yt, Player player, Item item, Direction attackDir) {
		if(Game.isMode("Creative"))
			return false; // Go directly to hurt method
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type.name.equals("pickaxe")) {
				if (player.payStamina(6 - tool.level) && tool.payDurability()) {
					hurt(level, xt, yt, tool.getDamage());
					return true;
				}
			}
		}
		return false;
	}

    public Item getOre() {
        return type.getOre();
    }

	public void hurt(Level level, int x, int y, int dmg) {
		int damage = level.getData(x, y) + dmg;
		int oreH = random.nextInt(10) * 4 + 20;
		if (Game.isMode("Creative")) dmg = damage = oreH;

		level.add(new SmashParticle(x * 16, y * 16));
		Sound.monsterHurt.play();

		level.add(new TextParticle("" + dmg, x * 16 + 8, y * 16 + 8, Color.RED));
		if (dmg > 0) {
			int count = random.nextInt(2);
			if (damage >= oreH) {
				if (type.name.equals("Cloud")) {
					level.setTile(x, y, Tiles.get("Cloud"));
				} else {
					level.setTile(x, y, Tiles.get("Dirt"));
				}
				count += 2;
			} else {
				level.setData(x, y, damage);
			}
			if (type.drop.equals(Items.get("gem"))){
				AchievementsDisplay.setAchievement("minicraft.achievement.find_gem", true);
			}
			level.dropItem(x * 16 + 8, y * 16 + 8, count, type.getOre());
		}
	}

	public void bumpedInto(Level level, int x, int y, Entity entity) {
		/// this was used at one point to hurt the player if they touched the ore; that's probably why the sprite is so spikey-looking.
	}
}
