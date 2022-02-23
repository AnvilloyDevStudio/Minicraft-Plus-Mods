package minicraft.screen;

import minicraft.core.Game;
import minicraft.core.io.InputHandler;
import minicraftmodsapiinterface.IEntity;
import minicraftmodsapiinterface.IInventory;
import minicraftmodsapiinterface.IPlayer;
import minicraft.entity.mob.Player;
import minicraft.item.Item;

public class PlayerInvDisplay extends Display {
	
	private IPlayer player;
	
	public PlayerInvDisplay(IPlayer player) {
		super(new InventoryMenu((IEntity)player, (IInventory)player.getInventory(), "Inventory"));
		this.player = player;
	}
	
	@Override
	public void tick(InputHandler input) {
		super.tick(input);
		
		if(input.getKey("menu").clicked) {
			Game.exitMenu();
			return;
		}
		
		if(input.getKey("attack").clicked && menus[0].getNumOptions() > 0) {
			((Player)player).activeItem = (Item) player.getInventory().remove(menus[0].getSelection());
			Game.exitMenu();
		}
	}
}
