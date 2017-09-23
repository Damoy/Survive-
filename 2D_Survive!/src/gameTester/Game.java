package gameTester;

import static entities.Player.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import audio.JukeBox;
import entities.Entity;
import entities.Player;
import entities.decorations.CactusDecoration;
import entities.decorations.CenterDecoration;
import entities.decorations.DecorationEntity;
import entities.decorations.LavaDecoration;
import entities.decorations.MobSpawnDecoration;
import entities.decorations.SandFlowerDecoration;
import entities.mobs.Blazer;
import entities.mobs.Lavalter;
import entities.mobs.Mob;
import entities.mobs.Sticker;
import entities.mobs.boss.Boss;
import entities.mobs.boss.Coctus;
import entities.mobs.boss.Dragone;
import entities.mobs.boss.Dragoone;
import entities.mobs.boss.Madjnouby;
import entities.mobs.boss.Volcanop;
import graphics.Texture;
import graphics.Window;
import gui.Menu;
import gui.Score;
import items.Bonus;
import physics.Collision;
import physics.PhysicsEngine;
import toolbox.Infos;
import toolbox.Logs;
import toolbox.Maths;
import toolbox.Save;
import toolbox.Side;
import toolbox.Side2;
import toolbox.Terrain;
import toolbox.TimeCounter;
import toolbox.Timer;
import toolbox.WorldSave;
import world.Tile;
import world.TileMap;

public class Game {
	
	public final static float TIMER_X = GamePanel.WIDTH / 30;
	public final static float TIMER_Y = GamePanel.HEIGHT * 1/15;
	public final static float BEST_SCORE_X = GamePanel.WIDTH * 1/30;
	public final static float BEST_SCORE_Y = GamePanel.HEIGHT * 19/20;
	
	public final static float BOSS_ENTER_X = GamePanel.WIDTH * 40 / 100;
	public final static float BOSS_ENTER_Y = GamePanel.HEIGHT * 35 / 100;
	
	public final static String GAME_TITLE = "Survive! | Version 0.2";
	
	private final static int TIME_GAME_TIMER = 60; // 60
	public static boolean DOUBLE_GUN_STATE = false;
	
	public static boolean canGenerateBonusSpeed = true;
	
	public final static int TERRAIN_CHOICE_STATE = 5;

	private GamePanel gamePanel;
	public static Font bestScoreFont = new Font("Arial", Font.PLAIN, 10);
	public static Font bossEnterFont = new Font("Arial", Font.PLAIN, 10);
	public static Font burningFont = new Font("Arial", Font.PLAIN, 9);
	private Menu menu;
	private Player player;
	private Score playerScore;
	private Window window;
	private Timer gameTimer;
	private TileMap map;
	private TimeCounter timer;
	private TimeCounter timer_boss = new TimeCounter();
	private PhysicsEngine physicsEngine;
	
	private List<Mob> mobs;
	private List<Bonus> allBonus = new ArrayList<>();
	private List<Bonus> bonusToRemove = new ArrayList<>();
	private boolean canUpdate = true;
	private boolean end = false;
	private boolean musicLaunched = false;
	private boolean endGameSet = false;
	private int state = 0; // 0 in menu 1 in game 2 in infos
	private int bestScore = 0;
	private Terrain terrain;

	
	private boolean load;
	
	public Game(){
		loadSounds();
		loadScores();

		map = new TileMap(16);
		
		// generateRandomMap(); TODO put in setState 1
		
		window = new Window(GAME_TITLE, this);
		this.gamePanel = window.getGamePanel();
		
		player = new Player(this, BASE_PLAYER_X, BASE_PLAYER_Y);
		this.playerScore = player.getScoreObj();
		
		mobs = new ArrayList<>();
		this.menu = new Menu(this);
		
		physicsEngine = new PhysicsEngine(player);
		
		load = false;
	}
	
	
	public void start(){
		window.start();
	}
	
	private Tile randomEarthTile(){
		switch(Maths.irand(4)){
			case 0:
				return new Tile(Texture.GREEN_TERRAIN, Tile.NORMAL);
			case 1:
				return new Tile(Texture.GREEN_TERRAIN2, Tile.NORMAL);
			case 2:
				return new Tile(Texture.GREEN_TERRAIN3, Tile.NORMAL);
			case 3:
				return new Tile(Texture.GREEN_TERRAIN4, Tile.NORMAL);
			default:
				return new Tile(Texture.GREEN_TERRAIN3, Tile.NORMAL);
		}
	}
	
	private Tile randomSandTile(){
		switch(Maths.irand(4)){
			case 0:
				return new Tile(Texture.SAND_TERRAIN, Tile.NORMAL);
			case 1:
				return new Tile(Texture.SAND_TERRAIN2, Tile.NORMAL);
			case 2:
				return new Tile(Texture.SAND_TERRAIN3, Tile.NORMAL);
			case 3:
				return new Tile(Texture.SAND_TERRAIN4, Tile.NORMAL);
			default:
				return new Tile(Texture.SAND_TERRAIN3, Tile.NORMAL);
		}
	}
	
	private Tile randomGlassTile(){
		switch(Maths.irand(4)){
			case 0:
				return new Tile(Texture.GLASS_TERRAIN, Tile.NORMAL);
			case 1:
				return new Tile(Texture.GLASS_TERRAIN2, Tile.NORMAL);
			case 2:
				return new Tile(Texture.GLASS_TERRAIN3, Tile.NORMAL);
			case 3:
				return new Tile(Texture.GLASS_TERRAIN4, Tile.NORMAL);
			default:
				return new Tile(Texture.GLASS_TERRAIN, Tile.NORMAL);
		}
	}
	
	private List<DecorationEntity> decorations = new ArrayList<>();
	
	private void clearDecorations(){
		decorations.clear();
	}
	
	public void generateMap(byte selected){
		clearDecorations();
		generateBaseDecorations();
		
		Tile[][] tiles = map.getTiles();
		
		switch(selected){
			case 0:
				for(int row = 0; row < map.getNumRows(); row++){
					for(int col = 0; col < map.getNumCols(); col++){
						tiles[row][col] = randomEarthTile();
					}
				}
				generateSandFlowers();
				terrain = Terrain.CLASSIC;
				break;
			case 1:
				for(int row = 0; row < map.getNumRows(); row++){
					for(int col = 0; col < map.getNumCols(); col++){
						tiles[row][col] = randomSandTile();
					}
				}
				generateCactus();
				terrain = Terrain.DESERT;
				break;
			case 2:
				for(int row = 0; row < map.getNumRows(); row++){
					for(int col = 0; col < map.getNumCols(); col++){
						tiles[row][col] = randomGlassTile();
					}
				}
				generateLava();
				canGenerateBonusSpeed = false; // removing speed bonus with lava terrain
				terrain = Terrain.VOLCAN;
				break;
		}
	}
	
	public void generateRandomMap(){
		clearDecorations();
		generateBaseDecorations();
		
		Tile[][] tiles = map.getTiles();
		
		switch(Maths.irand(3)){
			case 0:
				for(int row = 0; row < map.getNumRows(); row++){
					for(int col = 0; col < map.getNumCols(); col++){
						tiles[row][col] = randomEarthTile();
					}
				}
				generateSandFlowers();
				terrain = Terrain.CLASSIC;
				break;
			case 1:
				for(int row = 0; row < map.getNumRows(); row++){
					for(int col = 0; col < map.getNumCols(); col++){
						tiles[row][col] = randomSandTile();
					}
				}
				generateCactus();
				terrain = Terrain.DESERT;
				break;
			case 2:
				for(int row = 0; row < map.getNumRows(); row++){
					for(int col = 0; col < map.getNumCols(); col++){
						tiles[row][col] = randomGlassTile();
					}
				}
				generateLava();
				canGenerateBonusSpeed = false; // removing speed bonus with lava terrain
				terrain = Terrain.VOLCAN;
				break;
		}
	}
	
	private void generateBaseDecorations(){
		generateCenterDecoration();
		generateSpawnDecorations();
	}
	
	private void generateCenterDecoration(){
		decorations.add(new CenterDecoration());
	}
	
	private void generateSpawnDecorations(){
		MobSpawnDecoration leftSpawn = new MobSpawnDecoration((byte) 1, TileMap.SPAWN_LEFT_X, TileMap.SPAWN_LEFT_Y);
		MobSpawnDecoration topSpawn = new MobSpawnDecoration((byte) 2, TileMap.SPAWN_DOWN_X, TileMap.SPAWN_DOWN_Y);
		MobSpawnDecoration rightSpawn = new MobSpawnDecoration((byte) 3, TileMap.SPAWN_RIGHT_X, TileMap.SPAWN_RIGHT_Y);
		MobSpawnDecoration botSpawn = new MobSpawnDecoration((byte) 4, TileMap.SPAWN_TOP_X, TileMap.SPAWN_TOP_Y);
		
		decorations.add(leftSpawn);
		decorations.add(topSpawn);
		decorations.add(rightSpawn);
		decorations.add(botSpawn);
	}
	
	private void generateLava(){
		int lavaGenerated = 0;
		do{
			LavaDecoration lava = new LavaDecoration(Maths.frand(16, GamePanel.WIDTH - 32), 
					Maths.frand(8, GamePanel.HEIGHT - 32));
			
			if(!PhysicsEngine.checkDecorationCollisionWithDecorations(lava, decorations)){
				decorations.add(lava);
				lavaGenerated++;
			}
		}
		while(lavaGenerated < 5);
	}
	
	private void generateSandFlowers(){
		int flowersGenerated = 0;
		do{
			SandFlowerDecoration flower = new SandFlowerDecoration(Maths.frand(16, GamePanel.WIDTH - 32), 
					Maths.frand(8, GamePanel.HEIGHT - 32));
			
			if(!PhysicsEngine.checkDecorationCollisionWithDecorations(flower, decorations)){
				decorations.add(flower);
				flowersGenerated++;
			}
		}
		while(flowersGenerated < 5);
	}
	
	private void generateCactus(){
		int cactusGenerated = 0;
		do{
			CactusDecoration cactus = new CactusDecoration(Maths.frand(16, GamePanel.WIDTH - 32), 
					Maths.frand(8, GamePanel.HEIGHT - 32));
			
			if(!PhysicsEngine.checkDecorationCollisionWithDecorations(cactus, decorations)){
				decorations.add(cactus);
				cactusGenerated++;
			}
		}
		while(cactusGenerated < 5);
	}
	
	private void updateDecorations(){
		for(DecorationEntity d : decorations){
			d.update(this, mobs);
		}
	}
	
	private void renderDecorations(Graphics2D g){
		for(DecorationEntity d : decorations){
			d.render(g);
		}
	}
	
	private void loadGreenMap(){
		loadMap(1);
	}
	
	@SuppressWarnings("unused")
	private void loadGrayMap(){
		loadMap(2);
	}
	
	private void loadMap(int type){
		map.loadAllSameTiles(type, "./resources/textures/res_data.png");
	}
	
	private void loadSounds(){
		JukeBox.init();
	}
	
	public void loadScores(){
		List<Integer> scores = Save.SListToI(Save.loadScores());
		
		if(scores != null){
			if(scores.size() <= 0){
				bestScore = 0;
				return;
			}
			bestScore = Maths.max(scores);
		}
	}
	
	
	public void update(){
		if(state == 1){
			if(!musicLaunched){
				JukeBox.loopMainMusic();
				musicLaunched = true;
			}
			updateDecorations();
			checkTimerState();
			checkScoreState();
			
			updateRestaureTimer();
			
			if(!canUpdate) return;
			player.update(this, mobs);
			updateMobs();
			updateBonus();
			checkPlayerDecorationsCollision();
		}
		
		else if(state == Madjnouby.BOSS_STATE){
			updateBossState();
		}
		
		else if(state == TERRAIN_CHOICE_STATE){
			updateTerrainChoiceMenu();
		}
		
		else{
			menu.update();
		}
	}
	
	private void updateTerrainChoiceMenu(){
		if(terrainChoiceMenu == null) return;
		terrainChoiceMenu.update();
	}
	
	/**
	 * Checks if the item has to be deleted now
	 */
	private void updateBonus(){
		Iterator<Bonus> iterator = allBonus.iterator();
		
		while(iterator.hasNext()){
			Bonus next = iterator.next();
			
			next.getTimerDelete().increment();
			if(next.getTimerDelete().getTimeLeft() <= 0){
				iterator.remove();
			}
		}
	}
	
	private void checkPlayerDecorationsCollision(){
		Iterator<DecorationEntity> iterator = decorations.iterator();
		
		while(iterator.hasNext()){
			DecorationEntity de = iterator.next();
			
			if(de instanceof LavaDecoration){
				if(Collision.boxCollide(player.getBox(), de.getBox())){
					if(!player.isBurning()){
						player.lavaHit();
					}
				}
			}
			
			if(de instanceof CactusDecoration){
				
				Side2 collisionCactusSide = PhysicsEngine.boxCollideSide(player, de);
				
				if(!(collisionCactusSide == Side2.NULL)){
					if(!player.hittedByCactus()){
						player.cactusHit(collisionCactusSide);
					}
				}
			}
		}
	}
	
	private void checkThereIsNoBoss(){
		Iterator<Mob> iterator = mobs.iterator();
		
		while(iterator.hasNext()){
			if(iterator.next() instanceof Madjnouby){
				iterator.remove();
			}
			if(iterator.next() instanceof Coctus){
				iterator.remove();
			}
			if(iterator.next() instanceof Volcanop){
				iterator.remove();
			}
		}
	}
	
	public List<Boss> getBoss(List<Mob> mobs){
		Iterator<Mob> iterator = mobs.iterator();
		
		List<Boss> bosses = new ArrayList<>();
		
		while(iterator.hasNext()){
			Mob bossCandidate = iterator.next();
			
			if(bossCandidate instanceof Madjnouby && terrain == Terrain.CLASSIC){
				bosses.add((Madjnouby) bossCandidate);
			}
			
			if(bossCandidate instanceof Coctus && terrain == Terrain.DESERT){
				bosses.add((Coctus) bossCandidate);
			}
			
			if(bossCandidate instanceof Volcanop && terrain == Terrain.VOLCAN){
				bosses.add((Volcanop) bossCandidate);
			}
			
			if(bossCandidate instanceof Dragoone && terrain == Terrain.VOLCAN){
				bosses.add((Dragoone) bossCandidate);
			}
			
			if(bossCandidate instanceof Dragone && terrain == Terrain.VOLCAN){
				bosses.add((Dragone) bossCandidate);
			}
		}
		return bosses;
	}
	
	/**
	 * Update method for the boss state
	 */
	private void updateBossState(){
		// updates the player
		if(!timerBossStartGo && !timerBossEndGo){ //  && !timerBossEndGo
			updateDecorations();
			
			player.update(this, mobs);
			
			checkPlayerDecorationsCollision();
			
			// increments the time counter of the state boss
			timer_boss.increment();
			
			updateBosses();
		}
		else{
			checkBossTimerStates();
		}
	}
	
	private void updateBosses(){
		// get the boss and update it
		List<Boss> bosses = getBoss(mobs);
		
		if(bosses.isEmpty()){
			Logs.println("UpdateBossState, boss should not be empty !");
			return;
		}
		
		for(Boss boss : bosses){
			boss.update(this, mobs);
		}
	}
	
	private WorldSave worldSave;
	
	private void checkTimerState(){
		if(gameTimer.getTimePassed() >= gameTimer.getDelay()){
			Logs.println2("Game: SetEndGame from checkTimerState.");
			setEndGame();
			return;
		}
	}
	
	private void checkScoreState(){
		if(player.getScoreObj().getScore() > bestScore){
			bestScore = player.getScoreObj().getScore();
		}
	}
	
	public void setEndGame(){
		if(endGameSet) return;
		Logs.println2("Game: setEndGame, saveScore, endGameSet = " + endGameSet);
		saveScore();
		JukeBox.stopMainMusic();
		removePlayerProjectiles();
		player.setPos(BASE_PLAYER_X, BASE_PLAYER_Y);
		mobs.clear();
		canUpdate = false;
		end = true;
		endGameSet = true;
		Infos.setMenuInfo();
	}
	
	public void setEndEscape(){
		Logs.println2("Game: setEndEscape, saveScore, endGameSet = " + endGameSet);
		//saveScore();
		JukeBox.stopMainMusic();
		removePlayerProjectiles();
		player.setPos(BASE_PLAYER_X, BASE_PLAYER_Y);
		mobs.clear();
		endGameSet = true;
	}
	
	
	public void setEnd(boolean b){
		reset();
		this.end = b;
	}
	
	public void reset(){
		player = new Player(this, BASE_PLAYER_X, BASE_PLAYER_Y);
		this.playerScore = player.getScoreObj();
		allBonus = new ArrayList<>();
		bonusToRemove = new ArrayList<>();
		
		endGameSet = false;
		mobs = new ArrayList<>();
		gameTimer.reset(TIME_GAME_TIMER);
		loadScores();
	}
	
	private void removePlayerProjectiles(){
		player.getProjectiles().clear();
	}
	
	
	private void updateMobs(){
		populateMobs();
		try{
			for(Entity e : mobs){
				e.update(this, mobs);
			}
		} catch(Exception ex){
			
		}
	}
	
	private void populateMobs(){
		switch(Maths.irand(10)){
		case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
			if(timer.getTime() % 40 == 0){
				mobs.add(new Sticker(player));
			}
			break;
		case 8:
			if(timer.getTime() % 40 == 0){
				mobs.add(new Lavalter(player));
			}
			break;
		case 9:
			if(timer.getTime() % 40 == 0){
				mobs.add(new Blazer(player));
			}
			break;
		}
	}
	
	public void stop(){
		gamePanel.setRunning(false);
	}
	
	private Timer restaureTimer;
	
	public boolean restaureLevel(){
		if(!timerBossEndGo){
			mobs = worldSave.mobsSave;
			allBonus = worldSave.allBonusSave;
			state = 1;
			timerBossEndGo = false;
			
			canUpdate = false;
			restaureTimer = new Timer(3);
			return true;
		}
		return false;
	}
	
	private void updateRestaureTimer(){
		if(restaureTimer == null) return;
		restaureTimer.increment();
		if(restaureTimer.getTimeLeft() <= 0){
			canUpdate = true;
			restaureTimer = null;
			gameTimer.launch();
		}
	}
	
	public void render(Graphics2D g){
		if(state == 0){
			menu.render(g, 1);
		}
		else if(state == 1){
			map.render(g);
			renderDecorations(g);
			renderBestScore(g);
			renderTimer(g);
			renderBurningTimer(g);
			renderBonus(g);
			player.renderLives(g);
			player.render(g);
			renderMobsNoBoss(g);
			renderRestaureTimer(g);
			playerScore.render(terrain, g);
			if(end){
				renderEndText(g);
			}
		}
		else if(state == 2){
			menu.render(g, 2);
		}
		else if(state == 3){
			menu.render(g, 3);
		}
		else if(state == Madjnouby.BOSS_STATE){
			renderBossState(g);
		}
		else if(state == TERRAIN_CHOICE_STATE){
			renderTerrainChoiceMenu(g);
		}
	}
	
	class TerrainChoiceMenu{
		
		private class TerrainChoice{
			
			private float SCREEN_X = GamePanel.WIDTH / 2 - 131;
			private float SCREEN_Y = GamePanel.HEIGHT * 50/100;
			
			public TerrainChoice prev;
			public BufferedImage texture;
			
			public TerrainChoice(boolean scale, double amount, TerrainChoice prev, BufferedImage texture){
				this.prev = prev;
				
				if(scale){
					this.texture = Texture.scale(texture, amount);
				}
				else{
					this.texture = texture;
					SCREEN_X = GamePanel.WIDTH / 2 - 50;
				}
			}
			
			
			public void render(Graphics2D g){
//				g.setColor(Color.BLACK);
//				g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
				g.drawImage(texture, (int) SCREEN_X, (int) SCREEN_Y, null);
			}
		}
		
		public byte terrainSelected;
		public BufferedImage terrainTexSelected;
		
		public TerrainChoice classic;
		public TerrainChoice desert;
		public TerrainChoice volcan;
		public TerrainChoice random;
		
		public TerrainChoice currentTerrain;
		
		public TerrainChoiceMenu(){
			terrainSelected = (byte) 0;
			
			classic = new TerrainChoice(true, 0.5, null, Texture.CLASSIC_TERRAIN);
			desert = new TerrainChoice(true, 0.5, classic, Texture.DESERT_TERRAIN);
			volcan = new TerrainChoice(true, 0.5, desert, Texture.VOLCAN_TERRAIN);
			random = new TerrainChoice(false, 0.5, volcan, Texture.RANDOM_TERRAIN);
			
			currentTerrain = classic;
		}
		
		public void update(){
			checkInput();
			checkSelected();
		}
		
		private void checkInput(){
//			if(Keys.isPressed(Keys.LEFT)){
//				terrainSelected--;
//			}
//			if(Keys.isPressed(Keys.RIGHT)){
//				terrainSelected++;
//			}
		}
		
		private void checkSelected(){
			if(terrainSelected < 0){
				terrainSelected = (byte) 0;
			}
			if(terrainSelected > 3){
				terrainSelected = (byte) 3;
			}
			
			if(terrainSelected == (byte) 0){
				currentTerrain = classic;
			}
			else if(terrainSelected == (byte) 1){
				currentTerrain = desert;
			}
			else if(terrainSelected == (byte) 2){
				currentTerrain = volcan;
			}
			else if(terrainSelected == (byte) 3){
				currentTerrain = random;
			}
		}
		
		private Font font = new Font("Times New Roman", Font.PLAIN, 18);
		private Font titleFont = new Font("arial", Font.PLAIN, 18);
		private float SCREEN_X = GamePanel.WIDTH / 2 - 50;
		private float SCREEN_Y = GamePanel.HEIGHT * 38/100;
		
		public void render(Graphics2D g){
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
			
			g.setColor(Color.WHITE);
			
			g.setFont(titleFont);
			g.drawString("Choose your level", (int) GamePanel.WIDTH / 2 - 70, (int) GamePanel.HEIGHT * 15/100);
			
			g.setFont(font);
			switch(terrainSelected){
				case (byte) 0:
					g.drawString(">>    Classic", (int) SCREEN_X, (int) SCREEN_Y);
					break;
				case (byte) 1:
					g.drawString(">>    Desert", (int) SCREEN_X, (int) SCREEN_Y);
					break;
				case (byte) 2:
					g.drawString(">>    Volcano", (int) SCREEN_X, (int) SCREEN_Y);
					break;
				case (byte) 3:
					g.drawString(">>    Random", (int) SCREEN_X, (int) SCREEN_Y);
					break;
			}
			
			currentTerrain.render(g);
		}
	}
	
	public void incChoiceMenu(){
		if(terrainChoiceMenu == null) return;
		terrainChoiceMenu.terrainSelected++;
	}
	
	public void decChoiceMenu(){
		if(terrainChoiceMenu == null) return;
		terrainChoiceMenu.terrainSelected--;
	}
	
	private TerrainChoiceMenu terrainChoiceMenu;
	
	private void renderTerrainChoiceMenu(Graphics2D g){
		if(terrainChoiceMenu == null) return;
		terrainChoiceMenu.render(g);
	}
	
	private void renderBossState(Graphics2D g){
		map.render(g);
		renderDecorations(g);
		renderBestScore(g);
		renderFrozenTimer(g);
		player.renderLives(g);
		renderMobs(g);
		player.render(g);
		playerScore.render(terrain, g);
		
		if(timerBossStartGo){
			renderBossEnter(g);
		}
		
		if(timerBossEndGo){
			renderEndBoss(g);
		}
		
		renderBurningTimer(g);
		
		if(end){
			renderEndText(g);
		}
	}
	
	
	private void renderBossEnter(Graphics2D g){
		g.setColor(Color.BLACK);
		g.setFont(bossEnterFont);
		switch(terrain){
			case CLASSIC:
				g.drawString("Beat Madjounby !", BOSS_ENTER_X, BOSS_ENTER_Y);
				break;
			case DESERT:
				g.drawString("Beat Coctus !", BOSS_ENTER_X, BOSS_ENTER_Y);
				break;
			case VOLCAN:
				g.drawString("Beat Volcanop  !", BOSS_ENTER_X, BOSS_ENTER_Y);
				break;
		}
		g.drawString(timerBossStart.getTimeLeft() + "s", BOSS_ENTER_X + 20, BOSS_ENTER_Y + 15);
	}
	
	private void renderEndBoss(Graphics2D g){
		g.setColor(Color.BLACK);
		g.setFont(bossEnterFont);
		
		switch(terrain){
			case CLASSIC:
				g.drawString("You defeated Madjounby !", BOSS_ENTER_X - 15, BOSS_ENTER_Y);
				break;
			case DESERT:
				g.drawString("You defeated Coctus !", BOSS_ENTER_X - 15, BOSS_ENTER_Y);
				break;
			case VOLCAN:
				g.drawString("You defeated Volcanop !", BOSS_ENTER_X - 15, BOSS_ENTER_Y);
				break;
		}
		
		g.drawString("Get ready !", BOSS_ENTER_X + 20, BOSS_ENTER_Y + 15);
		g.drawString(timerBossEnd.getTimeLeft() + "s", BOSS_ENTER_X + 35, BOSS_ENTER_Y + 30);
	}
	
	private final static int BURN_X = GamePanel.WIDTH * 5 / 100;
	private final static int BURN_Y = GamePanel.HEIGHT * 25 / 100;
	
	private void renderBurningTimer(Graphics2D g){
		Timer burningTimer = player.getBurningTimer();
		if(burningTimer == null || burningTimer.getTimeLeft() == 0) return;
		
		if(terrain == Terrain.VOLCAN){
			g.setColor(Color.WHITE);
		}
		else{
			g.setColor(Color.BLACK);
		}
		
		g.setFont(burningFont);
		g.drawImage(Texture.FIRE, BURN_X, BURN_Y, null);
		g.drawString(burningTimer.getTimeLeft() + "s", BURN_X + 2, BURN_Y + 20);
	}
	
	private void renderRestaureTimer(Graphics2D g){
		if(restaureTimer == null) return;
		if(terrain == Terrain.VOLCAN){
			g.setColor(Color.WHITE);
		}
		else{
			g.setColor(Color.BLACK);
		}
		
		g.setFont(bossEnterFont);
		g.drawString("Get ready !", TileMap.CENTER_X - 15, TileMap.CENTER_Y - 40);
		g.drawString(restaureTimer.getTimeLeft() + "s", TileMap.CENTER_X, TileMap.CENTER_Y - 25);
		
	}
	
	private void renderBestScore(Graphics2D g){
		if(terrain == Terrain.VOLCAN){
			g.setColor(Color.WHITE);
		}
		else{
			g.setColor(Color.BLACK);
		}
		
		g.setFont(bestScoreFont);
		g.drawString("Best Score: " + bestScore, BEST_SCORE_X, BEST_SCORE_Y);
	}
	
	private void renderTimer(Graphics2D g){
		if(terrain == Terrain.VOLCAN){
			g.setColor(Color.WHITE);
		}
		
		g.drawString(gameTimer.getTimeLeft() + "s", TIMER_X, TIMER_Y);
		
		if(restaureTimer != null && restaureTimer.getTimeLeft() > 0){
			g.drawString(" > frozen", TIMER_X + 16, TIMER_Y);
		}
	}
	
	private void renderFrozenTimer(Graphics2D g){
		renderTimer(g);
		g.drawString(" > frozen", TIMER_X + 16, TIMER_Y);
	}
	
	private void renderBonus(Graphics2D g){
		for(Bonus pBonus : allBonus){
			pBonus.render(g);
		}
	}
	
	private void renderMobsNoBoss(Graphics2D g){
		try{
			for(Entity mob : mobs){
				if(mob instanceof Madjnouby) continue;
				if(mob instanceof Coctus) continue;
				if(mob instanceof Volcanop) continue;
				if(mob instanceof Dragoone) continue;
				if(mob instanceof Dragone) continue;
				mob.render(g);
			}
		}
		catch(Exception e){
			
		}
	}
	
	private void renderMobs(Graphics2D g){
		for(Entity mob : mobs){
			if(mob instanceof Dragoone) continue;
			if(mob instanceof Dragone) continue;
			mob.render(g);
		}
	}
	
	private void renderEndText(Graphics2D g){
		if(terrain == Terrain.VOLCAN){
			g.setColor(Color.WHITE);
		}
		String end = "Game over !";
		Font current = g.getFont();
		Font endFont = new Font(current.getFontName(), Font.BOLD, 25);
		g.setFont(endFont);
		g.drawString(end, GamePanel.WIDTH / 2 - 70, GamePanel.HEIGHT / 3 - 20);
		g.setFont(current);
	}
	

	
	public void spawnNewBonus(float x, float y){
		Bonus bonus = Bonus.generateNewBonus(x, y);
		if(bonus != null){
			allBonus.add(bonus);
		}
		else{
			Logs.println2("Bonus null !");
		}
	}
	
	public void addToBonusToRemove(Bonus pBonus){
		bonusToRemove.add(pBonus);
	}
	
	
	public TimeCounter getTimeCounter(){
		return timer;
	}
	
	public GamePanel getGamePanel(){
		return window.getGamePanel();
	}
	
	public int getState(){
		return state;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public List<Bonus> getAllBonus(){
		return allBonus;
	}
	
	public Timer getGameTimer(){
		return gameTimer;
	}
	
	public List<Bonus> getBonusToRemove(){
		return bonusToRemove;
	}
	
	public boolean isEnd(){
		return end;
	}
	
	public void setTimeCounter(TimeCounter tc){
		this.timer = tc;
	}
	
	public TimeCounter getBossTimer(){
		return timer_boss;
	}
	
	private Timer timerBossStart;
	private Timer timerBossEnd;
	
	private boolean timerBossStartGo = false;
	private boolean timerBossEndGo = false;
	
	public boolean isTimerBossStartGoing(){
		return timerBossStartGo;
	}
	
	public boolean isTimerBossEndGoing(){
		return timerBossEndGo;
	}
	
	public void setTimerBossStartGo(boolean timerBossStartGo) {
		this.timerBossStartGo = timerBossStartGo;
	}

	public void setTimerBossEndGo(boolean timerBossEndGo) {
		this.timerBossEndGo = timerBossEndGo;
	}

	public final static byte RANDOM_TERRAIN_FLAG = 127;
	
	public synchronized void setState(int state){
		setState(state, RANDOM_TERRAIN_FLAG);
	}
	
	public synchronized void setState(int state, byte selected){
		if(state == 1){
			if(selected < 3){
				generateMap(selected);
			}
			else{
				generateRandomMap();
			}
			player.setBurning(false);
			gameTimer = new Timer(TIME_GAME_TIMER);
			timer = new TimeCounter();
			JukeBox.loopMainMusic();
		}
		
		if(state == Madjnouby.BOSS_STATE){
			// clear bonuses
			allBonus.clear();
			
			// timer boss
			timerBossStart = new Timer(3);
			timerBossStartGo = true;
			
			// gameTimer
			gameTimer.stop();
			
			// worldSave
			worldSave = new WorldSave(mobs, allBonus);
			
			// mobs
			mobs.clear();
			
			Logs.println2("terrain : " + terrain);
			
			// generate the good boss
			switch(terrain){
				case CLASSIC:
					// Logs.println2("Added madjounby!");
					mobs.add(new Madjnouby(player));
					break;
				case DESERT:
					// Logs.println2("Added coctus!");
					mobs.add(new Coctus(player));
					break;
				case VOLCAN:
					// Logs.println2("Added volcanop !");
					mobs.add(new Volcanop(player));
					// Logs.println2("Added volcanop2 !");
					break;
				default:
					Logs.println2("DEFAULT");
			}

			
			// player
			player.setPos(BASE_PLAYER_X, BASE_PLAYER_Y);
			player.setDirection(Side.TOP);
			player.setTexture(Texture.PLAYER_BACK);
			player.clearProjectiles();
			player.setBurning(false);
			Logs.println2("Player set !");
		}
		
		if(state == TERRAIN_CHOICE_STATE){
			terrainChoiceMenu = new TerrainChoiceMenu();
		}
		
		Logs.println2("Set state : " + state);
		this.state = state;
	}
	
	private void checkBossTimerStates(){
		if(timerBossStartGo){
			timerBossStart.increment();
		}
		if(timerBossEndGo){
			timerBossEnd.increment();
		}
		
		if(timerBossStart.getTimeLeft() == 0){
			timerBossStartGo = false;
		}
		
		if(timerBossEnd == null) return;
		
		if(timerBossEnd.getTimeLeft() == 0){
			timerBossEndGo = false;
		}
	}
	
	public void addToMobs(Mob mob){
		if(!mobs.contains(mob)){
			mobs.add(mob);
		}
	}
	
	public void setCanUpdate(boolean b){
		this.canUpdate = b;
	}
	
	public Menu getMenu(){
		return menu;
	}
	
	public int getCurrentBestScore(){
		return bestScore;
	}
	
	public void setBestScore(int score){
		this.bestScore = score;
	}
	
	public void saveScore(){
		Save.saveScore(player.getScoreObj().getScore());
	}


	public Timer getTimerBossStart() {
		return timerBossStart;
	}


	public void setTimerBossStart(Timer timerBossStart) {
		this.timerBossStart = timerBossStart;
	}


	public Timer getTimerBossEnd() {
		return timerBossEnd;
	}


	public void setTimerBossEnd(Timer timerBossEnd) {
		this.timerBossEnd = timerBossEnd;
	}
	
	public Terrain getTerrain(){
		return terrain;
	}
	
	public List<DecorationEntity> getDecorations(){
		return decorations;
	}
	
	public TerrainChoiceMenu getTerrainChoiceMenu(){
		return terrainChoiceMenu;
	}

}
