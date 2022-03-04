package minicraft.entity.particle;

import java.util.HashMap;

import minicraft.entity.ClientTickable;
import minicraft.entity.Entity;
import minicraft.gfx.Sprite;
import minicraftmodsapiinterface.IScreen;

public class Particle extends Entity implements ClientTickable {
	private int time; // lifetime elapsed.
	private int lifetime;
	
	protected Sprite sprite;

	public static HashMap<String, Class<? extends Particle>> Particles = new HashMap<>();
	
	static {
		Particles.put("FireParticle", FireParticle.class);
		Particles.put("SmashParticle", SmashParticle.class);
		Particles.put("TextParticle", TextParticle.class);
	}

	/**
	 * Creates an particle entity at the given position. The particle has a x and y radius = 1.
	 * @param x X map coordinate
	 * @param y Y map coorindate
	 * @param xr x radius of the particle   
	 * @param lifetime How many game ticks the particle lives before its removed
	 * @param sprite The particle's sprite
	 */
	public Particle(int x, int y, int xr, int lifetime, Sprite sprite) {
		// Make a particle at the given coordinates
		super(xr, 1);
		this.x = x;
		this.y = y;
		this.lifetime = lifetime;
		this.sprite = sprite;
		time = 0;
	}
	public Particle(int x, int y, int lifetime, Sprite sprite) {
		this(x, y, 1, lifetime, sprite);
	}
	
	@Override
	public void tick() {
		time++;
		if (time > lifetime) {
			remove();
		}
	}
	
	@Override
	public void render(IScreen screen) { sprite.render(screen, x, y); }
	
	@Override
	public boolean isSolid() { return false; }
}
