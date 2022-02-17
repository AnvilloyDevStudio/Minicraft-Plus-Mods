// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import java.util.List;

public class Zap extends Entity
{
    private int lifeTime;
    public double xa;
    public double ya;
    public double xx;
    public double yy;
    private int time;
    private BlastMage owner;
    int rotation;
    int spin;
    
    public Zap(final BlastMage owner, final double xa, final double ya) {
        this.rotation = 0;
        this.spin = 0;
        this.owner = owner;
        final int x = owner.x;
        this.x = x;
        this.xx = x;
        final int y = owner.y;
        this.y = y;
        this.yy = y;
        this.xr = 0;
        this.yr = 0;
        this.xa = xa;
        this.ya = ya;
        this.lifeTime = 600 + this.random.nextInt(30);
    }
    
    @Override
    public void tick() {
        ++this.time;
        if (this.time >= this.lifeTime) {
            this.remove();
            return;
        }
        this.xx += this.xa;
        this.yy += this.ya;
        this.x = (int)this.xx;
        this.y = (int)this.yy;
        final List<Entity> toHit = this.level.getEntities(this.x, this.y, this.x, this.y);
        for (int i = 0; i < toHit.size(); ++i) {
            final Entity e = toHit.get(i);
            if (e instanceof Mob && !(e instanceof BlastMage)) {
                e.hurt(this.owner, 1, ((Mob)e).dir ^ 0x1);
            }
        }
    }
    
    @Override
    public boolean isBlockableBy(final Mob mob) {
        return true;
    }
    
    @Override
    public void render(final Screen screen) {
        if (this.time >= this.lifeTime - 120 && this.time / 6 % 2 == 0) {
            return;
        }
        final int xt = 9;
        final int yt = 13;
        ++this.rotation;
        if (this.rotation > 35) {
            this.rotation = 0;
        }
        this.spin = this.rotation / 9;
        screen.render(this.x - 4, this.y - 4 - 2, xt + yt * 32, Color.get(-1, 530, 551, 554), this.spin);
    }
}
