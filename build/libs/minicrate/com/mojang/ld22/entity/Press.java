// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.entity;

import com.mojang.ld22.menu.Menu;
import com.mojang.ld22.menu.CraftingMenu;
import com.mojang.ld22.crafting.Crafting;
import com.mojang.ld22.gfx.Color;

public class Press extends Furniture
{
    public Press() {
        super("Press");
        this.col = Color.get(-1, 321, 532, 444);
        this.sprite = 12;
        this.xr = 3;
        this.yr = 2;
    }
    
    @Override
    public boolean use(final Player player, final int attackDir) {
        player.game.setMenu(new CraftingMenu(Crafting.pressRecipes, player));
        return true;
    }
}
