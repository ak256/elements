package com.ak.elements.classes;

import com.ak.elements.classes.gases.Anti;
import com.ak.elements.classes.gases.Flame;
import com.ak.elements.classes.gases.Gas;
import com.ak.elements.classes.gases.Smoke;
import com.ak.elements.classes.liquids.Liquid;
import com.ak.elements.classes.solids.Fuse;
import com.ak.elements.classes.solids.Plant;
import com.ak.elements.classes.solids.Solid;
import com.ak.elements.classes.solids.Wood;

/**
 * List of elements.
 * <p>
 * Using an enum as an element wrapper provides easy access and paired names.
 * 
 * @author Andrew Klinge
 *
 */
public enum Elements {
	AIR(new Gas(0xffffff, 1.0f)),
	ANTI(new Anti(0xffffff)),
	FLAME(new Flame()),
	SMOKE(new Smoke(0x555555)),
	ASH(new Solid(0x232323)),
	DIRT(new Solid(0x3d371b)),
	WOOD(new Wood(0x603b21, 180, 0.4f, 0.01f, 0.0075f, 0.002f)),
	FUSE(new Fuse()),
	WATER(new Liquid(0x1103dd)),
	PLANT(new Plant(0x11ff33, 3, 3));
	
	public final Element element;
	
	Elements(Element element) {
		element.id = this;
		this.element = element;
	}
}