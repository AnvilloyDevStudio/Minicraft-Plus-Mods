package minicraft.level.tile;

import minicraft.gfx.Screen;
import minicraft.gfx.Sprite;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.level.Level;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import minicraft.core.Game;
import minicraft.core.io.Sound;
import minicraft.entity.Direction;
import minicraft.entity.Entity;
import minicraft.entity.mob.Mob;
import minicraft.entity.mob.Player;
import minicraft.entity.particle.*;

public class ModTile extends Tile {
    public static ArrayList<Tile> Instances = new ArrayList<>();
    private ModTileOption options;
    private static class ExtraAssets {
        public static Class<Tiles> tiles = Tiles.class;
        public static Class<Items> items = Items.class;
        public static Class<Game> game = Game.class;
        public static HashMap<String, Class<? extends Particle>> particles = Particle.Particles;
        public static Class<Sound> sound = Sound.class;
    }

    public ModTile(String name, Sprite sprite) {
        super(name, sprite);
        Instances.add(this);
    }
    public ModTile(String name, Sprite sprite, ModTileOption options) {
        super(name, sprite);
        Instances.add(this);
        this.options = options;
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
                options.render.invoke(this, screen, level, x, y, this.sprite, ExtraAssets.class);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        else sprite.render(screen, x*16, y*16);
	}

	public boolean tick(Level level, int xt, int yt) {
		try {
            return options.tick!=null? (boolean) options.tick.invoke(this, level, xt, yt, ExtraAssets.class): false;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
	}
	
	@Override
	public boolean hurt(Level level, int x, int y, Mob source, int dmg, Direction attackDir) {
		try {
            return options.hurt!=null? (boolean) options.hurt.invoke(this, level, x, y, source, dmg, attackDir, ExtraAssets.class): false;
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
