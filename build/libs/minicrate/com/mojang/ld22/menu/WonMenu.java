// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.menu;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;

public class WonMenu extends Menu
{
    private int inputDelay;
    
    public WonMenu() {
        this.inputDelay = 60;
    }
    
    @Override
    public void tick() {
        if (this.inputDelay > 0) {
            --this.inputDelay;
        }
        else if (this.input.attack.clicked || this.input.menu.clicked) {
            this.game.setMenu(new TitleMenu());
        }
    }
    
    @Override
    public void render(final Screen screen) {
        Font.renderFrame(screen, "", 1, 3, 18, 9);
        Font.draw("You won! Yay!", screen, 16, 32, Color.get(-1, 555, 555, 555));
        int seconds = this.game.gameTime / 60;
        int minutes = seconds / 60;
        final int hours = minutes / 60;
        minutes %= 60;
        seconds %= 60;
        String timeString = "";
        if (hours > 0) {
            timeString = String.valueOf(hours) + "h" + ((minutes < 10) ? "0" : "") + minutes + "m";
        }
        else {
            timeString = String.valueOf(minutes) + "m " + ((seconds < 10) ? "0" : "") + seconds + "s";
        }
        Font.draw("Time:", screen, 16, 40, Color.get(-1, 555, 555, 555));
        Font.draw(timeString, screen, 56, 40, Color.get(-1, 550, 550, 550));
        Font.draw("Score:", screen, 16, 48, Color.get(-1, 555, 555, 555));
        Font.draw(new StringBuilder().append(this.game.player.score).toString(), screen, 64, 48, Color.get(-1, 550, 550, 550));
        Font.draw("Press C to win", screen, 16, 64, Color.get(-1, 333, 333, 333));
    }
}
