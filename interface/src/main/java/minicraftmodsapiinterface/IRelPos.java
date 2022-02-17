package minicraftmodsapiinterface;

public interface IRelPos {
	public IRelPos getOpposite();
	
	/** positions the given rect around the given anchor. The double size is what aligns it to a point rather than a rect. */
	public IPoint positionRect(IDimension rectSize, IPoint anchor);
	// the point is returned as a rectangle with the given dimension and the found location, within the provided dummy rectangle.
	public IRectangle positionRect(IDimension rectSize, IPoint anchor, IRectangle dummy);
	
	/** positions the given rect to a relative position in the container. */ 
	public IPoint positionRect(IDimension rectSize, IRectangle container);
	
	// the point is returned as a rectangle with the given dimension and the found location, within the provided dummy rectangle.
	public IRectangle positionRect(IDimension rectSize, IRectangle container, IRectangle dummy);

}
