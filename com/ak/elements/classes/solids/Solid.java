package com.ak.elements.classes.solids;

import com.ak.elements.Main;
import com.ak.elements.classes.Element;
import com.ak.elements.classes.gases.Anti;
import com.ak.elements.classes.gases.Gas;
import com.ak.elements.classes.liquids.Liquid;

/**
 * An element greatly affected by gravity.
 * 
 * @author Andrew Klinge
 */
public class Solid extends Element {

	public Solid(int color) {
		super(color);
		updated = Main.time;
		vy = 0.7f;
	}

	@Override
	public void update(int x, int y) {
		// do falling
		int wait = Math.max(1, (int) (GRAV - (vy * GRAV)));
		if(Main.time - updated >= wait) {
			if(Main.game.collides(2, x, y)) {
				if(y == Main.game.grid[0].length - 1) {
					vy = 0;
				} else {
					Element at = Main.game.grid[x][y + 1];
					if(at instanceof Anti) {
						((Anti) at).collide(x, y + 1, x, y, 0, false);
						return;
					}
					if(at instanceof Liquid || at instanceof Gas) {
						// sink
						Main.game.grid[x][y] = Main.game.grid[x][y + 1];
						y++;
						Main.game.grid[x][y] = this;
						if(at instanceof Liquid) {
							if(vy > 0.5f)
								vy = 0.5f;
							else
								vy -= 0.05f;
						}
					} else if(at.vy == 0)
						vy = 0;
				}
			} else {
				if(vy == 0)
					vy = 0.698f;
				vy += GRAV * 0.002f;
				Main.game.grid[x][y] = null;
				y++;
				Main.game.grid[x][y] = this;
			}
			subupdate(x, y);
			updated = Main.time;
		}
	}

	@Override
	public Solid clone() {
		Solid clone = new Solid(color);
		clone.id = id;
		return clone;
	}
}