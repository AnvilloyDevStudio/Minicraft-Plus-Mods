package minicraft.level.tile;

import minicraft.gfx.Screen;
import minicraft.gfx.Sprite;
import minicraft.item.Item;
import minicraft.level.Level;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import minicraft.entity.Direction;
import minicraft.entity.Entity;
import minicraft.entity.mob.Mob;
import minicraft.entity.mob.Player;

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
        System.out.println(options.Connections);
        if (options.Connections!=null) for (Entry<String, Boolean> set : options.Connections.entrySet()) Connections.set(set.getKey(), set.getValue());
    }
    public static class ModTileOption {
        public boolean mayPass;
        public Method mayPassMethod;
        public Map<String, Boolean> Connections;
        public Method render;
        public Method tick;
        public Method hurt;
        public Method interact;
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
    
    public void render(Screen screen, Level level, int x, int y) {
		if (options.render!=null)
            try {
                options.render.invoke(this, screen, level, x, y);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
	}

	public boolean tick(Level level, int xt, int yt) {
		try {
            return options.tick!=null? (boolean) options.tick.invoke(this, level, xt, yt): false;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
	}
	
	@Override
	public boolean hurt(Level level, int x, int y, Mob source, int dmg, Direction attackDir) {
		try {
            return options.hurt!=null? (boolean) options.hurt.invoke(this, level, x, y, source, dmg, attackDir): false;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
	}
	
	@Override
	public boolean interact(Level level, int xt, int yt, Player player, Item item, Direction attackDir) {
		try {
            return options.interact!=null? (boolean) options.interact.invoke(this, level, xt, yt, player, item, attackDir): false;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
	}
}
