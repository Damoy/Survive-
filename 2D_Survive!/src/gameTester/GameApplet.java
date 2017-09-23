package gameTester;

import java.applet.Applet;
import java.awt.BorderLayout;

public class GameApplet extends Applet {

	private static final long serialVersionUID = 1779638091085528729L;
	private Game game = new Game();

	public void init() {
		setLayout(new BorderLayout());
		add(game.getGamePanel(), BorderLayout.CENTER);
	}

	public void start() {
		game.start();
	}

	public void stop() {
		game.stop();
	}
}