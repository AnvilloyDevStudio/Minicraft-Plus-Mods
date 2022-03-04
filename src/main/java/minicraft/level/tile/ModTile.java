package minicraft.level.tile;

import minicraft.gfx.Sprite;
import minicraft.mod.GameAssets;
import minicraftmodsapiinterface.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

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
        public Class<?> Optionclass;
    }
    @Override
    public boolean mayPass(ILevel level, int x, int y, IEntity e) {
		try {
            return options.mayPassMethod!=null? (boolean) options.mayPassMethod.invoke(options.Optionclass, level, x, y, e): options.mayPass? true: false;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
            e1.printStackTrace();
            return false;
        }
	}
    
    public void render(IScreen screen, ILevel level, int x, int y) {
        if (options.render!=null)
            try {
                options.render.invoke(options.Optionclass, screen, level, x, y, this.sprite, new GameAssets());
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        else sprite.render(screen, x*16, y*16);
	}

	public boolean tick(ILevel level, int xt, int yt) {
		try {
            return options.tick!=null? (boolean) options.tick.invoke(options.Optionclass, level, xt, yt, new GameAssets()): false;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
	}
	
	@Override
	public boolean hurt(ILevel level, int x, int y, IMob source, int dmg, IDirection attackDir) {
		try {
            return options.hurt!=null? (boolean) options.hurt.invoke(options.Optionclass, level, x, y, source, dmg, attackDir, new GameAssets()): false;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
	}
	
	@Override
	public boolean interact(ILevel level, int xt, int yt, IPlayer player, IItem item, IDirection attackDir) {
		try {
            return options.interact!=null? (boolean) options.interact.invoke(options.Optionclass, level, xt, yt, player, item, attackDir): false;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
	}
}
