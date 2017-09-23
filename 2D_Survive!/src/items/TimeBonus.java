package items;

import java.awt.Graphics2D;
import java.util.List;

import entities.mobs.Mob;
import gameTester.Game;
import graphics.Texture;
import toolbox.Maths;

public class TimeBonus extends Bonus{

	private int time;

	public TimeBonus(float initX, float initY) {
		super(Texture.TIME_BONUS, initX, initY);
		time = Maths.irand(5) + 2;
		bonusPoints = 400;
	}


	@Override
	public void update(Game game, List<Mob> mobs) {
		
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(texture, (int) x, (int) y, null);		
	}
	
	public int getTime(){
		return time;
	}


	@Override
	public Bonus clone() {
		TimeBonus tbBonus = new TimeBonus(x, y);
		tbBonus.time = time;
		return tbBonus;
	}
	
	public void setTime(int time){
		this.time = time;
	}
	
	
}
