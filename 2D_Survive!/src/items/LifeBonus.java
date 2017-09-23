package items;

import java.awt.Graphics2D;
import java.util.List;

import entities.mobs.Mob;
import gameTester.Game;
import graphics.Texture;

public class LifeBonus extends Bonus{

	public LifeBonus(float initX, float initY) {
		super(Texture.SMALL_LIFE, initX, initY);
		bonusPoints = 400;
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
		return new LifeBonus(x, y);
	}

}
