package entities.mobs.boss;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import audio.JukeBox;
import entities.Player;
import entities.mobs.Mob;
import entities.projectiles.DragonProjectile;
import gameTester.Game;
import gameTester.GamePanel;
import graphics.Texture;
import physics.Collision;
import toolbox.Logs;
import toolbox.Maths;
import toolbox.Side;
import toolbox.Side2;
import toolbox.Timer;

/**
 * The Volcanop boss black dragon
 * @author Damoy
 */
public class Dragoone extends VolcanopDragon{

	private final static byte MAX_LIVES = 5;
	private final static float BASE_SPEED = 0.5f;
	
	private byte lives = MAX_LIVES;
	private byte livesHit;
	private int bonusHit = 200;
	
	private LifeGui lifeGui;
	private Timer actionTimer;
	private Timer timerHit;
	private Timer timerSpell1;
	private Timer timerBeforeReGo;
	
	private boolean shouldExists;
	private boolean cloned = false;
	private boolean canUpdate = true;
	private boolean spell1Generated = false;
	private boolean canMove = false;
	private boolean timeToGoBack = false;
	private boolean savedForLater = false;
	private boolean stop = false;
	
	private List<DragonProjectile> projectiles;
	private Side2 direction;
	
	
	public Dragoone(Volcanop guardian, Player playerToChase) {
		super(Texture.DRAGOONE_RIGHT, 0, 0, playerToChase, 0, 0, Side.DOWN, 10000, 5);
		
		shouldExists = false;
		projectiles = new ArrayList<>();
		this.guardian = guardian;

		initTimer();
		initLifeGui();
		initPos();
	}
	
	
	/**
	 * Initialization
	 */
	private void initTimer(){
		actionTimer = new Timer(8);
	}
	
	private void initLifeGui(){
		if(guardian.isDragoneSpawn()){
			lifeGui = new LifeGui(true, lives);
		}
		else{
			lifeGui = new LifeGui(false, lives);
		}
	}
	
	private void initPos(){
		x = guardian.getX();
		y = guardian.getY() + 27;
		box.update(x, y);
	}
	
	
	private byte countHit;
	
	@Override
	public void hit() {
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
		Logs.println2("hit = true !");
		// timerHit = new Timer(500, false);
		timerHit = new Timer(800, false);
		playerToChase.addScore(bonusHit);
	}
	
	private void checkTimerHit(){
		if(timerHit == null) return;
		timerHit.increment();
		if(timerHit.getTimeLeft() == 0){
			Logs.println2("hit = false !");
			hit = false;
			timerHit = null;
		}
	}
	
	
	@Override
	public void update(Game game, List<Mob> mobs) {
		//if(!stop){
			updateExistence();
			checkPlayerCollision(mobs);
			
			updateActionTimer(mobs);
			updateTimerSpell1();
			
			updatePos(game);
			updateTexture();
			checkTimerHit();
			
			updateProjectiles(game, mobs);
			checkProjectilesToRemove();
			updateLifeGui();
			
			checkState(mobs);
		//}
		checkTimerBeforeReGo();
	}
	
	private void checkState(List<Mob> mobs){
		if(lives < 1){
			guardian.removeDragoone(mobs);
		}
	}
	
	private void updateExistence(){
		if(actionTimer.getTimeLeft() == 8){
			shouldExists = true;
		}
	}
	
	private void updateLifeGui(){
		lifeGui.update(livesHit);
	}
	
	private void updateProjectiles(Game game, List<Mob> mobs){
		for(DragonProjectile projectile : projectiles){
			if(projectile == null) continue;
			projectile.update(game, mobs);
		}
	}
	
	private void checkProjectilesToRemove(){
		Iterator<DragonProjectile> iterator = projectiles.iterator();
		
		while(iterator.hasNext()){
			DragonProjectile next = iterator.next();
			
			if(next.isDead() || (next.getX() == 0 && next.getY() == 0)){
				iterator.remove();
			}
		}
	}
	
	private void genProjectileSpell1(){
		if(!spell1Generated){
			timerSpell1 = new Timer(1); 
			
			DragonProjectile p_top_left = new DragonProjectile(this, Side2.TOP_LEFT);
			DragonProjectile p_top_right = new DragonProjectile(this, Side2.TOP_RIGHT);
			DragonProjectile p_top = new DragonProjectile(this, Side2.TOP);
			
			DragonProjectile p_bot_left = new DragonProjectile(this, Side2.BOT_LEFT);
			DragonProjectile p_bot_right = new DragonProjectile(this, Side2.BOT_RIGHT);
			DragonProjectile p_bot = new DragonProjectile(this, Side2.BOT);
			
			DragonProjectile p_left = new DragonProjectile(this, Side2.LEFT);
			DragonProjectile p_right = new DragonProjectile(this, Side2.RIGHT);
			
			projectiles.add(p_top_left);
			projectiles.add(p_top_right);
			projectiles.add(p_top);
			
			projectiles.add(p_bot_left);
			projectiles.add(p_bot_right);
			projectiles.add(p_bot);
			
			projectiles.add(p_left);
			projectiles.add(p_right);
			
			spell1Generated = true;
			
		}
	}
	
	private void updateTimerSpell1(){
		if(timerSpell1 == null) return;
		timerSpell1.increment();
		
		if(timerSpell1.getTimeLeft() == 0){
			canMove = true;
		}
	}
	
	private float speed = VolcanopDragon.getRandomSpeed();
	
	private void updatePos(Game game){
		float px = playerToChase.getX();
		float py = playerToChase.getY();
		
		
		if(timeToGoBack){
			genProjectileSpell1();
			
			if(!canMove) return;
			
			float gx = guardian.getX();
			float gy = guardian.getY();
			
			if(gx < x){
				x -= speed;
			}
			if(gx > x){
				x += speed;
			}
			
			if(gy < y){
				y -= speed;
			}
			if(gy > y){
				y += speed;
			}
			
			box.update(x, y);
			return;
		}
		
		if(game.getTimeCounter().getTime() % 30 == 0){
			DragonProjectile genProj = genNewProjectile();
			projectiles.add(genProj);
		}
		
		/**
		 * We suppose there is no collision possible, since
		 * a verification just happened (checkPlayerCollision())
		 */
		if(px < x){
			x -= speed;
		}
		if(px > x){
			x += speed;
		}
		
		if(py < y){
			y -= speed;
		}
		if(py > y){
			y += speed;
		}
		
		/**
		 * Check if have to retake in count the player collision
		 */
//		if(xWhenCollided != 0){
//			if(Math.abs(xWhenCollided - x) > distanceBeforeReCheckCollision || 
//					Math.abs(yWhenCollided - y) > distanceBeforeReCheckCollision){
//				collidedPlayer = false;
//			}
//		}
		
		if(y < 0){
			y = 0;
		}
		
		if(y + texture.getHeight() + 70 >= playerToChase.getY()){
			y = playerToChase.getY() - texture.getWidth() - 70;
		}
		
		box.update(x, y);
	}
	
	private float distanceBeforeReCheckCollision = 5f;
	
	private DragonProjectile genNewProjectile(){
		float px = playerToChase.getX();
		float py = playerToChase.getY();
		int pw = playerToChase.getTexture().getWidth();
		int ph = playerToChase.getTexture().getHeight();
		int w = texture.getWidth();
		int h = texture.getHeight();
		
		Side2 newProjSide = null;
		
		if(px + pw < x){
			if(py + ph < y){
				newProjSide = Side2.TOP_LEFT;
			}
			else if(py > y + h){
				newProjSide = Side2.BOT_LEFT;
			}
			else{
				newProjSide = Side2.LEFT;
			}
		}
		
		else if(px > x + w){
			if(py + ph < y){
				newProjSide = Side2.TOP_RIGHT;
			}
			else if(py > y + h){
				newProjSide = Side2.BOT_RIGHT;
			}
			else{
				newProjSide = Side2.RIGHT;
			}
		}
		
		else{
			if(py + ph < y){
				newProjSide = Side2.TOP;
			}
			else if(py > y + h){
				newProjSide = Side2.BOT;
			}
			else{
				newProjSide = Side2.NULL; // TODO COLLISION SHOULD HAPPEN
			}
		}
		
		return new DragonProjectile(this, newProjSide);
	}
	
	private void updateTexture(){
		float px = playerToChase.getX();
		
		if(px < x){
			texture = Texture.DRAGOONE_LEFT;
			direction = Side2.LEFT;
		}
		if(px >= x){
			texture = Texture.DRAGOONE_RIGHT;
			direction = Side2.RIGHT;
		}

		if(x > px && x + texture.getWidth() < px + playerToChase.getTexture().getWidth()){
			texture = Texture.DRAGOONE_RIGHT;
			direction = Side2.RIGHT;
		}
		
		if(hit){
			if(direction.equals(Side2.LEFT)){
				texture = Texture.DRAGOONE_LEFT_HIT;
			}
			else if(direction.equals(Side2.RIGHT)){
				texture = Texture.DRAGOONE_RIGHT_HIT;
			}
			else{
				throw new IllegalStateException("direction should not be other than left or right");
			}
		}
		
	}
	
	private boolean collidedPlayer = false;
	private float xWhenCollided;
	private float yWhenCollided;
	
	private void checkPlayerCollision(List<Mob> mobs){
		// if(collidedPlayer) return;
		if(Collision.boxCollide(box, playerToChase.getBox())){
			// JukeBox.playLose();
			playerToChase.subLife();
			
//			xWhenCollided = x;
//			yWhenCollided = y;
			
			// actionTimer.setTimeLeft(0);
			// checkActionTimer(mobs);
			
			collidedPlayer = true;
			return;
		}
	}
	
	private void updateActionTimer(List<Mob> mobs){
		if(actionTimer == null) return;
		
		actionTimer.increment();
		
		checkActionTimer(mobs);
	}
	
	private boolean canRender = true;
	
	private void checkActionTimer(List<Mob> mobs){
		if(actionTimer.getTimeLeft() == 0){
			timeToGoBack = true;
			
			if(Collision.boxCollide(box, guardian.getBox())){
				if(!savedForLater){
					// canRender = false;
					guardian.saveForLaterDragoone(mobs);
					savedForLater = true;
				}
			}
		}
	}
	
	private void checkTimerBeforeReGo(){
		if(timerBeforeReGo == null) return;
		
		timerBeforeReGo.increment();
		
		if(timerBeforeReGo.getTimeLeft() == 0){
			//canUpdate = true;
			stop = false;
		}
	}

	
	/**
	 * Render part
	 */
	
	@Override
	public void render(Graphics2D g) {
		// if(stop) return; TODO SEE
		// if(!canRender) return;
		if(shouldExists){
			renderLifeGui(g);
		}
		renderProjectiles(g);
		g.drawImage(texture, (int) x, (int) y, null);
	}
	
	private void renderProjectiles(Graphics2D g){
		for(DragonProjectile projectile : projectiles){
			if(projectile == null) continue;
			projectile.render(g);
		}
	}
	
	private void renderLifeGui(Graphics2D g){
		lifeGui.render(g);
	}
	
	
	public void makeExists(){
		if(shouldExists) return;
		shouldExists = true;
	}
	
	
	@Override
	public Dragoone clone() {
		try{
			if(cloned) return null;
			Dragoone clone = new Dragoone(guardian.clone(), playerToChase);
			clone.actionTimer = new Timer(actionTimer);
			clone.bonusPoints = bonusPoints;
			clone.box = box;
			clone.hit = hit;
			clone.lives = lives;
			clone.side = side;
			clone.speedX = speedX;
			clone.speedY = speedY;
			clone.texture = texture;
			clone.timerHit = new Timer(timerHit);
			clone.timesHit = timesHit;
			clone.timeToGoBack = timeToGoBack;
			clone.x = x;
			clone.y = y;
			clone.bonusHit = bonusHit;
			clone.canMove = canMove;
			clone.canUpdate = canUpdate;
			clone.direction = direction;
			clone.lifeGui = lifeGui;
			clone.livesHit = livesHit;
			clone.projectiles = projectiles;
			clone.shouldExists = shouldExists;
			clone.spell1Generated = spell1Generated;
			clone.stop = stop;
			clone.timerBeforeReGo = timerBeforeReGo;
			clone.timerSpell1 = timerSpell1;
			
			cloned = true;
			return clone;
		}
		catch(Exception e){
			return null;
		}
	}

	@Override
	protected void setMob() {
		
	}
	
	
	/**
	 * LIFE GUI
	 * @author Damien
	 *
	 */
	
	private class LifeGui{
		
		private class Texture{
			
			public float xStart = GamePanel.WIDTH *  40 / 100;
			public float yStart = GamePanel.HEIGHT * 80 / 100;
			
			public BufferedImage texture;
			public float x, y;
			public Texture prev;
			
			public Texture(boolean otherSpawned, BufferedImage tex, Texture father){
				this.texture = tex;
				
				if(otherSpawned){
					yStart -= 20;
				}
				
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
		public byte hit;
		public Texture[] textures;
		
		public LifeGui(boolean otherSpawned, byte numberOfLives){
			lives = numberOfLives;
			generateTextures(otherSpawned);
		}
		
		private void generateTextures(boolean otherSpawned){
			textures = new Texture[Dragoone.this.lives];
			
			BufferedImage tex = graphics.Texture.BOSS_LIFE_GUI_GREEN_LEFT;
			
			textures[0] = new Texture(otherSpawned, tex, null);
			
			for(byte b = 1; b < lives - 1; b++){
				textures[b] = new Texture(otherSpawned, graphics.Texture.BOSS_LIFE_GUI_GREEN_MID, textures[b - 1]);
			}
			
			textures[lives - 1] = new Texture(otherSpawned, graphics.Texture.BOSS_LIFE_GUI_GREEN_RIGHT,
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


	
}
