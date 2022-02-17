// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.crafting;

import com.mojang.ld22.entity.Player;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.ToolType;

public class ToolRecipe extends Recipe
{
    private ToolType type;
    private int level;
    
    public ToolRecipe(final ToolType type, final int level) {
        super(new ToolItem(type, level));
        this.type = type;
        this.level = level;
    }
    
    @Override
    public void craft(final Player player) {
        player.inventory.add(0, new ToolItem(this.type, this.level));
    }
}
