// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.entity.particle;

import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.entity.Entity;

public class TextParticle extends Entity
{
    private String msg;
    private int col;
    private int time;
    public double xa;
    public double ya;
    public double za;
    public double xx;
    public double yy;
    public double zz;
    
    public TextParticle(final String msg, final int x, final int y, final int col) {
        this.time = 0;
        this.msg = msg;
        this.x = x;
        this.y = y;
        this.col = col;
        this.xx = x;
        this.yy = y;
        this.zz = 2.0;
        this.xa = this.random.nextGaussian() * 0.3;
        this.ya = this.random.nextGaussian() * 0.2;
        this.za = this.random.nextFloat() * 0.7 + 2.0;
    }
    
    @Override
    public void tick() {
        ++this.time;
        if (this.time > 60) {
            this.remove();
        }
        this.xx += this.xa;
        this.yy += this.ya;
        this.zz += this.za;
        if (this.zz < 0.0) {
            this.zz = 0.0;
            this.za *= -0.5;
            this.xa *= 0.6;
            this.ya *= 0.6;
        }
        this.za -= 0.15;
        this.x = (int)this.xx;
        this.y = (int)this.yy;
    }
    
    @Override
    public void render(final Screen screen) {
        Font.draw(this.msg, screen, this.x - this.msg.length() * 4 + 1, this.y - (int)this.zz + 1, Color.get(-1, 0, 0, 0));
        Font.draw(this.msg, screen, this.x - this.msg.length() * 4, this.y - (int)this.zz, this.col);
    }
}
