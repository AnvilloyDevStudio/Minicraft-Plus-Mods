package minicraftmodsapiinterface;

public interface IDisplay {
	
	int selection = 0;
	
	// Called during setMenu()
	public void init(IDisplay parent);
	
	public void onExit();
	
	public IDisplay getParent();
	
	public void tick(IInputHandler input);
	
	// Sub-classes can do extra rendering here; this renders each menu that should be rendered, in the order of the array, such that the currently selected menu is rendered last, so it appears on top (if they even overlap in the first place).
	public void render(IScreen screen);
}
