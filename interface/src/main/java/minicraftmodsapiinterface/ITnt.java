package minicraftmodsapiinterface;

import java.awt.event.ActionEvent;

public interface ITnt {
    public void tick();
	
	public void render(IScreen screen);
	
	/**
	 * Does the explosion.
	 */
	public void actionPerformed(ActionEvent e);
	
	public boolean interact(IPlayer player, IItem heldItem, IDirection attackDir);
}
