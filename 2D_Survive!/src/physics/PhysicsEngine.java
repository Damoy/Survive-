package physics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import entities.Entity;
import entities.Player;
import entities.decorations.CenterDecoration;
import entities.decorations.DecorationEntity;
import entities.mobs.Blazer;
import entities.mobs.Lavalter;
import entities.mobs.Mob;
import entities.mobs.Sticker;
import entities.mobs.boss.Madjnouby;
import entities.projectiles.Projectile;
import gameTester.GamePanel;
import toolbox.Logs;
import toolbox.Maths;
import toolbox.Side;
import toolbox.Side2;
import toolbox.Terrain;

public class PhysicsEngine {
	
	public static int RATE_DROP = 3; // % = 1/RATE_DROP
	
	private List<Entity> entities;
	
	public PhysicsEngine(Entity... entities){
		this.entities = new ArrayList<>();
		for(Entity e : entities){
			if(e instanceof Player){
				addPlayerProjectiles(e);
			}
			this.entities.add(e);
		}
	}
	
	private void addPlayerProjectiles(Entity e){
		if(e instanceof Player){
			addProjectiles((Player) e);
		}
	}
	
	private void addProjectiles(Player player){
		for(Entity e : player.getProjectiles()){
			this.entities.add(e);
		}
	}
	
	public static void checkBossProjectileCollision(Player player, Madjnouby boss){
		
	}
	
	public static boolean checkDecorationCollisionWithDecorations(DecorationEntity check, List<DecorationEntity> decorations){
		Iterator<DecorationEntity> iterator = decorations.iterator();
		
		while(iterator.hasNext()){
			DecorationEntity de = iterator.next();
			
			if(check.equals(de)) continue;
			
			if(Collision.boxCollide(check.getBox(), de.getBox())){
				return true;
			}
		}
		return false;
	}
	
	public static DecorationEntity getDeCollidingWith(float x, float y, int w, int h, List<DecorationEntity> decorations){
		Iterator<DecorationEntity> iterator = decorations.iterator();
		
		while(iterator.hasNext()){
			DecorationEntity next = iterator.next();
			
			if(next instanceof CenterDecoration){
				continue;
			}
			
			if(Collision.boxCollide(next.getBox(), x, y, w, h)){
				return next;
			}
		}
		return null;
	}
	
	public static float getPosXWithoutDecorationsCollision(float x, float y, int w, int h, List<DecorationEntity> decorations){
		DecorationEntity deColliding = getDeCollidingWith(x, y, w, h, decorations);
		
		if(deColliding == null){
			return x;
		}
		
		int rand = Maths.irand(2);
		
		do{
			if(rand == 0){
				x -= 0.2f;
			}
			else if(rand == 1){
				x += 0.2f;
			}
		}
		while(Collision.boxCollide(deColliding.getBox(), x, y, w, h));
		
		if(x < 0){
			x = 0;
		}
		
		if(x + w > GamePanel.WIDTH){
			x = GamePanel.WIDTH - w;
		}
		
		if(getDeCollidingWith(x, y, w, h, decorations) != null){
			return getPosXWithoutDecorationsCollision(x, y, w, h, decorations);
		}
		
		return x;
	}
	
	public static float getPosYWithoutDecorationsCollision(float x, float y, int w, int h, List<DecorationEntity> decorations){
		DecorationEntity deColliding = getDeCollidingWith(x, y, w, h, decorations);
		
		if(deColliding == null){
			return y;
		}
		
		int rand = Maths.irand(2);
		
		do{
			if(rand == 0){
				y -= 0.2f;
			}
			else if(rand == 1){
				y += 0.2f;
			}
		}
		while(Collision.boxCollide(deColliding.getBox(), x, y, w, h));
		
		if(y < 0){
			y = 0;
		}
		
		if(y + h > GamePanel.HEIGHT){
			y = GamePanel.HEIGHT - h;
		}
		
		if(getDeCollidingWith(x, y, w, h, decorations) != null){
			return getPosXWithoutDecorationsCollision(x, y, w, h, decorations);
		}
		
		return y;
	}
	
	public static List<Entity> checkProjectileCollisionWith(Terrain terrain, Player player, Projectile p, List<Mob> mobs, List<DecorationEntity> decorations){
		List<Entity> collisions = new ArrayList<>();
		for(Entity e : mobs){
			if(Collision.boxCollide(p.getBox(), e.getBox())){
				if(e instanceof Sticker){
					Sticker s = (Sticker) e;
					player.getScoreObj().addScore(s.getBonusPoints());
					switch(Maths.irand(RATE_DROP)){
					case 0:
						Logs.println("Projectile collision (sticker) bonus with pos : " + e.getX() + ", " + e.getY());
						
						if(terrain == Terrain.DESERT || terrain == Terrain.VOLCAN){
							float x = getPosXWithoutDecorationsCollision(e.getX(), e.getY(), e.getTexture().getWidth(), e.getTexture().getHeight(), decorations);
							float y = getPosYWithoutDecorationsCollision(e.getX(), e.getY(), e.getTexture().getWidth(), e.getTexture().getHeight(), decorations);
							player.getGame().spawnNewBonus(x, y);
						}
						else{
							player.getGame().spawnNewBonus(e.getX(), e.getY());
						}
						
						break;
					}
					collisions.add(e);
				}
				else if(e instanceof Lavalter){
					Lavalter l = (Lavalter) e;
					player.getScoreObj().addScore(l.getBonusPoints());
					switch(Maths.irand(RATE_DROP)){
					case 0:
						Logs.println("Projectile collision (lavalter) bonus with pos : " + e.getX() + ", " + e.getY());
						
						if(terrain == Terrain.DESERT || terrain == Terrain.VOLCAN){
							float x = getPosXWithoutDecorationsCollision(e.getX(), e.getY(), e.getTexture().getWidth(), e.getTexture().getHeight(), decorations);
							float y = getPosYWithoutDecorationsCollision(e.getX(), e.getY(), e.getTexture().getWidth(), e.getTexture().getHeight(), decorations);
							player.getGame().spawnNewBonus(x, y);
						}
						else{
							player.getGame().spawnNewBonus(e.getX(), e.getY());
						}
						
						break;
					}
					collisions.add(e);
				}
				else if(e instanceof Blazer){
					Blazer b = (Blazer) e;
					player.getScoreObj().addScore(b.getBonusPoints());
					switch(Maths.irand(RATE_DROP)){
					case 0:
						Logs.println("Projectile collision (blazer) bonus with pos : " + e.getX() + ", " + e.getY());
						
						if(terrain == Terrain.DESERT || terrain == Terrain.VOLCAN){
							float x = getPosXWithoutDecorationsCollision(e.getX(), e.getY(), e.getTexture().getWidth(), e.getTexture().getHeight(), decorations);
							float y = getPosYWithoutDecorationsCollision(e.getX(), e.getY(), e.getTexture().getWidth(), e.getTexture().getHeight(), decorations);
							player.getGame().spawnNewBonus(x, y);
						}
						else{
							player.getGame().spawnNewBonus(e.getX(), e.getY());
						}
						break;
					}
					collisions.add(e);
				}
			}
		}
		return collisions;
	}
	
	
	public static Side2 boxCollideSide(Player player, DecorationEntity de){
		AABB p_box = player.getBox();
		AABB de_box = de.getBox();
		
		if(!Collision.boxCollide(p_box, de_box)){
			return Side2.NULL;
		}
		
		float pbx = p_box.getX();
		float pby = p_box.getY();
		
		float dpx = de_box.getX();
		float dpy = de_box.getY();
		
		if(pbx <= dpx){
			if(pby <= dpy){
				return Side2.TOP_LEFT;
			}
			if(pby > dpy){
				return Side2.BOT_LEFT;
			}
		}
		
		if(pbx > dpx){
			if(pby <= dpy){
				return Side2.TOP_RIGHT;
			}
			if(pby > dpy){
				return Side2.BOT_RIGHT;
			}
		}
		
		throw new IllegalStateException("Should not be here !");
	}
	
}
