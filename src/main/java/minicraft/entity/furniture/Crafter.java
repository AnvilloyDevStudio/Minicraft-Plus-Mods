package minicraft.entity.furniture;

import java.util.ArrayList;
import java.util.HashMap;

import minicraft.core.Game;
import minicraft.entity.mob.Player;
import minicraft.gfx.Sprite;
import minicraft.item.Recipe;
import minicraft.item.Recipes;
import minicraft.screen.CraftingDisplay;

public class Crafter extends Furniture {
	
	public static class Type {
		public static ArrayList<Type> Instances = new ArrayList<>();
		public static HashMap<String, Type> Types = new HashMap<>();

		public ArrayList<Recipe> recipes;
		protected Sprite sprite;
		protected int xr, yr;
		public String name;
		
		static {
			new Type("Workbench", new Sprite(16, 26, 2, 2, 2), 3, 2, Recipes.workbenchRecipes);
			new Type("Oven", new Sprite(12, 26, 2, 2, 2), 3, 2, Recipes.ovenRecipes);
			new Type("Furnace", new Sprite(14, 26, 2, 2, 2), 3, 2, Recipes.furnaceRecipes);
			new Type("Anvil", new Sprite(8, 26, 2, 2, 2), 3, 2, Recipes.anvilRecipes);
			new Type("Enchanter", new Sprite(24, 26, 2, 2, 2), 7, 2, Recipes.enchantRecipes);
			new Type("Loom", new Sprite(26, 26, 2, 2, 2), 7, 2, Recipes.loomRecipes);
		}
		
		public Type(String name, Sprite sprite, int xr, int yr, ArrayList<Recipe> list) {
			this.name = name;
			this.sprite = sprite;
			this.xr = xr;
			this.yr = yr;
			recipes = list;
			Instances.add(this);
			Types.put(this.name, this);
			Crafter.names.add(this.name);
		}
	}
	public static ArrayList<String> names = new ArrayList<>();
	
	public Crafter.Type type;
	
	/**
	 * Creates a crafter of a given type.
	 * @param type What type of crafter this is.
	 */
	public Crafter(Crafter.Type type) {
		super(type.name, type.sprite, type.xr, type.yr);
		this.type = type;
	}
	
	public boolean use(Player player) {
		Game.setMenu(new CraftingDisplay(type.recipes, type.name, player));
		return true;
	}
	
	@Override
	public Furniture clone() {
		return new Crafter(type);
	}
	
	@Override
	public String toString() { return type.name+getDataPrints(); }
}
