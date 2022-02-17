package minicraftmodsapiinterface;

public interface IItemEntity {
    	/**
	 * Returns a string representation of the itementity
	 * @return string representation of this entity
	 */
	public String getData();
	
	public void tick();

	public boolean isSolid();

	public void render(IScreen screen);

}
