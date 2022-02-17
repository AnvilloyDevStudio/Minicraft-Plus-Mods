// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22;

import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import java.applet.Applet;

public class GameApplet extends Applet
{
    private static final long serialVersionUID = 1L;
    private Game game;
    
    public GameApplet() {
        this.game = new Game();
    }
    
    @Override
    public void init() {
        this.setLayout(new BorderLayout());
        this.add(this.game, "Center");
    }
    
    @Override
    public void start() {
        this.game.start();
    }
    
    @Override
    public void stop() {
        this.game.stop();
    }
}
