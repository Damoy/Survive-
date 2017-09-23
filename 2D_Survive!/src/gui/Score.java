package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import gameTester.Game;
import gameTester.GamePanel;
import toolbox.Terrain;

public class Score {

	public final static float SCORE_X = GamePanel.WIDTH * 6/7;
	public final static float SCORE_Y = GamePanel.HEIGHT * 1/15;
	
	private int score;
	
	public Score(){
		score = 0;
	}
	
	public void render(Terrain terrain, Graphics2D g){
		if(terrain == Terrain.VOLCAN){
			g.setColor(Color.WHITE);
		}
		else{
			g.setColor(Color.BLACK);
		}
		g.setFont(Game.bestScoreFont);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawString(score + "p", SCORE_X, SCORE_Y);
	}

	public void addScore(int bonus){
		score += bonus;
		checkState();
	}
	
	public void subScore(int malus){
		score -= malus;
		checkState();
	}
	
	private void checkState(){
		if(score < 0){
			score = 0;
		}
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	
	
}
