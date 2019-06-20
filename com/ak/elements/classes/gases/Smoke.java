package com.ak.elements.classes.gases;

import com.ak.elements.Main;
import com.ak.elements.classes.Element;
import com.ak.elements.util.Utils;

/**
 * A slow-falling gas that disappears after a while.
 * 
 * @author Andrew Klinge
 */
public class Smoke extends Gas {
	private final long creationTime; // time of creation
	private final long lifespan;

	public Smoke(int color) {
		super(color, 5);
		creationTime = Main.time;
		lifespan = Main.rand.nextInt(300) + 800;
	}

	@Override
	protected void subupdate(int x, int y) {
		// slow gravity
		if(Main.game.collides(2, x, y)) {
			if(y == Main.game.grid[0].length - 1) {
				vy = 0;
			} else {
				Element at = Main.game.grid[x][y + 1];
				if(at instanceof Gas) {
					at.vy = vy;
				} else if(at.vy == 0)
					vy = 0;
			}
		} else {
			vy += 0.01f;
			Main.game.grid[x][y] = null;
			y++;
			Main.game.grid[x][y] = this;
		}
		vx = Utils.clamp(vx, -0.3f, 0.3f);
		vy = Utils.clamp(vy, -0.3f, 0.1f);

		// disappear
		if(Main.time - creationTime > lifespan) {
			Main.game.grid[x][y] = null;
			return;
		}
	}

	@Override
	protected void collide(int x, int y, int ex, int ey, float v, boolean isX) {
		super.collide(x, y, ex, ey, v * 0.5f, isX);
	}

	@Override
	protected void newVelocity(float v, boolean x) {
		super.newVelocity(v * 0.5f, x);
	}

	@Override
	public Smoke clone() {
		Smoke clone = new Smoke(color);
		clone.id = id;
		return clone;
	}
}