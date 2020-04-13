package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Game extends JPanel implements KeyListener, Runnable {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 400;
	public static final int HEIGHT = 630;
	public static final Font main = new Font("Times New Romans", Font.PLAIN, 28);
	private Thread game;
	private boolean running;
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private GameBoard board;
	
	private long startTime;
	private long elapsed;
	private boolean set;

	public Game() {
		setFocusable(true);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		addKeyListener(this);
		
		// Center and move 10 pixels
		board = new GameBoard(WIDTH/2 - GameBoard.BOARD_WIDTH / 2, HEIGHT - GameBoard.BOARD_HEIGHT - 10);
	}

	private void update() {
		if (Keyboard.pressed[KeyEvent.VK_SPACE]) {
			System.out.println("Hit Space");
		}
		if (Keyboard.pressed[KeyEvent.VK_DOWN]) {
			System.out.println("Hit DOWN");
		}
		board.update();
		Keyboard.update();

	}

	private void render() {
		// Draw on the board.
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		// Render Board
		board.render(g);
		g.dispose();

		Graphics2D g2d = (Graphics2D) getGraphics();
		g2d.drawImage(image, 0, 0, null);
	}

	@Override
	public void run() {
		// Setting up variables
		int fps = 0, updates = 0;
		long fpsTimer = System.currentTimeMillis();
		double nsPerUpdate = 1000000000.0 / 60;

		// Last update time in nanoseconds
		double then = System.nanoTime();
		double unprocessed = 0;

		while (running) {
			// Checking if update is needed
			boolean shouldRender = false;
			double now = System.nanoTime();
			unprocessed += (now - then) / nsPerUpdate;
			then = now;

			// update queue
			while (unprocessed >= 1) {
				updates++;
				update();
				unprocessed--;
				shouldRender = true;
			}
			// Rendering
			if (shouldRender) {
				fps++;
				render();
				shouldRender = false;
			} else {
				try {
					Thread.sleep(1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// FPS Timer
		if (System.currentTimeMillis() - fpsTimer > 1000) {
			System.out.printf("%d fps %d updates", fps, updates);
			System.out.println();
			fps = 0;
			updates = 0;
			fpsTimer += 1000;

		}
	}

	public synchronized void start() {
		if (running) {
			return;
		}
		running = true;
		game = new Thread(this, "game");
		game.start();
	}

	public synchronized void stop() {
		if (!running) {
			return;
		}
		running = false;
		System.exit(0);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Keyboard.keyPressed(e);

	}

	@Override
	public void keyReleased(KeyEvent e) {
		Keyboard.keyReleased(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
