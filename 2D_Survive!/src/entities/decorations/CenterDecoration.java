package entities.decorations;

import java.awt.Graphics2D;
import java.util.List;

import entities.mobs.Mob;
import gameTester.Game;
import graphics.Texture;
import world.TileMap;

public class CenterDecoration extends DecorationEntity{

	public CenterDecoration() {
		super(Texture.CENTER, TileMap.CENTER_X, TileMap.CENTER_Y);
	}

	@Override
	public void update(Game game, List<Mob> mobs) {
		
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(texture, (int) x, (int) y, null);
	}

}
