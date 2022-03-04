package minicraftmodsapiinterface;

public class IPoint {
	public int x, y;
	
	public IPoint() { this(0, 0); }
	public IPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public IPoint(IPoint model) {
		x = model.x;
		y = model.y;
	}
	
	public void translate(int xoff, int yoff) {
		x += xoff;
		y += yoff;
	}
	
	public String toString() {
		return "("+x+","+y+")";
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof IPoint)) return false;
		IPoint o = (IPoint) other;
		return x == o.x && y == o.y;
	}
	
	@Override
	public int hashCode() { return x * 71 + y; }
}
