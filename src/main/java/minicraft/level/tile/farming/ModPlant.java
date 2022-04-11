package minicraft.level.tile.farming;

import minicraft.entity.Entity;
import minicraft.entity.mob.Player;
import minicraft.gfx.Screen;
import minicraft.gfx.SpriteSheet;
import minicraft.item.Items;
import minicraft.level.Level;
import minicraft.level.tile.Tiles;

public class ModPlant extends Plant {
    private String seed;
    private String harvestment;
    private int xPos;
    private int yPos;
    private SpriteSheet sheet;

    public ModPlant(String name, String seed, String harvestment, int xPos, int yPos, SpriteSheet sheet) {
        super(name);
        this.seed = seed;
        this.harvestment = harvestment;
        this.xPos = xPos;
        this.yPos = yPos;
        this.sheet = sheet;
    }

    @Override
    public void render(Screen screen, Level level, int x, int y) {
        int age = level.getData(x, y);
        int icon = age / (maxAge / 5);

        Tiles.get("Farmland").render(screen, level, x, y);
        
        screen.render(x * 16 + 0, y * 16 + 0, this.xPos + this.yPos * 32 + icon, 0, (SpriteSheet)this.sheet, -1, false);
        screen.render(x * 16 + 8, y * 16 + 0, this.xPos + this.yPos * 32 + icon, 0, (SpriteSheet)this.sheet, -1, false);
        screen.render(x * 16 + 0, y * 16 + 8, this.xPos + this.yPos * 32 + icon, 1, (SpriteSheet)this.sheet, -1, false);
        screen.render(x * 16 + 8, y * 16 + 8, this.xPos + this.yPos * 32 + icon, 1, (SpriteSheet)this.sheet, -1, false);
    }

    @Override
    protected void harvest(Level level, int x, int y, Entity entity) {
        int age = level.getData(x, y);

        level.dropItem(x * 16 + 8, y * 16 + 8, 1, Items.get(seed));

        int count = 0;
        if (age >= maxAge) {
            count = random.nextInt(3) + 2;
        } else if (age >= maxAge - maxAge / 5) {
            count = random.nextInt(2) + 1;
        }

        level.dropItem(x * 16 + 8, y * 16 + 8, count, Items.get(harvestment));

        if (age >= maxAge && entity instanceof Player) {
            ((Player)entity).addScore(random.nextInt(5) + 1);
        }

        level.setTile(x, y, Tiles.get("Dirt"));
    }
}

