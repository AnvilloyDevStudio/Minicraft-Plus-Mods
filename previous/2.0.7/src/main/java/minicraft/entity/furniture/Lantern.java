package minicraft.entity.furniture;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import minicraft.gfx.Sprite;

public class Lantern extends Furniture {
	public static class Type {
		public static ArrayList<Type> Instances = new ArrayList<>();
		public static LinkedHashMap<String, Type> Types = new LinkedHashMap<>();

		static {
			new Type("NORM", "Lantern", 9, 0);
			new Type("IRON", "Iron Lantern", 12, 2);
			new Type("GOLD", "Gold Lantern", 15, 4);
		}
		
		protected int light, offset;
		protected String title;
		protected Sprite sprite;
		public String name;
			
		Type(String name, String title, int light, int offset) {
			this.name = name;
			this.title = title;
			this.offset = offset;
			this.sprite = null;
			this.light = light;
			Instances.add(this);
			Types.put(name, this);
		}
		public Type(String name, String title, int light, Sprite sprite) {
			this.name = name;
			this.title = title;
			this.sprite = sprite;
			this.light = light;
			Instances.add(this);
			Types.put(name, this);
		}
	}
	
	public Lantern.Type type;
	
	/**
	 * Creates a lantern of a given type.
	 * @param type Type of lantern.
	 */
	public Lantern(Lantern.Type type) {
		super(type.title, type.sprite == null? new Sprite(18 + type.offset, 26, 2, 2, 2): type.sprite, 3, 2);
		this.type = type;
	}
	
	@Override
	public Furniture clone() {
		return new Lantern(type);
	}
	
	/** 
	 * Gets the size of the radius for light underground (Bigger number, larger light) 
	 */
	@Override
	public int getLightRadius() {
		return type.light;
	}
}
