package com.ak.elements.classes.gases;

import com.ak.elements.Main;

/**
 * A particle that bounces around and annihilates itself and other elements upon collision.
 * 
 * @author Andrew Klinge
 */
public class Anti extends Gas {

	public Anti(int color) {
		super(color, 1);
	}

	@Override
	public void collide(int x, int y, int ex, int ey, float v, boolean isX) {
		Main.game.grid[ex][ey] = null;
		Main.game.grid[x][y] = null;
	}

	@Override
	public Anti clone() {
		Anti clone = new Anti(color);
		clone.id = id;
		return clone;
	}
}