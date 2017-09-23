package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gameTester.Game;
import gameTester.GamePanel;
import graphics.Texture;
import toolbox.Infos;
import toolbox.Logs;
import toolbox.Save;

public class Menu {

	private Game game;
	
	private int buttonSelected = 0;
	private Font titleFont, font, font2;
	
	private List<Integer> scores = new ArrayList<>();
	
	public Menu(Game game){
		this.game = game;
		font = new Font("Arial", Font.PLAIN, 10);
		font2 = new Font("Arial", Font.PLAIN, 13);
		titleFont = new Font("Times New Roman", Font.PLAIN, 28);
		
		loadScores();
	}
	
	public void loadScores(){
		scores = Save.SListToI(Save.loadScores());
		Collections.sort(scores);
		Collections.reverse(scores);
		Logs.printIList(scores);
	}
	

	public void render(Graphics2D g, int state){
		if(state == 1){
			renderMainMenu(g);
		}
		else if(state == 2){
			renderHelpMenu(g);
		}
		else if(state == 3){
			renderScoresMenu(g);
		}
	}
	
	private void renderHelpMenu(Graphics2D g){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
		g.setColor(Color.WHITE);
		g.setFont(titleFont);
		g.drawString("Help", GamePanel.WIDTH / 2 - 20, GamePanel.HEIGHT / 8);
		
		g.setFont(font);
		g.drawString("Kill the waves of monsters and collect the powerups !", 10, 60);
		
		g.drawString("You have a time limit to collect a maximum of points !",  10, 80);
		g.drawString("You have 5 lives, you lose them by colliding with a mob or if", 10, 100);
		g.drawString("it managed to reach the center of the map !",  10, 120);
		g.drawString("You have 12 lives max !", 10, 140);
		
		g.drawString("You can move with ZQSD / WASD / arrows and shoot with space.",  10, 160);
		g.drawString("You can de/activate the sound by pressing X / C.",  10, 180);
		g.drawString("You can de/activate the character movements by pressing V / B.",  10, 200);
		
		g.drawString("Press enter to return", 10, 230);
	}
	
	
	private void renderMainMenu(Graphics2D g){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
		g.setColor(Color.WHITE);
		Font f_save = g.getFont();
		g.setFont(new Font(f_save.getFontName(), Font.PLAIN, 13));
		//g.setFont(new Font("Arial", Font.PLAIN, 14));
		
		g.drawImage(Texture.GAME_TITLE, GamePanel.WIDTH / 2 - 115, GamePanel.HEIGHT / 4 - 20, null);
		
		g.drawString("Play", GamePanel.WIDTH / 2 - 10, GamePanel.HEIGHT / 2 - 10);
		
		g.drawString("Scores", GamePanel.WIDTH / 2 - 10, GamePanel.HEIGHT / 2 + 30);
		
		g.drawString("Help", GamePanel.WIDTH / 2 - 10, GamePanel.HEIGHT / 2 + 70);
		
		g.drawString("Exit", GamePanel.WIDTH / 2 - 10, GamePanel.HEIGHT / 2 + 110);
		
		g.setFont(new Font("Arial", Font.CENTER_BASELINE, 9));
//		g.drawString("up = Z, W", 15, GamePanel.HEIGHT * 90/100);
//		
		g.drawString("Version 0.2", 10, GamePanel.HEIGHT * 97/100);
		g.drawString("© 2017 Damoy", GamePanel.WIDTH * 75/100, GamePanel.HEIGHT * 93/100);
		g.drawString("All Rights Reserved", GamePanel.WIDTH * 70/100, GamePanel.HEIGHT * 97/100);
		
		
		f_save = g.getFont();
		AffineTransform orig = g.getTransform();
		g.rotate(Math.PI / 5);
		g.setFont(new Font(f_save.getFontName(), 10, 10));

		g.drawString(Infos.MENU_INFO, GamePanel.WIDTH / 4 + 132, - 140);
		g.setTransform(orig);
		g.setFont(f_save);
		
		
		if(buttonSelected == 0){
			g.drawString(">>", GamePanel.WIDTH / 2 - 60, GamePanel.HEIGHT / 2 - 10);
		}
		else if(buttonSelected == 1){
			g.drawString(">>", GamePanel.WIDTH / 2 - 60, GamePanel.HEIGHT / 2 + 30);
		}
		else if(buttonSelected == 2){
			g.drawString(">>", GamePanel.WIDTH / 2 - 60, GamePanel.HEIGHT / 2 + 70);
		}
		else if(buttonSelected == 3){
			g.drawString(">>", GamePanel.WIDTH / 2 - 60, GamePanel.HEIGHT / 2 + 110);
		}
	}
	
	public void renderScoresMenu(Graphics2D g){
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
		g.setColor(Color.WHITE);
		g.setFont(titleFont);
		g.drawString("Best scores", GamePanel.WIDTH / 2 - 60, GamePanel.HEIGHT * 15/100);
		
		g.setFont(new Font("Arial", Font.PLAIN, 14));
		
		try{
			for(int i = 0; i < 5; i++){
				g.drawString((i+1) + ".  " + scores.get(i), GamePanel.WIDTH / 2 - 100, GamePanel.HEIGHT / 2 - 40 + (i * 30));
			}
			
			for(int i = 0; i < 5; i++){
				g.drawString((6+i) + ".  " + scores.get(6+i), GamePanel.WIDTH / 2 + 50, GamePanel.HEIGHT / 2 - 40 + (i * 30));
			}
		}
		catch(Exception e){
			
		}
		
		g.setFont(new Font("Arial", Font.PLAIN, 10));
		g.drawString("Press enter to return", 10, 230);
	}
	
	public void update(){
	}
	
	public Game getGame(){
		return game;
	}
	
	public int getButtonSelected(){
		return buttonSelected;
	}
	
	public void incButtonSelected(){
		buttonSelected++;
	}
	
	public void decButtonSelected(){
		buttonSelected--;
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
}
