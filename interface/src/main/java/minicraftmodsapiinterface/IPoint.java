package minicraftmodsapiinterface;

public interface IPoint {
    public void translate(int xoff, int yoff);
	
	public String toString();

    public int getX();
    public int getY();
}
