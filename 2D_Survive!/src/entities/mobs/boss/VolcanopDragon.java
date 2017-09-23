package entities.mobs.boss;

import java.awt.image.BufferedImage;

import entities.Player;
import entities.projectiles.DragonProjectile;
import toolbox.Maths;
import toolbox.Side;

public abstract class VolcanopDragon extends Boss{
	
	protected int lives;
	protected Volcanop guardian;

	public VolcanopDragon(BufferedImage texture, float initX, float initY, Player playerToChase, float speedX,
			float speedY, Side side, int bonusPoints, int timesHit) {
		super(texture, initX, initY, playerToChase, speedX, speedY, side, bonusPoints, timesHit);
	}
	
	@Override
	public void setLives(int lives) {
		this.lives = lives;
	}


	@Override
	public int getLives() {
		return lives;
	}

	@Override
	public int getLivesRemaining() {
		return lives;
	}
	
	
	public static float getRandomSpeed(){
		return Maths.frandR(0.35f, 0.75f);
	}
	

}
