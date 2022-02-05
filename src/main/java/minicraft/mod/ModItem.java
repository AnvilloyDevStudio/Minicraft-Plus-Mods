// package minicraft.mod;

// import java.util.ArrayList;
// import minicraft.core.Mods;
// import minicraft.item.*;
// import minicraft.core.io.Localization;
// import minicraft.entity.Direction;
// import minicraft.entity.mob.Player;
// import minicraft.gfx.Font;
// import minicraft.gfx.Screen;
// import minicraft.gfx.Sprite;
// import minicraft.level.Level;
// import minicraft.level.tile.Tile;

// public abstract class ModItem {
//     private static final ArrayList<ModItem> ModItems;
//     static {
//         ArrayList<Item> items = Mods.Items;
//         ArrayList<ModItem> moditems = new ArrayList<>();
//         for (Item item : items) {
//             switch (item.itemtype) {
//                 case "tool":
//                     moditems.add(new ToolItem(item.type));
//             }
//         }
//     }

//     public String ItemType;
// 	private final String name;
// 	public Sprite sprite;
// 	public String itemtype;
	
// 	public boolean used_pending = false; // This is for multiplayer, when an item has been used, and is pending server response as to the outcome, this is set to true so it cannot be used again unless the server responds that the item wasn't used. Which should basically replace the item anyway, soo... yeah. this never gets set back.
	
// 	protected ModItem(String name) {
// 		sprite = Sprite.missingTexture(1, 1);
// 		this.name = name;
// 	}
// 	protected ModItem(String name, Sprite sprite) {
// 		this.name = name;
// 		this.sprite = sprite;
// 	}

// 	/** Renders an item on the HUD */
// 	public void renderHUD(Screen screen, int x, int y, int fontColor) {
// 		String dispName = getDisplayName();
// 		sprite.render(screen, x, y);
// 		Font.drawBackground(dispName, screen, x + 8, y, fontColor);
// 	}
	
// 	/** Determines what happens when the player interacts with a tile */
// 	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, Direction attackDir) {
// 		return false;
// 	}
	
// 	/** Returning true causes this item to be removed from the player's active item slot */
// 	public boolean isDepleted() {
// 		return false;
// 	}
	
// 	/** Returns if the item can attack mobs or not */
// 	public boolean canAttack() {
// 		return false;
// 	}

// 	/** Sees if an item equals another item */
// 	public boolean equals(ModItem item) {
// 		return item != null && item.getClass().equals(getClass()) && item.name.equals(name);
// 	}
	
// 	@Override
// 	public int hashCode() { return name.hashCode(); }
	
// 	/** This returns a copy of this item, in all necessary detail. */
// 	public abstract ModItem clone();
	
// 	@Override
// 	public String toString() {
// 		return name + "-Item";
// 	}
	
// 	/** Gets the necessary data to send over a connection. This data should always be directly input-able into Items.get() to create a valid item with the given properties. */
// 	public String getData() {
// 		return name;
// 	}

// 	public String getType() {
// 		return itemtype;
// 	}
	
// 	public final String getName() { return name; }
	
// 	// Returns the String that should be used to display this item in a menu or list. 
// 	public String getDisplayName() {
// 		return " " + Localization.getLocalized(getName());
// 	}
	
// 	public boolean interactsWithWorld() { return true; }
// }
