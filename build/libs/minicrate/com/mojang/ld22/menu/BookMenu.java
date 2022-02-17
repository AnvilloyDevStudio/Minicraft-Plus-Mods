// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.menu;

import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;

public class BookMenu extends Menu
{
    private int selected;
    private static final String[] options;
    
    static {
        options = new String[] { "Close Spell Book", "Cast Darkness", "Cast Light", "Cast Extiguish Fire" };
    }
    
    public BookMenu() {
        this.selected = 0;
    }
    
    @Override
    public void tick() {
        if (this.input.up.clicked) {
            --this.selected;
        }
        if (this.input.down.clicked) {
            ++this.selected;
        }
        final int len = BookMenu.options.length;
        if (this.selected < 0) {
            this.selected += len;
        }
        if (this.selected >= len) {
            this.selected -= len;
        }
        if (this.input.menu.clicked) {
            this.game.setMenu(null);
        }
        if (this.selected == 1) {
            this.game.makeDark();
        }
        if (this.selected == 2) {
            this.game.makeLight();
        }
        if (this.selected == 3) {
            this.game.stopFire();
        }
    }
    
    @Override
    public void render(final Screen screen) {
        screen.clear(0);
        Font.draw("Spell Book", screen, (screen.w - 80) / 2, 16, Color.get(0, 111, 111, 111));
        Font.draw("apparently written from memory", screen, (screen.w - 248) / 2, 24, Color.get(0, 111, 111, 111));
        if (this.game.cheaterLevel() == 0) {
            Font.draw("by an honorable mage", screen, (screen.w - 160) / 2, 32, Color.get(0, 111, 111, 111));
        }
        else {
            Font.draw("by a level " + this.game.cheaterLevel() + " cheater ", screen, (screen.w - 184) / 2, 40, Color.get(0, 111, 111, 111));
        }
        final String tempStr = "Player Score " + this.game.player.score;
        Font.draw(tempStr, screen, (screen.w - tempStr.length() * 8) / 2, 48, Color.get(0, 111, 111, 111));
        Font.draw("Current Depth " + this.game.currentDepth(), screen, (screen.w - 128) / 2, 56, Color.get(0, 111, 111, 111));
        for (int i = 0; i < 4; ++i) {
            String msg = BookMenu.options[i];
            int col = Color.get(0, 222, 222, 222);
            if (i == this.selected) {
                msg = "> " + msg + " <";
                col = Color.get(0, 555, 555, 555);
            }
            Font.draw(msg, screen, (screen.w - msg.length() * 8) / 2, (10 + i) * 8, col);
        }
        Font.draw("(Arrow keys,X and C)", screen, 0, screen.h - 8, Color.get(0, 111, 111, 111));
    }
}
