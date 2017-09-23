package entities.projectiles;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import entities.Entity;
import entities.Player;
import entities.mobs.Mob;
import entities.mobs.boss.Coctus;
import gameTester.Game;
import gameTester.GamePanel;
import graphics.Texture;
import physics.Collision;
import toolbox.Side2;

public class CoctusProjectile extends Entity{

	private boolean canRender;
	private boolean canUpdate;
	private Side2 side;
	private int rotation;
	private float speedX, speedY;
	private Coctus boss;
	
	
	public CoctusProjectile(Coctus boss, Side2 side, float speedX, float speedY) {
		super(Texture.COCTUS_PROJECTILE, 0, 0);
		this.boss = boss;
		// speedX = 1f;
		// speedY = 1f;
		this.speedX = speedX;
		this.speedY = speedY;
		canRender = true;
		canUpdate = true;
		this.side = Side2.copy(side.toString());
		setProjectile();
	}
	
	
	private void setProjectile(){
		setPosition();
	}
	
	
	private void setPosition(){
		if(!canUpdate) return;
		switch(side){
			case BOT_LEFT:
				//Logs.println2("BOT LEFT SIDE, COCTUS PROJ");
				x = boss.getX() + boss.getTexture().getWidth() / 3 - texture.getWidth() / 2;
				y = boss.getY() + boss.getTexture().getHeight() - texture.getHeight() / 2;
				break;
			case BOT_RIGHT:
				//Logs.println2("BOT RIGHT SIDE, COCTUS PROJ");
				//x = boss.getX() + (2 * (boss.getTexture().getWidth() / 3)) - texture.getWidth() / 2;
				x = boss.getX() + boss.getTexture().getWidth() + texture.getWidth() / 2;
				y = boss.getY() + boss.getTexture().getHeight() - texture.getHeight() / 2;
				break;
			case TOP_LEFT:
				//Logs.println2("TOP LEFT SIDE, COCTUS PROJ");
				x = boss.getX() + boss.getTexture().getWidth() / 3 - texture.getWidth() / 2;
				y = boss.getY() - texture.getHeight();
				break;
			case TOP_RIGHT:
				//Logs.println2("TOP RIGHT SIDE, COCTUS PROJ");
				//x = boss.getX() + (2 * (boss.getTexture().getWidth() / 3)) - texture.getWidth() / 2;
				x = boss.getX() + boss.getTexture().getWidth() + texture.getWidth() / 2;
				y = boss.getY() - texture.getHeight();
				break;
			case LEFT:
				x = boss.getX() - texture.getWidth();
				y = boss.getY() + (boss.getTexture().getHeight() / 2);
				break;
			case RIGHT:
				x = boss.getX() + boss.getTexture().getWidth() + texture.getWidth();
				y = boss.getY() + (boss.getTexture().getHeight() / 2);
				break;
			case TOP:
				x = boss.getX() + boss.getTexture().getWidth() / 2 - texture.getWidth() / 2;
				y = boss.getY() - texture.getHeight();
				break;
			case BOT:
				x = boss.getX() + boss.getTexture().getWidth() / 2 - texture.getWidth() / 2;
				y = boss.getY() + boss.getTexture().getHeight();
				break;
			default:
				throw new IllegalStateException();
		}
	}
	

	@Override
	public void update(Game game, List<Mob> mobs) {
		if(!canUpdate) return;
		if(x <= 0 || x >= GamePanel.WIDTH || y <= 0 || y >= GamePanel.HEIGHT || !canRender){
			removeThis();
			return;
		}
		
		updatePosition();
		checkPlayerCollision(game);
	}
	
	
	private void updatePosition(){
		x += speedX;
		if(!(side == Side2.LEFT || side == Side2.RIGHT)){
			y += speedY;
		}
		box.update(x, y);
	}
	
	
	private void checkPlayerCollision(Game game){
		Player p = boss.getPlayerToChase();
		if(Collision.boxCollide(p.getBox(), box)){
			p.subLife();
			p.checkLives(game);
			removeThis();
		}
	}
	
	
	private void removeThis(){
		List<CoctusProjectile> bossProjectiles = boss.getProjectiles();
		Iterator<CoctusProjectile> iterator = bossProjectiles.iterator();
		
		while(iterator.hasNext()){
			CoctusProjectile next = iterator.next();
			if(next.equals(this)){
				iterator.remove();
				return;
			}
		}
		canUpdate = false;
	}

	
	@Override
	public void render(Graphics2D g) {
		g.drawImage(texture, (int) x, (int) y, null);
	}
	
	
	public CoctusProjectile clone(){
		CoctusProjectile projectile = new CoctusProjectile(boss.clone(), side, speedX, speedY);
		projectile.box = box;
		projectile.canRender = canRender;
		projectile.canUpdate = canUpdate;
		projectile.rotation = rotation;
		projectile.side = side;
		projectile.speedX = speedX;
		projectile.speedY = speedY;
		projectile.texture = texture;
		projectile.x = x;
		projectile.y = y;
		return projectile;
	}
	
	
	public static List<CoctusProjectile> cloneList(List<CoctusProjectile> projectiles){
	    List<CoctusProjectile> clone = new ArrayList<>(projectiles.size());
	    
	    try{
	    	for(CoctusProjectile projectile : projectiles){
	    		clone.add(projectile.clone());
	    	}
	    }
	    catch(Exception e){
	    	
	    }
	    return clone;
	}

}
