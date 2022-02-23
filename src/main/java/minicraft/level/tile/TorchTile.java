package minicraft.level.tile;

import minicraft.core.io.Sound;
import minicraft.gfx.Sprite;
import minicraft.item.Items;
import minicraft.item.PowerGloveItem;
import minicraftmodsapiinterface.*;

public class TorchTile extends Tile {
	private static Sprite sprite = new Sprite(11, 3, 0);
	
	private Tile onType;
	
	public static TorchTile getTorchTile(ITile onTile) {
		int id = ((Tile)onTile).id & 0xFFFF;
		if(id < 32768) id += 32768;
		else System.out.println("Tried to place torch on torch tile...");
		
		if(Tiles.containsTile(id))
			return (TorchTile)Tiles.get(id);
		else {
			TorchTile tile = new TorchTile((Tile)onTile);
			Tiles.add(id, tile);
			return tile;
		}
	}
	
	private TorchTile(Tile onType) {
		super("Torch "+ onType.name, sprite);
		this.onType = onType;
		Connections.set("sand", onType.Connections.get("sand"));
		Connections.set("grass", onType.Connections.get("grass"));
		Connections.set("fluid", onType.Connections.get("fluid"));
	}
	
	public void render(IScreen screen, ILevel level, int x, int y) {
		onType.render(screen, level, x, y);
		sprite.render(screen, x * 16 + 4, y * 16 + 4);
	}
	
	public int getLightRadius(ILevel level, int x, int y) {
		return 5;
	}
	
	public boolean interact(ILevel level, int xt, int yt, IPlayer player, IItem item, IDirection attackDir) {
		if(item instanceof PowerGloveItem) {
			level.setTile(xt, yt, this.onType);
			Sound.monsterHurt.play();
			level.dropItem(xt*16+8, yt*16+8, Items.get("Torch"));
			return true;
		} else {
			return false;
		}
	}
}
