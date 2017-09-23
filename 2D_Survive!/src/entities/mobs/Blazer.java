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

public class Blazer extends Mob{
	
	private final static int MOVE_SEED = 1000;
	private boolean timeToMove = false;

	public Blazer(Player playerToChase) {
		super(Texture.BLAZER_RIGHT, 0, 0, playerToChase,
				(float) Math.random(), (float) Math.random(),
				Side.getRandomLRSide(), Maths.irand(700) + 100, 2);
		speedX = speedY = 0.75f;
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
//		setPos(Mob.randomSpawnX(playerToChase, this, Texture.BLAZER_RIGHT.getWidth()),
//				Mob.randomSpawnY(playerToChase, this, Texture.BLAZER_RIGHT.getHeight()));
		setPos(Mob.randomSpawnX(playerToChase, Texture.BLAZER_RIGHT.getWidth()),
				Mob.randomSpawnY(playerToChase, Texture.BLAZER_RIGHT.getHeight()));
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
			texture = Texture.BLAZER_LEFT;
			break;
		case RIGHT:
			texture = Texture.BLAZER_RIGHT;
			break;
		default:
			texture = Texture.BLAZER_RIGHT;
		}
	}
	

	@Override
	public void update(Game game, List<Mob> mobs) {
		
		if(Collision.boxCollide(box, playerToChase.getBox())){
			playerToChase.subLife();
			removeThis(mobs);
			return;
		}
		
		checkMove();
		setMob();
	}
	
	private void checkMove(){
		
		if(playerToChase.getX() < x){
			x -= speedX;
		}
		
		if(playerToChase.getX() > x){
			x += speedX;
		}
		
		if(playerToChase.getY() < y){
			y -= speedY;
		}
		
		if(playerToChase.getY() > y){
			y += speedY;
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
	public Mob clone() {
		Blazer blazer = new Blazer(playerToChase);
		blazer.setPos(x, y);
		blazer.setSide(side);
		blazer.setSpeedX(speedX);
		blazer.setSpeedY(speedY);
		blazer.setTexture(texture);
		return blazer;
	}

}
