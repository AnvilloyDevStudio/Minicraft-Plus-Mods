package minicraft.mods.coremods;

import java.util.ArrayList;

import minicraft.mods.coremods.interfaces.Tickable;
import minicraft.screen.Display;

public class ModProcedure {
	/** These {@link Display} objects are rendered on GUI HUD. */
	public static final ArrayList<Display> displays0 = new ArrayList<>();
	/** These {@link Display} objects are rendered on GUI HUD Debug Screen. */
	public static final ArrayList<Display> displays1 = new ArrayList<>();
	/** These {@link Display} objects are rendered on screen. */
	public static final ArrayList<Display> displays2 = new ArrayList<>();

	/** These {@link Tickable} objects are invoked when {@link minicraft.core.Updater#paused} is false. */
	public static final ArrayList<Tickable> tickables0 = new ArrayList<>();
	/** These {@link Tickable} objects are invoked when {@link minicraft.core.Updater#paused} is true. */
	public static final ArrayList<Tickable> tickables1 = new ArrayList<>();
	/** These {@link Tickable} objects are invoked when focus. */
	public static final ArrayList<Tickable> tickables2 = new ArrayList<>();
}
