package minicraftmodsapiinterface;

import java.util.HashMap;

public abstract class IGameAssets {
    public abstract class Tiles {
        public abstract ITile get(String name);
    }
    public Tiles Tiles;
    public abstract class Items {
        public abstract IItem get(String name);
    }
    public Items Items;
    public abstract class Game {
        public abstract void setMenu(IDisplay display);
        public abstract boolean isMode(String mode);
    }
    public Game Game;
    public HashMap<String, Class<?>> particles;
    public Class<?> sound;
}
