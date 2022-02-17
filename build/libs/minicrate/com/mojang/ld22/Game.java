// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22;

import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import java.awt.Dimension;
import com.mojang.ld22.gfx.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.ImageObserver;
import java.awt.Image;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.level.tile.Tile;
import com.mojang.ld22.menu.WonMenu;
import com.mojang.ld22.menu.LevelTransitionMenu;
import com.mojang.ld22.menu.DeadMenu;
import com.mojang.ld22.menu.TitleMenu;
import java.io.IOException;
import com.mojang.ld22.gfx.SpriteSheet;
import javax.imageio.ImageIO;
import com.mojang.ld22.entity.Entity;
import java.awt.image.DataBufferInt;
import com.mojang.ld22.menu.Menu;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.gfx.Screen;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.awt.Canvas;

public class Game extends Canvas implements Runnable
{
    private static final long serialVersionUID = 1L;
    private Random random;
    public static final String NAME = "Minicrate";
    public static final int HEIGHT = 200;
    public static final int WIDTH = 300;
    private static final int SCALE = 3;
    private BufferedImage image;
    private int[] pixels;
    private boolean running;
    private Screen screen;
    private Screen lightScreen;
    private InputHandler input;
    private int[] colors;
    private int tickCount;
    public int gameTime;
    private Level level;
    private Level[] levels;
    private int currentLevel;
    public Player player;
    public Menu menu;
    private int playerDeadTime;
    private int pendingLevelChange;
    private int wonTimer;
    public boolean hasWon;
    public int[] levelLight;
    
    public Game() {
        this.random = new Random();
        this.image = new BufferedImage(300, 200, 1);
        this.pixels = ((DataBufferInt)this.image.getRaster().getDataBuffer()).getData();
        this.running = false;
        this.input = new InputHandler(this);
        this.colors = new int[256];
        this.tickCount = 0;
        this.gameTime = 0;
        this.levels = new Level[5];
        this.currentLevel = 3;
        this.wonTimer = 0;
        this.hasWon = false;
        this.levelLight = new int[] { 0, 0, 0, 1, 1 };
    }
    
    public void setMenu(final Menu menu) {
        this.menu = menu;
        if (menu != null) {
            menu.init(this, this.input);
        }
    }
    
    public int playerScore() {
        if (this.level.player != null) {
            return this.level.player.score;
        }
        return 0;
    }
    
    public int cheaterLevel() {
        if (this.level.player != null) {
            return this.player.isTest;
        }
        return 0;
    }
    
    public void start() {
        this.running = true;
        new Thread(this).start();
    }
    
    public void stop() {
        this.running = false;
    }
    
    public void resetGame() {
        this.playerDeadTime = 0;
        this.wonTimer = 0;
        this.gameTime = 0;
        this.hasWon = false;
        this.levels = new Level[5];
        this.currentLevel = 3;
        this.levels[4] = new Level(128, 128, 1, null);
        this.levels[3] = new Level(128, 128, 0, this.levels[4]);
        this.levels[2] = new Level(128, 128, -1, this.levels[3]);
        this.levels[1] = new Level(128, 128, -2, this.levels[2]);
        this.levels[0] = new Level(128, 128, -3, this.levels[1]);
        this.level = this.levels[this.currentLevel];
        (this.player = new Player(this, this.input)).findStartPos(this.level);
        this.level.add(this.player);
        for (int i = 0; i < 5; ++i) {
            this.levels[i].trySpawn(5000);
        }
    }
    
    private void init() {
        int pp = 0;
        for (int r = 0; r < 6; ++r) {
            for (int g = 0; g < 6; ++g) {
                for (int b = 0; b < 6; ++b) {
                    final int rr = r * 255 / 5;
                    final int gg = g * 255 / 5;
                    final int bb = b * 255 / 5;
                    final int mid = (rr * 30 + gg * 59 + bb * 11) / 100;
                    final int r2 = (rr + mid * 1) / 2 * 230 / 255 + 10;
                    final int g2 = (gg + mid * 1) / 2 * 230 / 255 + 10;
                    final int b2 = (bb + mid * 1) / 2 * 230 / 255 + 10;
                    this.colors[pp++] = (r2 << 16 | g2 << 8 | b2);
                }
            }
        }
        try {
            this.screen = new Screen(300, 200, new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("/icons.png"))));
            this.lightScreen = new Screen(300, 200, new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("/icons.png"))));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.resetGame();
        this.setMenu(new TitleMenu());
    }
    
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double unprocessed = 0.0;
        final double nsPerTick = 1.6666666666666666E7;
        int frames = 0;
        int ticks = 0;
        long lastTimer1 = System.currentTimeMillis();
        this.init();
        while (this.running) {
            final long now = System.nanoTime();
            unprocessed += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shouldRender = true;
            while (unprocessed >= 1.0) {
                ++ticks;
                this.tick();
                --unprocessed;
                shouldRender = true;
            }
            try {
                Thread.sleep(2L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (shouldRender) {
                ++frames;
                this.render();
            }
            if (System.currentTimeMillis() - lastTimer1 > 1000L) {
                lastTimer1 += 1000L;
                System.out.println(String.valueOf(ticks) + " ticks, " + frames + " fps");
                frames = 0;
                ticks = 0;
            }
        }
    }
    
    public void tick() {
        ++this.tickCount;
        if (!this.hasFocus()) {
            this.input.releaseAll();
        }
        else {
            if (!this.player.removed && !this.hasWon) {
                ++this.gameTime;
            }
            this.input.tick();
            if (this.menu != null) {
                this.menu.tick();
            }
            else {
                if (this.player.removed) {
                    ++this.playerDeadTime;
                    if (this.playerDeadTime > 60) {
                        this.setMenu(new DeadMenu());
                    }
                }
                else if (this.pendingLevelChange != 0) {
                    this.setMenu(new LevelTransitionMenu(this.pendingLevelChange));
                    this.pendingLevelChange = 0;
                }
                if (this.wonTimer > 0 && --this.wonTimer == 0) {
                    this.setMenu(new WonMenu());
                }
                this.level.tick();
                ++Tile.tickCount;
            }
        }
    }
    
    public void changeLevel(final int dir) {
        this.level.remove(this.player);
        this.currentLevel += dir;
        this.level = this.levels[this.currentLevel];
        this.player.x = (this.player.x >> 4) * 16 + 8;
        this.player.y = (this.player.y >> 4) * 16 + 8;
        this.level.add(this.player);
    }
    
    public int currentDepth() {
        return this.level.whatDepth();
    }
    
    public void makeDark() {
        this.levelLight[this.currentDepth() + 3] = 0;
    }
    
    public void makeLight() {
        this.levelLight[this.currentDepth() + 3] = 1;
    }
    
    public void render() {
        final BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            this.requestFocus();
            return;
        }
        int xScroll = this.player.x - this.screen.w / 2;
        int yScroll = this.player.y - (this.screen.h - 8) / 2;
        if (xScroll < 16) {
            xScroll = 16;
        }
        if (yScroll < 16) {
            yScroll = 16;
        }
        if (xScroll > this.level.w * 16 - this.screen.w - 16) {
            xScroll = this.level.w * 16 - this.screen.w - 16;
        }
        if (yScroll > this.level.h * 16 - this.screen.h - 16) {
            yScroll = this.level.h * 16 - this.screen.h - 16;
        }
        if (this.currentLevel > 3) {
            final int col = Color.get(20, 20, 121, 121);
            for (int y = 0; y < 14; ++y) {
                for (int x = 0; x < 24; ++x) {
                    this.screen.render(x * 8 - (xScroll / 4 & 0x7), y * 8 - (yScroll / 4 & 0x7), 0, col, 0);
                }
            }
        }
        this.level.renderBackground(this.screen, xScroll, yScroll);
        this.level.renderSprites(this.screen, xScroll, yScroll);
        if (this.levelLight[this.currentDepth() + 3] == 0) {
            this.lightScreen.clear(0);
            this.level.renderLight(this.lightScreen, xScroll, yScroll);
            this.screen.overlay(this.lightScreen, xScroll, yScroll);
        }
        this.renderGui();
        if (!this.hasFocus()) {
            this.renderFocusNagger();
        }
        for (int y2 = 0; y2 < this.screen.h; ++y2) {
            for (int x2 = 0; x2 < this.screen.w; ++x2) {
                final int cc = this.screen.pixels[x2 + y2 * this.screen.w];
                if (cc < 255) {
                    this.pixels[x2 + y2 * 300] = this.colors[cc];
                }
            }
        }
        final Graphics g = bs.getDrawGraphics();
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        final int ww = 900;
        final int hh = 600;
        final int xo = (this.getWidth() - ww) / 2;
        final int yo = (this.getHeight() - hh) / 2;
        g.drawImage(this.image, xo, yo, ww, hh, null);
        g.dispose();
        bs.show();
    }
    
    private void renderGui() {
        for (int y = 0; y < 2; ++y) {
            for (int x = 0; x < 20; ++x) {
                this.screen.render(x * 8, this.screen.h - 16 + y * 8, 384, Color.get(0, 0, 0, 0), 0);
            }
        }
        for (int i = 0; i < 10; ++i) {
            if (i < this.player.health) {
                this.screen.render(i * 8, this.screen.h - 16, 384, Color.get(0, 200, 500, 533), 0);
            }
            else {
                this.screen.render(i * 8, this.screen.h - 16, 384, Color.get(0, 100, 0, 0), 0);
            }
            if (this.player.staminaRechargeDelay > 0) {
                if (this.player.staminaRechargeDelay / 4 % 2 == 0) {
                    this.screen.render(i * 8, this.screen.h - 8, 385, Color.get(0, 555, 0, 0), 0);
                }
                else {
                    this.screen.render(i * 8, this.screen.h - 8, 385, Color.get(0, 110, 0, 0), 0);
                }
            }
            else if (i < this.player.stamina) {
                this.screen.render(i * 8, this.screen.h - 8, 385, Color.get(0, 220, 550, 553), 0);
            }
            else {
                this.screen.render(i * 8, this.screen.h - 8, 385, Color.get(0, 110, 0, 0), 0);
            }
        }
        if (this.player.activeItem != null) {
            this.player.activeItem.renderInventory(this.screen, 80, this.screen.h - 16);
        }
        if (this.menu != null) {
            this.menu.render(this.screen);
        }
    }
    
    public void stopFire() {
        this.level.stopFire();
    }
    
    private void renderFocusNagger() {
        final String msg = "Click to focus!";
        final int xx = (300 - msg.length() * 8) / 2;
        final int yy = 96;
        final int w = msg.length();
        final int h = 1;
        this.screen.render(xx - 8, yy - 8, 416, Color.get(-1, 1, 5, 445), 0);
        this.screen.render(xx + w * 8, yy - 8, 416, Color.get(-1, 1, 5, 445), 1);
        this.screen.render(xx - 8, yy + 8, 416, Color.get(-1, 1, 5, 445), 2);
        this.screen.render(xx + w * 8, yy + 8, 416, Color.get(-1, 1, 5, 445), 3);
        for (int x = 0; x < w; ++x) {
            this.screen.render(xx + x * 8, yy - 8, 417, Color.get(-1, 1, 5, 445), 0);
            this.screen.render(xx + x * 8, yy + 8, 417, Color.get(-1, 1, 5, 445), 2);
        }
        for (int y = 0; y < h; ++y) {
            this.screen.render(xx - 8, yy + y * 8, 418, Color.get(-1, 1, 5, 445), 0);
            this.screen.render(xx + w * 8, yy + y * 8, 418, Color.get(-1, 1, 5, 445), 1);
        }
        if (this.tickCount / 20 % 2 == 0) {
            Font.draw(msg, this.screen, xx, yy, Color.get(5, 333, 333, 333));
        }
        else {
            Font.draw(msg, this.screen, xx, yy, Color.get(5, 555, 555, 555));
        }
    }
    
    public void scheduleLevelChange(final int dir) {
        this.pendingLevelChange = dir;
    }
    
    public static void main(final String[] args) {
        final Game game = new Game();
        game.setMinimumSize(new Dimension(900, 600));
        game.setMaximumSize(new Dimension(900, 600));
        game.setPreferredSize(new Dimension(900, 600));
        final JFrame frame = new JFrame("Minicrate");
        frame.setDefaultCloseOperation(3);
        frame.setLayout(new BorderLayout());
        frame.add(game, "Center");
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        game.start();
    }
    
    public void won() {
        this.wonTimer = 180;
        this.hasWon = true;
    }
}
