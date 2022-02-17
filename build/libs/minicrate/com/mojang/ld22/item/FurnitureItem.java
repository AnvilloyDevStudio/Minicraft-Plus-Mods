// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.item;

import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.level.tile.Tile;
import com.mojang.ld22.entity.ItemEntity;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.entity.Furniture;

public class FurnitureItem extends Item
{
    public Furniture furniture;
    public boolean placed;
    
    public FurnitureItem(final Furniture furniture) {
        this.placed = false;
        this.furniture = furniture;
    }
    
    @Override
    public int getColor() {
        return this.furniture.col;
    }
    
    @Override
    public int getSprite() {
        return this.furniture.sprite + 320;
    }
    
    @Override
    public void renderIcon(final Screen screen, final int x, final int y) {
        screen.render(x, y, this.getSprite(), this.getColor(), 0);
    }
    
    @Override
    public void renderInventory(final Screen screen, final int x, final int y) {
        screen.render(x, y, this.getSprite(), this.getColor(), 0);
        Font.draw(this.furniture.name, screen, x + 8, y, Color.get(-1, 555, 555, 555));
    }
    
    @Override
    public void onTake(final ItemEntity itemEntity) {
    }
    
    @Override
    public boolean canAttack() {
        return false;
    }
    
    @Override
    public boolean interactOn(final Tile tile, final Level level, final int xt, final int yt, final Player player, final int attackDir) {
        if (tile.mayPass(level, xt, yt, this.furniture)) {
            this.furniture.x = xt * 16 + 8;
            this.furniture.y = yt * 16 + 8;
            level.add(this.furniture);
            return this.placed = true;
        }
        return false;
    }
    
    @Override
    public boolean isDepleted() {
        return this.placed;
    }
    
    @Override
    public String getName() {
        return this.furniture.name;
    }
}
