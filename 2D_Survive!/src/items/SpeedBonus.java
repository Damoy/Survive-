package items;

import java.awt.Graphics2D;
import java.util.List;

import entities.mobs.Mob;
import gameTester.Game;
import gameTester.GamePanel;
import graphics.Texture;

public class SpeedBonus extends Bonus{

	public final static float SPEED_TIMER_X = GamePanel.WIDTH / 2 + 25;
	public final static float SPEED_TIMER_Y = GamePanel.HEIGHT * 1/5;
	
	public SpeedBonus(float initX, float initY) {
		super(Texture.SPEED_BOOST, initX, initY);
		bonusPoints = 300;
	}

	@Override
	public void update(Game game, List<Mob> mobs) {
		
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(texture, (int) x, (int) y, null);
	}

	@Override
	public Bonus clone() {
		return new SpeedBonus(x, y);
	}

}
