// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.menu;

import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;

public class MoreDetailsMenu extends Menu
{
    private Menu parent;
    
    public MoreDetailsMenu(final Menu parent) {
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
        Font.draw("More Details", screen, 20, 8, Color.get(0, 555, 555, 555));
        Font.draw("If you dig a hole with a shovel,", screen, 4, 24, Color.get(0, 333, 333, 333));
        Font.draw("and then dig with a pick,", screen, 4, 32, Color.get(0, 333, 333, 333));
        Font.draw("you can make a stair down.", screen, 4, 40, Color.get(0, 333, 333, 333));
        Font.draw("There will not be a matching stair", screen, 4, 56, Color.get(0, 333, 333, 333));
        Font.draw("up, so be careful.", screen, 4, 64, Color.get(0, 333, 333, 333));
        Font.draw("If you are on the lava level,", screen, 4, 80, Color.get(0, 333, 333, 333));
        Font.draw("the stair down will take you to", screen, 4, 88, Color.get(0, 333, 333, 333));
        Font.draw("limbo where nothing ever happens.", screen, 4, 96, Color.get(0, 333, 333, 333));
        Font.draw("Cloth thrown on water turns to stone,", screen, 4, 112, Color.get(0, 333, 333, 333));
        Font.draw("of course!", screen, 4, 120, Color.get(0, 333, 333, 333));
        Font.draw("If you are a horrible cheater,", screen, 4, 136, Color.get(0, 333, 333, 333));
        Font.draw("pressing the T button", screen, 4, 144, Color.get(0, 333, 333, 333));
        Font.draw("will help you cheat horribly.", screen, 4, 152, Color.get(0, 333, 333, 333));
    }
}
