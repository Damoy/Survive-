package gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import gameTester.GamePanel;
import graphics.Texture;

public class Life{

	public final static int BASE_LIVES = 3;
	public final static float POS_X = GamePanel.WIDTH * 10/100;
	public final static float POS_Y = GamePanel.HEIGHT * 10/100;
	
	private BufferedImage texture;
	private int lives;
	
	public Life(int nb){
		lives = nb;
		setTexture();
	}
	
	public Life(){
		lives = BASE_LIVES;
		setTexture();
	}
	
	private void setTexture(){
		texture = Texture.LIFE;
	}
	

	public BufferedImage getTexture() {
		return texture;
	}

	public int getLives() {
		return lives;
	}
	
	public void addLife(){
		if(lives >= 12){
			return;
		}
		lives++;
	}
	
	public void subLife(){
		lives--;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}
	
	public void render(Graphics2D g, float x, float y){
		g.drawImage(texture, (int) x, (int) y, null);
	}
	
}
