package minicraft.mod;

import java.util.ArrayList;
import java.util.List;

import minicraft.core.Mods;
import minicraft.item.*;


public class ModItem /*extends Item*/ {
    public static void init() {}
    private static final ArrayList<Item> ModItems;
    public static ArrayList<ToolType> ToolTypes;
    static {
        ArrayList<Mods.Mod.Item> items = Mods.Items;
        ArrayList<Item> moditems = new ArrayList<>();
        for (Mods.Mod.Item item : items) {
            switch (item.itemtype) {
                case "Tool":
                    moditems.add(item.toToolItem());
                    break;
                case "Stackable":
                    moditems.add(item.toStackableItem());
                    break;
                case "Furniture":
                    moditems.add(item.toFurnitureItem());
                    break;
                case "Food":
                    moditems.add(item.toFoodItem());
                    break;
                case "Bucket":
                    moditems.add(item.toBucketItem());
                case "Armor":
                    moditems.add(item.toArmorItem());
            }
        }
        ModItems = moditems;
        for (Mods.Mod.Recipe recipe : Mods.Recipes) {
            switch (recipe.type) {
                case "workbench":
                    Recipes.workbenchRecipes.add(recipe.toRecipe());
                    break;
                case "craft":
                    Recipes.craftRecipes.add(recipe.toRecipe());
                    break;
                case "anvil":
                    Recipes.anvilRecipes.add(recipe.toRecipe());
                    break;
                case "enchant":
                    Recipes.enchantRecipes.add(recipe.toRecipe());
                    break;
                case "furnace":
                    Recipes.furnaceRecipes.add(recipe.toRecipe());
                    break;
                case "loom":
                    Recipes.loomRecipes.add(recipe.toRecipe());
                    break;
                case "oven":
                    Recipes.ovenRecipes.add(recipe.toRecipe());
                    break;
                default:
                    if (!Recipes.modRecipes.containsKey(recipe.type)) Recipes.modRecipes.put(recipe.type, new ArrayList<>(List.of(recipe.toRecipe())));
                    else Recipes.modRecipes.get(recipe.type).add(recipe.toRecipe());
            }
        }
        ModTile.init();
    }
    public static class ModItemLevel {
        public int level;
        public String name;
        ModItemLevel(String name, int level) {
            this.name = name;
            this.level = level;
        }
    }
    public static ArrayList<Item> getAllInstances() {
        return ModItems;
    }
}
