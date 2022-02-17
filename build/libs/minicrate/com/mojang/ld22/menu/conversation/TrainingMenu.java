// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.menu.conversation;

import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.menu.ListItem;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.sound.Sound;
import java.util.Collections;
import java.util.Comparator;
import java.util.Collection;
import java.util.ArrayList;
import com.mojang.ld22.crafting.Recipe;
import java.util.List;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.menu.Menu;

public class TrainingMenu extends Menu
{
    private Player player;
    private int selected;
    private int chosen;
    private List<Recipe> recipes;
    
    public TrainingMenu(final List<Recipe> recipes, final Player player) {
        this.selected = 0;
        this.chosen = 0;
        this.recipes = new ArrayList<Recipe>(recipes);
        this.player = player;
        for (int i = 0; i < recipes.size(); ++i) {
            this.recipes.get(i).checkCanCraft(player);
        }
        Collections.sort(this.recipes, new Comparator<Recipe>() {
            @Override
            public int compare(final Recipe r1, final Recipe r2) {
                if (r1.canCraft && !r2.canCraft) {
                    return -1;
                }
                if (!r1.canCraft && r2.canCraft) {
                    return 1;
                }
                return 0;
            }
        });
    }
    
    @Override
    public void tick() {
        if (this.input.menu.clicked) {
            this.game.setMenu(null);
        }
        if (this.input.up.clicked) {
            --this.selected;
        }
        if (this.input.down.clicked) {
            ++this.selected;
        }
        final int len = this.recipes.size();
        if (len == 0) {
            this.selected = 0;
        }
        if (this.selected < 0) {
            this.selected += len;
        }
        if (this.selected >= len) {
            this.selected -= len;
        }
        if (this.input.attack.clicked && len > 0) {
            final Recipe r = this.recipes.get(this.selected);
            r.checkCanCraft(this.player);
            if (r.canCraft) {
                r.deductCost(this.player);
                r.craft(this.player);
                if (this.selected == 0) {
                    this.player.woodsman = 1;
                }
                else if (this.selected == 1) {
                    this.player.seaCaptain = 1;
                }
                Sound.craft.play();
            }
            for (int i = 0; i < this.recipes.size(); ++i) {
                this.recipes.get(i).checkCanCraft(this.player);
            }
        }
    }
    
    @Override
    public void render(final Screen screen) {
        Font.renderFrame(screen, "I am Bob.", 0, 13, 32, 20);
        Font.draw("I can train you, but first,", screen, 4, 112, Color.get(0, 333, 333, 333));
        Font.draw("I must have something to eat!", screen, 4, 120, Color.get(0, 333, 333, 333));
        Font.draw("I could sure go for a burger", screen, 4, 128, Color.get(0, 333, 333, 333));
        Font.draw("or some kinchi jigae,", screen, 4, 136, Color.get(0, 333, 333, 333));
        Font.draw("with some Hard Cider or", screen, 4, 144, Color.get(0, 333, 333, 333));
        Font.draw("Cherry Wine!", screen, 4, 152, Color.get(0, 333, 333, 333));
        Font.renderFrame(screen, "Have", 17, 1, 26, 3);
        Font.renderFrame(screen, "Cost", 17, 4, 26, 11);
        Font.renderFrame(screen, "Training", 0, 1, 16, 11);
        this.renderItemList(screen, 0, 1, 13, 13, this.recipes, this.selected);
        if (this.recipes.size() > 0) {
            final Recipe recipe = this.recipes.get(this.selected);
            final int hasResultItems = this.player.inventory.count(recipe.resultTemplate);
            final int xo = 144;
            screen.render(xo, 16, recipe.resultTemplate.getSprite(), recipe.resultTemplate.getColor(), 0);
            Font.draw(new StringBuilder().append(hasResultItems).toString(), screen, xo + 8, 16, Color.get(-1, 555, 555, 555));
            final List<Item> costs = recipe.costs;
            for (int i = 0; i < costs.size(); ++i) {
                final Item item = costs.get(i);
                final int yo = (5 + i) * 8;
                screen.render(xo, yo, item.getSprite(), item.getColor(), 0);
                int requiredAmt = 1;
                if (item instanceof ResourceItem) {
                    requiredAmt = ((ResourceItem)item).count;
                }
                int has = this.player.inventory.count(item);
                int color = Color.get(-1, 555, 555, 555);
                if (has < requiredAmt) {
                    color = Color.get(-1, 222, 222, 222);
                }
                if (has > 999) {
                    has = 999;
                }
                Font.draw(requiredAmt + "/" + has, screen, xo + 8, yo, color);
            }
        }
    }
}
