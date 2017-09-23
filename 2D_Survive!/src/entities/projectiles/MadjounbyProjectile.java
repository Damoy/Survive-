package entities.projectiles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import entities.Entity;
import entities.mobs.Mob;
import entities.mobs.boss.Madjnouby;
import gameTester.Game;
import gameTester.GamePanel;
import physics.Collision;
import toolbox.Color;
import toolbox.Logs;
import toolbox.Side;

public class MadjounbyProjectile extends Entity{

	private Side direction;
	private Color projectileColor;
	private float speedX;
	private float speedY;
	private boolean canRender = true;
	private boolean canUpdate = true;
	private Madjnouby boss;
	private BufferedImage baseProjTexture;
	
	public MadjounbyProjectile(Madjnouby boss, BufferedImage texture) {
		super(texture, boss.getX(), boss.getY());
		speedX = 0f;
		speedY = 6f;
		this.boss = boss;
		this.baseProjTexture = texture;
		setProjectile();
	}
	
	
	private void setProjectile(){
		speedY = Math.abs(speedY);
		speedX = 0;
		
		x = boss.getX() + boss.getTexture().getWidth() / 2 - texture.getWidth() / 2;
		y = boss.getY() + boss.getTexture().getHeight() - texture.getHeight() / 2;
		
		
		this.texture = baseProjTexture;
		box.update(x, y);
	}


	
	@Override
	public void update(Game game, List<Mob> mobs) {
		if(!canUpdate) return;
		if(x <= 0 || x >= GamePanel.WIDTH || y <= 0 || y >= GamePanel.HEIGHT || !canRender){
			removeThis();
			return;
		}

		y += speedY;
		box.update(x, y);
		
		if(Collision.boxCollide(game.getPlayer().getBox(), x, y, texture.getWidth(), texture.getHeight())){
			game.getPlayer().subLife();
			game.getPlayer().checkLives(game);
			removeThis();
			return;
		}
		

	}
	
	private void removeThis(){
		List<MadjounbyProjectile> bossProjectiles = boss.getProjectiles();
		Iterator<MadjounbyProjectile> iterator = bossProjectiles.iterator();
		
		while(iterator.hasNext()){
			MadjounbyProjectile next = iterator.next();
			if(next.equals(this)){
				// TODO things this does not work, works because of canUpdate ?
				iterator.remove();
				return;
			}
		}
		
		canUpdate = false;
		Logs.println("BossProjectile, removeThis removed nothing !");
	}

	@Override
	public void render(Graphics2D g) {
		if(!canUpdate) return;
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
	
	
	public static List<MadjounbyProjectile> cloneList(List<MadjounbyProjectile> list) {
	    List<MadjounbyProjectile> clone = new ArrayList<>(list.size());
	    
	    try{
	    	for(MadjounbyProjectile projectile : list){
	    		clone.add(projectile.clone());
	    	}
	    }
	    catch(Exception e){
	    	
	    }
	    return clone;
	}
	
	public MadjounbyProjectile clone(){
		MadjounbyProjectile projectile = new MadjounbyProjectile(boss.clone(), baseProjTexture);
		projectile.box = box;
		projectile.canRender = canRender;
		projectile.direction = direction;
		projectile.projectileColor = projectileColor;
		projectile.speedX = speedX;
		projectile.speedY = speedY;
		projectile.texture = texture;
		projectile.x = x;
		projectile.y = y;
		return projectile;
	}
	
	
	public Color getColor(){
		return projectileColor;
	}
	
	
	
	

}
