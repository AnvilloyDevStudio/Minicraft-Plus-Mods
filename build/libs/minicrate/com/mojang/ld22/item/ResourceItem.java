// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.item;

import com.mojang.ld22.entity.Player;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.level.tile.Tile;
import com.mojang.ld22.entity.ItemEntity;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.resource.Resource;

public class ResourceItem extends Item
{
    public Resource resource;
    public int count;
    
    public ResourceItem(final Resource resource) {
        this.count = 1;
        this.resource = resource;
    }
    
    public ResourceItem(final Resource resource, final int count) {
        this.count = 1;
        this.resource = resource;
        this.count = count;
    }
    
    @Override
    public int getColor() {
        return this.resource.color;
    }
    
    @Override
    public int getSprite() {
        return this.resource.sprite;
    }
    
    @Override
    public void renderIcon(final Screen screen, final int x, final int y) {
        screen.render(x, y, this.resource.sprite, this.resource.color, 0);
    }
    
    @Override
    public void renderInventory(final Screen screen, final int x, final int y) {
        screen.render(x, y, this.resource.sprite, this.resource.color, 0);
        Font.draw(this.resource.name, screen, x + 32, y, Color.get(-1, 555, 555, 555));
        int cc = this.count;
        if (cc > 999) {
            cc = 999;
        }
        Font.draw(new StringBuilder().append(cc).toString(), screen, x + 8, y, Color.get(-1, 444, 444, 444));
    }
    
    @Override
    public String getName() {
        return this.resource.name;
    }
    
    @Override
    public void onTake(final ItemEntity itemEntity) {
    }
    
    @Override
    public boolean interactOn(final Tile tile, final Level level, final int xt, final int yt, final Player player, final int attackDir) {
        if (this.resource.interactOn(tile, level, xt, yt, player, attackDir)) {
            --this.count;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean isDepleted() {
        return this.count <= 0;
    }
}
