package com.ak.elements.classes.solids;

import com.ak.elements.Game;
import com.ak.elements.Main;
import com.ak.elements.classes.Element;
import com.ak.elements.classes.Elements;
import com.ak.elements.util.Utils;

/**
 * An element that grows when around itself and water.
 * 
 * @author Andrew Klinge
 */
public class Plant extends Wood {
	protected final int baseColor;
	protected long updated2; // used for growing
	protected int life;
	protected final int maxLife;
	protected int growth;
	protected final int maxGrowth;

	public Plant(int color, int growth, int maxGrowth) {
		super(color, 100, 0, 0.09f, 0.005f, 0);
		this.baseColor = color;
		this.maxGrowth = maxGrowth;
		this.growth = Math.min(maxGrowth, growth);
		life = Main.rand.nextInt(500) + 400;
		maxLife = life;
	}

	@Override
	public void update(int x, int y) {
		super.update(x, y);

		if(Main.time - updated2 >= 30) {
			updated2 = Main.time;
			
			// change color based on health
			if(!burning) this.color = Utils.mix(baseColor, (int) (0x00ff00 * (life / (float) maxLife)));

			Element[] adj = getAdjacent(x, y);
			boolean hasAir = hasAir(x, y);
			if(hasAir && (equals(adj[0]) || equals(adj[1]) || equals(adj[2]) || equals(adj[3]))) {
				{// use water
					boolean wu = (adj[0] != null && adj[0].equals(Elements.WATER.element));
					boolean wl = (adj[3] != null && adj[3].equals(Elements.WATER.element));
					boolean wr = (adj[1] != null && adj[1].equals(Elements.WATER.element));
					if((wu || wl || wr)) {
						if(life < maxLife) {
							life += 100;
						}
						if(growth < maxGrowth) {
							growth++;
							if(wu)
								Main.game.grid[x][y - 1] = null;
							if(wl)
								Main.game.grid[x - 1][y] = null;
							if(wr)
								Main.game.grid[x + 1][y] = null;
						}
					}
				}
				if(growth != 0) {// grow
					boolean nu = (adj[0] == null);
					boolean nl = (adj[3] == null);
					boolean nr = (adj[1] == null);
					if((nu || nl || nr) && Main.rand.nextFloat() < 0.3f) {
						int dir = 0;
						if(!nu || ((nl || nr) && Main.rand.nextFloat() < 0.15f)) {
							if(nl && nr) {
								if(Main.rand.nextBoolean()) {
									dir = 3;
								} else {
									dir = 1;
								}
							} else if(nl) {
								dir = 3;
							} else {
								dir = 1;
							}
						}
						Plant clone = clone();
						clone.life = maxLife;
						clone.growth = growth - 1;
						Main.game.grid[x + (dir == 3 ? -1 : dir == 1 ? 1 : 0)][y + (dir == 0 ? -1 : 0)] = clone;
						growth--;
					} else if(equals(adj[0]) && Main.rand.nextFloat() <= 0.6f) { // share growth potential
						((Plant) adj[0]).growth++;
						growth--;
					} else if(equals(adj[3]) && Main.rand.nextFloat() <= 0.5f) {
						((Plant) adj[3]).growth++;
						growth--;
					} else if(equals(adj[1])) {
						((Plant) adj[1]).growth++;
						growth--;
					}
				}
			} else {
				life -= hasAir ? 20 : 60;
				if(life <= 0) {
					Main.game.grid[x][y] = null;
					return;
				}
			}
		}
	}
	
	private boolean hasAir(int x, int y) {
		// must be an opening within 2 grid spaces
		final int radius = 2;
		final int min = 12;
		int count = 0;
		
		for(int i = x - radius; i < x + radius + 1; i++) {
			for(int j = y - radius; j < y + radius + 1; j++) {
				if(i > 0 && j > 0 && i < Game.SIZE - 1 && j < Game.SIZE - 1 && Main.game.grid[i][j] == null) {
					count++;
				}
			}
		}
		
		return count >= min;
	}

	@Override
	public Plant clone() {
		Plant clone = new Plant(baseColor, growth, maxGrowth);
		clone.id = id;
		return clone;
	}
}