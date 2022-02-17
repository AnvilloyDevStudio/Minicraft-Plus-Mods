// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.crafting;

import com.mojang.ld22.entity.Player;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;

public class ResourceRecipe extends Recipe
{
    private Resource resource;
    
    public ResourceRecipe(final Resource resource) {
        super(new ResourceItem(resource, 1));
        this.resource = resource;
    }
    
    @Override
    public void craft(final Player player) {
        player.inventory.add(0, new ResourceItem(this.resource, 1));
    }
}
