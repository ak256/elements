package com.ak.elements.classes.solids;

/**
 * A fast burning form of wood.
 * 
 * @author Andrew Klinge
 */
public class Fuse extends Wood {

	public Fuse() {
		super(0xceb47b, 30, 0, 0.15f, 0, 0);
	}

	@Override
	public Fuse clone() {
		Fuse clone = new Fuse();
		clone.id = id;
		return clone;
	}
}