package minicraft.level.tile;

import minicraft.core.io.Sound;
import minicraft.gfx.Sprite;
import minicraft.item.Items;
import minicraft.item.ToolItem;
import minicraftmodsapiinterface.*;

public class PathTile extends Tile {
    private static Sprite sprite = new Sprite(14, 4, 2, 2, 1);

    public PathTile(String name) {
        super(name, sprite);
        Connections.set("grass", true);
        maySpawn = true;
    }

    public boolean interact(ILevel level, int xt, int yt, IPlayer player, IItem item, IDirection attackDir) {
        if (item instanceof ToolItem) {
            ToolItem tool = (ToolItem) item;
            if (tool.type.name.equals("shovel")){
                if (player.payStamina(4 - (tool.level.level-1)) && tool.payDurability()) {
                    level.setTile(xt, yt, Tiles.get("Hole"));
                    Sound.monsterHurt.play();
                    level.dropItem(xt * 16 + 8, yt * 16 + 8, Items.get("Dirt"));
                    return true;
                }
            }
        }
        return false;
    }
}
