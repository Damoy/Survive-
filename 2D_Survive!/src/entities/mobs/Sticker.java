package entities.mobs;

import java.awt.Graphics2D;
import java.util.List;

import entities.Player;
import gameTester.Game;
import gameTester.GamePanel;
import graphics.Texture;
import physics.Collision;
import toolbox.Maths;
import toolbox.Side;
import static world.TileMap.*;

public class Sticker extends Mob{

	public Sticker(Player player) {
		super(Texture.STICKER_NORMAL, 0, 0, player, Maths.irand(1) + 1f,
				Maths.irand(1) + 1f, Side.getRandomSide(), Maths.irand(200) + 50, 1);
	}
	
	
	@Override
	protected void setMob(){
		switch(side){
		case LEFT:
			speedX = Math.abs(speedX);
			speedY = 0;
			x = 16f;
			y = GamePanel.HEIGHT / 2 - 8;
			texture = Texture.STICKER_RIGHT;
			break;
		case RIGHT:
			speedX = -speedX;
			speedY = 0;
			x = GamePanel.WIDTH - 16f;
			y = GamePanel.HEIGHT / 2 - 8;
			texture = Texture.STICKER_LEFT;
			break;
		case TOP:
			speedY = Math.abs(speedY);
			speedX = 0;
			x = GamePanel.WIDTH / 2 - 8f;;
			y = 16f;
			texture = Texture.STICKER_NORMAL;
			break;
		case DOWN:
			x = GamePanel.WIDTH / 2 - 8f;;
			y = GamePanel.HEIGHT - 16f;
			speedY = -speedY;
			speedX = 0;
			texture = Texture.STICKER_NORMAL;
			break;
		}
		box.update(x, y);
	}

	
	@Override
	public void update(Game game, List<Mob> mobs) {
		if((side == Side.LEFT && x >= CENTER_X) ||
				(side == Side.RIGHT && x <= CENTER_X) ||
				(side == Side.TOP && y >= CENTER_Y) ||
				(side == Side.DOWN && y <=CENTER_Y)){
			playerToChase.getScoreObj().subScore(bonusPoints);
			playerToChase.subLife();
			removeThis(mobs);
			return;
		}
		
		if(Collision.boxCollide(box, playerToChase.getBox())){
			playerToChase.subLife();
			removeThis(mobs);
			return;
		}
		x += speedX;
		y += speedY;
		box.update(x, y);
	}
	

	
	@Override
	public void render(Graphics2D g) {
		g.drawImage(texture, (int) x, (int) y, null);
	}


	@Override
	public Mob clone() {
		Sticker sticker = new Sticker(playerToChase);
		sticker.setPos(x, y);
		sticker.setSide(side);
		sticker.setSpeedX(speedX);
		sticker.setSpeedY(speedY);
		sticker.setTexture(texture);
		return sticker;
	}
	


}
