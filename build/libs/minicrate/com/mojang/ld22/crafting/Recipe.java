// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.crafting;

import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;
import java.util.ArrayList;
import com.mojang.ld22.item.Item;
import java.util.List;
import com.mojang.ld22.menu.ListItem;

public abstract class Recipe implements ListItem
{
    public List<Item> costs;
    public boolean canCraft;
    public Item resultTemplate;
    
    public Recipe(final Item resultTemplate) {
        this.costs = new ArrayList<Item>();
        this.canCraft = false;
        this.resultTemplate = resultTemplate;
    }
    
    public Recipe addCost(final Resource resource, final int count) {
        this.costs.add(new ResourceItem(resource, count));
        return this;
    }
    
    public void checkCanCraft(final Player player) {
        for (int i = 0; i < this.costs.size(); ++i) {
            final Item item = this.costs.get(i);
            if (item instanceof ResourceItem) {
                final ResourceItem ri = (ResourceItem)item;
                if (!player.inventory.hasResources(ri.resource, ri.count)) {
                    this.canCraft = false;
                    return;
                }
            }
        }
        this.canCraft = true;
    }
    
    @Override
    public void renderInventory(final Screen screen, final int x, final int y) {
        screen.render(x, y, this.resultTemplate.getSprite(), this.resultTemplate.getColor(), 0);
        final int textColor = this.canCraft ? Color.get(-1, 555, 555, 555) : Color.get(-1, 222, 222, 222);
        Font.draw(this.resultTemplate.getName(), screen, x + 8, y, textColor);
    }
    
    public abstract void craft(final Player p0);
    
    public void deductCost(final Player player) {
        for (int i = 0; i < this.costs.size(); ++i) {
            final Item item = this.costs.get(i);
            if (item instanceof ResourceItem) {
                final ResourceItem ri = (ResourceItem)item;
                player.inventory.removeResource(ri.resource, ri.count);
            }
        }
    }
}
