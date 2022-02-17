// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.sound;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound
{
    public static final Sound playerHurt;
    public static final Sound playerDeath;
    public static final Sound monsterHurt;
    public static final Sound test;
    public static final Sound pickup;
    public static final Sound bossdeath;
    public static final Sound craft;
    private AudioClip clip;
    
    static {
        playerHurt = new Sound("/playerhurt.wav");
        playerDeath = new Sound("/death.wav");
        monsterHurt = new Sound("/monsterhurt.wav");
        test = new Sound("/test.wav");
        pickup = new Sound("/pickup.wav");
        bossdeath = new Sound("/bossdeath.wav");
        craft = new Sound("/craft.wav");
    }
    
    private Sound(final String name) {
        try {
            this.clip = Applet.newAudioClip(Sound.class.getResource(name));
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
    public void play() {
        try {
            new Thread() {
                @Override
                public void run() {
                    Sound.this.clip.play();
                }
            }.start();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
