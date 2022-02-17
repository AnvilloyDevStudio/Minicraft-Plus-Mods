// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.item;

import com.mojang.ld22.level.Level;
import com.mojang.ld22.level.tile.Tile;
import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.entity.ItemEntity;
import com.mojang.ld22.menu.ListItem;

public class Item implements ListItem
{
    public int getColor() {
        return 0;
    }
    
    public int getSprite() {
        return 0;
    }
    
    public void onTake(final ItemEntity itemEntity) {
    }
    
    @Override
    public void renderInventory(final Screen screen, final int x, final int y) {
    }
    
    public boolean interact(final Player player, final Entity entity, final int attackDir) {
        return false;
    }
    
    public void renderIcon(final Screen screen, final int x, final int y) {
    }
    
    public boolean interactOn(final Tile tile, final Level level, final int xt, final int yt, final Player player, final int attackDir) {
        return false;
    }
    
    public boolean isDepleted() {
        return false;
    }
    
    public boolean canAttack() {
        return false;
    }
    
    public int getAttackDamageBonus(final Entity e) {
        return 0;
    }
    
    public String getName() {
        return "";
    }
    
    public boolean matches(final Item item) {
        return item.getClass() == this.getClass();
    }
}
