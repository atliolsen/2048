package game;

import javax.swing.JFrame;

public class StartGame {
	public static void main(String[] args) {
		Game game = new Game();
		
		JFrame windows = new JFrame("2048");
		windows.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		windows.setResizable(false);
		windows.add(game);
		windows.pack();
		windows.setLocationRelativeTo(null);
		windows.setVisible(true);
		
		game.start();
		
	}
}
