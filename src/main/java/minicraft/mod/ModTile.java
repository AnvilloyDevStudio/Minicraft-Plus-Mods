package minicraft.mod;

import minicraft.core.Mods;
import minicraft.level.tile.Tiles;

public class ModTile {
    public static void init() {}
    static {
        for (Mods.Mod.Tile tile : Mods.Tiles) {
            switch (tile.tiletype) {
                case "Ore":
                    Tiles.add(tile.toOreTile());
                    break;
                case "Plant":
                    Tiles.add(tile.toPlant());
                    break;
                default:
                    Tiles.add(tile.toTile());
            }
        }
    }
}
