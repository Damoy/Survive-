package graphics;

import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.sun.xml.internal.ws.db.glassfish.BridgeWrapper;

import gameTester.Game;
import gameTester.GamePanel;


public class Window {

	private JFrame win;
	private Game game;
	private GamePanel gameP;
	
	public Window(String title, Game game){
		win = new JFrame(title);
		this.game = game;
	}
	
	public Game getGame(){
		return game;
	}
	
	private void setDefaultState(){
		gameP = new GamePanel(game, this);
		win.add(gameP);
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.setResizable(false);
		win.pack();
		win.setLocationRelativeTo(null);
		setIconImage("./data/logo_20x20.png", "./data/logo_26x26.png",
			"./data/logo_28x28.png");
	}
	
	public void start(){
		setDefaultState();
		win.setVisible(true);
	}
	
	
	public void setTitle(String newTitle){
		win.setTitle(Game.GAME_TITLE + " | " + newTitle);
	}
	
	public void setIconImage(String path1, String path2, String path3){
		ImageIcon ii1 = new ImageIcon(path1);
		ImageIcon ii2 = new ImageIcon(path2);
		ImageIcon ii3 = new ImageIcon(path3);
		setIconImage(ii1.getImage(), ii2.getImage(), ii3.getImage());
	}
	
	/** This method sets the icon image of the frame according to the best imageIcon size requirements for the system's appearance settings.
	 *  This method should only be called after pack() or show() has been called for the Frame.
	 * @param frame The Frame to set the image icon for.
	 * @param image20x20 An image, 20 pixels wide by 20 pixels high
	 * @param image26x26 An image, 26 pixels wide by 26 pixels high
	 * @param image28x28 An image, 28 pixels wide by 28 pixels high
	 */
	private void setIconImage(Image image20x20, Image image26x26, Image image28x28) {
	    Insets insets = win.getInsets();
	    int titleBarHeight = insets.top;
	    if (titleBarHeight == 32) {
	        // It's 'Windows Classic Style with Large Fonts', so use a 26 x 26 image
	        if (image26x26 != null) win.setIconImage(image26x26);
	    }
	    else {
	        // Use the default 20 x 20 image - Looks fine on all other Windows Styles & Font Sizes (except 'Windows Classic Style with Extra Large Fonts' where image is slightly distorted. Have to live with that as cannot differentiate between 'Windows XP Style with Normal Fonts' appearance and 'Windows Classic Style with Extra Large Fonts' appearance as they both have the same insets values)
	        if (image20x20 != null) win.setIconImage(image20x20);
	    }
	}
	
	public GamePanel getGamePanel(){
		return gameP;
	}
}
