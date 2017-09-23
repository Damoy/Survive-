package entities;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import audio.JukeBox;
import entities.mobs.Mob;
import entities.mobs.boss.Coctus;
import entities.mobs.boss.Madjnouby;
import entities.mobs.boss.Volcanop;
import entities.projectiles.Projectile;
import gameTester.Game;
import gameTester.GamePanel;
import graphics.Texture;
import gui.Life;
import gui.Score;
import input.Keys;
import items.Bonus;
import items.BossEnterBonus;
import items.DoubleGunBonus;
import items.DropBonus;
import items.LifeBonus;
import items.ProjectileBonus;
import items.SpeedBonus;
import items.TimeBonus;
import items.X2PtsBonus;
import physics.Collision;
import physics.PhysicsEngine;
import toolbox.Color;
import toolbox.Logs;
import toolbox.Side;
import toolbox.Side2;
import toolbox.Terrain;
import toolbox.Timer;

public class Player extends Entity{
	
	public final static float BASE_PLAYER_X = GamePanel.WIDTH / 2 - 8;
	public final static float BASE_PLAYER_Y = GamePanel.HEIGHT/ 2 - 8;
	
	private final static int PLAYER_lIFE = 5; // 3000
	
	// private final static float BASE_SPEED = 1.5f;
	private final static float BASE_SPEED = 2f;
	private final static float EVOLVED_SPEED = 4f;
	// private final static float EVOLVED_SPEED = 3f;
	
	private final static float PROJ_BASE_SPEED = 4f;

	private float speed = BASE_SPEED;
	private float projectileSpeedSource;
	private boolean canMove;
	private boolean doublePoints;
	private Score score;
	private Life lives;
	private Game game;
	private Side direction;
	private Color projectileColorSource;
	private List<Projectile> projectiles;
	
	
	private boolean renderX2, renderDropUp, renderDoubleGunTimer, renderSpeedTimer;
	private Timer x2Timer, dropUpTimer, doubleGunTimer, speedTimer;
	
	
	private final static float WALK_MAX = 16f;
	private float walkDistance = 0f;
	private int front, back, right, left;
	private boolean setLeftTex, setRightTex;

	private float gravity;
	private float maxHeight;
	private float vx;
	private float vy;
	private float groundHeight = 0f;
	private boolean stopCactusHit = false;
	private boolean cptEnded = false;


	public Player(Game game, float initX, float initY) {
		super(Texture.PLAYER_FRONT, initX, initY);
		direction = Side.DOWN;
		projectiles = new ArrayList<>();
		this.score = new Score();
		this.lives = new Life(PLAYER_lIFE);
		this.game = game;
		this.projectileColorSource = Color.RED;
		this.projectileSpeedSource = PROJ_BASE_SPEED;
		burning = false;
		canMove = true;
		doublePoints = false;
		gravity = 0.25f; 
		vx = 2f;
	}
	
	public Player(Player player){
		super(player.getTexture(), player.getX(), player.getY());
		this.direction = player.getDirection();
		this.projectiles = player.getProjectiles();
		this.score = player.getScoreObj();
		this.lives = player.getLives();
		this.game = player.getGame();
		this.projectileColorSource = player.getProjectileColorSource();
		this.projectileSpeedSource = player.getProjectileSpeedSource();
		this.burning = player.burning;
		this.canMove = player.canMove;
		this.doublePoints = player.doublePoints;
		gravity = 0.25f; 
		vx = 2f;
	}

	
	@Override
	public void update(Game game, List<Mob> mobs) {
		checkLives(game);
		checkX2TimerState();
		checkDropUpTimerState();
		checkDoubleGunTimerState();
		checkSpeedTimer();
		
		// checks, if the user relaunches the sound while the fire is burning
		// , the time the fire sound has to play 
		checkTimerToPlayFireSoundAfterResumeSound();
		checkInputs();
		checkPosition();
		
		updateBurningTimer();
		updateCactusTimer();
		
		checkBonusToRemove(game);
		updateBonus(game, mobs);
	}
	
	public void checkLives(Game game){
		if(lives.getLives() <= 0){
			Logs.println2("Game: SetEndGame from checkLives.");
			game.setEndGame();
		}
	}
	
	private void checkX2TimerState(){
		if(x2Timer == null) return;
		x2Timer.increment();
		if(x2Timer.getTimeLeft() <= 0){
			renderX2 = false;
			x2Timer = null;
		}
	}
	
	private void checkDropUpTimerState(){
		if(dropUpTimer == null) return;
		dropUpTimer.increment();
		if(dropUpTimer.getTimeLeft() <= 0){
			renderDropUp = false;
			dropUpTimer = null;
		}
	}
	
	private void checkDoubleGunTimerState(){
		if(doubleGunTimer == null) return;
		doubleGunTimer.increment();
		if(doubleGunTimer.getTimeLeft() <= 0){
			renderDoubleGunTimer = false;
			doubleGunTimer = null;
			Game.DOUBLE_GUN_STATE = false;
		}
	}
	
	private void checkSpeedTimer(){
		if(speedTimer == null) return;
		speedTimer.increment();
		if(speedTimer.getTimeLeft() <= 0){
			speed = BASE_SPEED;
			for(Projectile projectile : projectiles){
				projectile.setSpeed(ProjectileBonus.getSpeed(projectile.getColor()));
			}
			renderSpeedTimer = false;
			speedTimer = null;
		}
	}
	
	private boolean oneOfUpKeys(){
		return (Keys.isPressed(Keys.KEY_Z) || Keys.isPressed(Keys.KEY_W) || Keys.isPressed(Keys.UP)); 
	}
	
	private boolean oneOfDownKeys(){
		return (Keys.isPressed(Keys.KEY_S) || Keys.isPressed(Keys.DOWN));
	}
	
	private boolean oneOfLeftKeys(){
		return (Keys.isPressed(Keys.KEY_Q) || Keys.isPressed(Keys.KEY_A) || Keys.isPressed(Keys.LEFT)); 
	}
	
	private boolean oneOfRightKeys(){
		return (Keys.isPressed(Keys.KEY_D) ||  Keys.isPressed(Keys.RIGHT)); 
	}
	
	
	private void checkInputs(){
		checkOptionsInput();
		
		float dx = 0;
		float dy = 0;
		
		/**
		 * All possible movement input, ex for going top : w z up
		 */
		if(oneOfUpKeys() && oneOfLeftKeys()){
			setLeftTex = false;
		}
		
		if(oneOfUpKeys() && oneOfRightKeys()){ 
			setRightTex = false;
		}
		
		if(oneOfDownKeys() && oneOfLeftKeys()){
			setLeftTex = false;
		}
		
		if(oneOfDownKeys() && oneOfRightKeys()){ 
			setRightTex = false;
		}
		
		if(oneOfLeftKeys() && !oneOfDownKeys() && !oneOfUpKeys()){
			setLeftTex = true;
		}
		
		if(oneOfRightKeys() && !oneOfDownKeys() && !oneOfUpKeys()){
			setRightTex = true;
		}
		
		if(oneOfUpKeys()){
			if(back == 0){
				texture = Texture.PLAYER_BACK;
			}
			if(back == 1){
				texture = Texture.PLAYER_BACK2;
			}
			
			if(walkDistance >= WALK_MAX){
				back++;
				if(back > 1){
					back = 0;
				}
				walkDistance = 0;
			}
			
			direction = Side.TOP;
			if(canMove){
				dy -= speed;
				walkDistance += speed;
			}
		}
		
		if(oneOfLeftKeys()){
			if(setLeftTex){
				if(left == 0){
					texture = Texture.PLAYER_LEFT;
				}
				if(left == 1){
					texture = Texture.PLAYER_LEFT_STAND;
				}
				if(left == 2){
					texture = Texture.PLAYER_LEFT2;
				}
			
				if(walkDistance >= WALK_MAX){
					left++;
					if(left > 2){
						left = 0;
					}
					walkDistance = 0;
				}
			}
			direction = Side.LEFT;
			if(canMove){
				dx -= speed;
				walkDistance += speed;
			}
		}
		
		if(oneOfRightKeys()){
			if(setRightTex){
				if(right == 0){
					texture = Texture.PLAYER_RIGHT;
				}
				if(right == 1){
					texture = Texture.PLAYER_RIGHT_STAND;
				}
				if(right == 2){
					texture = Texture.PLAYER_RIGHT2;
				}
			
				if(walkDistance >= WALK_MAX){
					right++;
					if(right > 2){
						right = 0;
					}
					walkDistance = 0;
				}
			}
			direction = Side.RIGHT;
			if(canMove){
				dx += speed;
				walkDistance += speed;
			}
		}
		
		if(oneOfDownKeys()){
			if(front == 0){
				texture = Texture.PLAYER_FRONT;
			}
			if(front == 1){
				texture = Texture.PLAYER_FRONT2;
			}
			
			if(walkDistance >= WALK_MAX){
				front++;
				if(front > 1){
					front = 0;
				}
				walkDistance = 0;
			}
			direction = Side.DOWN;
			if(canMove){
				dy += speed;
				walkDistance += speed;
			}
		}
		
		if(!hittedByCactus){
			x += dx;
			y += dy;
		}
		

		else if(hittedByCactus){
			if(top){
				vy -= gravity;
				y -= vy;
				x += vx;
				
				if(y < maxHeight){
					hittedByCactus = false;
			
					vy = 0f;
					vx = 2f;
					gravity = 0.25f;
				}
			}
			
			else{
				vy += gravity;
				y += vy;
				x += vx;
				
				if(y > maxHeight){
					hittedByCactus = false;
					vy = 0f;
					vx = 2f;
					gravity = 0.25f;
				}
			}
		}
		
		if(burning){
			setBurningTexture();
		}
		
		box.update(x, y);
	}
	

	
	private void setBurningTexture(){
		Texture.turnRed(texture);
	}
	
	private Timer timerToPlayFireSoundAfterResumeSound;
	private boolean checkTimerToPlayFireSoundAfterResumeSound = false;
	
	public void checkOptionsInput(){
		if(Keys.isPressed(Keys.KEY_X)){
			if(!JukeBox.isMuted()){
				JukeBox.stopAllSounds();
				JukeBox.mute();
			}
		}
		
		if(Keys.isPressed(Keys.KEY_C)){
			if(JukeBox.isMuted()){
				JukeBox.unmute();
				JukeBox.resumeMainMusic();
				
				if(burning && !JukeBox.hasFireSoundBeenActivated()){
					JukeBox.playFire(); // result : fire still play after end of burning
					timerToPlayFireSoundAfterResumeSound = new Timer(burningTimer.getTimeLeft());
					checkTimerToPlayFireSoundAfterResumeSound = true;
				}
				
				else if(burning && JukeBox.hasFireSoundBeenActivated()){
					JukeBox.resumeFireSound();
				}
			}
		}
		
		if(Keys.isPressed(Keys.KEY_B)){
			if(!canMove){
				canMove = true;
			}
		}
		
		if(Keys.isPressed(Keys.KEY_V)){
			if(canMove){
				canMove = false;
			}
		}
	}
	
	
	private void checkTimerToPlayFireSoundAfterResumeSound(){
		if(checkTimerToPlayFireSoundAfterResumeSound){
			if(timerToPlayFireSoundAfterResumeSound == null){
				Logs.println("timerToPlayFireSoundAfterResumeSound null !");
				return;
			}
			
			if(timerToPlayFireSoundAfterResumeSound.getTimeLeft() == 0){
				JukeBox.stopFireSound();
				Logs.println("Stopped fire sound");
				checkTimerToPlayFireSoundAfterResumeSound = false;
			}
		}
	}
	
	private void checkPosition(){
		if(x <= 0){
			x = 0;
		}
		if(x + texture.getWidth() >= GamePanel.WIDTH){
			x = GamePanel.WIDTH - texture.getWidth();
		}
		if(y < 0){
			y = 0;
		}
		if(y + texture.getHeight() >= GamePanel.HEIGHT){
			y = GamePanel.HEIGHT - texture.getHeight();
		}
	}
	
	private void checkBonusToRemove(Game game){
		game.getAllBonus().removeAll(game.getBonusToRemove());
	}
	
	private void updateBonus(Game game, List<Mob> mobs){
		checkProjectilesStates();
		checkBonus(game);
		updateProjectiles(game, mobs);
	}
	
	private void updateProjectiles(Game game, List<Mob> mobs){
		Iterator<Projectile> iterator = projectiles.iterator();
		
		try{
			while(iterator.hasNext()){
				Projectile currentProj = iterator.next();
				currentProj.update(game, mobs);
			}
		}
		catch(Exception e){
			
		}
	}
	
	public void removeProjectiles(){
		projectiles.clear();
	}
	
	
	private void checkProjectilesStates(){
		Iterator<Projectile> iterator = projectiles.iterator();
		try{
			while(iterator.hasNext()){
				Projectile projectile = iterator.next();
				float x = projectile.x;
				float y = projectile.y;
				if(x <= 0 || x >= GamePanel.WIDTH || y <= 0 || y >= GamePanel.HEIGHT){
					iterator.remove();
				}
			}
		}
		catch(Exception e){
			
		}
	}
	
	
	private void checkBonus(Game game){
		List<Bonus> bonuss = game.getAllBonus();
		Iterator<Bonus> iterator = bonuss.iterator();
		
		try{
			while(iterator.hasNext()){
				Bonus bonus = iterator.next();
				
				// no bonus saw in boss state
				if(game.getState() == Madjnouby.BOSS_STATE && !(bonus instanceof BossEnterBonus)){
					continue;
				}
				
				if(bonus instanceof ProjectileBonus){
					ProjectileBonus pBonus = (ProjectileBonus) bonus;
					if(Collision.boxCollide(box, pBonus.getBox())){
						JukeBox.playPowerUp();
						Logs.println("Bonus collision detected at, bonus box pos : "
								+ pBonus.getBox().getX() + ", " + pBonus.getBox().getY());
						if(renderSpeedTimer){
							projectileSpeedSource = pBonus.getSpeed() * 2;
						}
						else{
							projectileSpeedSource = pBonus.getSpeed();
						}
						projectileColorSource = pBonus.getColor();
						if(doublePoints){
							Logs.println("Adding " + pBonus.getBonusPoints() + " pts to the player score");
							addScore(pBonus.getBonusPoints());
							Logs.println("Adding " + pBonus.getBonusPoints() + " pts to the player score");
							addScore(pBonus.getBonusPoints());
						}
						else{
							Logs.println("Adding " + pBonus.getBonusPoints() + " pts to the player score");
							addScore(pBonus.getBonusPoints());
						}
						game.addToBonusToRemove(pBonus);
					}
				}
				if(bonus instanceof X2PtsBonus){
					if(Collision.boxCollide(box, bonus.getBox())){
						renderX2 = true;
						x2Timer = new Timer(10, true);
						Logs.println("x2Bonus collision detected at, bonus box pos : "
								+ bonus.getBox().getX() + ", " + bonus.getBox().getY());
						JukeBox.playPowerUp();
						doublePoints = true;
						Logs.println("Adding x2 points : " + bonus.getBonusPoints());
						addScore(bonus.getBonusPoints());
						game.addToBonusToRemove(bonus);
					}
				}
				if(bonus instanceof DropBonus){
					if(Collision.boxCollide(box, bonus.getBox())){
						JukeBox.playPowerUp();
						renderDropUp = true;
						dropUpTimer = new Timer(10, true);
						Logs.println("Drop bonus collision detected at, bonus box pos : "
								+ bonus.getBox().getX() + ", " + bonus.getBox().getY());
						addScore(bonus.getBonusPoints());
						PhysicsEngine.RATE_DROP = 1;
						game.addToBonusToRemove(bonus);
					}
				}
				if(bonus instanceof LifeBonus){
					if(Collision.boxCollide(box, bonus.getBox())){
						JukeBox.playPowerUp();
						addScore(bonus.getBonusPoints());
						Logs.println("Life bonus collision detected at, bonus box pos : "
								+ bonus.getBox().getX() + ", " + bonus.getBox().getY());
						addLife();
						game.addToBonusToRemove(bonus);
					}
				}
				if(bonus instanceof DoubleGunBonus){
					if(Collision.boxCollide(box, bonus.getBox())){
						JukeBox.playPowerUp();
						addScore(bonus.getBonusPoints());
						Logs.println("Double gun bonus collision detected at, bonus box pos : "
								+ bonus.getBox().getX() + ", " + bonus.getBox().getY());
						Game.DOUBLE_GUN_STATE = true;
						doubleGunTimer = new Timer(10, true);
						renderDoubleGunTimer = true;
						game.addToBonusToRemove(bonus);
						
					}
				}
				if(bonus instanceof SpeedBonus){
					if(Collision.boxCollide(box, bonus.getBox())){
						JukeBox.playPowerUp();
						addScore(bonus.getBonusPoints());
						Logs.println("Speed bonus collision detected at, bonus box pos : "
								+ bonus.getBox().getX() + ", " + bonus.getBox().getY());
						
						speed = EVOLVED_SPEED;
						
						for(Projectile projectile : projectiles){
							projectile.setSpeed(2 * ProjectileBonus.getSpeed(projectile.getColor()));
						}
						
						speedTimer = new Timer(10, true);
						renderSpeedTimer = true;
						game.addToBonusToRemove(bonus);
					}
				}
				if(bonus instanceof TimeBonus){
					if(Collision.boxCollide(box, bonus.getBox())){
						JukeBox.playPowerUp();
						addScore(bonus.getBonusPoints());
						Logs.println("Time bonus collision detected at, bonus box pos : "
								+ bonus.getBox().getX() + ", " + bonus.getBox().getY());
						game.getGameTimer().add(((TimeBonus) bonus).getTime());
						game.addToBonusToRemove(bonus);
					}
				}
				if(bonus instanceof BossEnterBonus){
					if(Collision.boxCollide(box, bonus.getBox())){
						// TODO see cloned if removes bug
						JukeBox.playPowerUp();
						
						switch(game.getTerrain()){
							case CLASSIC:
								Madjnouby.cloned = false;
								break;
							case DESERT:
								Coctus.cloned = false;
								break;
							case VOLCAN:
								Volcanop.cloned = false;
								break;
						}
						
						// Madjnouby.cloned = false;
						Logs.println2("State Boss state");
						//game.setState(Boss.BOSS_STATE);
						game.setState(Madjnouby.BOSS_STATE);
						game.addToBonusToRemove(bonus);
					}
				}
			}
		}
		catch(Exception e){
			
		}
	}
	
	private boolean burning;
	private Timer burningTimer;
	
	public void lavaHit(){
		if(!burning){
			JukeBox.playFire();
			//Logs.println2("Lava, lives--");
			burningTimer = new Timer(3);
			burning = true;
		}
	}
	
	private boolean hittedByCactus;
	private Timer cactusTimer;
	private boolean top = false;
	
	public void cactusHit(Side2 cactusSide){
		if(!hittedByCactus){
			JukeBox.playLose();
			// subLife();
			switch(cactusSide){
				case TOP_LEFT:
					top = true;
					vx = -vx;
					gravity = -gravity;
					break;
				case TOP_RIGHT:
					top = true;
					gravity = -gravity;
					break;
				case BOT_LEFT:
					top = false;
					vx = -vx;
					break;
				case BOT_RIGHT:
					top = false;
					break;
				case NULL:
					Logs.println2("Player, cactusHit, NULL");
					break;
				default:
					break;
			}
			cactusTimer = new Timer(2);
			hittedByCactus = true;
			
			if(top){
				maxHeight = y - 30;
			}
			else{
				maxHeight = y + 30;
			}
		}
	}
	
	private void updateCactusTimer(){
		if(cactusTimer == null) return;
		
		if(hittedByCactus){
			cactusTimer.increment();
			
			if(cactusTimer.getTimeLeft() <= 0){
				hittedByCactus = false;
				cactusTimer = null;
				
				// reset cactus collision
				vy = 0f;
				vx = 0.25f;
				gravity = 0.25f;
			}
		}
	}
	
	public boolean hittedByCactus(){
		return hittedByCactus;
	}
	
	private void updateBurningTimer(){
		//Logs.println2("burning: " + burning);
		if(burningTimer == null) return;
		
		if(burning){
			burningTimer.increment();
			
			if(burningTimer.getTimeLeft() <= 0){
				burning = false;
				burningTimer = null;
				subLife();
				turnTexturesToNormal();
				//Logs.println2("Turned burning to false set texture to normal");
			}
		}
	}
	
	private void turnTexturesToNormal(){
		// Texture.undoRed(texture);
		Texture.PLAYER_BACK = Texture.getTexture(16, 2, 7);
		Texture.PLAYER_BACK2 = Texture.getTexture(16, 2, 8);
		
		Texture.PLAYER_LEFT = Texture.getTexture(16, 2, 10);
		Texture.PLAYER_LEFT_STAND = Texture.getTexture(16, 2, 13);
		Texture.PLAYER_LEFT2 = Texture.getTexture(16, 2, 14);
		
		Texture.PLAYER_RIGHT = Texture.getTexture(16, 2, 9);
		Texture.PLAYER_RIGHT_STAND = Texture.getTexture(16, 2, 11);
		Texture.PLAYER_RIGHT2 = Texture.getTexture(16, 2, 12);
		
		Texture.PLAYER_FRONT = Texture.getTexture(16, 2, 5);
		Texture.PLAYER_FRONT2 = Texture.getTexture(16, 2, 6);
		
	}
	
	public void setBurning(boolean b){
		burning = b;
	}
	
	public Timer getBurningTimer(){
		return burningTimer;
	}
	
	/**
	 * TODO SEE WHY COLOR NOT WHITE IN VOLCAN TERRAIN, SEE TERRRAIN PRINT
	 */
	@Override
	public void render(Graphics2D g) {
		
//		g.drawImage(Texture.LONG_CARRE, (int) x - Texture.LONG_CARRE.getWidth(), (int) y, null);
//		g.drawImage(Texture.LONG_CARRE, (int) x + texture.getWidth(),
//				(int) y, null);
//		
//		g.drawImage(Texture.HAUT_CARRE, (int) x + texture.getWidth() / 2,
//				(int) y - Texture.HAUT_CARRE.getHeight(), null);
//		g.drawImage(Texture.HAUT_CARRE, (int) x + texture.getWidth() / 2,
//				(int) y + texture.getHeight(), null);
		
		// renderLives(g);
		renderProjectiles(g);
		if(renderX2){
			renderX2Timer(g);
		}
		if(renderDropUp){
			renderDropUpTimer(g);
		}
		if(renderDoubleGunTimer){
			renderDoubleGunTimer(g);
		}
		if(renderSpeedTimer){
			renderSpeedTimer(g);
		}
		g.drawImage(texture, (int) x, (int) y, null);
	}
	
	private void renderX2Timer(Graphics2D g){
		g.setFont(Game.bestScoreFont);
		if(game.getTerrain() == Terrain.VOLCAN){
			g.setColor(java.awt.Color.WHITE);
		}
		else{
			g.setColor(java.awt.Color.BLUE);
		}
		g.drawString("x2 pts " + x2Timer.getTimeLeft() + "s", X2PtsBonus.X2_TIMER_X, X2PtsBonus.X2_TIMER_Y);
		g.setColor(java.awt.Color.BLACK);
	}
	
	private void renderDropUpTimer(Graphics2D g){
		g.setFont(Game.bestScoreFont);
		if(game.getTerrain() == Terrain.VOLCAN){
			g.setColor(java.awt.Color.WHITE);
		}
		else{
			g.setColor(java.awt.Color.BLUE);
		}
		g.drawString("100% drop " + dropUpTimer.getTimeLeft() + "s",
				DropBonus.DROP_UP_X, DropBonus.DROP_UP_Y);
		g.setColor(java.awt.Color.BLACK);
	}
	
	private void renderDoubleGunTimer(Graphics2D g){
		g.setFont(Game.bestScoreFont);
		if(game.getTerrain() == Terrain.VOLCAN){
			g.setColor(java.awt.Color.WHITE);
		}
		else{
			g.setColor(java.awt.Color.BLUE);
		}
		g.drawString("x2 gun " + doubleGunTimer.getTimeLeft() + "s",
				DoubleGunBonus.DOUBLE_GUN_TIMER_X, DoubleGunBonus.DOUBLE_GUN_TIMER_Y);
		g.setColor(java.awt.Color.BLACK);
	}
	
	private void renderSpeedTimer(Graphics2D g){
		g.setFont(Game.bestScoreFont);
		if(game.getTerrain() == Terrain.VOLCAN){
			g.setColor(java.awt.Color.WHITE);
		}
		else{
			g.setColor(java.awt.Color.BLUE);
		}
		g.drawString("x2 speed " + speedTimer.getTimeLeft() + "s",
				SpeedBonus.SPEED_TIMER_X, SpeedBonus.SPEED_TIMER_Y);
		g.setColor(java.awt.Color.BLACK);
	}
	
	public void renderLives(Graphics2D g){
		for(int i = 0; i < lives.getLives(); i++){
			float x = Life.POS_X + (i * 20);
			if(x >= GamePanel.WIDTH) return;
			lives.render(g, x, Life.POS_Y);
		}
	}
	
	private void renderProjectiles(Graphics2D g){
		try{
			for(Projectile projectile : projectiles){
				projectile.render(g);
			}
		}
		catch(Exception e){
			//
		}
	}
	
	public void clearProjectiles(){
		projectiles.clear();
	}
	
	public void addLife(){
		lives.addLife();
	}
	
	public void subLife(){
		JukeBox.playLose();
		lives.subLife();
	}
	
	public Game getGame(){
		return game;
	}
	
	public void addScore(int toAdd){
		score.addScore(toAdd);
	}
	
	public void subScore(int toSub){
		score.subScore(toSub);
	}
	
	public Color getProjectileColorSource(){
		return projectileColorSource;
	}
	
	public float getProjectileSpeedSource(){
		return projectileSpeedSource;
	}
	
	public Side getDirection(){
		return direction;
	}
	
	public List<Projectile> getProjectiles(){
		return projectiles;
	}
	
	public Score getScoreObj(){
		return score;
	}
	
	public Life getLives(){
		return lives;
	}

	public Score getScore() {
		return score;
	}

	public void setScore(Score score) {
		this.score = score;
	}
	
	public void setDirection(Side direction){
		this.direction = direction;
	}
	
	public boolean isBurning(){
		return burning;
	}
	

}
