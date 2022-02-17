// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.menu;

import java.util.List;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.entity.Player;

public class InventoryMenu extends Menu
{
    private Player player;
    private int selected;
    
    public InventoryMenu(final Player player) {
        this.selected = 0;
        this.player = player;
        if (player.activeItem != null) {
            player.inventory.items.add(0, player.activeItem);
            player.activeItem = null;
        }
    }
    
    @Override
    public void tick() {
        if (this.input.menu.clicked) {
            this.game.setMenu(null);
        }
        if (this.input.up.clicked) {
            --this.selected;
        }
        if (this.input.down.clicked) {
            ++this.selected;
        }
        final int len = this.player.inventory.items.size();
        if (len == 0) {
            this.selected = 0;
        }
        if (this.selected < 0) {
            this.selected += len;
        }
        if (this.selected >= len) {
            this.selected -= len;
        }
        if (this.input.attack.clicked && len > 0) {
            final Item item = this.player.inventory.items.remove(this.selected);
            this.player.activeItem = item;
            this.game.setMenu(null);
        }
    }
    
    @Override
    public void render(final Screen screen) {
        Font.renderFrame(screen, "inventory", 1, 1, 22, 20);
        this.renderItemList(screen, 1, 1, 17, 19, this.player.inventory.items, this.selected);
    }
}
