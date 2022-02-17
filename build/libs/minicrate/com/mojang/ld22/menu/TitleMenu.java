// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.menu;

import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.sound.Sound;

public class TitleMenu extends Menu
{
    private int selected;
    private static final String[] options;
    
    static {
        options = new String[] { "Start game", "How to play", "About", "Some Details", "More Details", "Still More Details" };
    }
    
    public TitleMenu() {
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
        final int len = TitleMenu.options.length;
        if (this.selected < 0) {
            this.selected += len;
        }
        if (this.selected >= len) {
            this.selected -= len;
        }
        if (this.input.attack.clicked || this.input.menu.clicked) {
            if (this.selected == 0) {
                Sound.test.play();
                this.game.resetGame();
                this.game.setMenu(null);
            }
            if (this.selected == 1) {
                this.game.setMenu(new InstructionsMenu(this));
            }
            if (this.selected == 2) {
                this.game.setMenu(new AboutMenu(this));
            }
            if (this.selected == 3) {
                this.game.setMenu(new DetailsMenu(this));
            }
            if (this.selected == 4) {
                this.game.setMenu(new MoreDetailsMenu(this));
            }
            if (this.selected == 5) {
                this.game.setMenu(new StillMoreDetailsMenu(this));
            }
        }
    }
    
    @Override
    public void render(final Screen screen) {
        screen.clear(0);
        final int h = 2;
        final int w = 17;
        final int titleColor = Color.get(0, 8, 131, 551);
        final int xo = (screen.w - w * 8) / 2;
        final int yo = 24;
        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                screen.render(xo + x * 8, yo + y * 8, x + (y + 6) * 32, titleColor, 0);
            }
        }
        Font.draw("Version 13", screen, (screen.w - 80) / 2, 48, Color.get(0, 111, 111, 111));
        Font.draw("Orbs", screen, (screen.w - 32) / 2, 56, Color.get(0, 111, 111, 111));
        for (int i = 0; i < 6; ++i) {
            String msg = TitleMenu.options[i];
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
