package minicraft.level.tile;

import minicraft.core.Game;
import minicraft.core.io.Sound;
import minicraft.entity.Direction;
import minicraft.entity.Entity;
import minicraft.entity.mob.Mob;
import minicraft.entity.mob.Player;
import minicraft.entity.mob.boss.AirWizard;
import minicraft.entity.particle.SmashParticle;
import minicraft.entity.particle.TextParticle;
import minicraft.gfx.Color;
import minicraft.gfx.ConnectorSprite;
import minicraft.gfx.Sprite;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.item.ToolItem;
import minicraft.level.Level;

public class WallTile extends Tile {

    private static final String obrickMsg = "The airwizard must be defeated first.";

    private ConnectorSprite sprite;

    protected Material type;

    protected WallTile(Material type) {
        super(type.name() + " Wall", (ConnectorSprite) null);

        connectsToSkyGrass = true;
        connectsToSkyHighGrass = true;
        connectsToSkyDirt = true;

        this.type = type;
        switch (type) {
        case Wood:
            sprite = new ConnectorSprite(WallTile.class, new Sprite(0, 14, 3, 3, 1, 3), new Sprite(3, 14, 2, 2, 1, 3),
                    new Sprite(1, 15, 2, 2, 1, 0, true));
            break;
        case Stone:
            sprite = new ConnectorSprite(WallTile.class, new Sprite(10, 14, 3, 3, 1, 3), new Sprite(13, 14, 2, 2, 1, 3),
                    new Sprite(11, 15, 2, 2, 1, 0, true));
            break;
        case Obsidian:
            sprite = new ConnectorSprite(WallTile.class, new Sprite(20, 14, 3, 3, 1, 3), new Sprite(23, 14, 2, 2, 1, 3),
                    new Sprite(21, 15, 2, 2, 1, 0, true));
            break;
        case Spruce:
            sprite = new ConnectorSprite(WallTile.class, new Sprite(30, 14, 3, 3, 1, 3), new Sprite(33, 14, 2, 2, 1, 3),
                    new Sprite(31, 15, 2, 2, 1, 0, true));
            break;
        case Birch:
            sprite = new ConnectorSprite(WallTile.class, new Sprite(40, 14, 3, 3, 1, 3), new Sprite(43, 14, 2, 2, 1, 3),
                    new Sprite(41, 15, 2, 2, 1, 0, true));
            break;
        case Holy:
            sprite = new ConnectorSprite(WallTile.class, new Sprite(50, 14, 3, 3, 1, 3), new Sprite(53, 14, 2, 2, 1, 3),
                    new Sprite(51, 15, 2, 2, 1, 0, true));
            break;
        }
        csprite = sprite;
    }

    @Override
    public boolean mayPass(Level level, int x, int y, Entity e) {
        return false;
    }

    @Override
    public boolean hurt(Level level, int x, int y, Mob source, int dmg, Direction attackDir) {
        if (Game.isMode("creative") || level.depth != -3 || type != Material.Obsidian || AirWizard.beaten) {
            hurt(level, x, y, random.nextInt(6) / 6 * dmg / 2);
            return true;
        } else {
            Game.notifications.add(obrickMsg);
            return false;
        }
    }

    @Override
    public boolean interact(Level level, int xt, int yt, Player player, Item item, Direction attackDir) {
        if (Game.isMode("creative"))
            return false; // go directly to hurt method
        if (item instanceof ToolItem) {
            ToolItem tool = (ToolItem) item;
            if (tool.type == type.getRequiredTool()) {
                if (level.depth != -3 || type != Material.Obsidian || AirWizard.beaten) {
                    if (player.payStamina(4 - tool.level) && tool.payDurability()) {
                        hurt(level, xt, yt, random.nextInt(10) + (tool.level) * 5 + 10);
                        return true;
                    }
                } else {
                    Game.notifications.add(obrickMsg);
                }
            }
        }
        return false;
    }

    @Override
    public void hurt(Level level, int x, int y, int dmg) {
        int damage = level.getData(x, y) + dmg;
        int sbwHealth = 100;
        if (Game.isMode("creative"))
            dmg = damage = sbwHealth;

        level.add(new SmashParticle(x * 16, y * 16));
        Sound.Tile_generic_hurt.play();

        level.add(new TextParticle("" + dmg, x * 16 + 8, y * 16 + 8, Color.RED));
        if (damage >= sbwHealth) {

            String itemName = "";
            String tilename = "";

            switch (type) {
            case Wood:
                itemName = "Plank";
                tilename = "Wood Planks";
                break;
            case Stone:
                itemName = "Stone Brick";
                tilename = "Stone Bricks";
                break;
            case Obsidian:
                itemName = "Obsidian Brick";
                tilename = "Obsidian";
                break;
            case Spruce:
                itemName = "Spruce Plank";
                tilename = "Spruce Planks";
                break;
            case Birch:
                itemName = "Birch Plank";
                tilename = "Birch Planks";
                break;
            case Holy:
                itemName = "Holy Brick";
                tilename = "Holy Bricks";
                break;
            }

            level.dropItem(x * 16 + 8, y * 16 + 8, 1, 6 - type.ordinal(), Items.get(itemName));
            level.setTile(x, y, Tiles.get(tilename));
        } else {
            level.setData(x, y, damage);
        }
    }

    @Override
    public boolean tick(Level level, int xt, int yt) {
        int damage = level.getData(xt, yt);
        if (damage > 0) {
            level.setData(xt, yt, damage - 1);
            return true;
        }
        return false;
    }

    @Override
    public String getName(int data) {
        return Material.values[data].name() + " Wall";
    }
}