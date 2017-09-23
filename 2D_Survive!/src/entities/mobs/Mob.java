package entities.mobs;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import entities.Entity;
import entities.Player;
import entities.mobs.boss.Volcanop;
import gameTester.GamePanel;
import toolbox.Logs;
import toolbox.Maths;
import toolbox.Side;

public abstract class Mob extends Entity implements Cloneable{

	protected Player playerToChase;
	protected Side side;
	
	protected float speedX;
	protected float speedY;
	protected int bonusPoints;
	
	protected int timesHit;
	
	public Mob(BufferedImage texture, float initX, float initY, Player playerToChase, float speedX,
			float speedY, Side side, int bonusPoints, int timesHit) {
		super(texture, initX, initY);
		this.playerToChase = playerToChase;
		this.speedX = speedX;
		this.speedY = speedY;
		this.side = side;
		this.bonusPoints = bonusPoints;
		this.timesHit = timesHit;
		
//		if(this instanceof Volcanop){
//			Logs.println2("GENERATED VOLCANOP");
//		}
		
//		if(!(this instanceof Lavalter) && !(this instanceof Blazer)){
//			setMob();
//		}
		if(this instanceof Sticker){
			setMob();
		}
	}
	
	public abstract Mob clone();
	protected abstract void setMob();
	
	protected void removeThis(List<Mob> mobs){
		Iterator<Mob> iterator = mobs.iterator();
		while(iterator.hasNext()){
			Entity e = iterator.next();
			if(e == this){
				try{
					iterator.remove();
				}
				catch(Exception ex){
				}
			}
		}
	}
	
	private static final float DISTANCE_FROM_PLAYER = 80f;
	
//	public static float randomSpawnX(Player player, Mob mob, int w){
//		float x = w;
//		do{
//			x = (float) w + Maths.irand(GamePanel.WIDTH - w);
//			
//			if(!(Math.abs(player.getX() - x) >= DISTANCE_FROM_PLAYER)){
//				//Logs.println2("Distance : " + Math.abs(player.getX() - x) );
//				continue;
//			}
//			
//			Entity.updateBoxPosX(mob, x);
//		}
//		while(Collision.boxCollide(player.getBox(), mob.getBox()));
//		
//		if(Math.abs(player.getX() - x) < DISTANCE_FROM_PLAYER){
//			Logs.println2("NOT GOOD DISTANCE X : " + Math.abs(player.getX() - x));
//		}
//		return x;
//	}
	
	public static float randomSpawnX(Player player, int w){
		float x = w;
		do{
			x = (float) w + Maths.irand(GamePanel.WIDTH - w);
		}
		while(!(Math.abs(player.getX() - x) >= DISTANCE_FROM_PLAYER));
		
		if(Math.abs(player.getX() - x) < DISTANCE_FROM_PLAYER){
			Logs.println2("NOT GOOD DISTANCE X : " + Math.abs(player.getX() - x));
		}
		
		return x;
	}
	
//	public static float randomSpawnY(Player player, Mob mob, int h){
//		float y = h;
//		do{
//			y = (float) h + Maths.irand(GamePanel.HEIGHT - h);
//			
//			if(!(Math.abs(player.getY() - y) >= DISTANCE_FROM_PLAYER)){
//				//Logs.println2("Distance : " + Math.abs(player.getY() - y));
//				continue;
//			}
//			
//			Entity.updateBoxPosY(mob, y);
//		}
//		while(Collision.boxCollide(player.getBox(), mob.getBox()));
//		
//		if(Math.abs(player.getY() - y) < DISTANCE_FROM_PLAYER){
//			Logs.println2("NOT GOOD DISTANCE Y : " + Math.abs(player.getY() - y));
//		}
//		return y;
//	}
	
	public static float randomSpawnY(Player player, int h){
		float y = h;
		do{
			y = (float) h + Maths.irand(GamePanel.HEIGHT - h);
		}
		while(!(Math.abs(player.getY() - y) >= DISTANCE_FROM_PLAYER));
		
		if(Math.abs(player.getY() - y) < DISTANCE_FROM_PLAYER){
			Logs.println2("NOT GOOD DISTANCE Y : " + Math.abs(player.getY() - y));
		}
		return y;
	}
	

	public Player getPlayerToChase() {
		return playerToChase;
	}

	public void setPlayerToChase(Player playerToChase) {
		this.playerToChase = playerToChase;
	}

	public Side getSide() {
		return side;
	}

	public void setSide(Side side) {
		this.side = side;
	}

	public float getSpeedX() {
		return speedX;
	}

	public void setSpeedX(float speedX) {
		this.speedX = speedX;
	}

	public float getSpeedY() {
		return speedY;
	}

	public void setSpeedY(float speedY) {
		this.speedY = speedY;
	}

	public int getBonusPoints() {
		return bonusPoints;
	}

	public void setBonusPoints(int bonusPoints) {
		this.bonusPoints = bonusPoints;
	}
	
	
	public static List<Mob> cloneList(List<Mob> list) {
	    List<Mob> clone = new ArrayList<Mob>(list.size());
	    for(Mob mob : list){
	    	clone.add(mob.clone());
	    }
	    return clone;
	}
	

}
