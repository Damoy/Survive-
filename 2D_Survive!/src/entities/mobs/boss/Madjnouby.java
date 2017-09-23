package entities.mobs.boss;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import audio.JukeBox;
import entities.Player;
import entities.mobs.Mob;
import entities.projectiles.MadjounbyProjectile;
import gameTester.Game;
import gameTester.GamePanel;
import graphics.Texture;
import physics.Collision;
import toolbox.Side;
import toolbox.Timer;

public class Madjnouby extends Boss{
	
	public final static int BOSS_STATE = 42;
	private final static float BASE_SPEED = 1f;
	private final static int MAX_LIVES = 10;
	private int lives = MAX_LIVES;
	
	private LifeGui lifeGui = new LifeGui((byte) lives);
	private List<MadjounbyProjectile> projectiles;

	public Madjnouby(Player playerToChase) { // 15 = width / 2
		super(Texture.BOSS_LOOK_FRONT, GamePanel.WIDTH / 2 - 15, GamePanel.HEIGHT * 4 / 100, playerToChase,
				BASE_SPEED, 0f, Side.DOWN, 10000, 20);
		projectiles = new ArrayList<>();
	}
	
	private class LifeGui{
		
		private class Texture{
			
			public float xStart = GamePanel.WIDTH *  40 / 100;
			public float yStart = GamePanel.HEIGHT * 90 / 100;
			
			public BufferedImage texture;
			public float x, y;
			@SuppressWarnings("unused")
			public Texture prev;
			
			public Texture(BufferedImage tex, Texture father){
				this.texture = tex;
				
				if(father == null){
					x = xStart;
					y = yStart;
				}
				else{
					this.prev = father;
					x = father.x + father.texture.getWidth();
					y = father.y;
				}
			}
			
			public void render(Graphics2D g){
				g.drawImage(texture, (int) x, (int) y, null);
			}

			
		}
		
		public byte lives;
		// public byte hit;
		public Texture[] textures;
		
		public LifeGui(byte numberOfLives){
			lives = numberOfLives;
			generateTextures();
		}
		
		private void generateTextures(){
			textures = new Texture[Madjnouby.this.lives];
			
			BufferedImage tex = graphics.Texture.BOSS_LIFE_GUI_GREEN_LEFT;
			
			textures[0] = new Texture(tex, null);
			
			for(byte b = 1; b < lives - 1; b++){
				textures[b] = new Texture(graphics.Texture.BOSS_LIFE_GUI_GREEN_MID, textures[b - 1]);
			}
			
			textures[lives - 1] = new Texture(graphics.Texture.BOSS_LIFE_GUI_GREEN_RIGHT,
					textures[lives - 2]);
		}
		
		
		public void update(byte livesBossHit){
			if(livesBossHit == 0 || livesBossHit > lives) return;
			
			if(livesBossHit == lives){
				for(Texture t : textures){
					if(t != null){
						t = null;
					}
				}
				return;
			}
			
			if(livesBossHit >= 1){
				textures[lives - 1].texture = graphics.Texture.BOSS_LIFE_GUI_RED_RIGHT;

				for(byte b = 1; b < livesBossHit; b++){
					textures[lives - 1 - b].texture = graphics.Texture.BOSS_LIFE_GUI_RED_MID;
				}
			}
		}
		
		public void render(Graphics2D g){
			for(Texture texture : textures){
				if(texture == null) return;
				texture.render(g);
			}
		}
	}

	
	private void setCurrentTexture(){
		if(playerToChase.getX() - x < 0){
			currentTexture = (byte) 0;
		}
		if(playerToChase.getX() - x == 0){
			currentTexture = (byte) 1;
		}
		if(playerToChase.getX() - x > 0){
			currentTexture = (byte) 2;
		}
	}
	
	private byte currentTexture = 0;
	
	private void setTexture(){
		switch(currentTexture){
			case (byte) 0:
				texture = Texture.BOSS_LOOK_LEFT;
				break;
			case (byte) 1:
				texture = Texture.BOSS_LOOK_FRONT;
				break;
			case (byte) 2:
				texture = Texture.BOSS_LOOK_RIGHT;
				break;
			default:
				texture = Texture.BOSS_LOOK_FRONT;
		}
		if(hit){
			texture = Texture.BOSS_LOOK_HIT;
		}
	}
	
	private byte livesHit;
	private int bonusHit = bonusPoints / MAX_LIVES;
	
	@Override
	public void hit(){
		JukeBox.play("enemyHit");
		lives--;
		livesHit++;
		hit = true;
		timerHit = new Timer(500, false);
		playerToChase.addScore(bonusHit);
	}

	@Override
	public void update(Game game, List<Mob> mobs) {
		generateNewProjectile(game);
		
		try{
			updateProjectiles(game, mobs);
		}
		catch(Exception e){
			
		}
		
		
		updatePos(game);
		setCurrentTexture();
		setTexture();
		
		checkPlayerCollision();
		
		checkTimerHit();
		updateLifeGui();
		
		checkState(game, mobs);
		
//		try{
//			updateProjectiles(game, mobs);
//		}
//		catch(Exception e){
//			
//		}
		
	}
	
	private void updateLifeGui(){
		lifeGui.update(livesHit);
	}
	
	private void updateProjectiles(Game game, List<Mob> mobs){
		Iterator<MadjounbyProjectile> iterator = projectiles.iterator();
		
		while(iterator.hasNext()){
			iterator.next().update(game, mobs);
		}
	}
	
	private void generateNewProjectile(Game game){
		if(game.getBossTimer().getTime() % 40 == 0 && !game.isTimerBossStartGoing()){
			addProjectile(new MadjounbyProjectile(this, Texture.PROJECTILE_BOSS_DOWN));
		}
	}
	
	private void checkPlayerCollision(){
		if(Collision.boxCollide(box, playerToChase.getBox())){
			playerToChase.subLife();
			playerToChase.setX(Player.BASE_PLAYER_X);
			playerToChase.setY(Player.BASE_PLAYER_Y);
			return;
		}
	}
	
	private void updatePos(Game game){
		if(game.getBossTimer().getTime() % 40 == 0){
			if(playerToChase.getX() - x < 0){
				speedX = -BASE_SPEED;
			}
			if(playerToChase.getX() - x == 0){
				speedX = 0;
			}
			if(playerToChase.getX() - x > 0){
				speedX = BASE_SPEED;
			}
		}
		
		x += speedX;
		
		if(x + texture.getWidth() >= GamePanel.WIDTH){
			x -= speedX;
		}
		if(x < 0){
			x -= speedX;
		}
		
		box.update(x, y);
	}
	
	private void checkTimerHit(){
		if(timerHit == null) return;
		timerHit.increment();
		if(timerHit.getTimeLeft() == 0){
			hit = false;
		}
	}

	
	private boolean gameChanged = false;
	
	/**
	 * Removes the boss from the mobs list 
	 * if the boss is dead
	 * @param mobs the list of mobs
	 */
	private void checkState(Game game, List<Mob> mobs){
		if(lives < 1){
			if(!gameChanged){
				playerToChase.removeProjectiles();
				game.setTimerBossEnd(new Timer(3));
				game.setTimerBossEndGo(true);
				gameChanged = true;
			}
			
			if(game.restaureLevel()){
				removeThis(mobs);
			}
			// playerToChase.addScore(bonusPoints);
		}
	}

	@Override
	public void render(Graphics2D g) {
		g.drawImage(texture, (int) x, (int) y, null);
		renderProjectiles(g);
		renderLifeGui(g);
	}
	
	private void renderLifeGui(Graphics2D g){
		lifeGui.render(g);
	}
	
	private void renderProjectiles(Graphics2D g){
		for(MadjounbyProjectile projectile : projectiles){
			projectile.render(g);
		}
	}


	@Override
	protected void setMob() {
		
	}

	public static boolean cloned = false;
	
	@Override
	public Madjnouby clone() {
		try{
			if(cloned) return null; // TODO check this
			Madjnouby madjnouby = new Madjnouby(playerToChase);
			madjnouby.setLives(lives);
			madjnouby.setPos(x, y);
			madjnouby.setSide(side);
			madjnouby.setSpeedX(speedX);
			madjnouby.setSpeedY(speedY);
			madjnouby.setTexture(texture);
			madjnouby.setTimerHit(new Timer(timerHit));
			madjnouby.setHit(hit);
			madjnouby.setProjectiles(MadjounbyProjectile.cloneList(projectiles));
			
			cloned = true;
			
			return madjnouby;
		}
		catch(Exception e){
			return null;
		}
	}
	
	@Override
	public void setLives(int lives){
		this.lives = lives;
	}
	
	@Override
	public void setTimerHit(Timer timer){
		this.timerHit = timer;
	}
	
	@Override
	public boolean isHit(){
		return hit;
	}
	
	@Override
	public void setHit(boolean hit){
		this.hit = hit;
	}
	
	public byte getCurrentTexture(){
		return currentTexture;
	}
	
	public void setCurrentTexture(byte texture){
		this.currentTexture = texture;
	}


	public List<MadjounbyProjectile> getProjectiles() {
		return projectiles;
	}
	
	public void removeProjectile(MadjounbyProjectile projectile){
		projectiles.remove(projectile);
	}
	
	public void addProjectile(MadjounbyProjectile projectile){
		projectiles.add(projectile);
	}
	
	public void setProjectiles(List<MadjounbyProjectile> projectiles){
		this.projectiles = projectiles;
	}

	@Override
	public int getLivesRemaining() {
		return lives;
	}

	@Override
	public int getLives() {
		return lives;
	}
	
	
	
	

}
