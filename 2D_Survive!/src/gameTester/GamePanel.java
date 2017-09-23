package gameTester;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


import audio.JukeBox;
import entities.mobs.boss.Madjnouby;
import entities.projectiles.Projectile;
import graphics.Window;
import gui.Menu;
import input.Keys;
import toolbox.Infos;
import toolbox.Logs;
import toolbox.TimeCounter;
import toolbox.Timer;



@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener{
	
	// dimensions
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 2;
	
	private static final int S_WIDTH = WIDTH * SCALE;
	private static final int S_HEIGHT = HEIGHT * SCALE;
	
	// game thread
	private Thread thread;
	private boolean running;
	// private int FPS = 60;
	
	// image
	private BufferedImage image;
	private Graphics2D g;
	
	// the window where to game panel component has been
	// added in
	private Window window;
	
	private Game game;
	private TimeCounter timeCounter;
	private Timer gameTimer;
	private Menu menu;
	

	public GamePanel(Game game, Window window){
		super();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		//setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		init(game, window);
	}
	
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	
	private void init(Game game, Window window) {
		this.window = window;
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		
		running = true;
		this.game = game;
		menu = game.getMenu();
	}
	
	public void setTimeCounter(TimeCounter tc){
		this.timeCounter = tc;
	}
	
	public void setGameTimer(Timer gameTimer){
		this.gameTimer = gameTimer;
	}
	
	
	public void run() {
		long lastTime = System.nanoTime();
		double unprocessed = 0;
		double nsPerTick = 1000000000.0 / 60;
		int frames = 0;
		int ticks = 0;
		long lastTimer1 = System.currentTimeMillis();


		while (running) {
			if(timeCounter != null){
				timeCounter.increment();
			}
			
			if(gameTimer != null){
				gameTimer.increment();
			}
			
			long now = System.nanoTime();
			unprocessed += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;
			while (unprocessed >= 1) {
				ticks++;
				update();
				unprocessed -= 1;
				shouldRender = true;
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (shouldRender) {
				frames++;
				render();
				renderToScreen();
			}

			if (System.currentTimeMillis() - lastTimer1 > 1000) {
				lastTimer1 += 1000;
				window.setTitle(frames + " fps");
				Logs.println2(ticks + " ticks");
				frames = 0;
				ticks = 0;
			}
			
			if(game.isEnd()){
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				game.setState(0);
				game.setCanUpdate(true);
				game.setEnd(false);
			}
		}
	}
	
	private void update() {
		game.update();
	}
	
	private void render(){
		game.render(g);
	}
	
	private void renderToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, S_WIDTH, S_HEIGHT, null);
		g2.dispose();
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	
	private void playerShootSpaceEvent(){
		if(game.isEnd()) return;
		
		// shoot input
		if((game.getState() == 1 || game.getState() == Madjnouby.BOSS_STATE)){
			
			if(game.getState() == Madjnouby.BOSS_STATE &&
					(game.isTimerBossStartGoing() || game.isTimerBossEndGoing())){
				return;
			}
			JukeBox.play("playerShoot");
			Projectile projectile1 = new Projectile(game.getPlayer());
			game.getPlayer().getProjectiles().add(projectile1);
			
			if(Game.DOUBLE_GUN_STATE){
				Projectile projectile2 = Projectile.generateNewProjectile(game.getPlayer(), projectile1);
				game.getPlayer().getProjectiles().add(projectile2);
			}
			
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int buttonSelected = menu.getButtonSelected();
		
		if(game.getState() == Game.TERRAIN_CHOICE_STATE){
			if(e.getKeyCode() == KeyEvent.VK_LEFT){
				JukeBox.playMenuOption();
				game.decChoiceMenu();
			}
			else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
				JukeBox.playMenuOption();
				game.incChoiceMenu();
			}
			else if(e.getKeyCode() == KeyEvent.VK_ENTER){
				JukeBox.playMenuSelection();
				byte selected = game.getTerrainChoiceMenu().terrainSelected;
				// Logs.println3(selected + "");
				game.setState(1, selected);
				setTimeCounter(game.getTimeCounter());
				setGameTimer(game.getGameTimer());
				
			}
			else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
				JukeBox.playMenuSelection();
				game.setState(0);
			}
			
			return;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			playerShootSpaceEvent();
		}
		
		if(game.getState() == 0){
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				JukeBox.playMenuSelection();
				
				if(buttonSelected == 0){
					game.setState(Game.TERRAIN_CHOICE_STATE);
				}
				
				if(buttonSelected == 1){
					menu.loadScores();
					game.setState(3); // scores
				}
				
				if(buttonSelected == 2){
					game.setState(2); // about
				}
				
				if(buttonSelected == 3){
					System.exit(0);
				}
			}
			
			if(buttonSelected == 0){
				if(e.getKeyCode() == KeyEvent.VK_DOWN ||
						e.getKeyCode() == KeyEvent.VK_S){
					JukeBox.playMenuOption();
					menu.incButtonSelected();
				}
			}
			
			else if(buttonSelected == 1 || buttonSelected == 2){
				if(e.getKeyCode() == KeyEvent.VK_Z|| e.getKeyCode() == KeyEvent.VK_W
						|| e.getKeyCode() == KeyEvent.VK_UP){
					JukeBox.playMenuOption();
					menu.decButtonSelected();
				}
				
				if(e.getKeyCode() == KeyEvent.VK_DOWN ||
						e.getKeyCode() == KeyEvent.VK_S){
					JukeBox.playMenuOption();
					menu.incButtonSelected();
				}
			}
			
			else if(buttonSelected == 3){
				if(e.getKeyCode() == KeyEvent.VK_Z|| e.getKeyCode() == KeyEvent.VK_W
						|| e.getKeyCode() == KeyEvent.VK_UP){
					JukeBox.playMenuOption();
					menu.decButtonSelected();
				}
			}
		}
		
		else if(game.getState() == 2 || game.getState() == 3){
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				JukeBox.playMenuSelection();
				game.setState(0);
			}
		}
		
		
		
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			if(game.getState() == 0){
				System.exit(0);
			}
			else if(game.getState() == 1){
				JukeBox.playMenuSelection();
				game.setEndEscape();
				game.reset();
				game.setState(0);
				Infos.setMenuInfo();
			}
			
			else if(game.getState() == 2 || game.getState() == 3){
				JukeBox.playMenuSelection();
				game.setState(0);
			}
			
			return;
		}
		
		
		
		if(game.getState() != 0){
			Keys.keySet(e.getKeyCode(), true);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER){
			return;
		}
		if(game.getState() != 0){
			Keys.keySet(e.getKeyCode(), false);
		}
	}
	
	public TimeCounter getTimeCounter(){
		return timeCounter;
	}
	
	public void setRunning(boolean run){
		this.running = run;
	}
	
}