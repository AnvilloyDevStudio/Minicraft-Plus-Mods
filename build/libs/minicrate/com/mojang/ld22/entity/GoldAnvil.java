// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.entity;

import com.mojang.ld22.menu.Menu;
import com.mojang.ld22.menu.CraftingMenu;
import com.mojang.ld22.crafting.Crafting;
import com.mojang.ld22.gfx.Color;

public class GoldAnvil extends Furniture
{
    public GoldAnvil() {
        super("GoldAnvil");
        this.col = Color.get(-1, 110, 330, 553);
        this.sprite = 0;
        this.xr = 3;
        this.yr = 2;
    }
    
    @Override
    public boolean use(final Player player, final int attackDir) {
        player.game.setMenu(new CraftingMenu(Crafting.goldAnvilRecipes, player));
        return true;
    }
    
    @Override
    public int getLightRadius() {
        return 24;
    }
}
