// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.menu;

import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;

public class AboutMenu extends Menu
{
    private Menu parent;
    
    public AboutMenu(final Menu parent) {
        this.parent = parent;
    }
    
    @Override
    public void tick() {
        if (this.input.attack.clicked || this.input.menu.clicked) {
            this.game.setMenu(this.parent);
        }
    }
    
    @Override
    public void render(final Screen screen) {
        screen.clear(0);
        Font.draw("About MiniCrate", screen, 20, 8, Color.get(0, 555, 555, 555));
        Font.draw("Minicraft was made", screen, 4, 24, Color.get(0, 333, 333, 333));
        Font.draw("by Markus Persson", screen, 4, 32, Color.get(0, 333, 333, 333));
        Font.draw("For the 22'nd ludum", screen, 4, 40, Color.get(0, 333, 333, 333));
        Font.draw("dare competition in", screen, 4, 48, Color.get(0, 333, 333, 333));
        Font.draw("december 2011.", screen, 4, 56, Color.get(0, 333, 333, 333));
        Font.draw("it is dedicated to", screen, 4, 72, Color.get(0, 333, 333, 333));
        Font.draw("Markus' father. <3", screen, 4, 80, Color.get(0, 333, 333, 333));
        Font.draw("MiniCrate is a Silly", screen, 4, 96, Color.get(0, 333, 333, 333));
        Font.draw("Mod of it by Bob", screen, 4, 104, Color.get(0, 333, 333, 333));
    }
}
