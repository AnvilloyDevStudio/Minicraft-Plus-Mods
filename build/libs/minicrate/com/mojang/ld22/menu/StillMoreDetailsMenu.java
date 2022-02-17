// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.menu;

import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;

public class StillMoreDetailsMenu extends Menu
{
    private Menu parent;
    
    public StillMoreDetailsMenu(final Menu parent) {
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
        Font.draw("Still More Details", screen, 20, 8, Color.get(0, 555, 555, 555));
        Font.draw("You will need boxium to make a staff", screen, 4, 24, Color.get(0, 333, 333, 333));
        Font.draw("so you can turn dirt into flowers or", screen, 4, 32, Color.get(0, 333, 333, 333));
        Font.draw("trees into lava, stairs up and such.", screen, 4, 40, Color.get(0, 333, 333, 333));
        Font.draw("Stairs up the the clouds can put", screen, 4, 56, Color.get(0, 333, 333, 333));
        Font.draw("you into horrid places", screen, 4, 64, Color.get(0, 333, 333, 333));
        Font.draw("The Orb will let you walk freely", screen, 4, 80, Color.get(0, 333, 333, 333));
        Font.draw("but the Orb can mess things up. ", screen, 4, 88, Color.get(0, 333, 333, 333));
        Font.draw("Lemonade Kitty is your friend", screen, 4, 104, Color.get(0, 333, 333, 333));
        Font.draw("Sheepish and Chicky are too!", screen, 4, 112, Color.get(0, 333, 333, 333));
        Font.draw("Use your sword on tall grass.", screen, 4, 120, Color.get(0, 333, 333, 333));
        Font.draw("Search the woods for ginger", screen, 4, 128, Color.get(0, 333, 333, 333));
        Font.draw("and truffles", screen, 4, 136, Color.get(0, 333, 333, 333));
        Font.draw("", screen, 4, 144, Color.get(0, 333, 333, 333));
        Font.draw("Sea Monsters are good eating!", screen, 4, 152, Color.get(0, 333, 333, 333));
        Font.draw("The White Orb makes you what you", screen, 4, 168, Color.get(0, 333, 333, 333));
        Font.draw("appear to be and lets you walk ", screen, 4, 176, Color.get(0, 333, 333, 333));
        Font.draw("through stone.", screen, 4, 184, Color.get(0, 333, 333, 333));
    }
}
