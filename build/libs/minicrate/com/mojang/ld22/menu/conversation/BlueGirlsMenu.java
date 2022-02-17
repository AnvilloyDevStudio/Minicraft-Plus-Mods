// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.menu.conversation;

import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import java.util.Random;
import com.mojang.ld22.menu.Menu;

public class BlueGirlsMenu extends Menu
{
    protected Random random;
    int whatToSay;
    int talkLoop;
    
    public BlueGirlsMenu(final Menu parent) {
        this.random = new Random();
        this.talkLoop = 0;
    }
    
    @Override
    public void tick() {
        if (this.input.attack.clicked || this.input.menu.clicked) {
            this.game.setMenu(null);
        }
    }
    
    @Override
    public void render(final Screen screen) {
        screen.clear(0);
        Font.draw("Azure says,", screen, 20, 8, Color.get(0, 555, 555, 555));
        if (this.talkLoop == 0) {
            this.whatToSay = this.random.nextInt(3) + 1;
            this.talkLoop = 1;
        }
        switch (this.whatToSay) {
            case 1: {
                Font.draw("I want, a lot of things,", screen, 4, 24, Color.get(0, 333, 333, 333));
                Font.draw("But they don't exist in this world!", screen, 4, 32, Color.get(0, 333, 333, 333));
                Font.draw("Better drawn turduckens for one thing.", screen, 4, 40, Color.get(0, 333, 333, 333));
                Font.draw("Oh, I can go on for a while.", screen, 4, 48, Color.get(0, 333, 333, 333));
                Font.draw("A save system,", screen, 4, 56, Color.get(0, 333, 333, 333));
                Font.draw("A town or two,", screen, 4, 64, Color.get(0, 333, 333, 333));
                Font.draw("Armor,", screen, 4, 80, Color.get(0, 333, 333, 333));
                Font.draw("A use for truffles,", screen, 4, 88, Color.get(0, 333, 333, 333));
                Font.draw("Pretty clothing,", screen, 4, 96, Color.get(0, 333, 333, 333));
                Font.draw("New Shoes,", screen, 4, 104, Color.get(0, 333, 333, 333));
                Font.draw("More Magic Items", screen, 4, 112, Color.get(0, 333, 333, 333));
                Font.draw("Making a house,", screen, 4, 120, Color.get(0, 333, 333, 333));
                Font.draw("More People,", screen, 4, 128, Color.get(0, 333, 333, 333));
                Font.draw("More intelligent Pets,", screen, 4, 136, Color.get(0, 333, 333, 333));
                Font.draw("But these things don't exist!", screen, 4, 144, Color.get(0, 333, 333, 333));
                Font.draw("Did I mention a save system?", screen, 4, 152, Color.get(0, 333, 333, 333));
                break;
            }
            case 2: {
                Font.draw("You can call me, Azure!", screen, 4, 24, Color.get(0, 333, 333, 333));
                break;
            }
            default: {
                Font.draw("I want, rice and a pony!", screen, 4, 24, Color.get(0, 333, 333, 333));
                Font.draw("But they don't exist in this world!", screen, 4, 32, Color.get(0, 333, 333, 333));
                break;
            }
        }
    }
}
