// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.menu;

import java.util.List;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.entity.Inventory;
import com.mojang.ld22.entity.Player;

public class ContainerMenu extends Menu
{
    private Player player;
    private Inventory container;
    private int selected;
    private String title;
    private int oSelected;
    private int window;
    
    public ContainerMenu(final Player player, final String title, final Inventory container) {
        this.selected = 0;
        this.window = 0;
        this.player = player;
        this.title = title;
        this.container = container;
    }
    
    @Override
    public void tick() {
        if (this.input.menu.clicked) {
            this.game.setMenu(null);
        }
        if (this.input.left.clicked) {
            this.window = 0;
            final int tmp = this.selected;
            this.selected = this.oSelected;
            this.oSelected = tmp;
        }
        if (this.input.right.clicked) {
            this.window = 1;
            final int tmp = this.selected;
            this.selected = this.oSelected;
            this.oSelected = tmp;
        }
        final Inventory i = (this.window == 1) ? this.player.inventory : this.container;
        final Inventory i2 = (this.window == 0) ? this.player.inventory : this.container;
        final int len = i.items.size();
        if (this.selected < 0) {
            this.selected = 0;
        }
        if (this.selected >= len) {
            this.selected = len - 1;
        }
        if (this.input.up.clicked) {
            --this.selected;
        }
        if (this.input.down.clicked) {
            ++this.selected;
        }
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
            i2.add(this.oSelected, i.items.remove(this.selected));
            if (this.selected >= i.items.size()) {
                this.selected = i.items.size() - 1;
            }
        }
    }
    
    @Override
    public void render(final Screen screen) {
        if (this.window == 1) {
            screen.setOffset(48, 0);
        }
        Font.renderFrame(screen, this.title, 1, 1, 12, 11);
        this.renderItemList(screen, 1, 1, 12, 11, this.container.items, (this.window == 0) ? this.selected : (-this.oSelected - 1));
        Font.renderFrame(screen, "inventory", 13, 1, 24, 11);
        this.renderItemList(screen, 13, 1, 24, 11, this.player.inventory.items, (this.window == 1) ? this.selected : (-this.oSelected - 1));
        screen.setOffset(0, 0);
    }
}
