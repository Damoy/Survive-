package items;

import java.awt.Graphics2D;
import java.util.List;

import entities.mobs.Mob;
import gameTester.Game;
import graphics.Texture;

public class BossEnterBonus extends Bonus{

	public BossEnterBonus(float initX, float initY) {
		super(Texture.BOSS_ENTER, initX, initY);
		bonusPoints = 10000;
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
		return new BossEnterBonus(x, y);
	}

}
