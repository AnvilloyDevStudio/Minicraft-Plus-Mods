package minicraft.mod;

import java.util.HashMap;

import minicraft.core.Game;
import minicraft.core.io.Sound;
import minicraft.entity.particle.Particle;
import minicraft.item.Items;
import minicraft.level.tile.Tiles;
import minicraft.screen.Display;
import minicraftmodsapiinterface.*;

public class GameAssets extends IGameAssets {
    public class Tiles extends IGameAssets.Tiles {
        @Override
        public ITile get(String tile) {
            return minicraft.level.tile.Tiles.get(tile);
        }
    }
    public Tiles Tiles = new Tiles();
    public class Items extends IGameAssets.Items {
        @Override
        public IItem get(String item) {
            return minicraft.item.Items.get(item);
        }
    }
    public Items Items = new Items();
    public class Game extends IGameAssets.Game {
        @Override
        public void setMenu(IDisplay display) {
            minicraft.core.Game.setMenu((Display)display);
        }
        @Override
        public boolean isMode(String mode) {
            return Game.isMode(mode);
        }
    }
    public Game Game = new Game();
    public HashMap<String, Class<? extends Particle>> particles = Particle.Particles;
    public Class<Sound> sound = Sound.class;
}
