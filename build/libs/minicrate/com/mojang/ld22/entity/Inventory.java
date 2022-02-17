// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.entity;

import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.item.ResourceItem;
import java.util.ArrayList;
import com.mojang.ld22.item.Item;
import java.util.List;

public class Inventory
{
    public List<Item> items;
    
    public Inventory() {
        this.items = new ArrayList<Item>();
    }
    
    public void add(final Item item) {
        this.add(this.items.size(), item);
    }
    
    public void add(final int slot, final Item item) {
        if (item instanceof ResourceItem) {
            final ResourceItem toTake = (ResourceItem)item;
            final ResourceItem has = this.findResource(toTake.resource);
            if (has == null) {
                this.items.add(slot, toTake);
            }
            else {
                final ResourceItem resourceItem = has;
                resourceItem.count += toTake.count;
            }
        }
        else {
            this.items.add(slot, item);
        }
    }
    
    private ResourceItem findResource(final Resource resource) {
        for (int i = 0; i < this.items.size(); ++i) {
            if (this.items.get(i) instanceof ResourceItem) {
                final ResourceItem has = this.items.get(i);
                if (has.resource == resource) {
                    return has;
                }
            }
        }
        return null;
    }
    
    public boolean hasResources(final Resource r, final int count) {
        final ResourceItem ri = this.findResource(r);
        return ri != null && ri.count >= count;
    }
    
    public boolean removeResource(final Resource r, final int count) {
        final ResourceItem ri = this.findResource(r);
        if (ri == null) {
            return false;
        }
        if (ri.count < count) {
            return false;
        }
        final ResourceItem resourceItem = ri;
        resourceItem.count -= count;
        if (ri.count <= 0) {
            this.items.remove(ri);
        }
        return true;
    }
    
    public int count(final Item item) {
        if (!(item instanceof ResourceItem)) {
            int count = 0;
            for (int i = 0; i < this.items.size(); ++i) {
                if (this.items.get(i).matches(item)) {
                    ++count;
                }
            }
            return count;
        }
        final ResourceItem ri = this.findResource(((ResourceItem)item).resource);
        if (ri != null) {
            return ri.count;
        }
        return 0;
    }
}
