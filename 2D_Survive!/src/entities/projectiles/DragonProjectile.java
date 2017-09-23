package entities.projectiles;

import java.awt.Graphics2D;
import java.util.List;

import entities.Entity;
import entities.Player;
import entities.mobs.Mob;
import entities.mobs.boss.Dragone;
import entities.mobs.boss.Dragoone;
import entities.mobs.boss.VolcanopDragon;
import gameTester.Game;
import gameTester.GamePanel;
import graphics.Texture;
import physics.Collision;
import toolbox.Logs;
import toolbox.Maths;
import toolbox.Side2;
import toolbox.Timer;

public class DragonProjectile extends Entity{

	private final static float BASE_SPEED = 2f;
	private Side2 dragooneStartSide;
	private float speedX;
	private float speedY;
	
	private VolcanopDragon source;
	private Timer timerBeforeMove;
	
	public final static byte DRAGOONE_TYPE = 1;
	public final static byte DRAGONE_TYPE = 2;
	
	private byte type;
	
	public DragonProjectile(VolcanopDragon source, Side2 side){
		super(Texture.DRAGOONE_BOMB, 0, 0);
		this.source = source;
		dragooneStartSide = side;
		timerBeforeMove = new Timer(2);
		setDragonType();
		setInitTexture();
		setInitPos();
		setSpeeds();
	}
	
	private void setDragonType(){
		if(source instanceof Dragoone){
			type = DRAGOONE_TYPE;
		}
		if(source instanceof Dragone){
			type = DRAGONE_TYPE;
		}
	}
	
	private void setInitTexture(){
		if(type == DRAGOONE_TYPE){
			texture = Texture.DRAGOONE_BOMB;
		}
		if(type == DRAGONE_TYPE){
			texture = Texture.DRAGONE_BOMB;
		}
	}
	
	private void setInitPos(){
		switch(dragooneStartSide){
			case BOT_LEFT:
				x = source.getX() + source.getTexture().getWidth() / 3 - texture.getWidth() / 2;
				y = source.getY() + source.getTexture().getHeight() - texture.getHeight() / 2;
				break;
			case BOT_RIGHT:
				x = source.getX() + source.getTexture().getWidth() + texture.getWidth() / 2;
				y = source.getY() + source.getTexture().getHeight() - texture.getHeight() / 2;
				break;
			case TOP_LEFT:
				x = source.getX() + source.getTexture().getWidth() / 3 - texture.getWidth() / 2;
				y = source.getY() - texture.getHeight();
				break;
			case TOP_RIGHT:
				x = source.getX() + source.getTexture().getWidth() + texture.getWidth() / 2;
				y = source.getY() - texture.getHeight();
				break;
			case LEFT:
				x = source.getX() - texture.getWidth();
				y = source.getY() + (source.getTexture().getHeight() / 2);
				break;
			case RIGHT:
				x = source.getX() + source.getTexture().getWidth() + texture.getWidth();
				y = source.getY() + (source.getTexture().getHeight() / 2);
				break;
			case TOP:
				x = source.getX() + source.getTexture().getWidth() / 2 - texture.getWidth() / 2;
				y = source.getY() - texture.getHeight();
				break;
			case BOT:
				x = source.getX() + source.getTexture().getWidth() / 2 - texture.getWidth() / 2;
				y = source.getY() + source.getTexture().getHeight();
				break;
			default:
				//throw new IllegalStateException();
		}
	}
	
	private float getRandomSpeed(){
		return Maths.frandR(0.5f, 1.1f);
	}
	
	private void setSpeeds(){
		speedX = 0;
		speedY = 0;
		
		switch(dragooneStartSide){
			case TOP:
				speedY = -getRandomSpeed();
				break;
			case TOP_LEFT:
				speedX = -getRandomSpeed();
				speedY = -getRandomSpeed();
				break;
			case TOP_RIGHT:
				speedX = getRandomSpeed();
				speedY = -getRandomSpeed();
				break;
			case LEFT:
				speedX = -getRandomSpeed();
				break;
			case RIGHT:
				speedX = getRandomSpeed();
				break;
			case BOT:
				speedY = getRandomSpeed();
				break;
			case BOT_LEFT:
				speedX = -getRandomSpeed();
				speedY = getRandomSpeed();
				break;
			case BOT_RIGHT:
				speedX = getRandomSpeed();
				speedY = getRandomSpeed();
				break;
			case NULL:
				break;
			default:
				break;
		}
	}

	@Override
	public void update(Game game, List<Mob> mobs) {
		if(timerBeforeMove == null || !(timerBeforeMove.getTimeLeft() == 0)){
			timerBeforeMove.increment();
			return;
		}
		updatePos();
		checkPlayerCollision(game);
	}
	
	private void checkPlayerCollision(Game game){
		Player p = game.getPlayer();
		
		if(Collision.boxCollide(box, p.getBox())){
			p.subLife();
			dead = true;
		}
	}
	
	private boolean textureSet = false;
	
	private void setRedTexture(){
		if(!textureSet){
			if(type == DRAGOONE_TYPE){
				texture = Texture.DRAGOONE_BOMB_RED;
			}
			if(type == DRAGONE_TYPE){
				texture = Texture.DRAGONE_BOMB_RED;
			}
			textureSet = true;
		}
	}
	
	private boolean dead = false;
	
	private void updatePos(){
		setRedTexture();
		x += speedX;
		y += speedY;
		box.update(x, y);
		
		if(x < 0 || x > GamePanel.WIDTH || y < 0 || y > GamePanel.HEIGHT){
			dead = true;
		}
		
		Logs.println2(type == DRAGOONE_TYPE ? "DRAGOONE PROJ" : "DRAGONE PROJ");
	}
	
	public boolean isDead(){
		return dead;
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(texture, (int) x, (int) y, null);
	}

}
