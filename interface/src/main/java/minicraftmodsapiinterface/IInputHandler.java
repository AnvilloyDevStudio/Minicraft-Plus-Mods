package minicraftmodsapiinterface;

import java.util.ArrayList;
import java.awt.event.KeyEvent;

public interface IInputHandler {
    public String keyToChange = null; // This is used when listening to change key bindings.
	
	public String getChangedKey();
	
	public void resetKeyBindings();
	
	/** Processes each key one by one, in keyboard. */
	public void tick();
	
	// The Key class.
	public abstract class IKey {
		// presses = how many times the Key has been pressed.
		// absorbs = how many key presses have been processed.
		// down = if the key is currently physically being held down.
		// clicked = if the key is still being processed at the current tick.
		public boolean down, clicked;
		// sticky = true if presses reaches 3, and the key continues to be held down.
		
		boolean stayDown;
		
		
		/** toggles the key down or not down. */
		public abstract void toggle(boolean pressed) ;
		
		/** Processes the key presses. */
		public abstract void tick();
		
		public abstract void release();
		
		// Custom toString() method, I used it for debugging.
		public abstract String toString();
	}
	
	/** This is used to stop all of the actions when the game is out of focus. */
	public void releaseAll();
	
	/// This is meant for changing the default keys. Call it from the options menu, or something.
	public void setKey(String keymapKey, String keyboardKey);
	
	/** Simply returns the mapped value of key in keymap. */
	public String getMapping(String actionKey);
	
	/// THIS is pretty much the only way you want to be interfacing with this class; it has all the auto-create and protection functions and such built-in.
	public IKey getKey(String keytext);
	
	/// This method provides a way to press physical keys without actually generating a key event.
	/*public void pressKey(String keyname, boolean pressed) {
		Key key = getPhysKey(keyname);
		key.toggle(pressed);
		//System.out.println("Key " + keyname + " is clicked: " + getPhysKey(keyname).clicked);
	}*/
	
	public ArrayList<String> getAllPressedKeys();
	
	/** Used by Save.java, to save user key preferences. */
	public String[] getKeyPrefs();
	
	
	public void changeKeyBinding(String actionKey);
	
	public void addKeyBinding(String actionKey);
	
	/// Event methods, many to satisfy interface requirements...
	public void keyPressed(KeyEvent ke);
	public void keyReleased(KeyEvent ke);
	public void keyTyped(KeyEvent ke);
	
	public String addKeyTyped(String typing, String pattern);
}
