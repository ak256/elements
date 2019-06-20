package com.ak.elements.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.ak.elements.Main;

/**
 * My custom input handling class. <br>
 * Needs to be initialized to add listeners to the main class, but everything is accessed statically.
 * <p>
 * <strong>update() must be called at the end of every program update cycle.</strong>
 * 
 * @author Andrew Klinge
 */
public final class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	/** All integer ID values of buttons. */
	private static final Boolean[] keys = new Boolean[65536], mouse = new Boolean[4];

	/** The recently typed characters (pressed keyboard buttons). */
	private static String typed = "";

	/** The direction the mouse wheel was scrolled (- away, + towards). */
	private static int wheelDir;

	/** The mouse screen position. */
	private static int mouseX, mouseY;

	private static int mouseXOff, mouseYOff;

	/** Creates the input manager and adds listeners to Main. */
	public Input(Main main) {
		main.addKeyListener(this);
		main.addMouseListener(this);
		main.addMouseMotionListener(this);
		main.addMouseWheelListener(this);
		main.setFocusTraversalKeysEnabled(false);
	}

	/**
	 * @param keys - the keys that should all be pressed
	 * @return true if all the keys are held down.
	 */
	public static boolean isCombo(int... keys) {
		for(int k : keys) {
			if(Input.keys[k] == null || !Input.keys[k]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return true if any of the keys are pressed.
	 */
	public static boolean isPressed(int... keys) {
		for(int k : keys) {
			if(Input.keys[k] != null && !Input.keys[k]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return true if any of the keys are held down.
	 */
	public static boolean isDown(int... keys) {
		for(int k : keys) {
			if(Input.keys[k] != null && Input.keys[k]) {
				return true;
			}
		}
		return false;
	}

	public void update() {
		typed = "";
		wheelDir = 0;

		for(int i = 0; i < keys.length; i++) {
			if(keys[i] != null && !keys[i])
				keys[i] = true;
		}
		for(int i = 0; i < mouse.length; i++) {
			if(mouse[i] != null && !mouse[i])
				mouse[i] = true;
		}
	}

	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		
		if(code == KeyEvent.VK_BACK_SPACE) {
			typed = null;
		} else if(code != KeyEvent.VK_SHIFT && (code < KeyEvent.VK_F1 || code > KeyEvent.VK_F12) && (code > KeyEvent.VK_DOWN || code < KeyEvent.VK_LEFT)) {
			typed = typed + e.getKeyChar();
		}
		if(code > 0 && code < keys.length && keys[code] == null) {
			keys[code] = false;
		}
	}

	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if(code > 0 && code < keys.length)
			keys[code] = null;
	}

	public void keyTyped(KeyEvent arg0) {
	}

	public void mouseDragged(MouseEvent arg0) {
		setMouse(arg0.getX(), arg0.getY());
	}

	public void mouseMoved(MouseEvent arg0) {
		setMouse(arg0.getX(), arg0.getY());
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		mouse[e.getButton()] = false;
	}

	public void mouseReleased(MouseEvent e) {
		mouse[e.getButton()] = null;
	}

	public void mouseWheelMoved(MouseWheelEvent arg0) {
		wheelDir = arg0.getWheelRotation();
	}

	private void setMouse(int x, int y) {
		mouseX = x + mouseXOff;
		mouseY = y + mouseYOff;
	}

	public static void setMouseOffset(int x, int y) {
		mouseXOff = x;
		mouseYOff = y;
	}

	/**
	 * @return mouse x relative to the screen
	 */
	public static int x() {
		return mouseX;
	}

	/**
	 * @return mouse y relative to the screen
	 */
	public static int y() {
		return mouseY;
	}

	/**
	 * @return whether the mouse is at the current position
	 */
	public static boolean mouseAt(int x, int y) {
		return mouseX == x && mouseY == y;
	}

	/**
	 * @return the distance to the mouse cursor
	 */
	public static int[] dist(int x, int y) {
		return new int[] {x - mouseX, y - mouseY};
	}

	/**
	 * @return whether the mouse is inside the rectangle
	 */
	public static boolean mouseIn(int x, int y, int w, int h) {
		return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
	}

	/** 
	 * @return whether the given mouse key was clicked
	 */
	public static boolean clicked(int mouseKey) {
		return mouse[mouseKey] != null && mouse[mouseKey] == false;
	}
	
	/** @return whether the given mouse key is held down. */
	public static boolean clicking(int mouseKey) {
		return mouse[mouseKey] != null && mouse[mouseKey] == true;
	}

	/**
	 * @return the mouse-wheel scrolling direction (- away, 0 neutral, + towards).
	 */
	public static int scrolling() {
		return wheelDir;
	}

	/**
	 * @return text that has been typed.
	 */
	public static String getTyped() {
		return typed;
	}
}