// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener
{
    public List<Key> keys;
    public Key up;
    public Key down;
    public Key left;
    public Key right;
    public Key attack;
    public Key menu;
    public Key plant;
    public Key cheat;
    
    public void releaseAll() {
        for (int i = 0; i < this.keys.size(); ++i) {
            this.keys.get(i).down = false;
        }
    }
    
    public void tick() {
        for (int i = 0; i < this.keys.size(); ++i) {
            this.keys.get(i).tick();
        }
    }
    
    public InputHandler(final Game game) {
        this.keys = new ArrayList<Key>();
        this.up = new Key();
        this.down = new Key();
        this.left = new Key();
        this.right = new Key();
        this.attack = new Key();
        this.menu = new Key();
        this.plant = new Key();
        this.cheat = new Key();
        game.addKeyListener(this);
    }
    
    @Override
    public void keyPressed(final KeyEvent ke) {
        this.toggle(ke, true);
    }
    
    @Override
    public void keyReleased(final KeyEvent ke) {
        this.toggle(ke, false);
    }
    
    private void toggle(final KeyEvent ke, final boolean pressed) {
        if (ke.getKeyCode() == 104) {
            this.up.toggle(pressed);
        }
        if (ke.getKeyCode() == 98) {
            this.down.toggle(pressed);
        }
        if (ke.getKeyCode() == 100) {
            this.left.toggle(pressed);
        }
        if (ke.getKeyCode() == 102) {
            this.right.toggle(pressed);
        }
        if (ke.getKeyCode() == 87) {
            this.up.toggle(pressed);
        }
        if (ke.getKeyCode() == 83) {
            this.down.toggle(pressed);
        }
        if (ke.getKeyCode() == 65) {
            this.left.toggle(pressed);
        }
        if (ke.getKeyCode() == 68) {
            this.right.toggle(pressed);
        }
        if (ke.getKeyCode() == 38) {
            this.up.toggle(pressed);
        }
        if (ke.getKeyCode() == 40) {
            this.down.toggle(pressed);
        }
        if (ke.getKeyCode() == 37) {
            this.left.toggle(pressed);
        }
        if (ke.getKeyCode() == 39) {
            this.right.toggle(pressed);
        }
        if (ke.getKeyCode() == 9) {
            this.menu.toggle(pressed);
        }
        if (ke.getKeyCode() == 18) {
            this.menu.toggle(pressed);
        }
        if (ke.getKeyCode() == 65406) {
            this.menu.toggle(pressed);
        }
        if (ke.getKeyCode() == 32) {
            this.attack.toggle(pressed);
        }
        if (ke.getKeyCode() == 17) {
            this.attack.toggle(pressed);
        }
        if (ke.getKeyCode() == 96) {
            this.attack.toggle(pressed);
        }
        if (ke.getKeyCode() == 155) {
            this.attack.toggle(pressed);
        }
        if (ke.getKeyCode() == 10) {
            this.menu.toggle(pressed);
        }
        if (ke.getKeyCode() == 88) {
            this.menu.toggle(pressed);
        }
        if (ke.getKeyCode() == 67) {
            this.attack.toggle(pressed);
        }
        if (ke.getKeyCode() == 80) {
            this.plant.toggle(pressed);
        }
        if (ke.getKeyCode() == 84) {
            this.cheat.toggle(pressed);
        }
    }
    
    @Override
    public void keyTyped(final KeyEvent ke) {
    }
    
    public class Key
    {
        public int presses;
        public int absorbs;
        public boolean down;
        public boolean clicked;
        
        public Key() {
            InputHandler.this.keys.add(this);
        }
        
        public void toggle(final boolean pressed) {
            if (pressed != this.down) {
                this.down = pressed;
            }
            if (pressed) {
                ++this.presses;
            }
        }
        
        public void tick() {
            if (this.absorbs < this.presses) {
                ++this.absorbs;
                this.clicked = true;
            }
            else {
                this.clicked = false;
            }
        }
    }
}
