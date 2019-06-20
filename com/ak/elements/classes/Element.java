package com.ak.elements.classes;

import java.awt.Color;
import java.awt.Graphics;

import com.ak.elements.Game;
import com.ak.elements.Main;

/**
 * A single simulated particle.
 * 
 * @author Andrew Klinge
 *
 */
public abstract class Element {

	/** Maximum falling speed. */
	public static final int GRAV = 9;

	/** Parent Elements wrapper enum. */
	public Elements id;

	/** Directional velocity. */
	public float vx, vy;

	/** The time at which this element was updated. */
	public long updated;

	/** RGB color integer. */
	public int color;

	public Element(int color) {
		this.color = color;
	}

	public void update(int x, int y) {
	}

	protected void subupdate(int x, int y) {
	}

	public void render(Graphics g, int x, int y) {
		g.setColor(new Color(color));
		g.fillRect(Game.OFFSET_X + (x * Game.SCALE), Game.OFFSET_Y + (y * Game.SCALE), Game.SCALE, Game.SCALE);
	}

	/** @return an array of adjacent elements starting with the above element and going clockwise around. */
	public final Element[] getAdjacent(int x, int y) {
		return new Element[] {
				(y == 0 ? null : Main.game.grid[x][y - 1]),
				(x == Main.game.grid.length - 1 ? null : Main.game.grid[x + 1][y]),
				(y == Main.game.grid[0].length - 1 ? null : Main.game.grid[x][y + 1]),
				(x == 0 ? null : Main.game.grid[x - 1][y]) };
	}

	public final boolean equals(Element e) {
		return e != null && e.id == id;
	}

	@Override
	public abstract Element clone();
}