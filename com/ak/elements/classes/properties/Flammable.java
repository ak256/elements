package com.ak.elements.classes.properties;

/**
 * Represents an element that can be ignited by flame and burned.
 * 
 * @author Andrew Klinge
 */
public interface Flammable {
	
	/** Attempts to ignite the element with the given amount of energy. */
	public void ignite(float energy);
}