package minicraft.level.tile.farming;

import minicraft.core.io.Sound;
import minicraft.entity.ItemEntity;
import minicraft.gfx.Sprite;
import minicraft.item.ToolItem;
import minicraft.level.tile.Tile;
import minicraft.level.tile.Tiles;
import minicraftmodsapiinterface.*;

public class FarmTile extends Tile {
    private static Sprite sprite = new Sprite(12, 0, 2, 2, 1, true, new int[][] {{1, 0}, {0, 1}});

    public FarmTile(String name) {
        super(name, sprite);
    }
    protected FarmTile(String name, Sprite sprite) {
        super(name, sprite);
    }

    @Override
    public boolean interact(ILevel level, int xt, int yt, IPlayer player, IItem item, IDirection attackDir) {
        if (item instanceof ToolItem) {
            ToolItem tool = (ToolItem) item;
            if (tool.type.name.equals("shovel")) {
                if (player.payStamina(4 - (tool.level.level-1)) && tool.payDurability()) {
                    level.setTile(xt, yt, Tiles.get("Dirt"));
                    Sound.monsterHurt.play();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean tick(ILevel level, int xt, int yt) {
        int age = level.getData(xt, yt);
        if (age < 5) level.setData(xt, yt, age + 1);
        return true;
    }

    @Override
    public void steppedOn(ILevel level, int xt, int yt, IEntity entity) {
        if (entity instanceof ItemEntity) return;
        if (random.nextInt(60) != 0) return;
        if (level.getData(xt, yt) < 5) return;
        level.setTile(xt, yt, Tiles.get("Dirt"));
    }
}
