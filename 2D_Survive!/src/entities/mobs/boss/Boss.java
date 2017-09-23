package entities.mobs.boss;

import java.awt.image.BufferedImage;

import entities.Player;
import entities.mobs.Mob;
import toolbox.Side;
import toolbox.Timer;

public abstract class Boss extends Mob{

	protected Timer timerHit;
	protected boolean hit;
	
	
	public Boss(BufferedImage texture, float initX, float initY, Player playerToChase, float speedX, float speedY,
			Side side, int bonusPoints, int timesHit) {
		super(texture, initX, initY, playerToChase, speedX, speedY, side, bonusPoints, timesHit);
	}

	public abstract void setLives(int lives);
	public abstract int getLives();
	public abstract void hit();
	public abstract int getLivesRemaining();


	public Timer getTimerHit() {
		return timerHit;
	}

	public void setTimerHit(Timer timerHit) {
		this.timerHit = timerHit;
	}

	public boolean isHit() {
		return hit;
	}

	public void setHit(boolean hit) {
		this.hit = hit;
	}
	
	
	
	

}
