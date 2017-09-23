package entities.mobs;

import java.awt.Graphics2D;
import java.util.List;

import audio.JukeBox;
import entities.Player;
import gameTester.Game;
import gameTester.GamePanel;
import graphics.Texture;
import physics.Collision;
import toolbox.Maths;
import toolbox.Side;
import toolbox.Timer;

public class Lavalter extends Mob{
	
	private final static int MOVE_SEED = 1000;
	private boolean timeToMove = false;

	public Lavalter(Player playerToChase) {
		super(Texture.LAVALTER_RIGHT, 0, 0,
				playerToChase,  (float) Math.random(), (float) Math.random(),
				Side.getRandomLRSide(), Maths.irand(400) + 50, 2);
		setPos();
		setMob();
	}

	@Override
	protected void setMob() {
		checkPos();
		setSide();
		setTexture();
		box.update(x, y);
	}
	
	private void setPos(){
//		setPos(Mob.randomSpawnX(playerToChase, this, Texture.LAVALTER_RIGHT.getWidth()),
//				Mob.randomSpawnY(playerToChase, this, Texture.LAVALTER_RIGHT.getHeight()));
		setPos(Mob.randomSpawnX(playerToChase, Texture.LAVALTER_RIGHT.getWidth()),
				Mob.randomSpawnY(playerToChase, Texture.LAVALTER_RIGHT.getHeight()));
	}
	
	private void checkPos(){
		if(x <= texture.getWidth()){
			x = texture.getWidth();
			timeToMove = true;
		}
		
		else if(x + texture.getWidth() >= GamePanel.WIDTH){
			x = GamePanel.WIDTH - texture.getWidth();
			timeToMove = true;
		}
		
		if(y <= 0){
			y = 0;
			timeToMove = true;
		}
		
		else if(y + texture.getHeight() >= GamePanel.HEIGHT){
			y = GamePanel.HEIGHT - texture.getHeight();
			timeToMove = true;
		}
	}
	
	private void setSide(){
		if(speedX > 0){
			side = Side.RIGHT;
		}
		
		else if(speedX < 0){
			side = Side.LEFT;
		}
	}
	
	private void setTexture(){
		switch(side){
		case LEFT:
			texture = Texture.LAVALTER_LEFT;
			break;
		case RIGHT:
			texture = Texture.LAVALTER_RIGHT;
			break;
		default:
			texture = Texture.LAVALTER_RIGHT;
		}
	}
	
	private void setSpeed(){
		switch(Maths.irand(2)){
			case 0:
				speedX = Math.abs(speedX);
				break;
			case 1:
				speedX = -speedX;
				break;
		}
		
		switch(Maths.irand(2)){
			case 0:
				speedY = Math.abs(speedY);
				break;
			case 1:
				speedY = -speedY;
				break;
		}
	}

	@Override
	public void update(Game game, List<Mob> mobs) {
		checkMove();
		
		if(Collision.boxCollide(box, playerToChase.getBox())){
			playerToChase.subLife();
			removeThis(mobs);
			return;
		}
		x += speedX;
		y += speedY;
		setMob();
	}
	
	private void checkMove(){
		if(timeToMove){
			setSpeed();
			timeToMove = false;
			return;
		}
		switch(Maths.irand(MOVE_SEED)){
		case 0:
			setSpeed();
			break;
		default:
		}
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(texture, (int) x, (int) y, null);
	}
	
	public boolean isTimeToMove() {
		return timeToMove;
	}

	public void setTimeToMove(boolean timeToMove) {
		this.timeToMove = timeToMove;
	}

	@Override
	public Mob clone(){
		Lavalter lavalter = new Lavalter(playerToChase);
		lavalter.setPos(x, y);
		lavalter.setSide(side);
		lavalter.setSpeedX(speedX);
		lavalter.setSpeedY(speedY);
		lavalter.setTexture(texture);
		return lavalter;
	}


}
