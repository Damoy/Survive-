package entities.decorations;

import java.awt.Graphics2D;
import java.util.List;

import entities.mobs.Mob;
import gameTester.Game;
import graphics.Texture;

public class LavaDecoration extends DecorationEntity{

	public LavaDecoration(float initX, float initY) {
		super(Texture.LAVA, initX, initY);
	}

	@Override
	public void update(Game game, List<Mob> mobs) {
		
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(texture, (int) x, (int) y, null);
	}

}
