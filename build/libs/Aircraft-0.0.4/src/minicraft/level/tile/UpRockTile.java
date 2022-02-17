package minicraft.level.tile;

import minicraft.core.Game;
import minicraft.core.io.Settings;
import minicraft.core.io.Sound;
import minicraft.entity.Direction;
import minicraft.entity.Entity;
import minicraft.entity.mob.Mob;
import minicraft.entity.mob.Player;
import minicraft.entity.particle.SmashParticle;
import minicraft.entity.particle.TextParticle;
import minicraft.gfx.Color;
import minicraft.gfx.ConnectorSprite;
import minicraft.gfx.Screen;
import minicraft.gfx.Sprite;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.item.ToolItem;
import minicraft.item.ToolType;
import minicraft.level.Level;

public class UpRockTile extends Tile {
    private ConnectorSprite sprite = new ConnectorSprite(UpRockTile.class, new Sprite(58, 6, 3, 3, 1, 3),
            new Sprite(61, 8, 2, 2, 1, 3), new Sprite(61, 6, 2, 2, 1, 3)) {
	   
       
       public boolean connectsTo(Tile tile, boolean isSide) {
           return tile != Tiles.get("rock") && tile  != Tiles.get("dirt") && tile != Tiles.get("grass") && 
            	   tile != Tiles.get("sand") && tile != Tiles.get("Orange tulip") &&
            	   tile != Tiles.get("tree") && tile != Tiles.get("birch tree") &&
            	   tile != Tiles.get("Stairs Down") && tile != Tiles.get("Stairs up") &&
            	   tile != Tiles.get("lava") && tile != Tiles.get("water") && 
            	   tile != Tiles.get("cactus") && tile != Tiles.get("flower") &&
            	   tile != Tiles.get("Hole") && tile != Tiles.get("Snow") &&
            	   tile != Tiles.get("Lawn") && tile != Tiles.get("path") && 
            	   tile != Tiles.get("Birch tree") && tile != Tiles.get("Fir tree") && 
            	   tile != Tiles.get("Wood wall") && tile != Tiles.get("path") && 
            	   tile != Tiles.get("ice spike") && tile != Tiles.get("Carrot") ;
       }
	   };

	    private boolean dropCoal = false;
	    private final int maxHealth = 50;

	    private int damage;

	    protected UpRockTile(String name) {
	        super(name, (ConnectorSprite) null);
	        csprite = sprite;
	    }

	    @Override
	    public void render(Screen screen, Level level, int x, int y) {
	    	
	        Tiles.get("rock").render(screen, level, x, y);
	        sprite.render(screen, level, x, y);
	    }

	    @Override
	    public boolean mayPass(Level level, int x, int y, Entity e) {
	        return false;
	    }

	    @Override
	    public boolean hurt(Level level, int x, int y, Mob source, int dmg, Direction attackDir) {
	        hurt(level, x, y, dmg);
	        return true;
	    }

	    @Override
	    public boolean interact(Level level, int xt, int yt, Player player, Item item, Direction attackDir) {

	        // creative mode can just act like survival here
	        if (item instanceof ToolItem) {
	            ToolItem tool = (ToolItem) item;
	            if (tool.type == ToolType.Pickaxe && player.payStamina(4 - tool.level) && tool.payDurability()) {

	                // Drop coal since we use a pickaxe.
	                dropCoal = true;
	                hurt(level, xt, yt, random.nextInt(10) + (tool.level) * 5 + 10);
	                return true;
	            }
	        }
	        return false;
	    }

	    @Override
	    public void hurt(Level level, int x, int y, int dmg) {
	        damage = level.getData(x, y) + dmg;

	        if (Game.isMode("creative")) {
	            dmg = damage = maxHealth;
	            dropCoal = true;
	        }

	        level.add(new SmashParticle(x * 16, y * 16));
	        Sound.Tile_generic_hurt.play();

	        level.add(new TextParticle("" + dmg, x * 16 + 8, y * 16 + 8, Color.RED));
	        if (damage >= maxHealth) {

	            if (dropCoal) {
	                level.dropItem(x * 16 + 8, y * 16 + 8, 1, 3, Items.get("Stone"));
	                int coal = 0;

	                if (!Settings.get("diff").equals("Hard")) {
	                    coal++;
	                }

	                level.dropItem(x * 16 + 8, y * 16 + 8, coal, coal + 1, Items.get("Coal"));

	            } else {
	                level.dropItem(x * 16 + 8, y * 16 + 8, 2, 4, Items.get("Stone"));

	            }

	            level.setTile(x, y, Tiles.get("Rock"));

	        } else {
	            level.setData(x, y, damage);

	        }
	    }

	    @Override
	    public boolean tick(Level level, int xt, int yt) {
	        damage = level.getData(xt, yt);
	        if (damage > 0) {
	            level.setData(xt, yt, damage - 1);
	            return true;
	        }
	        return false;
	    }
	}