package items;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;

import entities.mobs.Mob;
import gameTester.Game;
import gameTester.GamePanel;
import graphics.Texture;

public class DropBonus extends Bonus{

	public final static float DROP_UP_X = GamePanel.WIDTH * 1/5 - 45;
	public final static float DROP_UP_Y = GamePanel.HEIGHT * 1/5;
	public final static Font DROP_UP_FONT = new Font("Times New Roman", Font.PLAIN, 10);
	
	public DropBonus(float initX, float initY) {
		super(Texture.DROP_UP, initX, initY);
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
		return new DropBonus(x, y);
	}

}
