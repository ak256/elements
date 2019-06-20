package com.ak.elements.util;

import java.awt.Color;

/**
 * Utility class for commonly used functions.
 * 
 * @author AK
 */
public final class Utils {

	public static int mix(int col1, int col2) {
		Color c1 = new Color(col1);
		Color c2 = new Color(col2);
		return new Color((c1.getRed() + c2.getRed()) / 2, (c1.getGreen() + c2.getGreen()) / 2, (c1.getBlue() + c2.getBlue()) / 2).getRGB();
	}
	
	public static float clamp(float vx, float f, float g) {
		return vx < f ? f : vx > g ? g : vx;
	}
}