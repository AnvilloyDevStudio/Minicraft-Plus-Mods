package minicraft.level.tile.farming;

import minicraft.entity.Entity;
import minicraft.entity.ItemEntity;
import minicraft.entity.mob.Player;
import minicraft.gfx.Screen;
import minicraft.item.Items;
import minicraft.level.Level;
import minicraft.level.tile.Tile;
import minicraft.level.tile.Tiles;

public class SkyWartTile extends SkyPlant {

    public SkyWartTile(String name) {
        super(name);
    }

    @Override
    public void render(Screen screen, Level level, int x, int y) {
        int age = level.getData(x, y);
        int icon = age / (maxAge / 5);

        Tiles.get("Sky Farmland").render(screen, level, x, y);

        screen.render(x * 16 + 0, y * 16 + 0, 13 + 3 * 32 + icon, 0, 1);
        screen.render(x * 16 + 8, y * 16 + 0, 13 + 3 * 32 + icon, 0, 1);
        screen.render(x * 16 + 0, y * 16 + 8, 13 + 3 * 32 + icon, 1, 1);
        screen.render(x * 16 + 8, y * 16 + 8, 13 + 3 * 32 + icon, 1, 1);
    }

    @Override
    protected boolean IfCloud(Level level, int xs, int ys) {
        Tile[] areaTiles = level.getAreaTiles(xs, ys, 3);
        for (Tile t : areaTiles)
            if (t == Tiles.get("Sky grass"))
                return true;

        return false;
    }

    @Override
    protected void harvest(Level level, int x, int y, Entity entity) {
        if (entity instanceof ItemEntity)
            return;
        int age = level.getData(x, y);

        // level.dropItem(x*16+8, y*16+8, 1, 2, Items.get("seeds"));

        int count = 0;
        if (age >= maxAge) {
            count = random.nextInt(3) + 2;
        } else if (age >= maxAge - maxAge / 5) {
            count = random.nextInt(2);
        }

        level.dropItem(x * 16 + 8, y * 16 + 8, count, Items.get("Sky wart"));

        if (age >= maxAge && entity instanceof Player) {
            ((Player) entity).addScore(random.nextInt(4) + 1);
        }

        level.setTile(x, y, Tiles.get("Sky farmland"));
    }

}
