package minicraft.mod;

import java.util.ArrayList;
import java.util.List;

import minicraft.core.Mods;
import minicraft.item.*;
import minicraft.level.tile.Tiles;


public class ModLoadAssets /*extends Item*/ {
    public static void init() {}
    static {
        for (Mods.Mod.Item item : Mods.Items) {
            switch (item.itemtype) {
                case "Tool":
                    for (int a = 0; a<item.itype.length; a++) Items.add(item.toToolItem(true, a));
                    break;
                case "Stackable":
                    Items.add(item.toStackableItem());
                    break;
                case "Furniture":
                    Items.add(item.toFurnitureItem());
                    break;
                case "Food":
                    Items.add(item.toFoodItem());
                    break;
                case "Bucket":
                    Items.add(item.toBucketItem());
                    break;
                case "Armor":
                    for (int a = 0; a<item.itype.length; a++) Items.add(item.toArmorItem(a));
            }
        }
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
        for (Mods.Mod.Tile tile : Mods.Tiles) {
            switch (tile.tiletype) {
                case "Ore":
                    Tiles.add(tile.id, tile.toOreTile());
                    break;
                case "Plant":
                    Tiles.add(tile.id, tile.toPlant());
                    break;
                default:
                    Tiles.add(tile.id, tile.toTile());
            }
        }
    }
}
