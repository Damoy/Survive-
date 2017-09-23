package entities.decorations;

import java.awt.Graphics2D;
import java.util.List;

import entities.mobs.Mob;
import gameTester.Game;
import graphics.Texture;

public class MobSpawnDecoration extends DecorationEntity{

	private byte type;
	
	public MobSpawnDecoration(byte type, float initX, float initY) {
		super(Texture.SPAWN_IND_DOWN, initX, initY);
		this.type = type;
		generateTexture();
	}
	
	private void generateTexture(){
		switch(type){
			case (byte) 1:
				texture = Texture.SPAWN_IND_RIGHT;
				break;
			case (byte) 2:
				texture = Texture.SPAWN_IND_DOWN;
				break;
			case (byte) 3:
				texture = Texture.SPAWN_IND_LEFT;
				break;
			case (byte) 4:
				texture = Texture.SPAWN_IND_TOP;
				break;
			default:
				texture = Texture.SPAWN_IND_DOWN;
		}
	}

	@Override
	public void update(Game game, List<Mob> mobs) {
		
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(texture, (int) x, (int) y, null);
	}

}
