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
import entities.projectiles.VolcanopProjectile;
import gameTester.Game;
import gameTester.GamePanel;
import graphics.Texture;
import physics.Collision;
import toolbox.Logs;
import toolbox.Maths;
import toolbox.Side;
import toolbox.Side2;
import toolbox.Timer;

public class Volcanop extends Boss{

	private final static float BASE_SPEED = 0.5f; // 0.75f
	private final static int MAX_LIVES = 10;
	private final static byte DRAGOONE_GEN = 1;
	private final static byte DRAGONE_GEN = 2;
	private final static byte NO_GEN = 3;
	// private final static float MAX_SPEED_Y = 1.5f;
	
	public static boolean cloned = false;
	
	private byte livesHit;
	private byte lastGenerated;
	private byte countHit = 0;
	private int lives = MAX_LIVES;
	private int bonusHit = bonusPoints / MAX_LIVES;
	
	private Dragoone dragoone;
	private Dragone dragone;
	
	private LifeGui lifeGui = new LifeGui((byte) lives);
	private Timer timerToGen;
	private Timer fromWhenToGenDragone;
	
	private boolean gameChanged = false;
	private boolean dragooneSpawned = false;
	private boolean dragoneSpawned = false;
	private boolean canSpawnDragoones = true;
	private boolean canSpawnDragones = true;
	private boolean canGen;
	private boolean timeToRemoveDragoone = false;
	private boolean timeToRemoveDragone = false;
	private boolean firstTime = true;
	private boolean endDragoone = false;
	private boolean stopCheckDragoone = false;
	private boolean stopCheckDragone = false;
	
	
	private List<VolcanopDragon> dragons;
	private List<VolcanopProjectile> projectiles;
	private List<VolcanopProjectile> projectilesToRemove;
	
	
	public Volcanop(Player playerToChase) {
		super(Texture.VOLCANO_BOSS_FULL, GamePanel.WIDTH / 2 - 15, GamePanel.HEIGHT * 4 / 100,
				playerToChase, BASE_SPEED, BASE_SPEED, Side.DOWN, 30000, 10);
		 
		canGen = true;
		timerToGen = new Timer(6);
		dragons = new ArrayList<>();
		projectiles = new ArrayList<>();
		projectilesToRemove = new ArrayList<>();
		lastGenerated = NO_GEN;
	}
	
	private boolean canBeHit(){
		return (!dragoneSpawned && !dragooneSpawned && !canSpawnDragones && !canSpawnDragoones);
	}

	@Override
	public void hit(){
		if(!canBeHit()) return;
		JukeBox.play("enemyHit");
		
		if(countHit >= 2){ // 3 hits, 30 lives then
			lives--;
			livesHit++;
			countHit = 0;
		}
		else{
			countHit++;
		}
		
		hit = true;
		timerHit = new Timer(500, false);
		playerToChase.addScore(bonusHit / 3);
	}
	
	private void checkTimerHit(){
		if(timerHit == null) return;
		timerHit.increment();
		if(timerHit.getTimeLeft() == 0){
			hit = false;
		}
	}

	
	@Override
	public void update(Game game, List<Mob> mobs) {
		checkPlayerCollision();
		checkTimerHit();
		updateTimerDragonGen(mobs);
		updateFromWhenToGenDragoneTimer();
		updateLifeGui();
		updateDragons(game, mobs);
		genProjectile(game);
		
		updatePosition(game);
		checkTexture();
		updateProjectiles(game, mobs);
		checkProjectilesToRemove();
		checkState(game, mobs);
	}
	
	public void addToProjectilesToRemove(VolcanopProjectile projectile){
		if(!projectilesToRemove.contains(projectile)){
			projectilesToRemove.add(projectile);
		}
	}
	
	private void checkProjectilesToRemove(){
		Iterator<VolcanopProjectile> iterator = projectilesToRemove.iterator();
		
		while(iterator.hasNext()){
			VolcanopProjectile toRemoveProj = iterator.next();
			
			Iterator<VolcanopProjectile> iterProjs = projectiles.iterator();
			
			while(iterProjs.hasNext()){
				VolcanopProjectile proj = iterProjs.next();
				
				if(toRemoveProj.equals(proj)){
					iterProjs.remove();
					iterator.remove();
				}
			}
		}

	}
	
	private void genProjectile(Game game){
		if(canBeHit()){
			if(game.getTimeCounter().getTime() % 40 == 0){
				JukeBox.playBossShot();
				
				VolcanopProjectile newProj;
				Side2 newProjSide = Side2.NULL;
				
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
				
				newProj = new VolcanopProjectile(this, newProjSide);
				
				projectiles.add(newProj);
			}
		}
	}
	
	private void updateProjectiles(Game game, List<Mob> mobs){
		if(canBeHit()){
			for(VolcanopProjectile projectile : projectiles){
				projectile.update(game, mobs);
			}
		}
	}
	
	
	private void checkTexture(){
		if(dragoneSpawned && dragooneSpawned){
			texture = Texture.VOLCANO_BOSS_ALONE;
		}
		
		else if(dragoneSpawned && !dragooneSpawned){
			if(canSpawnDragoones){
				texture = Texture.VOLCANO_BOSS_DRAGOONE;
			}
		}
		
		else if(!dragoneSpawned && dragooneSpawned){
			if(canSpawnDragones){
				texture = Texture.VOLCANO_BOSS_DRAGONE;
			}
		}
		
		else{
			texture = Texture.VOLCANO_BOSS_FULL;
		}
		
		if(texture == Texture.VOLCANO_BOSS_FULL){
			if(canSpawnDragones && !canSpawnDragoones){
				texture = Texture.VOLCANO_BOSS_DRAGONE;
			}
			
			else if(!canSpawnDragones && canSpawnDragoones){
				texture = Texture.VOLCANO_BOSS_DRAGOONE;
			}
			
			else if(!canSpawnDragones && !canSpawnDragoones){
				texture = Texture.VOLCANO_BOSS_ALONE;
			}
		}
		
		if(hit){
			if(texture == Texture.VOLCANO_BOSS_ALONE){
				texture = Texture.VOLCANO_BOSS_ALONE_HIT;
			}
			else if(texture == Texture.VOLCANO_BOSS_DRAGONE){
				texture = Texture.VOLCANO_BOSS_DRAGONE_HIT;
			}
			else if(texture == Texture.VOLCANO_BOSS_DRAGOONE){
				texture = Texture.VOLCANO_BOSS_DRAGOONE_HIT;
			}
			else if(texture == Texture.VOLCANO_BOSS_FULL){
				texture = Texture.VOLCANO_BOSS_FULL_HIT;
			}
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
	
	
	private float randomSpeed(){
		int rand = Maths.irand(2);
		
		float speed = Maths.frandR(0.5f, 1.5f);
		
		if(rand == 1){
			speed = -speed;
		}
		return speed;
	}
	
	private void updatePosition(Game game){
		boolean canBeHit = canBeHit();
		
		if(canBeHit){
			// speedY = randomSpeedY();
			if(game.getTimeCounter().getTime() % 40 == 0){
				speedX = randomSpeed();
				speedY = randomSpeed();
			}
		}
		else{
			speedX = BASE_SPEED;
			speedY = 0;
			
			float px = playerToChase.getX();
			int pw = playerToChase.getTexture().getWidth();
			int w = texture.getWidth();
			
			if(px + pw < x){
				speedX = -speedX;
			}
			
			else if(px > x + w){
				speedX = Math.abs(speedX);
			}
			else{
				speedX = 0;
			}
			
		}
		
		x += speedX;
		y += speedY;
		
		if(x < 0){
			x = 0;
		}
		if(x + texture.getWidth() > GamePanel.WIDTH){
			x = GamePanel.WIDTH - texture.getWidth();
		}
		
		if(canBeHit){
			if(y < 0){
				y = 0;
			}
			if(y + texture.getHeight() > (2 * GamePanel.HEIGHT) / 3){
				y = (2 * GamePanel.HEIGHT) / 3 - texture.getHeight();
			}
		}
		
		box.update(x, y);
	}
	
	
	private void updateTimerDragonGen(List<Mob> mobs){
		if(timerToGen == null) return;
		
		timerToGen.increment();
		
		if(timerToGen.getTimeLeft() == 0){
			timerToGen.reset(3);
			if(firstTime){
				canGen = true;
				firstTime = false;
			}
			genNextDragon(mobs);
		}
		else{
			if(!endDragoone){
				canGen = false;
			}
		}
	}

	
	public void removeDragoone(List<Mob> mobs){
		deleteFromDragons(dragoone);
		deleteFromMobs(mobs, dragoone);
		stopDragoone();
		
		timeToRemoveDragoone = true;
		endDragoone = true;
		fromWhenToGenDragone = new Timer(3);
		dragooneSpawned = false;
	}
	
	
	private void updateFromWhenToGenDragoneTimer(){
		if(fromWhenToGenDragone == null) return;
		
		fromWhenToGenDragone.increment();
		if(fromWhenToGenDragone.getTimeLeft() == 0){
			canGen = true;
			fromWhenToGenDragone = null;
		}
	}
	
	
	public void removeDragone(List<Mob> mobs){
		deleteFromDragons(dragone);
		deleteFromMobs(mobs, dragone);
		stopDragone();
		
		timeToRemoveDragone = true;
		endDragoone = true;
		dragoneSpawned = false;
	}

	
	public void checkRemoveDragons(){
		if(timeToRemoveDragoone && !stopCheckDragoone){
			Logs.println2("Removed dragoone");
			dragons.remove(dragoone);
			timeToRemoveDragoone = false;
			stopCheckDragoone = true;
		}
		if(timeToRemoveDragone && !stopCheckDragone){
			Logs.println2("Removed dragone");
			dragons.remove(dragone);
			timeToRemoveDragone = false;
			stopCheckDragone = true;
		}
	}
	
	
	private void updateDragons(Game game, List<Mob> mobs){
		try{
			for(VolcanopDragon dragon : dragons){
				if(dragon != null){
					dragon.update(game, mobs);
				}
			}
		}
		catch(Exception e){
			
		}
	}
	
	
	public boolean removeDragon(VolcanopDragon dragonToRemove){
		Iterator<VolcanopDragon> iterator = dragons.iterator();
		
		while(iterator.hasNext()){
			VolcanopDragon next = iterator.next();
			if(next.equals(dragonToRemove)){
				
				if(next instanceof Dragoone){
					dragoone = null;
				}
				
				if(next instanceof Dragone){
					dragone = null;
				}
				
				iterator.remove();
				return true;
			}
		}
		return false;
	}
	
	
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
		}
	}

	
	public void saveForLaterDragoone(List<Mob> mobs){
		if(dragoone == null) return;
		deleteFromDragons(dragoone);
		deleteFromMobs(mobs, dragoone);
		dragoone = null;
		canGen = true; // TODO SEE CAN GEN
		fromWhenToGenDragone = new Timer(3);
		endDragoone = true;
		dragooneSpawned = false;
	}
	
	
	private void deleteFromList(List<?> list, VolcanopDragon dragon){
		Iterator<?> iterator = list.iterator();
		
		while(iterator.hasNext()){
			if(iterator.next().equals(dragon)){
				iterator.remove();
			}
		}
	}
	
	
	private void deleteFromDragons(VolcanopDragon dragon){
		deleteFromList(dragons, dragon);
	}
	
	
	private void deleteFromMobs(List<Mob> mobs, VolcanopDragon dragon){
		deleteFromList(mobs, dragon);
	}
	
	
	public void saveForLaterDragone(List<Mob> mobs){
		if(dragone == null) return;
		deleteFromDragons(dragone);
		deleteFromMobs(mobs, dragone);
		dragone = null;
		canGen = true; // TODO SEE CAN GEN
		fromWhenToGenDragone = new Timer(3);
		endDragoone = true;
		dragoneSpawned = false;
	}
	
	
	public void genNextDragon(List<Mob> mobs){
		if(!canGen){
			Logs.println2("Cant gen dragon ! (genNextDragon)");
			return;
		}
		
		if((!canSpawnDragones && canSpawnDragoones && !dragooneSpawned)
			|| (canSpawnDragoones && !dragooneSpawned && (lastGenerated == DRAGONE_GEN || lastGenerated == NO_GEN))){
				dragoone = new Dragoone(this, playerToChase);
				
				if(!dragons.contains(dragoone)){
					deletePotentialDragoones(dragons);
					dragons.add(dragoone);
				}
				
				if(!mobs.contains(dragoone)){
					deletePotentialDragoones(mobs);
					mobs.add(dragoone);
				}
				
				Logs.println2("GENERATED DRAGOONE");
				lastGenerated = DRAGOONE_GEN;
				dragooneSpawned = true;
				
				// TODO SEE
				if(canSpawnDragones && dragoneSpawned){
					texture = Texture.VOLCANO_BOSS_ALONE;
				}
				else if(canSpawnDragones && !dragoneSpawned){
					texture = Texture.VOLCANO_BOSS_DRAGONE;
				}
				else if(!canSpawnDragones){
					texture = Texture.VOLCANO_BOSS_ALONE;
				}
				
				return;
		}
		
		if((!canSpawnDragoones && canSpawnDragones && !dragoneSpawned) ||
				(canSpawnDragones && !dragoneSpawned && (lastGenerated == DRAGOONE_GEN || lastGenerated == NO_GEN))){
				dragone = new Dragone(this, playerToChase);
				
				if(!dragons.contains(dragone)){
					deletePotentialDragones(dragons);
					dragons.add(dragone);
				}
				
				if(!mobs.contains(dragone)){
					deletePotentialDragones(mobs);
					mobs.add(dragone);
				}
				
				lastGenerated = DRAGONE_GEN;
				dragoneSpawned = true;
				Logs.println2("GENERATED DRAGONE");
				
				// TODO SEE
				if(canSpawnDragoones && dragooneSpawned){
					texture = Texture.VOLCANO_BOSS_ALONE;
				}
				else if(canSpawnDragoones && !dragooneSpawned){
					texture = Texture.VOLCANO_BOSS_DRAGOONE;
				}
				else if(!canSpawnDragoones){
					texture = Texture.VOLCANO_BOSS_ALONE;
				}
				
				return;
		}
		
		Logs.println2("Generated nothing, canSpawnDragones : " + canSpawnDragones
				+ ", canSpawnDragoones : " + canSpawnDragoones + ", last generated : " + translateLastGenerated()
				+ ", dragooneSpawned : " + dragooneSpawned + ", dragoneSpawned : " + dragoneSpawned);
	}
	
	
	private String translateLastGenerated(){
		if(lastGenerated == DRAGONE_GEN){
			return "dragone was last generated !";
		}
		if(lastGenerated == DRAGOONE_GEN){
			return "dragoone was last generated !";
		}
		else{
			return "no dragon was generated !";
		}
	}
	
	
	private void deletePotentialDragones(List<?> list){
		Iterator<?> iterator = list.iterator();
		
		while(iterator.hasNext()){
			if(iterator.next() instanceof Dragone){
				iterator.remove();
			}
		}
	}
	
	
	private void deletePotentialDragoones(List<?> list){
		Iterator<?> iterator = list.iterator();
		
		while(iterator.hasNext()){
			if(iterator.next() instanceof Dragoone){
				iterator.remove();
			}
		}
	}
	
	
	public void stopDragoone(){
		if(canSpawnDragoones){
			dragoone = null;
			canSpawnDragoones = false;
		}
	}
	
	
	public void stopDragone(){
		if(canSpawnDragones){
			dragone = null;
			canSpawnDragones = false;
		}
	}
	
	
	private void updateLifeGui(){
		lifeGui.update(livesHit);
	}
	
	
	protected List<VolcanopDragon> getDragons(){
		return dragons;
	}

	
	public void setCanGen(boolean b){
		if(canGen == b) return;
		canGen = b;
	}
	
	
	
	/**
	 * Render part
	 */
	@Override
	public void render(Graphics2D g) {
		renderLifeGui(g);
		// renderGenTimer(g);
		renderDragons(g);
		renderProjectiles(g);
		g.drawImage(texture, (int) x, (int) y, null);
	}
	
	private void renderProjectiles(Graphics2D g){
		if(canBeHit()){
			for(VolcanopProjectile projectile : projectiles){
				projectile.render(g);
			}
		}
	}
	
	private void renderDragons(Graphics2D g){
		for(VolcanopDragon dragon : dragons){
			dragon.render(g);
		}
	}
	
	
	private void renderLifeGui(Graphics2D g){
		lifeGui.render(g);
	}
	
	
	private void renderGenTimer(Graphics2D g){
		if(timerToGen == null) return;
		g.drawString(timerToGen.getTimeLeft() + "s", GamePanel.WIDTH * 30/100, GamePanel.HEIGHT * 30/100);
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
	
	
	public boolean isDragooneSpawn(){
		return dragooneSpawned;
	}
	
	
	public boolean isDragoneSpawn(){
		return dragoneSpawned;
	}
	
	
	public void setDragooneSpawned(boolean dragooneSpawned) {
		this.dragooneSpawned = dragooneSpawned;
	}

	
	public void setDragoneSpawned(boolean dragoneSpawned) {
		this.dragoneSpawned = dragoneSpawned;
	}
	
	
	@Override
	public Volcanop clone() {
		try{
			if(cloned) return null; // TODO check this
			Volcanop volcanop = new Volcanop(playerToChase);
			volcanop.setLives(lives);
			volcanop.setPos(x, y);
			volcanop.setSide(side);
			volcanop.setSpeedX(speedX);
			volcanop.setSpeedY(speedY);
			volcanop.setTexture(texture);
			volcanop.setTimerHit(new Timer(timerHit));
			volcanop.setHit(hit);
			
			cloned = true;
			
			return volcanop;
		}
		catch(Exception e){
			return null;
		}
	}

	
	@Override
	protected void setMob() {
		
	}


	/**
	 * LifeGUI Stuff
	 *
	 */
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

		}
		
		public byte lives;
		public byte hit;
		public Texture[] textures;
		
		public LifeGui(byte numberOfLives){
			lives = numberOfLives;
			generateTextures();
		}
		
		private void generateTextures(){
			textures = new Texture[Volcanop.this.lives];
			
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

}
