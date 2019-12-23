package com.ak.elements.classes.solids;

import java.awt.Color;

import com.ak.elements.Main;
import com.ak.elements.classes.Element;
import com.ak.elements.classes.Elements;
import com.ak.elements.classes.properties.Flammable;
import com.ak.elements.util.Utils;

/**
 * A non-falling element that ignites when around hot flame and burns slowly.
 * 
 * @author Andrew Klinge
 */
public class Wood extends Element implements Flammable {
	protected boolean burning;
	protected final int burnTime;
	protected int burnTimer;
	protected float energy;
	protected final float ignition, igniteOther, smokeChance, sparkChance;
	protected final int baseColor;

	/**
	 * @param color
	 * @param burnTime - how long it takes to burn once ignited
	 * @param ignition - energy required to ignite
	 * @param igniteOther - percent chance to spread fire to nearby flammable elements every update (0.0 - 1.0)
	 * @param smokeChance - percent chance to create smoke every update
	 * @param sparkChance - percent chance to create a flame particle every update
	 */
	public Wood(int color, int burnTime, float ignition, float igniteOther, float smokeChance, float sparkChance) {
		super(color);
		this.baseColor = color;
		this.burnTime = burnTime;
		this.ignition = ignition;
		this.sparkChance = sparkChance;
		this.igniteOther = igniteOther;
		this.smokeChance = smokeChance;
	}

	@Override
	public void update(int x, int y) {
		if(burning && Main.time - updated >= 4) {
			// change color based on how long it has been burning
			this.color = Math.min(Utils.mix(baseColor, Color.HSBtoRGB(0.05f * Math.min(1, energy * 0.3f), 1.0f, 1.0f)), 0xff);
			
			burnTimer--;
			if(burnTimer <= 0) { // replace with ash once burnt completely
				Main.game.grid[x][y] = Elements.ASH.element.clone();
				return;
			}
			
			// try to ignite adjacent wood if on fire
			Element[] adj = getAdjacent(x, y);
			float n = energy * (burnTimer / (float) burnTime) * 2f; // energy to give to other elements if ignited
			int i = 0; // number of iterations
			for(Element e : adj) {
				if(e instanceof Flammable && Main.rand.nextFloat() <= igniteOther) {
					((Flammable) e).ignite(n);
				} else if(e == null) {
					// find open spot for element to appear
					int xx = x + (i == 3 ? -1 : i == 1 ? 1 : 0);
					int yy = y + (i == 0 ? -1 : i == 2 ? 1 : 0);
					
					if(xx > -1 && yy > -1 && xx < Main.game.grid.length && yy < Main.game.grid[0].length) {
						Element create = null;
						
						if(Main.rand.nextFloat() <= smokeChance) create = Elements.SMOKE.element.clone();
						else if(Main.rand.nextFloat() <= sparkChance) create = Elements.FLAME.element.clone();
						
						Main.game.grid[xx][yy] = create;
					}
				}
				i++;
			}
			updated = Main.time;
		}
	}

	@Override
	public void ignite(float energy) {
		if(!burning && energy >= ignition) {
			this.energy = Math.max(energy, 0.5f);
			burning = true;
			burnTimer = burnTime;
		}
	}

	@Override
	public Wood clone() {
		Wood clone = new Wood(color, burnTime, ignition, igniteOther, smokeChance, sparkChance);
		clone.id = id;
		return clone;
	}
}