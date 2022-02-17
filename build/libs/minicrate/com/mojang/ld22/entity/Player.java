// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.entity;

import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.ToolType;
import com.mojang.ld22.entity.particle.TextParticle;
import com.mojang.ld22.sound.Sound;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import java.util.List;
import com.mojang.ld22.menu.Menu;
import com.mojang.ld22.menu.InventoryMenu;
import com.mojang.ld22.level.tile.Tile;
import com.mojang.ld22.item.PowerGloveItem;
import com.mojang.ld22.item.FurnitureItem;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.Game;
import com.mojang.ld22.InputHandler;

public class Player extends Mob
{
    private InputHandler input;
    private int attackTime;
    private int attackDir;
    public Game game;
    public Inventory inventory;
    public Item attackItem;
    public Item activeItem;
    public int stamina;
    public int staminaRecharge;
    public int staminaRechargeDelay;
    public int score;
    public int maxStamina;
    private int onStairDelay;
    public int invulnerableTime;
    public int personGlow;
    public int shroomAbuse;
    public int isTest;
    int isTest2;
    int isTest3;
    public int woodsman;
    public int seaCaptain;
    public int playtest;
    int baseShape;
    
    public Player(final Game game, final InputHandler input) {
        this.inventory = new Inventory();
        this.maxStamina = 10;
        this.invulnerableTime = 0;
        this.personGlow = 0;
        this.shroomAbuse = 0;
        this.isTest = 0;
        this.isTest2 = 0;
        this.isTest3 = 0;
        this.woodsman = 0;
        this.seaCaptain = 0;
        this.playtest = 0;
        this.baseShape = 0;
        this.game = game;
        this.input = input;
        this.x = 24;
        this.y = 24;
        this.stamina = this.maxStamina;
        this.inventory.add(new FurnitureItem(new Workbench()));
        this.inventory.add(new PowerGloveItem());
        if (this.isTest2 == 1) {
            this.testInventory();
        }
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.invulnerableTime > 0) {
            --this.invulnerableTime;
        }
        if (this.isTest3 == 1) {
            this.invulnerableTime = 10;
        }
        final Tile onTile = this.level.getTile(this.x >> 4, this.y >> 4);
        if (onTile == Tile.stairsDown || onTile == Tile.stairsUp) {
            if (this.onStairDelay == 0) {
                this.changeLevel((onTile == Tile.stairsUp) ? 1 : -1);
                this.onStairDelay = 10;
                return;
            }
            this.onStairDelay = 10;
        }
        else if (this.onStairDelay > 0) {
            --this.onStairDelay;
        }
        if (this.stamina <= 0 && this.staminaRechargeDelay == 0 && this.staminaRecharge == 0) {
            this.staminaRechargeDelay = 40;
        }
        if (this.staminaRechargeDelay > 0) {
            --this.staminaRechargeDelay;
        }
        if (this.staminaRechargeDelay == 0) {
            ++this.staminaRecharge;
            if (this.isSwimming()) {
                this.staminaRecharge = 0;
            }
            while (this.staminaRecharge > 10) {
                this.staminaRecharge -= 10;
                if (this.stamina < this.maxStamina) {
                    ++this.stamina;
                }
            }
        }
        int xa = 0;
        int ya = 0;
        if (this.input.up.down) {
            --ya;
        }
        if (this.input.down.down) {
            ++ya;
        }
        if (this.input.left.down) {
            --xa;
        }
        if (this.input.right.down) {
            ++xa;
        }
        if (this.isSwimming() && this.tickTime % 60 == 0) {
            if (this.stamina > 0) {
                --this.stamina;
            }
            else {
                this.hurt(this, 1, this.dir ^ 0x1);
            }
        }
        if (this.staminaRechargeDelay % 2 == 0) {
            this.move(xa, ya);
        }
        if (this.input.attack.clicked && this.stamina != 0) {
            --this.stamina;
            this.staminaRecharge = 0;
            this.attack();
        }
        if (this.input.menu.clicked && !this.use()) {
            this.game.setMenu(new InventoryMenu(this));
        }
        if (this.input.cheat.clicked) {
            this.cheat();
        }
        if (this.attackTime > 0) {
            --this.attackTime;
        }
    }
    
    private boolean use() {
        final int yo = -2;
        if (this.dir == 0 && this.use(this.x - 8, this.y + 4 + yo, this.x + 8, this.y + 12 + yo)) {
            return true;
        }
        if (this.dir == 1 && this.use(this.x - 8, this.y - 12 + yo, this.x + 8, this.y - 4 + yo)) {
            return true;
        }
        if (this.dir == 3 && this.use(this.x + 4, this.y - 8 + yo, this.x + 12, this.y + 8 + yo)) {
            return true;
        }
        if (this.dir == 2 && this.use(this.x - 12, this.y - 8 + yo, this.x - 4, this.y + 8 + yo)) {
            return true;
        }
        int xt = this.x >> 4;
        int yt = this.y + yo >> 4;
        final int r = 12;
        if (this.attackDir == 0) {
            yt = this.y + r + yo >> 4;
        }
        if (this.attackDir == 1) {
            yt = this.y - r + yo >> 4;
        }
        if (this.attackDir == 2) {
            xt = this.x - r >> 4;
        }
        if (this.attackDir == 3) {
            xt = this.x + r >> 4;
        }
        return xt >= 0 && yt >= 0 && xt < this.level.w && yt < this.level.h && this.level.getTile(xt, yt).use(this.level, xt, yt, this, this.attackDir);
    }
    
    private void attack() {
        this.walkDist += 8;
        this.attackDir = this.dir;
        this.attackItem = this.activeItem;
        boolean done = false;
        if (this.activeItem != null) {
            this.attackTime = 10;
            final int yo = -2;
            final int range = 12;
            if (this.dir == 0 && this.interact(this.x - 8, this.y + 4 + yo, this.x + 8, this.y + range + yo)) {
                done = true;
            }
            if (this.dir == 1 && this.interact(this.x - 8, this.y - range + yo, this.x + 8, this.y - 4 + yo)) {
                done = true;
            }
            if (this.dir == 3 && this.interact(this.x + 4, this.y - 8 + yo, this.x + range, this.y + 8 + yo)) {
                done = true;
            }
            if (this.dir == 2 && this.interact(this.x - range, this.y - 8 + yo, this.x - 4, this.y + 8 + yo)) {
                done = true;
            }
            if (done) {
                return;
            }
            int xt = this.x >> 4;
            int yt = this.y + yo >> 4;
            final int r = 12;
            if (this.attackDir == 0) {
                yt = this.y + r + yo >> 4;
            }
            if (this.attackDir == 1) {
                yt = this.y - r + yo >> 4;
            }
            if (this.attackDir == 2) {
                xt = this.x - r >> 4;
            }
            if (this.attackDir == 3) {
                xt = this.x + r >> 4;
            }
            if (xt >= 0 && yt >= 0 && xt < this.level.w && yt < this.level.h) {
                if (this.activeItem.interactOn(this.level.getTile(xt, yt), this.level, xt, yt, this, this.attackDir)) {
                    done = true;
                }
                else if (this.level.getTile(xt, yt).interact(this.level, xt, yt, this, this.activeItem, this.attackDir)) {
                    done = true;
                }
                if (this.activeItem.isDepleted()) {
                    this.activeItem = null;
                }
            }
        }
        if (done) {
            return;
        }
        if (this.activeItem == null || this.activeItem.canAttack()) {
            this.attackTime = 5;
            final int yo = -2;
            final int range = 20;
            if (this.dir == 0) {
                this.hurt(this.x - 8, this.y + 4 + yo, this.x + 8, this.y + range + yo);
            }
            if (this.dir == 1) {
                this.hurt(this.x - 8, this.y - range + yo, this.x + 8, this.y - 4 + yo);
            }
            if (this.dir == 3) {
                this.hurt(this.x + 4, this.y - 8 + yo, this.x + range, this.y + 8 + yo);
            }
            if (this.dir == 2) {
                this.hurt(this.x - range, this.y - 8 + yo, this.x - 4, this.y + 8 + yo);
            }
            int xt = this.x >> 4;
            int yt = this.y + yo >> 4;
            final int r = 12;
            if (this.attackDir == 0) {
                yt = this.y + r + yo >> 4;
            }
            if (this.attackDir == 1) {
                yt = this.y - r + yo >> 4;
            }
            if (this.attackDir == 2) {
                xt = this.x - r >> 4;
            }
            if (this.attackDir == 3) {
                xt = this.x + r >> 4;
            }
            if (xt >= 0 && yt >= 0 && xt < this.level.w && yt < this.level.h) {
                this.level.getTile(xt, yt).hurt(this.level, xt, yt, this, this.random.nextInt(3) + 1, this.attackDir);
            }
        }
    }
    
    private boolean use(final int x0, final int y0, final int x1, final int y1) {
        final List<Entity> entities = this.level.getEntities(x0, y0, x1, y1);
        for (int i = 0; i < entities.size(); ++i) {
            final Entity e = entities.get(i);
            if (e != this && e.use(this, this.attackDir)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean interact(final int x0, final int y0, final int x1, final int y1) {
        final List<Entity> entities = this.level.getEntities(x0, y0, x1, y1);
        for (int i = 0; i < entities.size(); ++i) {
            final Entity e = entities.get(i);
            if (e != this && e.interact(this, this.activeItem, this.attackDir)) {
                return true;
            }
        }
        return false;
    }
    
    private void hurt(final int x0, final int y0, final int x1, final int y1) {
        final List<Entity> entities = this.level.getEntities(x0, y0, x1, y1);
        for (int i = 0; i < entities.size(); ++i) {
            final Entity e = entities.get(i);
            if (e != this) {
                e.hurt(this, this.getAttackDamage(e), this.attackDir);
            }
        }
    }
    
    private int getAttackDamage(final Entity e) {
        int dmg = this.random.nextInt(3) + 1;
        if (this.attackItem != null) {
            dmg += this.attackItem.getAttackDamageBonus(e);
        }
        return dmg;
    }
    
    @Override
    public void render(final Screen screen) {
        int xt = 0;
        int yt = 14;
        int pColor = Color.get(-1, 100, 220, 532);
        if (Player.playerShape == 0) {
            xt = 0;
            yt = 39;
            pColor = Color.get(-1, 100, 220, 532);
        }
        else if (Player.playerShape == 1) {
            xt = 16;
            yt = 14;
            pColor = Color.get(-1, 100, 220, 532);
        }
        else if (Player.playerShape == 2) {
            xt = 8;
            yt = 14;
            pColor = Color.get(-1, 100, 220, 532);
        }
        else if (Player.playerShape == 3) {
            xt = 24;
            yt = 14;
            pColor = Color.get(-1, 100, 220, 532);
        }
        else if (Player.playerShape == 4) {
            xt = 0;
            yt = 26;
            pColor = Color.get(-1, 100, 220, 532);
        }
        else if (Player.playerShape == 5) {
            xt = 8;
            yt = 26;
            pColor = Color.get(-1, 100, 220, 532);
        }
        else if (Player.playerShape == 6) {
            xt = 8;
            yt = 22;
            pColor = Color.get(-1, 100, 220, 532);
        }
        else if (Player.playerShape == 7) {
            xt = 16;
            yt = 20;
            pColor = Color.get(-1, 100, 220, 532);
        }
        else if (Player.playerShape == 8) {
            xt = 24;
            yt = 26;
            pColor = Color.get(-1, 100, 555, 532);
        }
        else if (Player.playerShape == 8) {
            xt = 16;
            yt = 26;
            pColor = Color.get(-1, 100, 220, 532);
        }
        else if (Player.playerShape == 8) {
            xt = 24;
            yt = 24;
            pColor = Color.get(-1, 100, 220, 532);
        }
        else if (Player.playerShape == 9) {
            xt = 8;
            yt = 39;
            pColor = Color.get(-1, 100, 220, 532);
        }
        else if (Player.playerShape == 10) {
            xt = 0;
            yt = 43;
            pColor = Color.get(-1, 510, 220, 532);
        }
        else if (Player.playerShape == 11) {
            xt = 0;
            yt = 39;
            pColor = Color.get(-1, 100, 220, 532);
        }
        else {
            Player.playerShape = this.baseShape;
            xt = 0;
            yt = 14;
            pColor = Color.get(-1, 100, 220, 532);
        }
        int flip1 = this.walkDist >> 3 & 0x1;
        int flip2 = this.walkDist >> 3 & 0x1;
        if (this.dir == 1) {
            xt += 2;
        }
        if (this.dir > 1) {
            flip1 = 0;
            flip2 = (this.walkDist >> 4 & 0x1);
            if (this.dir == 2) {
                flip1 = 1;
            }
            xt += 4 + (this.walkDist >> 3 & 0x1) * 2;
        }
        final int xo = this.x - 8;
        int yo = this.y - 11;
        if (this.isSwimming()) {
            if (this.seaCaptain == 1) {
                Player.playerShape = 7;
            }
            if (Player.playerShape == 6) {
                Player.playerShape = this.baseShape;
            }
            yo += 4;
            int waterColor = Color.get(-1, -1, 115, 335);
            if (this.tickTime / 8 % 2 == 0) {
                waterColor = Color.get(-1, 335, 5, 115);
            }
            screen.render(xo + 0, yo + 3, 421, waterColor, 0);
            screen.render(xo + 8, yo + 3, 421, waterColor, 1);
        }
        if (this.attackTime > 0 && this.attackDir == 1) {
            screen.render(xo + 0, yo - 4, 422, Color.get(-1, 555, 555, 555), 0);
            screen.render(xo + 8, yo - 4, 422, Color.get(-1, 555, 555, 555), 1);
            if (this.attackItem != null) {
                this.attackItem.renderIcon(screen, xo + 4, yo - 4);
            }
        }
        int col = pColor;
        if (this.hurtTime > 0) {
            col = Color.get(-1, 555, 555, 555);
        }
        if (this.activeItem instanceof FurnitureItem) {
            yt += 2;
            if (((FurnitureItem)this.activeItem).furniture.isWhiteOrb()) {
                this.setShape();
            }
        }
        screen.render(xo + 8 * flip1, yo + 0, xt + yt * 32, col, flip1);
        screen.render(xo + 8 - 8 * flip1, yo + 0, xt + 1 + yt * 32, col, flip1);
        if (!this.isSwimmer() && !this.isSwimming()) {
            screen.render(xo + 8 * flip2, yo + 8, xt + (yt + 1) * 32, col, flip2);
            screen.render(xo + 8 - 8 * flip2, yo + 8, xt + 1 + (yt + 1) * 32, col, flip2);
        }
        if (this.level.getTile(this.x >> 4, this.y >> 4) != Tile.water) {
            if (Player.playerShape == 7) {
                Player.playerShape = this.baseShape;
                if (this.baseShape == 7) {
                    Player.playerShape = 0;
                }
            }
        }
        else if (this.seaCaptain == 1) {
            Player.playerShape = 7;
        }
        else if (this.baseShape == 7) {
            Player.playerShape = 7;
        }
        if (this.attackTime > 0 && this.attackDir == 2) {
            screen.render(xo - 4, yo, 423, Color.get(-1, 555, 555, 555), 1);
            screen.render(xo - 4, yo + 8, 423, Color.get(-1, 555, 555, 555), 3);
            if (this.attackItem != null) {
                this.attackItem.renderIcon(screen, xo - 4, yo + 4);
            }
        }
        if (this.attackTime > 0 && this.attackDir == 3) {
            screen.render(xo + 8 + 4, yo, 423, Color.get(-1, 555, 555, 555), 0);
            screen.render(xo + 8 + 4, yo + 8, 423, Color.get(-1, 555, 555, 555), 2);
            if (this.attackItem != null) {
                this.attackItem.renderIcon(screen, xo + 8 + 4, yo + 4);
            }
        }
        if (this.attackTime > 0 && this.attackDir == 0) {
            screen.render(xo + 0, yo + 8 + 4, 422, Color.get(-1, 555, 555, 555), 2);
            screen.render(xo + 8, yo + 8 + 4, 422, Color.get(-1, 555, 555, 555), 3);
            if (this.attackItem != null) {
                this.attackItem.renderIcon(screen, xo + 4, yo + 8 + 4);
            }
        }
        if (this.activeItem instanceof FurnitureItem) {
            final Furniture furniture = ((FurnitureItem)this.activeItem).furniture;
            furniture.x = this.x;
            furniture.y = yo;
            furniture.render(screen);
        }
    }
    
    @Override
    public void touchItem(final ItemEntity itemEntity) {
        itemEntity.take(this);
        this.inventory.add(itemEntity.item);
    }
    
    @Override
    public boolean canSwim() {
        return true;
    }
    
    @Override
    public boolean canWalk() {
        return true;
    }
    
    @Override
    public boolean canDig() {
        return true;
    }
    
    @Override
    public boolean isPlayer() {
        return true;
    }
    
    @Override
    public boolean canClimb() {
        return Player.playerShape == 5 || this.woodsman == 1;
    }
    
    @Override
    public boolean canPass() {
        return this.holdingWhiteOrb();
    }
    
    @Override
    public boolean isSwimmer() {
        return Player.playerShape == 7;
    }
    
    @Override
    public boolean holdingOrb() {
        boolean gotIt = false;
        if (this.activeItem != null && this.activeItem instanceof FurnitureItem && ((FurnitureItem)this.activeItem).furniture.isOrb()) {
            gotIt = true;
        }
        return gotIt;
    }
    
    @Override
    public boolean holdingGreenOrb() {
        boolean gotIt = false;
        if (this.activeItem != null && this.activeItem instanceof FurnitureItem && ((FurnitureItem)this.activeItem).furniture.isGreenOrb()) {
            gotIt = true;
        }
        return gotIt;
    }
    
    @Override
    public boolean holdingWhiteOrb() {
        boolean gotIt = false;
        if (this.activeItem != null && this.activeItem instanceof FurnitureItem && ((FurnitureItem)this.activeItem).furniture.isWhiteOrb()) {
            gotIt = true;
        }
        return gotIt;
    }
    
    public boolean isCheater() {
        return this.isTest != 0;
    }
    
    @Override
    public boolean findStartPos(final Level level) {
        int x;
        int y;
        do {
            x = this.random.nextInt(level.w);
            y = this.random.nextInt(level.h);
        } while (level.getTile(x, y) != Tile.grass);
        this.x = x * 16 + 8;
        this.y = y * 16 + 8;
        return true;
    }
    
    public boolean payStamina(final int cost) {
        if (cost > this.stamina) {
            return false;
        }
        this.stamina -= cost;
        --this.personGlow;
        if (this.personGlow < 0) {
            this.personGlow = 0;
        }
        return true;
    }
    
    public void changeLevel(final int dir) {
        this.game.scheduleLevelChange(dir);
    }
    
    @Override
    public int getLightRadius() {
        int r = 2;
        if (Player.playerShape == 1) {
            r = 3;
        }
        if (Player.playerShape == 6) {
            r = 5;
        }
        r += this.personGlow;
        if (this.activeItem != null && this.activeItem instanceof FurnitureItem) {
            final int rr = ((FurnitureItem)this.activeItem).furniture.getLightRadius();
            if (rr > r) {
                r = rr;
            }
        }
        return r;
    }
    
    @Override
    protected void die() {
        super.die();
        Sound.playerDeath.play();
    }
    
    @Override
    protected void touchedBy(final Entity entity) {
        if (!(entity instanceof Player)) {
            entity.touchedBy(this);
        }
    }
    
    @Override
    protected void doHurt(final int damage, final int attackDir) {
        if (this.hurtTime > 0 || this.invulnerableTime > 0) {
            return;
        }
        Sound.playerHurt.play();
        this.level.add(new TextParticle(new StringBuilder().append(damage).toString(), this.x, this.y, Color.get(-1, 504, 504, 504)));
        this.health -= damage;
        if (attackDir == 0) {
            this.yKnockback = 6;
        }
        if (attackDir == 1) {
            this.yKnockback = -6;
        }
        if (attackDir == 2) {
            this.xKnockback = -6;
        }
        if (attackDir == 3) {
            this.xKnockback = 6;
        }
        this.hurtTime = 10;
        this.invulnerableTime = 30;
        --this.personGlow;
        if (this.personGlow < 0) {
            this.personGlow = 0;
        }
    }
    
    void cheat() {
        if (this.isTest == 0) {
            ++this.isTest;
            this.inventory.add(new ToolItem(ToolType.sword, 2));
            this.inventory.add(new ToolItem(ToolType.axe, 2));
            this.inventory.add(new ToolItem(ToolType.pickaxe, 2));
            this.inventory.add(new ToolItem(ToolType.shovel, 2));
            this.inventory.add(new ToolItem(ToolType.hoe, 2));
            this.inventory.add(new ToolItem(ToolType.lighter, 2));
        }
        else if (this.isTest == 1) {
            ++this.isTest;
            this.inventory.add(new ToolItem(ToolType.sword, 3));
            this.inventory.add(new ToolItem(ToolType.axe, 3));
            this.inventory.add(new ToolItem(ToolType.pickaxe, 3));
            this.inventory.add(new ToolItem(ToolType.shovel, 3));
            this.inventory.add(new ToolItem(ToolType.hoe, 3));
        }
        else if (this.isTest == 2) {
            ++this.isTest;
            this.inventory.add(new ToolItem(ToolType.sword, 4));
            this.inventory.add(new ToolItem(ToolType.axe, 4));
            this.inventory.add(new ToolItem(ToolType.pickaxe, 4));
            this.inventory.add(new ToolItem(ToolType.shovel, 4));
            this.inventory.add(new ToolItem(ToolType.hoe, 4));
        }
        else if (this.isTest == 3) {
            ++this.isTest;
            this.inventory.add(new FurnitureItem(new Furnace()));
            this.inventory.add(new FurnitureItem(new Anvil()));
            this.inventory.add(new FurnitureItem(new Oven()));
            this.inventory.add(new FurnitureItem(new Lantern()));
            this.inventory.add(new FurnitureItem(new Chest()));
            this.inventory.add(new ResourceItem(Resource.hurdle));
            this.inventory.add(new ResourceItem(Resource.hurdle));
            this.inventory.add(new ResourceItem(Resource.hurdle));
            this.inventory.add(new ResourceItem(Resource.hurdle));
            this.inventory.add(new ResourceItem(Resource.hurdle));
            this.inventory.add(new ResourceItem(Resource.hurdle));
            this.inventory.add(new ResourceItem(Resource.hurdle));
            this.inventory.add(new ResourceItem(Resource.hurdle));
            this.inventory.add(new ResourceItem(Resource.hurdle));
            this.inventory.add(new ResourceItem(Resource.hurdle));
            this.inventory.add(new ResourceItem(Resource.hurdle));
            this.inventory.add(new ResourceItem(Resource.hurdle));
            this.inventory.add(new ResourceItem(Resource.stile));
            this.inventory.add(new ResourceItem(Resource.stile));
        }
        else if (this.isTest == 4) {
            ++this.isTest;
            this.inventory.add(new FurnitureItem(new IronLantern()));
            this.inventory.add(new FurnitureItem(new GemLantern()));
        }
        else if (this.isTest == 5) {
            ++this.isTest;
            this.inventory.add(new FurnitureItem(new GoldAnvil()));
            this.inventory.add(new FurnitureItem(new GemLantern()));
        }
        else if (this.isTest == 6) {
            ++this.isTest;
            this.inventory.add(new ToolItem(ToolType.wand, 0));
        }
        else if (this.isTest == 7) {
            ++this.isTest;
            this.inventory.add(new ToolItem(ToolType.staff, 0));
        }
        else if (this.isTest == 8) {
            ++this.isTest;
            this.inventory.add(new FurnitureItem(new Orb()));
        }
        else if (this.isTest == 9) {
            ++this.isTest;
            this.inventory.add(new FurnitureItem(new Book()));
        }
        else if (this.isTest == 10) {
            ++this.isTest;
            this.inventory.add(new FurnitureItem(new GreenOrb()));
        }
        else if (this.isTest == 11) {
            ++this.isTest;
            this.inventory.add(new FurnitureItem(new WhiteOrb()));
        }
        else if (this.isTest == 12) {
            ++this.isTest;
            this.testInventory();
        }
        else {
            ++this.isTest;
            this.inventory.add(new FurnitureItem(new GemLantern()));
        }
    }
    
    void testInventory() {
        this.inventory.add(new ToolItem(ToolType.axe, 4));
        this.inventory.add(new FurnitureItem(new Book()));
        this.inventory.add(new FurnitureItem(new Orb()));
        this.inventory.add(new FurnitureItem(new GreenOrb()));
        this.inventory.add(new FurnitureItem(new Oven()));
        this.inventory.add(new FurnitureItem(new Kettle()));
        this.inventory.add(new FurnitureItem(new Crock()));
        this.inventory.add(new FurnitureItem(new Press()));
        this.inventory.add(new FurnitureItem(new Grill()));
        this.inventory.add(new ResourceItem(Resource.hurdle));
        this.inventory.add(new ResourceItem(Resource.hurdle));
        this.inventory.add(new ResourceItem(Resource.hurdle));
        this.inventory.add(new ResourceItem(Resource.hurdle));
        this.inventory.add(new ResourceItem(Resource.hurdle));
        this.inventory.add(new ResourceItem(Resource.hurdle));
        this.inventory.add(new ResourceItem(Resource.hurdle));
        this.inventory.add(new ResourceItem(Resource.hurdle));
        this.inventory.add(new ResourceItem(Resource.hurdle));
        this.inventory.add(new ResourceItem(Resource.hurdle));
        this.inventory.add(new ResourceItem(Resource.hurdle));
        this.inventory.add(new ResourceItem(Resource.hurdle));
        this.inventory.add(new ResourceItem(Resource.hurdle));
        this.inventory.add(new ResourceItem(Resource.hurdle));
        this.inventory.add(new ResourceItem(Resource.hurdle));
        this.inventory.add(new ResourceItem(Resource.hurdle));
        this.inventory.add(new ResourceItem(Resource.hurdle));
        this.inventory.add(new ResourceItem(Resource.hurdle));
        this.inventory.add(new ResourceItem(Resource.hurdle));
        this.inventory.add(new ResourceItem(Resource.hurdle));
        this.inventory.add(new ResourceItem(Resource.hurdle));
        this.inventory.add(new ResourceItem(Resource.hurdle));
        this.inventory.add(new ResourceItem(Resource.hurdle));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ResourceItem(Resource.stile));
        this.inventory.add(new ToolItem(ToolType.lighter, 2));
        this.inventory.add(new ToolItem(ToolType.staff, 0));
        this.inventory.add(new ToolItem(ToolType.wand, 0));
        this.inventory.add(new ToolItem(ToolType.axe, 4));
        this.inventory.add(new ToolItem(ToolType.sword, 4));
        this.inventory.add(new ToolItem(ToolType.pickaxe, 4));
        this.inventory.add(new ToolItem(ToolType.hoe, 4));
        this.inventory.add(new ToolItem(ToolType.shovel, 4));
        this.inventory.add(new ToolItem(ToolType.bow, 4));
        this.inventory.add(new ResourceItem(Resource.wheatSeeds));
        this.inventory.add(new ResourceItem(Resource.turnipSeeds));
        this.inventory.add(new ResourceItem(Resource.carrotSeed));
        this.inventory.add(new ResourceItem(Resource.radishSeeds));
        this.inventory.add(new ResourceItem(Resource.onionSeeds));
        this.inventory.add(new ResourceItem(Resource.garlicSeeds));
        this.inventory.add(new ResourceItem(Resource.pepperSeeds));
        this.inventory.add(new ResourceItem(Resource.tomatoSeeds));
        this.inventory.add(new ResourceItem(Resource.tomatoSeeds));
        this.inventory.add(new ResourceItem(Resource.beetSeeds));
        this.inventory.add(new ResourceItem(Resource.tomato));
        this.inventory.add(new ResourceItem(Resource.potato));
        this.inventory.add(new ResourceItem(Resource.carrot));
        this.inventory.add(new ResourceItem(Resource.radish));
        this.inventory.add(new ResourceItem(Resource.turnip));
        this.inventory.add(new ResourceItem(Resource.pepper));
        this.inventory.add(new ResourceItem(Resource.onion));
        this.inventory.add(new ResourceItem(Resource.garlic));
        this.inventory.add(new ResourceItem(Resource.turnipSeeds));
        this.inventory.add(new ResourceItem(Resource.carrotSeed));
        this.inventory.add(new ResourceItem(Resource.radishSeeds));
        this.inventory.add(new ResourceItem(Resource.onionSeeds));
        this.inventory.add(new ResourceItem(Resource.garlicSeeds));
        this.inventory.add(new ResourceItem(Resource.pepperSeeds));
        this.inventory.add(new ResourceItem(Resource.tomatoSeeds));
        this.inventory.add(new ResourceItem(Resource.tomatoSeeds));
        this.inventory.add(new ResourceItem(Resource.beetSeeds));
        this.inventory.add(new ResourceItem(Resource.tomato));
        this.inventory.add(new ResourceItem(Resource.potato));
        this.inventory.add(new ResourceItem(Resource.carrot));
        this.inventory.add(new ResourceItem(Resource.radish));
        this.inventory.add(new ResourceItem(Resource.turnip));
        this.inventory.add(new ResourceItem(Resource.pepper));
        this.inventory.add(new ResourceItem(Resource.onion));
        this.inventory.add(new ResourceItem(Resource.garlic));
        this.inventory.add(new ResourceItem(Resource.turnipSeeds));
        this.inventory.add(new ResourceItem(Resource.carrotSeed));
        this.inventory.add(new ResourceItem(Resource.cornSeeds));
        this.inventory.add(new ResourceItem(Resource.cabbageSeed));
        this.inventory.add(new ResourceItem(Resource.mustardSeed));
        this.inventory.add(new ResourceItem(Resource.lettuceSeeds));
        this.inventory.add(new ResourceItem(Resource.radishSeeds));
        this.inventory.add(new ResourceItem(Resource.onionSeeds));
        this.inventory.add(new ResourceItem(Resource.garlicSeeds));
        this.inventory.add(new ResourceItem(Resource.pepperSeeds));
        this.inventory.add(new ResourceItem(Resource.tomatoSeeds));
        this.inventory.add(new ResourceItem(Resource.tomatoSeeds));
        this.inventory.add(new ResourceItem(Resource.beetSeeds));
        this.inventory.add(new ResourceItem(Resource.tomato));
        this.inventory.add(new ResourceItem(Resource.corn));
        this.inventory.add(new ResourceItem(Resource.lettuce));
        this.inventory.add(new ResourceItem(Resource.mustardGrn));
        this.inventory.add(new ResourceItem(Resource.cabbage));
        this.inventory.add(new ResourceItem(Resource.melon));
        this.inventory.add(new ResourceItem(Resource.squash));
        this.inventory.add(new ResourceItem(Resource.oil));
        this.inventory.add(new ResourceItem(Resource.oil));
        this.inventory.add(new ResourceItem(Resource.bean));
        this.inventory.add(new ResourceItem(Resource.pea));
        this.inventory.add(new ResourceItem(Resource.peanut));
        this.inventory.add(new ResourceItem(Resource.potato));
        this.inventory.add(new ResourceItem(Resource.carrot));
        this.inventory.add(new ResourceItem(Resource.radish));
        this.inventory.add(new ResourceItem(Resource.turnip));
        this.inventory.add(new ResourceItem(Resource.pepper));
        this.inventory.add(new ResourceItem(Resource.onion));
        this.inventory.add(new ResourceItem(Resource.garlic));
        this.inventory.add(new ResourceItem(Resource.truffle));
        this.inventory.add(new ResourceItem(Resource.jigae));
        this.inventory.add(new ResourceItem(Resource.kimchi));
        this.inventory.add(new ResourceItem(Resource.ginger));
        this.inventory.add(new ResourceItem(Resource.sauce));
        this.inventory.add(new ResourceItem(Resource.squid));
        this.inventory.add(new ResourceItem(Resource.omelet));
        this.inventory.add(new ResourceItem(Resource.stew));
        this.inventory.add(new ResourceItem(Resource.milk));
        this.inventory.add(new ResourceItem(Resource.tomato));
        this.inventory.add(new ResourceItem(Resource.cheese));
        this.inventory.add(new ResourceItem(Resource.vinegar));
        this.inventory.add(new ResourceItem(Resource.goldIngot));
        this.inventory.add(new ResourceItem(Resource.silverIngot));
        this.inventory.add(new ResourceItem(Resource.goldIngot));
        this.inventory.add(new ResourceItem(Resource.silverIngot));
        this.inventory.add(new ResourceItem(Resource.noodle));
        this.inventory.add(new ResourceItem(Resource.macCheese));
        this.inventory.add(new ResourceItem(Resource.beer));
        this.inventory.add(new ResourceItem(Resource.cider));
        this.inventory.add(new ResourceItem(Resource.hardCider));
        this.inventory.add(new ResourceItem(Resource.cherryWine));
        this.inventory.add(new ResourceItem(Resource.ale));
        this.inventory.add(new ResourceItem(Resource.fat));
        this.inventory.add(new ResourceItem(Resource.chips));
        this.inventory.add(new ResourceItem(Resource.fries));
        this.inventory.add(new ResourceItem(Resource.meatSauce));
        this.inventory.add(new ResourceItem(Resource.spagetti));
        this.inventory.add(new ResourceItem(Resource.cookies));
        this.inventory.add(new ResourceItem(Resource.cake));
        this.inventory.add(new ResourceItem(Resource.cherryPie));
        this.inventory.add(new ResourceItem(Resource.hamburger));
        this.inventory.add(new ResourceItem(Resource.sausage));
        this.inventory.add(new ResourceItem(Resource.hotDog));
        this.inventory.add(new ResourceItem(Resource.pickles));
        this.inventory.add(new ResourceItem(Resource.chili));
        this.inventory.add(new ResourceItem(Resource.salad));
        this.inventory.add(new ResourceItem(Resource.natchos));
        this.inventory.add(new ResourceItem(Resource.beanDip));
        this.inventory.add(new ResourceItem(Resource.cabgeStew));
        this.inventory.add(new ResourceItem(Resource.greens));
        this.inventory.add(new ResourceItem(Resource.pizza));
        this.inventory.add(new ResourceItem(Resource.butter));
        this.inventory.add(new ResourceItem(Resource.casserole));
        this.inventory.add(new ResourceItem(Resource.cornChips));
    }
    
    public void makeSeaCaptain() {
        this.level.player.seaCaptain = 1;
    }
    
    public void makeWoodsman() {
        this.level.player.woodsman = 1;
    }
    
    public void gameWon() {
        this.level.player.invulnerableTime = 300;
        this.game.won();
    }
    
    public void setShape() {
        this.baseShape = Player.playerShape;
    }
}
