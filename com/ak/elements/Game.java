package com.ak.elements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.ak.elements.classes.Element;
import com.ak.elements.classes.Elements;
import com.ak.elements.util.Input;

/**
 * Controls the game and handles user input.
 * 
 * @author Andrew Klinge
 *
 */
public class Game {
	/** Rendering offset for drawing elements within the border. */
	public static final int OFFSET_X = 50, OFFSET_Y = 30;

	/** Size of the elements grid. */
	public static final int SIZE = 100;
	
	/** Number of pixels. For rendering elements. */
	public static final int SCALE = 5;
	
	public Element[][] grid = new Element[SIZE][SIZE];
	public Element selected = Elements.ANTI.element; // currently selected element

	private int selectIndex; // index of selected element in elements list

	public void update() {
		{// user controls
			if(Input.isPressed(KeyEvent.VK_R))
				grid = new Element[SIZE][SIZE]; // reset grid
			
			// elements selection based on mouse scroll wheel or arrow keys
			if(Input.scrolling() > 0 || Input.isPressed(KeyEvent.VK_RIGHT)) {
				selectIndex++;
				if(selectIndex >= Elements.values().length) {
					selectIndex = 0;
				}

				selected = Elements.values()[selectIndex].element;
				
			} else if(Input.scrolling() < 0 || Input.isPressed(KeyEvent.VK_LEFT)) {
				selectIndex--;
				if(selectIndex < 0) {
					selectIndex = Elements.values().length - 1;
				}
				
				selected = Elements.values()[selectIndex].element;
			}
			
			// creating and destroying elements
			// get grid coordinates of mouse
			int x = (Input.x() - OFFSET_X) / SCALE;
			int y = (Input.y() - OFFSET_Y) / SCALE;
			
			// dont do anything if the mouse is outside of the border
			if(x > -1 && y > -1 && x < grid.length && y < grid[0].length) {
				if(Input.clicking(MouseEvent.BUTTON1)) { // left click places elements
					if(grid[x][y] == null) {
						grid[x][y] = selected.clone();
					}
				} else if(Input.clicking(MouseEvent.BUTTON3)) { // right click deletes
					grid[x][y] = null;
				}
			}
		}
		
		// update each element
		for(int x = 0; x < SIZE; x++) {
			for(int y = 0; y < SIZE; y++) {
				if(grid[x][y] == null) continue;
				
				grid[x][y].update(x, y);
			}
		}
	}

	public void render(Graphics g) {
		// draw each element
		for(int x = 0; x < SIZE; x++) {
			for(int y = 0; y < SIZE; y++) {
				if(grid[x][y] == null) continue;
				
				grid[x][y].render(g, x, y);
			}
		}
		
		g.setColor(Color.WHITE);
		// draw border
		g.drawRect(OFFSET_X - 1, OFFSET_Y - 1, SIZE * SCALE + 1, SIZE * SCALE + 1);
		// draw element's name
		g.drawString(Elements.values()[selectIndex].toString().toLowerCase(), 10, 20);
	}

	/** @return whether one step forward in the given direction collides with the border. */
	public boolean collides(int dir, int x, int y) {
		if(dir == 0) return (y - 1 < 0 || grid[x][y - 1] != null);
		if(dir == 1) return (x + 1 >= SIZE || grid[x + 1][y] != null);
		if(dir == 2) return (y + 1 >= SIZE || grid[x][y + 1] != null);
		return (x - 1 < 0 || grid[x - 1][y] != null);
	}
}