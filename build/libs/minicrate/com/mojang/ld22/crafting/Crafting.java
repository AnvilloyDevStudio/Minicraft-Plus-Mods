// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.crafting;

import com.mojang.ld22.entity.WhiteOrb;
import com.mojang.ld22.entity.GreenOrb;
import com.mojang.ld22.entity.Orb;
import com.mojang.ld22.entity.Crock;
import com.mojang.ld22.entity.GemLantern;
import com.mojang.ld22.entity.Grill;
import com.mojang.ld22.entity.Kettle;
import com.mojang.ld22.entity.GoldAnvil;
import com.mojang.ld22.entity.GoldLantern;
import com.mojang.ld22.entity.IronLantern;
import com.mojang.ld22.item.ToolType;
import com.mojang.ld22.entity.Book;
import com.mojang.ld22.entity.Anvil;
import com.mojang.ld22.entity.Chest;
import com.mojang.ld22.entity.Workbench;
import com.mojang.ld22.entity.Press;
import com.mojang.ld22.entity.Furnace;
import com.mojang.ld22.entity.Oven;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.entity.Furniture;
import com.mojang.ld22.entity.Lantern;
import java.util.ArrayList;
import java.util.List;

public class Crafting
{
    public static final List<Recipe> goldAnvilRecipes;
    public static final List<Recipe> anvilRecipes;
    public static final List<Recipe> ovenRecipes;
    public static final List<Recipe> kettleRecipes;
    public static final List<Recipe> furnaceRecipes;
    public static final List<Recipe> workbenchRecipes;
    public static final List<Recipe> crockRecipes;
    public static final List<Recipe> pressRecipes;
    public static final List<Recipe> girlSales;
    public static final List<Recipe> grillRecipes;
    public static final List<Recipe> bobSales;
    
    static {
        goldAnvilRecipes = new ArrayList<Recipe>();
        anvilRecipes = new ArrayList<Recipe>();
        ovenRecipes = new ArrayList<Recipe>();
        kettleRecipes = new ArrayList<Recipe>();
        furnaceRecipes = new ArrayList<Recipe>();
        workbenchRecipes = new ArrayList<Recipe>();
        crockRecipes = new ArrayList<Recipe>();
        pressRecipes = new ArrayList<Recipe>();
        girlSales = new ArrayList<Recipe>();
        grillRecipes = new ArrayList<Recipe>();
        bobSales = new ArrayList<Recipe>();
        try {
            Crafting.workbenchRecipes.add(new FurnitureRecipe(Lantern.class).addCost(Resource.wood, 5).addCost(Resource.goop, 10).addCost(Resource.glass, 4));
            Crafting.workbenchRecipes.add(new FurnitureRecipe(Oven.class).addCost(Resource.stone, 15));
            Crafting.workbenchRecipes.add(new FurnitureRecipe(Furnace.class).addCost(Resource.stone, 20));
            Crafting.workbenchRecipes.add(new FurnitureRecipe(Press.class).addCost(Resource.wood, 15).addCost(Resource.ironIngot, 10));
            Crafting.workbenchRecipes.add(new FurnitureRecipe(Workbench.class).addCost(Resource.wood, 20));
            Crafting.workbenchRecipes.add(new FurnitureRecipe(Chest.class).addCost(Resource.wood, 20));
            Crafting.workbenchRecipes.add(new FurnitureRecipe(Anvil.class).addCost(Resource.ironIngot, 5));
            Crafting.workbenchRecipes.add(new FurnitureRecipe(Book.class).addCost(Resource.cloth, 5).addCost(Resource.goldIngot, 50).addCost(Resource.gem, 50));
            Crafting.workbenchRecipes.add(new ResourceRecipe(Resource.hurdle).addCost(Resource.wood, 5).addCost(Resource.silverOre, 1).addCost(Resource.stone, 2));
            Crafting.workbenchRecipes.add(new ResourceRecipe(Resource.stile).addCost(Resource.wood, 5).addCost(Resource.silverIngot, 1).addCost(Resource.goop, 5));
            Crafting.workbenchRecipes.add(new ToolRecipe(ToolType.sword, 0).addCost(Resource.wood, 5));
            Crafting.workbenchRecipes.add(new ToolRecipe(ToolType.axe, 0).addCost(Resource.wood, 5));
            Crafting.workbenchRecipes.add(new ToolRecipe(ToolType.hoe, 0).addCost(Resource.wood, 5));
            Crafting.workbenchRecipes.add(new ToolRecipe(ToolType.pickaxe, 0).addCost(Resource.wood, 5));
            Crafting.workbenchRecipes.add(new ToolRecipe(ToolType.shovel, 0).addCost(Resource.wood, 5));
            Crafting.workbenchRecipes.add(new ToolRecipe(ToolType.sword, 1).addCost(Resource.wood, 5).addCost(Resource.stone, 5));
            Crafting.workbenchRecipes.add(new ToolRecipe(ToolType.axe, 1).addCost(Resource.wood, 5).addCost(Resource.stone, 5));
            Crafting.workbenchRecipes.add(new ToolRecipe(ToolType.hoe, 1).addCost(Resource.wood, 5).addCost(Resource.stone, 5));
            Crafting.workbenchRecipes.add(new ToolRecipe(ToolType.pickaxe, 1).addCost(Resource.wood, 5).addCost(Resource.stone, 5));
            Crafting.workbenchRecipes.add(new ToolRecipe(ToolType.shovel, 1).addCost(Resource.wood, 5).addCost(Resource.stone, 5));
            Crafting.anvilRecipes.add(new ToolRecipe(ToolType.sword, 2).addCost(Resource.wood, 5).addCost(Resource.ironIngot, 5));
            Crafting.anvilRecipes.add(new ToolRecipe(ToolType.axe, 2).addCost(Resource.wood, 5).addCost(Resource.ironIngot, 5));
            Crafting.anvilRecipes.add(new ToolRecipe(ToolType.hoe, 2).addCost(Resource.wood, 5).addCost(Resource.ironIngot, 5));
            Crafting.anvilRecipes.add(new ToolRecipe(ToolType.pickaxe, 2).addCost(Resource.wood, 5).addCost(Resource.ironIngot, 5));
            Crafting.anvilRecipes.add(new ToolRecipe(ToolType.shovel, 2).addCost(Resource.wood, 5).addCost(Resource.ironIngot, 5));
            Crafting.anvilRecipes.add(new ToolRecipe(ToolType.lighter, 2).addCost(Resource.straw, 1).addCost(Resource.stone, 5).addCost(Resource.ironIngot, 1));
            Crafting.anvilRecipes.add(new FurnitureRecipe(IronLantern.class).addCost(Resource.ironIngot, 5).addCost(Resource.goop, 10).addCost(Resource.glass, 4));
            Crafting.anvilRecipes.add(new ToolRecipe(ToolType.sword, 3).addCost(Resource.wood, 5).addCost(Resource.goldIngot, 5));
            Crafting.anvilRecipes.add(new ToolRecipe(ToolType.axe, 3).addCost(Resource.wood, 5).addCost(Resource.goldIngot, 5));
            Crafting.anvilRecipes.add(new ToolRecipe(ToolType.hoe, 3).addCost(Resource.wood, 5).addCost(Resource.goldIngot, 5));
            Crafting.anvilRecipes.add(new ToolRecipe(ToolType.pickaxe, 3).addCost(Resource.wood, 5).addCost(Resource.goldIngot, 5));
            Crafting.anvilRecipes.add(new ToolRecipe(ToolType.shovel, 3).addCost(Resource.wood, 5).addCost(Resource.goldIngot, 5));
            Crafting.anvilRecipes.add(new FurnitureRecipe(GoldLantern.class).addCost(Resource.goldIngot, 5).addCost(Resource.goop, 10).addCost(Resource.glass, 4));
            Crafting.anvilRecipes.add(new FurnitureRecipe(GoldAnvil.class).addCost(Resource.goldIngot, 10).addCost(Resource.gem, 10).addCost(Resource.cloth, 10));
            Crafting.anvilRecipes.add(new FurnitureRecipe(Kettle.class).addCost(Resource.ironIngot, 5).addCost(Resource.stone, 10));
            Crafting.anvilRecipes.add(new FurnitureRecipe(Grill.class).addCost(Resource.ironIngot, 20));
            Crafting.goldAnvilRecipes.add(new ToolRecipe(ToolType.sword, 4).addCost(Resource.wood, 5).addCost(Resource.gem, 50));
            Crafting.goldAnvilRecipes.add(new ToolRecipe(ToolType.axe, 4).addCost(Resource.wood, 5).addCost(Resource.gem, 10));
            Crafting.goldAnvilRecipes.add(new ToolRecipe(ToolType.hoe, 4).addCost(Resource.wood, 5).addCost(Resource.gem, 10));
            Crafting.goldAnvilRecipes.add(new ToolRecipe(ToolType.pickaxe, 4).addCost(Resource.wood, 5).addCost(Resource.gem, 50));
            Crafting.goldAnvilRecipes.add(new ToolRecipe(ToolType.shovel, 4).addCost(Resource.wood, 5).addCost(Resource.gem, 10));
            Crafting.goldAnvilRecipes.add(new ToolRecipe(ToolType.wand, 0).addCost(Resource.wood, 5).addCost(Resource.gem, 10));
            Crafting.goldAnvilRecipes.add(new ToolRecipe(ToolType.staff, 0).addCost(Resource.wood, 5).addCost(Resource.gem, 50).addCost(Resource.boxium, 20));
            Crafting.goldAnvilRecipes.add(new FurnitureRecipe(GemLantern.class).addCost(Resource.gem, 10).addCost(Resource.goop, 10).addCost(Resource.glass, 4));
            Crafting.furnaceRecipes.add(new ResourceRecipe(Resource.ironIngot).addCost(Resource.ironOre, 4).addCost(Resource.coal, 1));
            Crafting.furnaceRecipes.add(new ResourceRecipe(Resource.silverIngot).addCost(Resource.silverOre, 4).addCost(Resource.coal, 1));
            Crafting.furnaceRecipes.add(new ResourceRecipe(Resource.goldIngot).addCost(Resource.goldOre, 4).addCost(Resource.coal, 1));
            Crafting.furnaceRecipes.add(new FurnitureRecipe(Crock.class).addCost(Resource.dirt, 20).addCost(Resource.sand, 20));
            Crafting.furnaceRecipes.add(new ResourceRecipe(Resource.glass).addCost(Resource.sand, 4).addCost(Resource.coal, 1));
            Crafting.furnaceRecipes.add(new FurnitureRecipe(Orb.class).addCost(Resource.gem, 25).addCost(Resource.glass, 4).addCost(Resource.boxium, 2));
            Crafting.furnaceRecipes.add(new FurnitureRecipe(GreenOrb.class).addCost(Resource.gem, 25).addCost(Resource.glass, 4).addCost(Resource.straw, 100));
            Crafting.furnaceRecipes.add(new FurnitureRecipe(WhiteOrb.class).addCost(Resource.gem, 25).addCost(Resource.glass, 4).addCost(Resource.bone, 30));
            Crafting.grillRecipes.add(new ResourceRecipe(Resource.sausage).addCost(Resource.wheat, 1).addCost(Resource.meat, 2));
            Crafting.grillRecipes.add(new ResourceRecipe(Resource.hotDog).addCost(Resource.bread, 2).addCost(Resource.sausage, 1).addCost(Resource.mustard, 1));
            Crafting.grillRecipes.add(new ResourceRecipe(Resource.hamburger).addCost(Resource.bread, 2).addCost(Resource.ketchup, 1).addCost(Resource.mustard, 1).addCost(Resource.pickles, 1).addCost(Resource.lettuce, 1).addCost(Resource.tomato, 1).addCost(Resource.onion, 1));
            Crafting.grillRecipes.add(new ResourceRecipe(Resource.steak).addCost(Resource.meat, 1));
            Crafting.workbenchRecipes.add(new ResourceRecipe(Resource.salad).addCost(Resource.lettuce, 1).addCost(Resource.onion, 1).addCost(Resource.tomato, 1).addCost(Resource.vinegar, 1).addCost(Resource.oil, 1));
            Crafting.ovenRecipes.add(new ResourceRecipe(Resource.bread).addCost(Resource.wheat, 4));
            Crafting.ovenRecipes.add(new ResourceRecipe(Resource.cake).addCost(Resource.wheat, 2).addCost(Resource.egg, 1).addCost(Resource.sugar, 1).addCost(Resource.fat, 1));
            Crafting.ovenRecipes.add(new ResourceRecipe(Resource.cookies).addCost(Resource.wheat, 2).addCost(Resource.egg, 1).addCost(Resource.sugar, 1));
            Crafting.ovenRecipes.add(new ResourceRecipe(Resource.cherryPie).addCost(Resource.wheat, 2).addCost(Resource.egg, 1).addCost(Resource.sugar, 1).addCost(Resource.fat, 1).addCost(Resource.cherry, 2));
            Crafting.ovenRecipes.add(new ResourceRecipe(Resource.pizza).addCost(Resource.wheat, 2).addCost(Resource.herbs, 1).addCost(Resource.tomato, 1).addCost(Resource.cheese, 1).addCost(Resource.meat, 1));
            Crafting.ovenRecipes.add(new ResourceRecipe(Resource.casserole).addCost(Resource.cheese, 2).addCost(Resource.herbs, 1).addCost(Resource.squash, 1).addCost(Resource.bread, 1));
            Crafting.ovenRecipes.add(new ResourceRecipe(Resource.natchos).addCost(Resource.cheese, 1).addCost(Resource.pepper, 1).addCost(Resource.cornChips, 1));
            Crafting.kettleRecipes.add(new ResourceRecipe(Resource.sugar).addCost(Resource.beet, 1));
            Crafting.kettleRecipes.add(new ResourceRecipe(Resource.chowder).addCost(Resource.milk, 3).addCost(Resource.potato, 1).addCost(Resource.fish, 2).addCost(Resource.carrot, 1).addCost(Resource.wheat, 1));
            Crafting.kettleRecipes.add(new ResourceRecipe(Resource.mustard).addCost(Resource.mustardSeed, 5));
            Crafting.kettleRecipes.add(new ResourceRecipe(Resource.cheese).addCost(Resource.milk, 3).addCost(Resource.vinegar, 1));
            Crafting.kettleRecipes.add(new ResourceRecipe(Resource.noodle).addCost(Resource.wheat, 3));
            Crafting.kettleRecipes.add(new ResourceRecipe(Resource.spagetti).addCost(Resource.noodle, 1).addCost(Resource.meatSauce, 1));
            Crafting.kettleRecipes.add(new ResourceRecipe(Resource.meatSauce).addCost(Resource.tomato, 2).addCost(Resource.onion, 1).addCost(Resource.garlic, 1).addCost(Resource.meat, 4));
            Crafting.kettleRecipes.add(new ResourceRecipe(Resource.sauce).addCost(Resource.ginger, 1).addCost(Resource.garlic, 1).addCost(Resource.pepper, 1));
            Crafting.kettleRecipes.add(new ResourceRecipe(Resource.jigae).addCost(Resource.kimchi, 1).addCost(Resource.stew, 1).addCost(Resource.onion, 1));
            Crafting.kettleRecipes.add(new ResourceRecipe(Resource.omelet).addCost(Resource.egg, 2).addCost(Resource.nopale, 1).addCost(Resource.onion, 1));
            Crafting.kettleRecipes.add(new ResourceRecipe(Resource.stew).addCost(Resource.meat, 1).addCost(Resource.carrot, 1).addCost(Resource.onion, 1));
            Crafting.kettleRecipes.add(new ResourceRecipe(Resource.fat).addCost(Resource.meat, 3));
            Crafting.kettleRecipes.add(new ResourceRecipe(Resource.chips).addCost(Resource.potato, 2).addCost(Resource.oil, 1));
            Crafting.kettleRecipes.add(new ResourceRecipe(Resource.fries).addCost(Resource.potato, 2).addCost(Resource.oil, 1));
            Crafting.kettleRecipes.add(new ResourceRecipe(Resource.cornChips).addCost(Resource.corn, 1).addCost(Resource.oil, 1));
            Crafting.kettleRecipes.add(new ResourceRecipe(Resource.greens).addCost(Resource.mustardGrn, 1).addCost(Resource.herbs, 1));
            Crafting.kettleRecipes.add(new ResourceRecipe(Resource.chili).addCost(Resource.bean, 1).addCost(Resource.pepper, 1).addCost(Resource.meat, 1).addCost(Resource.onion, 1));
            Crafting.kettleRecipes.add(new ResourceRecipe(Resource.cabgeStew).addCost(Resource.cabbage, 1).addCost(Resource.sausage, 1).addCost(Resource.onion, 1).addCost(Resource.potato, 1));
            Crafting.kettleRecipes.add(new ResourceRecipe(Resource.beanDip).addCost(Resource.bean, 1).addCost(Resource.fat, 1).addCost(Resource.pepper, 1));
            Crafting.kettleRecipes.add(new ResourceRecipe(Resource.ketchup).addCost(Resource.tomato, 1).addCost(Resource.vinegar, 1).addCost(Resource.sugar, 1));
            Crafting.crockRecipes.add(new ResourceRecipe(Resource.ale).addCost(Resource.wheat, 2));
            Crafting.crockRecipes.add(new ResourceRecipe(Resource.beer).addCost(Resource.wheat, 2).addCost(Resource.herbs, 1));
            Crafting.crockRecipes.add(new ResourceRecipe(Resource.kimchi).addCost(Resource.sauce, 1).addCost(Resource.radish, 1).addCost(Resource.onion, 1));
            Crafting.crockRecipes.add(new ResourceRecipe(Resource.vinegar).addCost(Resource.ale, 1));
            Crafting.crockRecipes.add(new ResourceRecipe(Resource.vinegar).addCost(Resource.cider, 1));
            Crafting.crockRecipes.add(new ResourceRecipe(Resource.hardCider).addCost(Resource.cider, 2));
            Crafting.crockRecipes.add(new ResourceRecipe(Resource.butter).addCost(Resource.milk, 1));
            Crafting.pressRecipes.add(new ResourceRecipe(Resource.cider).addCost(Resource.apple, 4));
            Crafting.pressRecipes.add(new ResourceRecipe(Resource.cherryWine).addCost(Resource.cherry, 4));
            Crafting.pressRecipes.add(new ResourceRecipe(Resource.oil).addCost(Resource.corn, 1));
            Crafting.pressRecipes.add(new ResourceRecipe(Resource.oil).addCost(Resource.bean, 1));
            Crafting.girlSales.add(new ResourceRecipe(Resource.chocolate).addCost(Resource.goldIngot, 1));
            Crafting.girlSales.add(new ResourceRecipe(Resource.cupCake).addCost(Resource.goldIngot, 1));
            Crafting.girlSales.add(new ResourceRecipe(Resource.pickles).addCost(Resource.goldIngot, 1));
            Crafting.bobSales.add(new ResourceRecipe(Resource.woodsman).addCost(Resource.jigae, 1).addCost(Resource.cherryWine, 1).addCost(Resource.goldIngot, 1));
            Crafting.bobSales.add(new ResourceRecipe(Resource.seaCaptain).addCost(Resource.hamburger, 1).addCost(Resource.hardCider, 1).addCost(Resource.goldIngot, 1));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
