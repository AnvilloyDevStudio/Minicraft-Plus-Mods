package minicraft.item;

public enum ToolType {
    Hoe(1, 64), // if there's a second number, it specifies durability.
    Shovel(0, 60), Sword(2, 68), Spear(7, 64), Pickaxe(3, 65), Axe(4, 65), Bow(5, 55), Claymore(6, 90),
    Shear(1, 42, true), Igniter(3, 16, true);

    public final int xPos; // X Position of origin
    public final int yPos; // Y position of origin
    public final int durability;
    public final boolean noLevel;

    /**
     * Create a tool with four levels: wood, stone, iron, gold, and gem. All these
     * levels are added automatically but sprites have to be added manually. Uses
     * line 14 in the item spritesheet.
     * 
     * @param xPos X position of the starting sprite in the spritesheet.
     * @param dur  Durability of the tool.
     */
    ToolType(int xPos, int dur) {
        this.xPos = xPos;
        yPos = 13;
        durability = dur;
        noLevel = false;
    }

    /**
     * Create a tool without a specified level. Uses line 13 in the items
     * spritesheet.
     * 
     * @param xPos    X position of the sprite in the spritesheet.
     * @param dur     Durabiltity of the tool.
     * @param noLevel If the tool has only one level.
     */
    ToolType(int xPos, int dur, boolean noLevel) {
        yPos = 12;
        this.xPos = xPos;
        durability = dur;
        this.noLevel = noLevel;
    }
}
