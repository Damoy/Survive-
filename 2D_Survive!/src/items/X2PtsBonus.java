package items;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;

import entities.mobs.Mob;
import gameTester.Game;
import gameTester.GamePanel;
import graphics.Texture;

public class X2PtsBonus extends Bonus{

	public final static float X2_TIMER_X = GamePanel.WIDTH * 6/7 - 5;
	public final static float X2_TIMER_Y = GamePanel.HEIGHT * 1/5;
	public final static Font X2_FONT = new Font("Times New Roman", Font.PLAIN, 10);
	
	public X2PtsBonus(float initX, float initY) {
		super(Texture.X2, initX, initY);
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
		return new X2PtsBonus(x, y);
	}

}
