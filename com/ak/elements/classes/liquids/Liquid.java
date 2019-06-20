package com.ak.elements.classes.liquids;

import com.ak.elements.Main;
import com.ak.elements.classes.Element;
import com.ak.elements.classes.gases.Anti;
import com.ak.elements.classes.gases.Gas;
import com.ak.elements.util.Utils;

/**
 * @author AK
 */
public class Liquid extends Element {

	private long updated2; // used for horizontal movement timing

	public Liquid(int color) {
		super(color);
		updated = Main.time;
		updated2 = Main.time;
		vy = 0.7f;
	}

	@Override
	public void update(int x, int y) {
		// horizontal movement
		// Always remember to comment your code when you write it, kiddos. Otherwise you will suffer, as I am.
		if(Main.time - updated2 >= 4) {
			if(vx == 0) {
				if(vy == 0 && y < Main.game.grid[0].length - 1) {
					boolean left = (x > 0 && Main.game.grid[x - 1][y + 1] == null);
					boolean right = (x < Main.game.grid.length - 1 && Main.game.grid[x + 1][y + 1] == null);

					if(left || right)
						vx = ((Main.rand.nextFloat() * 2) - 1) * 0.3f;
					if(left && !right)
						vx = -Math.abs(vx);
					else if(right && !left)
						vx = Math.abs(vx);
				}
			} else {
				vx = Utils.clamp(vx, -0.3f, 0.3f);
				boolean dx = vx < 0;

				if(Main.game.collides(dx ? 3 : 1, x, y)) {
					vx = 0;

				} else {
					Main.game.grid[x][y] = null;
					int newX = x + (dx ? -1 : 1);

					if(newX > -1 && newX < Main.game.grid.length) {
						Main.game.grid[newX][y] = this;
						x = newX;
					}
				}
			}

			updated2 = Main.time;
		}

		// gravity
		vy = Utils.clamp(vy, 0, 1);
		int wait = Math.max(1, (int) (GRAV - (vy * GRAV)));

		if(Main.time - updated >= wait) {
			if(Main.game.collides(2, x, y)) {
				if(y == Main.game.grid[0].length - 1) {
					zero(x, y);

				} else {
					Element at = Main.game.grid[x][y + 1];

					if(at instanceof Anti) {
						((Anti) at).collide(x, y + 1, x, y, 0, false);
						return;
					}
					if(at instanceof Gas) {
						at.vy = vy;

					} else if(at.vy == 0) {
						zero(x, y);
						at.vx = ((Main.rand.nextFloat() * 2) - 1);
					}
					if(vx == 0) {
						if(x > 0 && Main.game.grid[x - 1][y] == null) {
							vx = -0.3f;
						}
					}
				}
			} else {
				vy += GRAV * 0.05f;
				Main.game.grid[x][y] = null;
				y++;
				Main.game.grid[x][y] = this;
				if(vx > 0) {
					vx -= 0.01f;
					if(vx < 0)
						vx = 0;
				} else if(vx < 0) {
					vx += 0.01f;
					if(vx > 0)
						vx = 0;
				}
			}

			subupdate(x, y);
			updated = Main.time;
		}
	}

	/**
	 * Modifies horizontal movement somehow, while halting vertical movement.
	 */
	private void zero(int x, int y) {
		if(y == Main.game.grid[0].length - 1 || Main.game.grid[x][y + 1] == null) {
			vx = 0;
		} else if(vx == 0) {
			vx = ((Main.rand.nextFloat() * 2) - 1) * 0.3f;
		}
		vy = 0;
	}

	@Override
	public Liquid clone() {
		Liquid clone = new Liquid(color);
		clone.id = id;
		return clone;
	}
}