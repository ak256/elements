package com.ak.elements.classes.gases;

import java.awt.Color;

import com.ak.elements.Main;
import com.ak.elements.classes.Element;
import com.ak.elements.classes.properties.Flammable;
import com.ak.elements.util.Utils;

/**
 * An element that ignites other elements.
 * 
 * @author Andrew Klinge
 */
public class Flame extends Gas {
	
	/** Amount of energy to lose every update. */
	private static final float REDUCE = 0.01f;

	private float energy; // higher energy means more combustion. 0.0 - 1.0
	private boolean remove;
	private final long creationTime;
	private final int lifespan;

	public Flame() {
		super(0xff0000, 0.7f);
		energy = Main.rand.nextFloat() * 0.3f + 0.7f;
		creationTime = Main.time;
		lifespan = Main.rand.nextInt(20) + 30;
	}

	@Override
	protected void collide(int x, int y, int ex, int ey, float v, boolean isX) {
		Element e = Main.game.grid[ex][ey];
		if(e instanceof Flammable) {
			((Flammable) e).ignite(energy);
			remove = true;
		}
	}

	@Override
	public void subupdate(int x, int y) {
		int life = (int) (Main.time - creationTime);
		vx = Utils.clamp(vx, -0.3f, 0.3f);
		vy = Utils.clamp(vy, -0.3f, 0.3f);
		energy -= REDUCE;

		// update color based on energy
		this.color = Color.HSBtoRGB(0.15f * (1 - (life / (float) lifespan)), 1.0f, 1.0f);
		
		if(energy <= 0 || remove || life > lifespan) {
			Main.game.grid[x][y] = null;
		}
	}

	@Override
	public Flame clone() {
		Flame clone = new Flame();
		clone.id = id;
		return clone;
	}
}