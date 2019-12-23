package com.ak.elements.classes.gases;

import com.ak.elements.Main;
import com.ak.elements.classes.Element;
import com.ak.elements.classes.solids.Solid;

/**
 * An element that floats around and bounces off others.
 * 
 * @author Andrew Klinge
 */
public class Gas extends Element {

	/** Percentage of velocity to maintain after collision. */
	private static final float VEL_SAVE = 0.98f;

	private final float maxV; // max velocity

	public Gas(int color, float maxV) {
		super(color);
		this.maxV = maxV;
		randomVelocity();
		updated = Main.time;
	}

	@Override
	public void update(int x, int y) {
		if(Main.time - updated >= ((2 * maxV) - (Math.abs(vx) + Math.abs(vy))) * 3) {
			boolean dx = vx < 0;
			boolean dy = vy < 0;
			if(vx != 0) {
				if(Main.game.collides(dx ? 3 : 1, x, y)) {
					float nvx = vx * VEL_SAVE;
					if(dx && x > 0) {
						nvx /= 2;
						collide(x, y, x - 1, y, nvx, true);
					} else if(!dx && x < Main.game.grid.length - 1) {
						nvx /= 2;
						collide(x, y, x + 1, y, -nvx, true);
					}
					newVelocity(-nvx, true);
				} else {
					Main.game.grid[x][y] = null;
					x += (dx ? -1 : 1);
					Main.game.grid[x][y] = this;
				}
			}
			if(vy != 0) {
				if(Main.game.collides(dy ? 0 : 2, x, y)) {
					float nvy = vy * VEL_SAVE;
					if(dy && y > 0) {
						nvy /= 2;
						collide(x, y, x, y - 1, nvy, false);
					} else if(!dy && y < Main.game.grid[0].length - 1) {
						nvy /= 2;
						collide(x, y, x, y + 1, -nvy, false);
					}
					newVelocity(-nvy, false);
				} else {
					Main.game.grid[x][y] = null;
					y += (dy ? -1 : 1);
					Main.game.grid[x][y] = this;
				}
			}
			subupdate(x, y);
			updated = Main.time;
		}
	}

	protected void newVelocity(float v, boolean x) {
		if(x) vx = v;
		else vy = v;
	}

	protected void collide(int x, int y, int ex, int ey, float v, boolean isX) {
		Element e = Main.game.grid[ex][ey];
		if(!(e instanceof Solid)) {
			if(isX) e.vx += v;
			else e.vy += v;
		}
	}

	private void randomVelocity() {
		vx = Main.rand.nextFloat() * (Main.rand.nextBoolean() ? 1 : -1) * maxV;
		vy = Main.rand.nextFloat() * (Main.rand.nextBoolean() ? 1 : -1) * maxV;
		
		if(Main.rand.nextBoolean()) {
			if(Main.rand.nextBoolean()) {
				vx = 0;
			} else {
				vy = 0;
			}
		}
	}

	@Override
	public Gas clone() {
		Gas clone = new Gas(color, maxV);
		clone.id = id;
		return clone;
	}
}