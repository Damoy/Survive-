package items;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import entities.Entity;
import gameTester.Game;
import toolbox.Maths;
import toolbox.Pair;
import toolbox.Timer;

public abstract class Bonus extends Entity{

	protected int bonusPoints;
	protected Timer timerDelete;
	
	public final static int TIME_ITEM_STAY = 8; 
	
	public Bonus(BufferedImage texture, float initX, float initY) {
		super(texture, initX, initY);
		timerDelete = new Timer(TIME_ITEM_STAY);
	}

	public abstract Bonus clone();
	
	public int getBonusPoints(){
		return bonusPoints;
	}
	
	public Timer getTimerDelete(){
		return timerDelete;
	}
	
	public Pair<Float, Float> getPos(){
		return new Pair<Float, Float>(x, y);
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public static Bonus generateNewBonus(float x, float y){
		int rand = Maths.irand(100);
		
		//return new BossEnterBonus(x, y);
		
		if(rand < 30){
			return new ProjectileBonus(x, y);
		}
		
		if(rand >= 30 && rand < 40){
			return new X2PtsBonus(x, y);
		}
		
		if(rand >= 40 && rand < 50){
			return new DropBonus(x, y);
		}
		
		if(rand >= 50 && rand < 60){
			return new LifeBonus(x, y);
		}
		
		if(rand >= 60 && rand < 70){
			return new DoubleGunBonus(x, y);
		}
		
		if(Game.canGenerateBonusSpeed){
			if(rand >= 70 && rand < 80){
				return new SpeedBonus(x, y);
			}
		}
		
		if(rand >= 80 && rand < 90){
			return new TimeBonus(x, y);
		}
		
		if(rand >= 95){
			return new BossEnterBonus(x, y);
		}
		
		return null;
	}
	
	
	public static List<Bonus> cloneList(List<Bonus> list){
	    List<Bonus> clone = new ArrayList<Bonus>(list.size());
	    for(Bonus bonus : list){
	    	clone.add(bonus.clone());
	    }
	    return clone;
	}
}
