// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.entity.particle;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.sound.Sound;
import com.mojang.ld22.entity.Entity;

public class SmashParticle extends Entity
{
    private int time;
    
    public SmashParticle(final int x, final int y) {
        this.time = 0;
        this.x = x;
        this.y = y;
        Sound.monsterHurt.play();
    }
    
    @Override
    public void tick() {
        ++this.time;
        if (this.time > 10) {
            this.remove();
        }
    }
    
    @Override
    public void render(final Screen screen) {
        final int col = Color.get(-1, 555, 555, 555);
        screen.render(this.x - 8, this.y - 8, 389, col, 2);
        screen.render(this.x - 0, this.y - 8, 389, col, 3);
        screen.render(this.x - 8, this.y - 0, 389, col, 0);
        screen.render(this.x - 0, this.y - 0, 389, col, 1);
    }
}
