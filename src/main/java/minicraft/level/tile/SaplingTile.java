package minicraft.level.tile;

import minicraft.core.io.Sound;
import minicraft.gfx.Sprite;
import minicraftmodsapiinterface.*;

public class SaplingTile extends Tile {
	private static Sprite sprite = new Sprite(12, 1, 1);
	
	private Tile onType;
	private Tile growsTo;
	
	protected SaplingTile(String name, Tile onType, Tile growsTo) {
		super(name, sprite);
		this.onType = onType;
		this.growsTo = growsTo;
		Connections.set("sand", onType.Connections.get("sand"));
		Connections.set("grass", onType.Connections.get("grass"));
		Connections.set("fluid", onType.Connections.get("fluid"));
		maySpawn = true;
	}

	public void render(IScreen screen, ILevel level, int x, int y) {
		onType.render(screen, level, x, y);
		
		sprite.render(screen, x * 16, y * 16);
	}

	public boolean tick(ILevel level, int x, int y) {
		int age = level.getData(x, y) + 1;
		if (age > 100) {
			level.setTile(x, y, growsTo);
		} else {
			level.setData(x, y, age);
		}
		return true;
	}

	public boolean hurt(ILevel level, int x, int y, IMob source, int dmg, IDirection attackDir) {
		level.setTile(x, y, onType);
		Sound.monsterHurt.play();
		return true;
	}
}
