package minicraftmodsapiinterface;

import java.util.List;

public interface IMenu {
    
	boolean shouldRender = true;
	
	public void init();
	
	void setSelection(int idx);
	int getSelection();
	int getDispSelection();
	
	IListEntry[] getEntries();
	int getNumOptions();
	
	IRectangle getBounds();
	String getTitle();
	
	boolean isSelectable();
	boolean shouldRender();
	
	/** @noinspection SameParameterValue*/
	void translate(int xoff, int yoff);
	
	public void tick(IInputHandler input);
	
	public void render(IScreen screen);
	
	void updateSelectedEntry(IListEntry newEntry);
	void updateEntry(int idx, IListEntry newEntry);
	
	public void removeSelectedEntry();

	public void setColors(IMenu model);
	
	
	
	// This needs to be in the Menu class, to have access to the private constructor and fields.
	public static interface IBuilder {
		
		public IBuilder setEntries(IListEntry... entries);
		public IBuilder setEntries(List<IListEntry> entries);
		
		public IBuilder setPositioning(IPoint anchor, IRelPos menuPos);
		
		public IBuilder setSize(int width, int height);
		public IBuilder setMenuSize(IDimension d);
		
		public IBuilder setBounds(IRectangle rect);
		
		public IBuilder setDisplayLength(int numEntries);
		
		
		public IBuilder setTitlePos(IRelPos rp);
		
		public IBuilder setTitle(String title);

		public IBuilder setTitle(String title, int color);
		public IBuilder setTitle(String title, int color, boolean fullColor);
		
		public IBuilder setFrame(boolean hasFrame);

		
		public IBuilder setScrollPolicies(float padding, boolean wrap);
		
		public IBuilder setShouldRender(boolean render);
		
		public IBuilder setSelectable(boolean selectable);
		
		public IBuilder setSelection(int sel);
		public IBuilder setSelection(int sel, int dispSel);

		public IBuilder setSearcherBar(boolean searcherBar);
		
		public IMenu createMenu();		
		// returns a new Builder instance, that can be further modified to create another menu.
		public IBuilder copy();
	}
	
	public String toString();
}
