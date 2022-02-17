// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.menu;

import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;

public class DetailsMenu extends Menu
{
    private Menu parent;
    
    public DetailsMenu(final Menu parent) {
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
        Font.draw("Details", screen, 20, 8, Color.get(0, 555, 555, 555));
        Font.draw("Eating Fungus Makes you Glow,", screen, 4, 24, Color.get(0, 333, 333, 333));
        Font.draw("Eating too much Fungus Makes you", screen, 4, 32, Color.get(0, 333, 333, 333));
        Font.draw(" Sick. Try eating other things for", screen, 4, 40, Color.get(0, 333, 333, 333));
        Font.draw("a while.", screen, 4, 48, Color.get(0, 333, 333, 333));
        Font.draw("If you swim out and defeat a ship,", screen, 4, 64, Color.get(0, 333, 333, 333));
        Font.draw("you can take the helm and sail.", screen, 4, 72, Color.get(0, 333, 333, 333));
        Font.draw("Slime can make you glow.", screen, 4, 88, Color.get(0, 333, 333, 333));
        Font.draw("You can be changed by a touch!", screen, 4, 104, Color.get(0, 333, 333, 333));
        Font.draw("Fungus is easier to control with", screen, 4, 120, Color.get(0, 333, 333, 333));
        Font.draw("acorns than with axes.", screen, 4, 128, Color.get(0, 333, 333, 333));
        Font.draw("Cactus is often planted in rock", screen, 4, 144, Color.get(0, 333, 333, 333));
        Font.draw("gardens so if you dig a hole", screen, 4, 152, Color.get(0, 333, 333, 333));
        Font.draw("next to a cactus it will grow a", screen, 4, 160, Color.get(0, 333, 333, 333));
        Font.draw("rock!", screen, 4, 168, Color.get(0, 333, 333, 333));
    }
}
