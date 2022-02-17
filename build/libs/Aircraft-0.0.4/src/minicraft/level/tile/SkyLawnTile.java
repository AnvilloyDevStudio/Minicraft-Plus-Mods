package minicraft.level.tile;

import minicraft.core.io.Sound;
import minicraft.entity.Direction;
import minicraft.entity.mob.Mob;
import minicraft.entity.mob.Player;
import minicraft.gfx.ConnectorSprite;
import minicraft.gfx.Screen;
import minicraft.gfx.Sprite;
import minicraft.item.Item;
import minicraft.item.Items;
import minicraft.item.ToolItem;
import minicraft.item.ToolType;
import minicraft.level.Level;

public class SkyLawnTile extends Tile {
    private static Sprite sprite = new Sprite(48, 8, 1);

    protected SkyLawnTile(String name) {
        super(name, (ConnectorSprite) null);
        connectsToSkyGrass = true;
        maySpawn = true;
    }

    @Override
    public boolean tick(Level level, int xt, int yt) {

        if (random.nextInt(30) != 0)
            return false;

        int xn = xt;
        int yn = yt;

        if (random.nextBoolean())
            xn += random.nextInt(2) * 2 - 1;
        else
            yn += random.nextInt(2) * 2 - 1;

        if (level.getTile(xn, yn) == Tiles.get("Sky grass")) {
            level.setTile(xn, yn, this);
        }
        return false;
    }

    @Override
    public void render(Screen screen, Level level, int x, int y) {
        Tiles.get("Sky grass").render(screen, level, x, y);

        int data = level.getData(x, y);
        int shape = (data / 16) % 2;

        x = x << 4;
        y = y << 4;

        sprite.render(screen, x + 8 * shape, y);
        sprite.render(screen, x + 8 * (shape == 0 ? 1 : 0), y + 8);
    }

    @Override
    public boolean interact(Level level, int x, int y, Player player, Item item, Direction attackDir) {
        if (item instanceof ToolItem) {
            ToolItem tool = (ToolItem) item;
            if (tool.type == ToolType.Shovel) {
                if (player.payStamina(2 - tool.level) && tool.payDurability()) {
                    level.setTile(x, y, Tiles.get("Sky grass"));
                    Sound.Tile_generic_hurt.play();

                    if (random.nextInt(20) == 1) { // 20% chance to drop sky seeds
                        level.dropItem(x * 16 + 8, y * 16 + 8, Items.get("Sky Seeds"));
                    }

                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean hurt(Level level, int x, int y, Mob source, int dmg, Direction attackDir) {
        if (random.nextInt(12) == 1) { // 20% chance to drop sky seeds
            level.dropItem(x * 16 + 8, y * 16 + 8, Items.get("Sky Seeds"));
        }
        level.setTile(x, y, Tiles.get("Sky grass"));
        return true;
    }
}
