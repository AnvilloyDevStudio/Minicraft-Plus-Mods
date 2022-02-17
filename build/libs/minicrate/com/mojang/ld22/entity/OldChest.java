// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.entity;

import com.mojang.ld22.menu.Menu;
import com.mojang.ld22.menu.ContainerMenu;
import com.mojang.ld22.gfx.Color;

public class OldChest extends Furniture
{
    public Inventory inventory;
    
    public OldChest(final Inventory inventory) {
        super("OldChest");
        this.inventory = new Inventory();
        this.col = Color.get(-1, 110, 221, 442);
        this.sprite = 1;
    }
    
    @Override
    public boolean use(final Player player, final int attackDir) {
        player.game.setMenu(new ContainerMenu(player, "Chest", this.inventory));
        return true;
    }
}
