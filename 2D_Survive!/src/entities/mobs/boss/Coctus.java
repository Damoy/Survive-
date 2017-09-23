package entities.mobs.boss;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import audio.JukeBox;
import entities.Player;
import entities.mobs.Mob;
import entities.projectiles.CoctusProjectile;
import gameTester.Game;
import gameTester.GamePanel;
import graphics.Texture;
import physics.Collision;
import toolbox.Side;
import toolbox.Side2;
import toolbox.Timer;

public class Coctus extends Boss{
	
	private final static float BASE_SPEED = 0.55f;
	private final static int MAX_LIVES = 10;
	public static boolean cloned = false;
	private int lives = MAX_LIVES;
	
	private LifeGui lifeGui = new LifeGui((byte) lives);
	private Timer genTimer, genMultipleProjs;
	private List<CoctusProjectile> projectiles;
	
	private byte livesHit;
	private int bonusHit = bonusPoints / MAX_LIVES;
	
	private boolean genMultiple;
	

	public Coctus(Player playerToChase) {
		super(Texture.COCTUS_BOSS, GamePanel.WIDTH / 2 - 15, GamePanel.HEIGHT * 4 / 100, playerToChase,
				BASE_SPEED, BASE_SPEED, Side.DOWN, 10000, 10);
		projectiles = new ArrayList<>();
		genTimer = new Timer(2); // generate a projectile each 2 seconds
		genMultipleProjs = new Timer(4);
	}

	
	private void setTexture(){
		if(hit){
			texture = Texture.COCTUS_BOSS_HIT;
		}
		else{
			texture = Texture.COCTUS_BOSS;
		}
	}
	
	private byte countHit;
	
	public void hit(){
		JukeBox.play("enemyHit");
//		lives--;
//		livesHit++;
		
		if(countHit >= 1){ // 3 hits, 30 lives then
			lives--;
			livesHit++;
			countHit = 0;
		}
		else{
			countHit++;
		}
		
		hit = true;
		timerHit = new Timer(500, false);
		playerToChase.addScore(bonusHit);
	}

	
	@Override
	public void update(Game game, List<Mob> mobs) {
		generateNewProjectile(game);
		updatePos(game);
		setTexture();
		
		checkPlayerCollision();
		
		checkTimerHit();
		updateGenTimer(); // TODO NEW
		
		updateLifeGui();
		
		try{
			updateProjectiles(game, mobs);
		}
		catch(Exception e){
			
		}
		
		
		checkState(game, mobs);
	}
	
	
	private void updateLifeGui(){
		lifeGui.update(livesHit);
	}
	
	
	private void updateProjectiles(Game game, List<Mob> mobs){
		Iterator<CoctusProjectile> iterator = projectiles.iterator();
		
		while(iterator.hasNext()){
			iterator.next().update(game, mobs);
		}
	}
	
	
	private void updateGenTimer(){
		if(genTimer == null) return;
		genTimer.increment();
		
		if(genMultipleProjs == null) return;
		genMultipleProjs.increment();
		
		if(genMultipleProjs.getTimeLeft() == 0){
			genMultiple = true;
			genMultipleProjs.reset(4);
		}
		else{
			genMultiple = false;
		}
	}
	
	private boolean alreadyGenerated = false;
	
	private void generateNewProjectile(Game game){
		genMultipleProjSpell(game);
		genBasicSpell(game);
	}
	
	
	private void genMultipleProjSpell(Game game){
		if(genMultiple && !game.isTimerBossStartGoing()){
			Side2 s1, s2, s3, s4;
			float sx1, sx2, sx3, sx4;
			float sy1, sy2, sy3, sy4;
			float baseSpeed = 2f;
			
			s1 = Side2.TOP_LEFT;
			s2 = Side2.TOP_RIGHT;
			s3 = Side2.BOT_LEFT;
			s4 = Side2.BOT_RIGHT;
			
			sx1 = -baseSpeed;
			sx2 = baseSpeed;
			sx3 = -baseSpeed;
			sx4 = baseSpeed;
			
			sy1 = -baseSpeed;
			sy2 = -baseSpeed;
			sy3 = baseSpeed;
			sy4 = baseSpeed;
			
			CoctusProjectile c_proj1 = new CoctusProjectile(this, s1, sx1, sy1);
			CoctusProjectile c_proj2 = new CoctusProjectile(this, s2, sx2, sy2);
			CoctusProjectile c_proj3 = new CoctusProjectile(this, s3, sx3, sy3);
			CoctusProjectile c_proj4 = new CoctusProjectile(this, s4, sx4, sy4);
			
			addProjectile(c_proj1);
			addProjectile(c_proj2);
			addProjectile(c_proj3);
			addProjectile(c_proj4);
			
			alreadyGenerated = true;
		}
		else{
			alreadyGenerated = false;
		}
	}
	
	private void genBasicSpell(Game game){
		if(alreadyGenerated) return;
		
		if(genTimer.getTimeLeft() == 0 && !game.isTimerBossStartGoing()){
			// reset the shoot timer
			genTimer.reset(1);
			
			Side2 newProjSide = null;
			float speedX = 0f;
			float speedY = 0f;
			float speed = 3f;
			
			float px = playerToChase.getX();
			float py = playerToChase.getY();
			int pw = playerToChase.getTexture().getWidth();
			int ph = playerToChase.getTexture().getHeight();
			int w = texture.getWidth();
			int h = texture.getHeight();
			
			/**
			 * TODO SEE
			 */
			if(px + pw < x){
				if(py + ph < y){
					newProjSide = Side2.TOP_LEFT;
					speedY = -speed;
					speedX = -speed;
				}
				else if(py > y + h){
					newProjSide = Side2.BOT_LEFT;
					speedY = speed;
					speedX = -speed;
				}
				else{
					newProjSide = Side2.LEFT;
					speedX = -speed;
				}
			}
			
			else if(px > x + w){
				if(py + ph < y){
					newProjSide = Side2.TOP_RIGHT;
					speedY = -speed;
					speedX = speed;
				}
				else if(py > y + h){
					newProjSide = Side2.BOT_RIGHT;
					speedY = speed;
					speedX = speed;
				}
				else{
					newProjSide = Side2.RIGHT;
					speedX = speed;
				}
			}
			
			else{
				if(py + ph < y){
					newProjSide = Side2.TOP;
					speedY = -speed;
				}
				else if(py > y + h){
					newProjSide = Side2.BOT;
					speedY = speed;
				}
				else{
					newProjSide = Side2.NULL; // TODO COLLISION SHOULD HAPPEN
				}
			}
			
			// Logs.println2("projSide : " + projSide + ", speedX : " + speedX + ", speedY : " + speedY);
			addProjectile(new CoctusProjectile(this, newProjSide, speedX, speedY));
		}
	}
	
	private void checkPlayerCollision(){
		if(Collision.boxCollide(box, playerToChase.getBox())){
			// JukeBox.playLose();
			playerToChase.subLife();
			playerToChase.setX(Player.BASE_PLAYER_X);
			playerToChase.setY(Player.BASE_PLAYER_Y);
			return;
		}
	}
	
	
	private void updatePos(Game game){
		if(game.getBossTimer().getTime() % 50 == 0){
			if(playerToChase.getX() - x < 0){
				speedX = -BASE_SPEED;
			}
			if(playerToChase.getX() - x == 0){
				speedX = 0;
			}
			if(playerToChase.getX() - x > 0){
				speedX = BASE_SPEED;
			}
			
			if(playerToChase.getY() - y < 0){
				speedY = -BASE_SPEED;
			}
			if(playerToChase.getY() - y == 0){
				speedY = 0;
			}
			if(playerToChase.getY() - y > 0){
				speedY = BASE_SPEED;
			}
		}
		
		x += speedX;
		y += speedY;
		
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
	
	
	public void addProjectile(CoctusProjectile projectile){
		projectiles.add(projectile);
	}
	
	
	public void setProjectiles(List<CoctusProjectile> projectiles){
		this.projectiles = projectiles;
	}
	
	
	private boolean gameChanged = false;
	private boolean timeToRemove = false;
	
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
				
//				lifeGui.setAllRed();
//				if(timeToRemove){
//					removeThis(mobs);
//				}
//				if(!timeToRemove){
//					timeToRemove = true;
//				}
			}
			// playerToChase.addScore(bonusPoints);
		}
	}
	
	
	@Override
	public Coctus clone() {
		try{
			if(cloned) return null; // TODO check this
			Coctus coctus = new Coctus(playerToChase);
			coctus.setLives(lives);
			coctus.setPos(x, y);
			coctus.setSide(side);
			coctus.setSpeedX(speedX);
			coctus.setSpeedY(speedY);
			coctus.setTexture(texture);
			coctus.setTimerHit(new Timer(timerHit));
			coctus.setHit(hit);
			coctus.setProjectiles(CoctusProjectile.cloneList(projectiles));
			
			cloned = true;
			
			return coctus;
		}
		catch(Exception e){
			return null;
		}
	}

	
	@Override
	protected void setMob() {
		
	}

	
	@Override
	public void render(Graphics2D g) {
		renderLifeGui(g);
		renderProjectiles(g);
		g.drawImage(texture, (int) x, (int) y, null);
	}
	
	
	private void renderLifeGui(Graphics2D g){
		lifeGui.render(g);
	}
	
	
	private void renderProjectiles(Graphics2D g){
		for(CoctusProjectile projectile : projectiles){
			projectile.render(g);
		}
	}
	
	
	private class LifeGui{
		
		private class Texture{
			
			public float xStart = GamePanel.WIDTH *  40 / 100;
			public float yStart = GamePanel.HEIGHT * 90 / 100;
			
			public BufferedImage texture;
			public float x, y;
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

			// state == 0 : green, else red
			// side 0 left, 1 mid, 2 right
			public void update(byte state, byte side){
				
			}
			
		}
		
		public byte lives;
		public byte hit;
		public Texture[] textures;
		
		public LifeGui(byte numberOfLives){
			lives = numberOfLives;
			generateTextures();
		}
		
		private void generateTextures(){
			textures = new Texture[Coctus.this.lives];
			
			BufferedImage tex = graphics.Texture.BOSS_LIFE_GUI_GREEN_LEFT;
			
			textures[0] = new Texture(tex, null);
			
			for(byte b = 1; b < lives - 1; b++){
				textures[b] = new Texture(graphics.Texture.BOSS_LIFE_GUI_GREEN_MID, textures[b - 1]);
			}
			
			textures[lives - 1] = new Texture(graphics.Texture.BOSS_LIFE_GUI_GREEN_RIGHT,
					textures[lives - 2]);
		}
		
		public void setAllRed(){
			textures[0] = new Texture(graphics.Texture.BOSS_LIFE_GUI_RED_LEFT, null);
			for(byte b = 1; b < lives - 1; b++){
				textures[b] = new Texture(graphics.Texture.BOSS_LIFE_GUI_RED_MID,
						textures[b - 1]);
			}
			textures[lives - 1] = new Texture(graphics.Texture.BOSS_LIFE_GUI_RED_RIGHT, null);
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

	
	@Override
	public void setLives(int lives) {
		this.lives = lives;
	}


	@Override
	public int getLives() {
		return lives;
	}

	@Override
	public int getLivesRemaining() {
		return lives;
	}

	public List<CoctusProjectile> getProjectiles() {
		return projectiles;
	}
	
	

}
