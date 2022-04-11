package minicraft.level.tile;

import java.util.ArrayList;
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
import minicraft.item.TileItem;
import minicraft.item.ToolItem;
import minicraft.level.Level;

/// this is all the spikey stuff (except "cloud cactus")
public class OreTile extends Tile {
	private Sprite sprite;
	private OreType type;
	public static ArrayList<OreTile> Instances = new ArrayList<>();
	
	public static class OreType {
		public static ArrayList<OreType> Instances = new ArrayList<>();
		public static HashMap<String, OreType> OreTypes = new HashMap<>();

		static {
			new OreType("Iron", Items.get("Iron Ore"), 0);
			new OreType("Lapis", Items.get("Lapis"), 2);
			new OreType("Gold", Items.get("Gold Ore"), 4);
			new OreType("Gem", Items.get("Gem"), 6);
		}
		
		private Item drop;
		public int color;
		public String name;
		public Sprite sprite;
		
		OreType(String name, Item drop, int color) {
			this.name = name;
			this.drop = drop;
			this.color = color;
			sprite = null;
			Instances.add(this);
			OreTypes.put(name, this);
		}
		public OreType(String name, Item drop, Sprite sprite) {
			this.name = name;
			this.drop = drop;
			if (sprite == null) sprite = new Sprite(0, 30, 1);
			this.sprite = sprite;
			Instances.add(this);
			OreTypes.put(name, this);
		}
		
		protected Item getOre() {
			return (Item)drop.clone();
		}
    }
	
	public OreTile(OreType o) {
		super((o == OreTile.OreType.OreTypes.get("Lapis") ? "Lapis" : o.name + " Ore"), o.sprite==null? new Sprite(24 + o.color, 0, 2, 2, 1): o.sprite);
        this.type = o;
		this.sprite = super.sprite;
		Instances.add(this);
		Items.add(new TileItem(this.name+" OreTile", new Sprite(0, 31, 0), this.name, "rock", "dirt", "sand", "grass", "path"));
	}

	public void render(Screen screen, Level level, int x, int y) {
		sprite.color = DirtTile.dCol(((Level)level).depth);
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
				if (player.payStamina(6 - (tool.level.level-1)) && tool.payDurability()) {
					hurt(level, xt, yt, 1);
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
		int damage = level.getData(x, y) + 1;
		int oreH = random.nextInt(10) + 3;
		if (Game.isMode("Creative")) dmg = damage = oreH;
		
		level.add(new SmashParticle(x * 16, y * 16));
		Sound.monsterHurt.play();

		level.add(new TextParticle("" + dmg, x * 16 + 8, y * 16 + 8, Color.RED));
		if (dmg > 0) {
			int count = random.nextInt(2) + 0;
			if (damage >= oreH) {
				level.setTile(x, y, Tiles.get("Dirt"));
				count += 2;
			} else {
				level.setData(x, y, damage);
			}
			level.dropItem(x * 16 + 8, y * 16 + 8, count, type.getOre());
		}
	}

	public void bumpedInto(Level level, int x, int y, Entity entity) {
		/// this was used at one point to hurt the player if they touched the ore; that's probably why the sprite is so spikey-looking.
	}
}
