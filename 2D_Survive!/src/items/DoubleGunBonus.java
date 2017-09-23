package items;

import java.awt.Graphics2D;
import java.util.List;

import entities.mobs.Mob;
import gameTester.Game;
import gameTester.GamePanel;
import graphics.Texture;

public class DoubleGunBonus extends Bonus{

	public final static float DOUBLE_GUN_TIMER_X = GamePanel.WIDTH / 2 - 50;
	public final static float DOUBLE_GUN_TIMER_Y = GamePanel.HEIGHT * 1/5;
	
	public DoubleGunBonus(float initX, float initY) {
		super(Texture.DOUBLE_GUN, initX, initY);
		bonusPoints = 500;
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
		return new DoubleGunBonus(x, y);
	}

}
