package entities.projectiles;

import java.awt.Graphics2D;
import java.util.List;

import audio.JukeBox;
import entities.Entity;
import entities.Player;
import entities.mobs.Mob;
import entities.mobs.boss.Volcanop;
import gameTester.Game;
import gameTester.GamePanel;
import graphics.Texture;
import physics.Collision;
import sun.net.www.content.text.plain;
import toolbox.Logs;
import toolbox.Maths;
import toolbox.Side2;

public class VolcanopProjectile extends Entity{

	private final static float BASE_SPEED = 2f;
	private Side2 startSide;
	private float speedX;
	private float speedY;
	private Volcanop source;
	
	
	public VolcanopProjectile(Volcanop source, Side2 side){
		super(Texture.VOLCANO_PROJECTILE, 0, 0);
		this.source = source;
		this.startSide = side;
		
		// set the pos
		setInitPos();
		// set the speed
		setSpeeds();
	}
	
	
	private void setInitPos(){
		switch(startSide){
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
				Logs.println2("Side was null !");
		}
	}
	
	private float getRandomSpeed(){
		return Maths.frandR(0.1f, 1.5f);
	}
	
	//private float speed = getRandomSpeed();
	private float speed = BASE_SPEED;
	
	private void setSpeeds(){
		speedX = 0;
		speedY = 0;
		
		switch(startSide){
			case TOP:
				speedY = -speed;
				break;
			case TOP_LEFT:
				speedY = -speed;
				speedX = -speed;
				break;
			case TOP_RIGHT:
				speedY = -speed;
				speedX = speed;
				break;
			case LEFT:
				speedX = -speed;
				break;
			case RIGHT:
				speedX = speed;
				break;
			case BOT:
				speedY = speed;
				break;
			case BOT_LEFT:
				speedY = speed;
				speedX = -speed;
				break;
			case BOT_RIGHT:
				speedY = speed;
				speedX = speed;
				break;
			case NULL:
				speedX = 0;
				speedY = 0;
				break;
			default:
				break;
		}
	}

	@Override
	public void update(Game game, List<Mob> mobs) {
		updatePos();
		checkPlayerCollision(game);
	}
	
	private void checkPlayerCollision(Game game){
		// assuming that
		// all the mobs got the same player
		Player p = game.getPlayer();
		if(Collision.boxCollide(box, p.getBox())){
			// JukeBox.playLose();
			p.subLife();
			
			// TODO ADD THIS == REMOVE THIS
			source.addToProjectilesToRemove(this);
		}
	}
	
	
	
	private void updatePos(){
		x += speedX;
		y += speedY;
		box.update(x, y);
	}
	
	public boolean isDead(){
		return (x < 0 || x > GamePanel.WIDTH || y < 0 || y > GamePanel.HEIGHT);
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(texture, (int) x, (int) y, null);
	}

}
