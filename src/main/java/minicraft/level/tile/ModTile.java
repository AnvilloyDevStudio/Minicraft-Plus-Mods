package minicraft.level.tile;

import minicraft.gfx.Sprite;
import minicraft.level.Level;

import java.util.ArrayList;

import minicraft.entity.Entity;

public class ModTile extends Tile {
    public static ArrayList<Tile> Instances = new ArrayList<>();

    public ModTile(String name, Sprite sprite) {
        super(name, sprite);
        Instances.add(this);
    }
    public boolean mayPass(Level level, int x, int y, Entity e) {
		return true;
	}
}
