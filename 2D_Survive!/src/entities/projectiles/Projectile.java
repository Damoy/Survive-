package entities.projectiles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

import audio.JukeBox;
import entities.Entity;
import entities.Player;
import entities.mobs.Mob;
import entities.mobs.boss.Boss;
import entities.mobs.boss.Madjnouby;
import gameTester.Game;
import gameTester.GamePanel;
import graphics.Texture;
import physics.Collision;
import physics.PhysicsEngine;
import toolbox.Color;
import toolbox.Logs;
import toolbox.Side;

public class Projectile extends Entity{

	private Player player;
	private Side direction;
	private Color projectileColor;
	private float speedX;
	private float speedY;
	private boolean canRender = true;
	
	public Projectile(Player player) {
		super(Texture.PROJECTILE_RED_DOWN, player.getX(), player.getY());
		this.player = player;
		this.direction = player.getDirection();
		this.speedX = player.getProjectileSpeedSource(); // 4f
		this.speedY = player.getProjectileSpeedSource();
		this.projectileColor = player.getProjectileColorSource();
		
		if(this.projectileColor == null){
			System.out.println("Color null ! , going red");
			projectileColor = Color.RED;
		}
		this.texture = getColor(projectileColor);
		
		if(this.texture == null){
			System.out.println("WTF texture null !");
		}
		
		setProjectile();
	}
	

	
	private BufferedImage getColor(Color projectileColor){
		switch(projectileColor){
		case ORANGE:
			return Texture.PROJECTILE_ORANGE_RIGHT;
		case YELLOW:
			return Texture.PROJECTILE_YELLOW_RIGHT;
		case GREEN:
			return Texture.PROJECTILE_GREEN_RIGHT;
		case RED:
			return Texture.PROJECTILE_RED_RIGHT;
		}
		return null;
	}
	
	
	private void setProjectile(){
		switch(direction){
		case LEFT:
			speedX = -speedX;
			speedY = 0;
			x = player.getX() - player.getTexture().getWidth() + texture.getWidth() / 2;
			y = player.getY() + player.getTexture().getHeight() / 2 - texture.getHeight() / 2;
			
			switch(projectileColor){
				case ORANGE:
					texture = Texture.PROJECTILE_ORANGE_LEFT;
					break;
				case YELLOW:
					texture = Texture.PROJECTILE_YELLOW_LEFT;
					break;
				case GREEN:
					texture = Texture.PROJECTILE_GREEN_LEFT;
					break;
				case RED:	
					texture = Texture.PROJECTILE_RED_LEFT;
					break;
			}
			break;
			
		case RIGHT:
			speedX = Math.abs(speedX);
			speedY = 0;
			x = player.getX() + player.getTexture().getWidth() - texture.getWidth() / 2;
			y = player.getY() + player.getTexture().getHeight() / 2 - texture.getHeight() / 2;
			
			switch(projectileColor){
			case ORANGE:
				texture = Texture.PROJECTILE_ORANGE_RIGHT;
				break;
			case YELLOW:
				texture = Texture.PROJECTILE_YELLOW_RIGHT;
				break;
			case GREEN:
				texture = Texture.PROJECTILE_GREEN_RIGHT;
				break;
			case RED:	
				texture = Texture.PROJECTILE_RED_RIGHT;
				break;
		}
			break;
			
		case TOP:
			speedY = -speedY;
			speedX = 0;
			x = player.getX() + player.getTexture().getWidth() / 2 - texture.getWidth() / 2;
			y = player.getY() - player.getTexture().getHeight() / 2;
			
			switch(projectileColor){
			case ORANGE:
				texture = Texture.PROJECTILE_ORANGE_TOP;
				break;
			case YELLOW:
				texture = Texture.PROJECTILE_YELLOW_TOP;
				break;
			case GREEN:
				texture = Texture.PROJECTILE_GREEN_TOP;
				break;
			case RED:	
				texture = Texture.PROJECTILE_RED_TOP;
				break;
		}
			break;
			
		case DOWN:
			speedY = Math.abs(speedY);
			speedX = 0;
			x = player.getX() + player.getTexture().getWidth() / 2 - texture.getWidth() / 2;
			y = player.getY() + player.getTexture().getHeight() - texture.getHeight() / 2;
			
			switch(projectileColor){
			case ORANGE:
				texture = Texture.PROJECTILE_ORANGE_DOWN;
				break;
			case YELLOW:
				texture = Texture.PROJECTILE_YELLOW_DOWN;
				break;
			case GREEN:
				texture = Texture.PROJECTILE_GREEN_DOWN;
				break;
			case RED:	
				texture = Texture.PROJECTILE_RED_DOWN;
				break;
		}
			break;
		}
		box.update(x, y);
	}

	
	@Override
	public void update(Game game, List<Mob> mobs) {
		if(x <= 0 || x >= GamePanel.WIDTH || y <= 0 || y >= GamePanel.HEIGHT || !canRender){
			return;
		}
		
		/**
		 * Collision between bosses and player projectile
		 */
		if(game.getState() == Madjnouby.BOSS_STATE){
			
			List<Boss> bosses = game.getBoss(mobs);
			
			if(bosses.isEmpty()){
				Logs.println("Projectile, update: bosses should not be empty !");
				return;
			}
			
			box.update(x, y);
			
			Iterator<Boss> iterator = bosses.iterator();
			
			while(iterator.hasNext()){
				Boss next = iterator.next();
				
				if(Collision.boxCollide(box, next.getBox())){
					next.hit();
					canRender = false;
					Logs.println2("BOSS HITTED ! Lives remaining: " + next.getLivesRemaining());
				}
			}
			
			x += speedX;
			y += speedY;
			return;
		}
		
		List<Entity> collisions = PhysicsEngine.checkProjectileCollisionWith(game.getTerrain(), player, this, mobs, game.getDecorations());
		
		if(collisions.isEmpty()){
			x += speedX;
			y += speedY;
		}
		else{
			Iterator<Mob> iterator = mobs.iterator();
			try{
				while(iterator.hasNext()){
					for(int i = 0; i < collisions.size(); i++){
						Mob mob = iterator.next();
						if(mob.equals(collisions.get(i))){
							JukeBox.play("enemyHit");
							iterator.remove();
							canRender = false;
						}
					}
				}
			}
			catch(Exception e){
				// 
			}
			canRender = false;;
		}
		box.update(x, y);
	}

	@Override
	public void render(Graphics2D g) {
		if(x <= 0 || x >= GamePanel.WIDTH || y <= 0 || y >= GamePanel.HEIGHT || !canRender){
			return;
		}
		g.drawImage(texture, (int) x, (int) y, null);
	}
	
	public void setProjectileColor(Color color){
		this.projectileColor = color;
		setProjectile();
	}
	
	public void setSpeed(float speed){
		speedX = speed;
		speedY = speed;
		setProjectile();
	}
	
	public Side getDirection(){
		return direction;
	}
	
	public String toString(){
		return "Projectile, " + x + ", " + y + ", " + direction.toString() + ", speeds : " + speedX + ", " + speedY + ".";
	}
	
	public Projectile(){
		super(Texture.PROJECTILE_RED_DOWN, 0, 0);
	}
	
	
	public static Projectile generateNewProjectile(Player player, Projectile projectile){
		Side side = projectile.getDirection();
		float x = projectile.getX();
		float y = projectile.getY();
		Projectile newProjectile = new Projectile();
		newProjectile.player = player;
		newProjectile.direction = Side.copySide(side.toString());
		newProjectile.speedX = projectile.speedX;
		newProjectile.speedY = projectile.speedY;
		newProjectile.projectileColor = Color.copyColor(projectile.projectileColor.toString());
		newProjectile.texture = Texture.copy(projectile.getTexture());
		
		switch(newProjectile.projectileColor){
			case ORANGE:
				switch(newProjectile.direction){
				case TOP:
					projectile.setX(x - 5);
					x = projectile.getX();
					newProjectile.x = x + 10;
					newProjectile.y = y;
					newProjectile.texture = Texture.PROJECTILE_ORANGE_TOP;
					break;
				case DOWN:
					projectile.setX(x - 5);
					x = projectile.getX();
					newProjectile.x = x + 10;
					newProjectile.y = y;
					newProjectile.texture = Texture.PROJECTILE_ORANGE_DOWN;
					break;
				case LEFT:
					newProjectile.x = x;
					newProjectile.y = y + 5;
					newProjectile.texture = Texture.PROJECTILE_ORANGE_LEFT;
					break;
				case RIGHT:
					newProjectile.x = x;
					newProjectile.y = y + 5;
					newProjectile.texture = Texture.PROJECTILE_ORANGE_RIGHT;
					break;
			}
				break;
			case RED:
				switch(newProjectile.direction){
				case TOP:
					projectile.setX(x - 5);
					x = projectile.getX();
					newProjectile.x = x + 10;
					newProjectile.y = y;
					newProjectile.texture = Texture.PROJECTILE_RED_TOP;
					break;
				case DOWN:
					projectile.setX(x - 5);
					x = projectile.getX();
					newProjectile.x = x + 10;
					newProjectile.y = y;
					newProjectile.texture = Texture.PROJECTILE_RED_DOWN;
					break;
				case LEFT:
					newProjectile.x = x;
					newProjectile.y = y + 5;
					newProjectile.texture = Texture.PROJECTILE_RED_LEFT;
					break;
				case RIGHT:
					newProjectile.x = x;
					newProjectile.y = y + 5;
					newProjectile.texture = Texture.PROJECTILE_RED_RIGHT;
					break;
			}
				break;
			case GREEN:
				switch(newProjectile.direction){
				case TOP:
					projectile.setX(x - 5);
					x = projectile.getX();
					newProjectile.x = x + 10;
					newProjectile.y = y;
					newProjectile.texture = Texture.PROJECTILE_GREEN_TOP;
					break;
				case DOWN:
					projectile.setX(x - 5);
					x = projectile.getX();
					newProjectile.x = x + 10;
					newProjectile.y = y;
					newProjectile.texture = Texture.PROJECTILE_GREEN_DOWN;
					break;
				case LEFT:
					newProjectile.x = x;
					newProjectile.y = y + 5;
					newProjectile.texture = Texture.PROJECTILE_GREEN_LEFT;
					break;
				case RIGHT:
					newProjectile.x = x;
					newProjectile.y = y + 5;
					newProjectile.texture = Texture.PROJECTILE_GREEN_RIGHT;
					break;
			}
				break;
			case YELLOW:
				switch(newProjectile.direction){
				case TOP:
					projectile.setX(x - 5);
					x = projectile.getX();
					newProjectile.x = x + 10;
					newProjectile.y = y;
					newProjectile.texture = Texture.PROJECTILE_YELLOW_TOP;
					break;
				case DOWN:
					projectile.setX(x - 5);
					x = projectile.getX();
					newProjectile.x = x + 10;
					newProjectile.y = y;
					newProjectile.texture = Texture.PROJECTILE_YELLOW_DOWN;
					break;
				case LEFT:
					newProjectile.x = x;
					newProjectile.y = y + 5;
					newProjectile.texture = Texture.PROJECTILE_YELLOW_LEFT;
					break;
				case RIGHT:
					newProjectile.x = x;
					newProjectile.y = y + 5;
					newProjectile.texture = Texture.PROJECTILE_YELLOW_RIGHT;
					break;
			}
		}
		
		return newProjectile;
	}
	
	public Color getColor(){
		return projectileColor;
	}
	
	
	
	

}
