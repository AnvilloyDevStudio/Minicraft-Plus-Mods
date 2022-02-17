package minicraftmodsapiinterface;

public interface IRectangle {
    public int getLeft();
	public int getRight();
	public int getTop();
	public int getBottom();
	
	public int getWidth();
	public int getHeight();
	
	public IPoint getCenter();
	public IDimension getSize();
	
	public IPoint getPosition(IRelPos relPos);
	
	public boolean intersects(IRectangle other);
	
	public void setPosition(IPoint p, IRelPos relPos);
	public void setPosition(int x, int y, IRelPos relPos);
	
	public void translate(int xoff, int yoff);
	
	public void setSize(IDimension d, IRelPos anchor);
	public void setSize(int width, int height, IRelPos anchor);

}
