// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.crafting;

import com.mojang.ld22.entity.Player;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.FurnitureItem;
import com.mojang.ld22.entity.Furniture;

public class FurnitureRecipe extends Recipe
{
    private Class<? extends Furniture> clazz;
    
    public FurnitureRecipe(final Class<? extends Furniture> clazz) throws InstantiationException, IllegalAccessException {
        super(new FurnitureItem((Furniture)clazz.newInstance()));
        this.clazz = clazz;
    }
    
    @Override
    public void craft(final Player player) {
        try {
            player.inventory.add(0, new FurnitureItem((Furniture)this.clazz.newInstance()));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
