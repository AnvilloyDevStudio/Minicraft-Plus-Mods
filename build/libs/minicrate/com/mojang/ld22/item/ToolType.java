// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.item;

public class ToolType
{
    public static ToolType shovel;
    public static ToolType hoe;
    public static ToolType sword;
    public static ToolType pickaxe;
    public static ToolType axe;
    public static ToolType bow;
    public static ToolType wand;
    public static ToolType lighter;
    public static ToolType staff;
    public final String name;
    public final int sprite;
    
    static {
        ToolType.shovel = new ToolType("Shvl", 0);
        ToolType.hoe = new ToolType("Hoe", 1);
        ToolType.sword = new ToolType("Swrd", 2);
        ToolType.pickaxe = new ToolType("Pick", 3);
        ToolType.axe = new ToolType("Axe", 4);
        ToolType.bow = new ToolType("Bow", 5);
        ToolType.wand = new ToolType("Wand", 6);
        ToolType.lighter = new ToolType("Lighter", 8);
        ToolType.staff = new ToolType("Staff", 6);
    }
    
    private ToolType(final String name, final int sprite) {
        this.name = name;
        this.sprite = sprite;
    }
}
