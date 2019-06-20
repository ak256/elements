package com.ak.elements;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Random;

import javax.swing.JFrame;

import com.ak.elements.util.Input;

/**
 * Elements: a simple falling sand game clone.
 * <p>
 * Made for fun, and also a remake of the first game I ever made. <br>
 * Has a pretty simple engine that I wrote in a short amount of time.
 * 
 * @author Andrew Klinge
 *
 */
public class Main extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static final Random rand = new Random();
	public static long time;
	public static JFrame frame;
	public static Game game;

	private boolean running;
	private int frames;
	private Input input;

	public void init() {
		game = new Game();
		input = new Input(this);
	}

	public void update() {
		game.update();
		input.update();
		frame.setTitle("Elements   " + frames + " fps");
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, frame.getWidth(), frame.getHeight());

		game.render(g);

		g.dispose();
		bs.show();
	}

	public void run() {
		init();

		int frames = 0;
		double unprocessedSeconds = 0;
		long lastTime = System.nanoTime();
		double secondsPerTick = 1 / 60.0;
		int tickCount = 0;

		while(running) {
			boolean ticked = false;
			long now = System.nanoTime();
			long passedTime = now - lastTime;

			lastTime = now;
			if(passedTime < 0)
				passedTime = 0;
			if(passedTime > 100000000)
				passedTime = 100000000;
			unprocessedSeconds += passedTime / 1000000000.0;

			while(unprocessedSeconds > secondsPerTick) {
				update();
				time++;
				unprocessedSeconds -= secondsPerTick;
				ticked = true;

				tickCount++;
				if(tickCount % 60 == 0) {
					this.frames = frames;
					lastTime += 1000;
					frames = 0;
				}
			}

			if(ticked) {
				render();
				frames++;

			} else {
				try {
					Thread.sleep(1);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void start() {
		new Thread(this).start();
		running = true;
	}

	public void stop() {
		running = false;
	}

	public static void main(String[] args) {
		Main main = new Main();

		frame = new JFrame("Elements");
		frame.add(main);
		frame.setSize(new Dimension(600, 600));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
		main.start();
	}
}