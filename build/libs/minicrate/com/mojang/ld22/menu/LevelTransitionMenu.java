// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.menu;

import com.mojang.ld22.gfx.Screen;

public class LevelTransitionMenu extends Menu
{
    private int dir;
    private int time;
    
    public LevelTransitionMenu(final int dir) {
        this.time = 0;
        this.dir = dir;
    }
    
    @Override
    public void tick() {
        this.time += 2;
        if (this.time == 30) {
            this.game.changeLevel(this.dir);
        }
        if (this.time == 60) {
            this.game.setMenu(null);
        }
    }
    
    @Override
    public void render(final Screen screen) {
        for (int x = 0; x < 20; ++x) {
            for (int y = 0; y < 15; ++y) {
                final int dd = y + x % 2 * 2 + x / 3 - this.time;
                if (dd < 0 && dd > -30) {
                    if (this.dir > 0) {
                        screen.render(x * 8, y * 8, 0, 0, 0);
                    }
                    else {
                        screen.render(x * 8, screen.h - y * 8 - 8, 0, 0, 0);
                    }
                }
            }
        }
    }
}
