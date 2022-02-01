package minicraft.screen;

import minicraft.core.Game;
import minicraft.core.io.InputHandler;
import minicraft.entity.ItemHolder;
import minicraft.entity.furniture.Chest;
import minicraft.entity.mob.Player;
import minicraft.gfx.Screen;
import minicraft.item.Inventory;
import minicraft.item.Item;
import minicraft.item.StackableItem;

public class ContainerDisplay extends Display {
	
	private static final int padding = 10;
	
	private Player player;
	private Chest chest;
	
	public ContainerDisplay(Player player, Chest chest) {
		super(new InventoryMenu(chest, chest.getInventory(), chest.name), new InventoryMenu(player, player.getInventory(), "Inventory"));
		//pInv = player.getInventory();
		//cInv = chest.getInventory();
		this.player = player;
		this.chest = chest;
		
		menus[1].translate(menus[0].getBounds().getWidth() + padding, 0);
		
		if(menus[0].getNumOptions() == 0) onSelectionChange(0, 1);
	}
	
	@Override
	protected void onSelectionChange(int oldSel, int newSel) {
		super.onSelectionChange(oldSel, newSel);
		if(oldSel == newSel) return; // this also serves as a protection against access to menus[0] when such may not exist.
		int shift = 0;
		if(newSel == 0) shift = padding - menus[0].getBounds().getLeft();
		if(newSel == 1) shift = (Screen.w - padding) - menus[1].getBounds().getRight();
		for(Menu m: menus)
			m.translate(shift, 0);
	}
	
	private int getOtherIdx() { return (selection+1) % 2; }
	
	@Override
	public void tick(InputHandler input) {
		super.tick(input);
		
		if(input.getKey("menu").clicked || chest.isRemoved()) {
			Game.setMenu(null);
			return;
		}
		
		Menu curMenu = menus[selection];
		int otherIdx = getOtherIdx();
		
		if(curMenu.getNumOptions() > 0 && (input.getKey("attack").clicked || input.getKey("drop-one").clicked)) {
			// switch inventories
			Inventory from, to;
			if(selection == 0) {
				from = chest.getInventory();
				to = player.getInventory();
			} else {
				from = player.getInventory();
				to = chest.getInventory();
			}
			
			int toSel = menus[otherIdx].getSelection();
			int fromSel = curMenu.getSelection();
			
			if(Game.isValidClient() && from == chest.getInventory()) {
				// just send, take no actual action
				Game.client.removeFromChest(chest, fromSel, toSel, input.getKey("attack").clicked);
				return;
			}
			
			Item fromItem = from.get(fromSel);
			
			boolean transferAll = input.getKey("attack").clicked || !(fromItem instanceof StackableItem) || ((StackableItem)fromItem).count == 1;
			
			Item toItem = fromItem.clone();
			
			if(!transferAll) {
				((StackableItem)fromItem).count--; // this is known to be valid.
				((StackableItem)toItem).count = 1;
				// items are setup for sending.
			}
			else { // transfer whole item/stack.
				if(! (Game.isMode("creative") && from == player.getInventory()) )
					from.remove(fromSel); // remove it
			}
			
			if(!Game.isValidClient()) {
				to.add(toSel, toItem);
				update();
			} else if(to == chest.getInventory()) {
				// is online client, and from == player
				Game.client.addToChest(chest, toSel, toItem);
			}
		}
	}
	
	public void onInvUpdate(ItemHolder holder) {
		if(holder == player || holder == chest)
			update();
	}
	
	private void update() {
		menus[0] = new InventoryMenu((InventoryMenu) menus[0]);
		menus[1] = new InventoryMenu((InventoryMenu) menus[1]);
		menus[1].translate(menus[0].getBounds().getWidth() + padding, 0);
		onSelectionChange(0, selection);
	}
}
