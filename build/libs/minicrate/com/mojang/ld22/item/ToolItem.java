// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.item;

import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.entity.ItemEntity;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.gfx.Color;
import java.util.Random;

public class ToolItem extends Item
{
    private Random random;
    public static final int MAX_LEVEL = 10;
    public static final String[] LEVEL_NAMES;
    public static final int[] LEVEL_COLORS;
    public ToolType type;
    public int level;
    
    static {
        LEVEL_NAMES = new String[] { "Wood", "Rock", "Iron", "Gold", "Gem", "Dark", "Mega", "Red", "Green", "Blue", "Spell" };
        LEVEL_COLORS = new int[] { Color.get(-1, 100, 321, 431), Color.get(-1, 100, 321, 111), Color.get(-1, 100, 321, 333), Color.get(-1, 100, 321, 550), Color.get(-1, 100, 321, 45), Color.get(-1, 100, 321, 0), Color.get(-1, 100, 321, 505), Color.get(-1, 100, 321, 500), Color.get(-1, 100, 321, 40), Color.get(-1, 100, 321, 5), Color.get(-1, 100, 321, 555) };
    }
    
    public ToolItem(final ToolType type, final int level) {
        this.random = new Random();
        this.level = 0;
        this.type = type;
        this.level = level;
    }
    
    @Override
    public int getColor() {
        return ToolItem.LEVEL_COLORS[this.level];
    }
    
    @Override
    public int getSprite() {
        return this.type.sprite + 160;
    }
    
    @Override
    public void renderIcon(final Screen screen, final int x, final int y) {
        screen.render(x, y, this.getSprite(), this.getColor(), 0);
    }
    
    @Override
    public void renderInventory(final Screen screen, final int x, final int y) {
        screen.render(x, y, this.getSprite(), this.getColor(), 0);
        Font.draw(this.getName(), screen, x + 8, y, Color.get(-1, 555, 555, 555));
    }
    
    @Override
    public String getName() {
        return String.valueOf(ToolItem.LEVEL_NAMES[this.level]) + " " + this.type.name;
    }
    
    @Override
    public void onTake(final ItemEntity itemEntity) {
    }
    
    @Override
    public boolean canAttack() {
        return true;
    }
    
    @Override
    public int getAttackDamageBonus(final Entity e) {
        if (this.type == ToolType.axe) {
            return (this.level + 1) * 2 + this.random.nextInt(4);
        }
        if (this.type == ToolType.sword) {
            return (this.level + 1) * 3 + this.random.nextInt(2 + this.level * this.level * 2);
        }
        return 1;
    }
    
    @Override
    public boolean matches(final Item item) {
        if (item instanceof ToolItem) {
            final ToolItem other = (ToolItem)item;
            return other.type == this.type && other.level == this.level;
        }
        return false;
    }
}
