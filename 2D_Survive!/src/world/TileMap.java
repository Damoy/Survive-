package world;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


import gameTester.GamePanel;
import graphics.Texture;
import toolbox.Logs;

public class TileMap {

	// map indications positions
	public final static float CENTER_X = GamePanel.WIDTH / 2 - Texture.CENTER.getWidth() / 2;
	public final static float CENTER_Y = GamePanel.HEIGHT / 2 - Texture.CENTER.getHeight() / 2;
	
	private final static int SL_W = Texture.SPAWN_IND_DOWN.getWidth() / 2;
	private final static int SL_H = Texture.SPAWN_IND_DOWN.getHeight() / 2;
	
	public final static float SPAWN_LEFT_X = SL_W;
	public final static float SPAWN_LEFT_Y = GamePanel.HEIGHT / 2 - SL_H;
	
	public final static float SPAWN_DOWN_X = GamePanel.WIDTH / 2 - SL_W;
	public final static float SPAWN_DOWN_Y = GamePanel.HEIGHT - (SL_H * 3);
	
	public final static float SPAWN_RIGHT_X = GamePanel.WIDTH - (SL_W * 3);
	public final static float SPAWN_RIGHT_Y = GamePanel.HEIGHT / 2 - 8f;
	
	public final static float SPAWN_TOP_X = SPAWN_DOWN_X;
	public final static float SPAWN_TOP_Y = SL_H;
	
	private int tileSize;
	private int numRows;
	private int numCols;
	
	private BufferedImage tileset;
	private Tile[][] tiles;
	
	public TileMap(int tileSize){
		numRows = GamePanel.HEIGHT / tileSize + 2;
		numCols = GamePanel.WIDTH / tileSize + 2;
		this.tileSize = tileSize;
		tiles = new Tile[numRows][numCols];
	}
	
	public Tile[][] loadAllSameTiles(int type, String path) {
		if(type != 1 && type != 2){
			throw new IllegalArgumentException("LoadTiles type should be in [1:2] !");
		}
		
		try {
			tileset = ImageIO.read(new File(path));
			BufferedImage tileImage;
			
			for(int row = 0; row < numRows; row++){
				for(int col = 0; col < numCols; col++){
					tileImage = tileset.getSubimage(16 * (type - 1), 0, 16, 16);
					tiles[row][col] = new Tile(tileImage, Tile.NORMAL);
				}
			}
			
			Logs.println("All tiles texture loaded");
			return tiles;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public void render(Graphics2D g) {
		for(int row = 0; row < numRows; row++) {
			for(int col = 0; col < numCols; col++) {
				
				BufferedImage ti = tiles[row][col].getImage();
				
				int xTile = col * tileSize;
				int yTile = row * tileSize;
				
				g.drawImage(ti, xTile, yTile, null);
			}
		}
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}

	public int getNumRows() {
		return numRows;
	}

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	public int getNumCols() {
		return numCols;
	}

	public void setNumCols(int numCols) {
		this.numCols = numCols;
	}
	
	
	
	
	
	
}
