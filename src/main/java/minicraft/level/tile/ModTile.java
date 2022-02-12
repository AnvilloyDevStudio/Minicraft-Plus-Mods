package minicraft.level.tile;

import minicraft.gfx.Sprite;
import minicraft.level.Level;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import minicraft.entity.Entity;

public class ModTile extends Tile {
    public static ArrayList<Tile> Instances = new ArrayList<>();
    private ModTileOption options;

    public ModTile(String name, Sprite sprite) {
        super(name, sprite);
        Instances.add(this);
    }
    public ModTile(String name, Sprite sprite, ModTileOption options) {
        super(name, sprite);
        Instances.add(this);
        this.options = options;
    }
    public static class ModTileOption {
        public boolean mayPass;
        public Method mayPassMethod;
    }
    @Override
    public boolean mayPass(Level level, int x, int y, Entity e) {
		try {
            return options.mayPassMethod!=null? (boolean) options.mayPassMethod.invoke(this, level, x, y, e): options.mayPass? true: false;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
            e1.printStackTrace();
            return false;
        }
	}
}
