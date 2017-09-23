package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import toolbox.Logs;
import toolbox.Terrain;

/**
 * @author Damoy
 */
public class Texture {
	
	public static final BufferedImage NULL = getPTexture(255, 255, 1, 1);
	
	public static final BufferedImage LONG_CARRE = getPTexture(160, 85, 60, 7);
	public static final BufferedImage HAUT_CARRE = getPTexture(160, 95, 7, 60);

	// terrain
	public static final BufferedImage GREEN_TERRAIN = getTexture(16, 0, 0);
	public static final BufferedImage GREEN_TERRAIN2 = getTexture(16, 0, 2);
	public static final BufferedImage GREEN_TERRAIN3 = getTexture(16, 0, 3);
	public static final BufferedImage GREEN_TERRAIN4 = getTexture(16, 0, 4);
	
	public static final BufferedImage SAND_TERRAIN = getTexture(16, 13, 0);
	public static final BufferedImage SAND_TERRAIN2 = getTexture(16, 13, 1);
	public static final BufferedImage SAND_TERRAIN3 = getTexture(16, 13, 2);
	public static final BufferedImage SAND_TERRAIN4 = getTexture(16, 13, 3);
	
	public static final BufferedImage GLASS_TERRAIN = getTexture(16, 13, 6);
	public static final BufferedImage GLASS_TERRAIN2 = getTexture(16, 13, 7);
	public static final BufferedImage GLASS_TERRAIN3 = getTexture(16, 13, 8);
	public static final BufferedImage GLASS_TERRAIN4 = getTexture(16, 13, 9);
	
	public static final BufferedImage GRAY_TERRAIN = getTexture(16, 0, 1);
	public static final BufferedImage LAVA_TERRAIN = getTexture(16, 14, 0);

	
	// player
	public static BufferedImage PLAYER_BACK = getTexture(16, 2, 7);
	public static BufferedImage PLAYER_BACK2 = getTexture(16, 2, 8);
	
	public static BufferedImage PLAYER_LEFT = getTexture(16, 2, 10);
	public static BufferedImage PLAYER_LEFT_STAND = getTexture(16, 2, 13);
	public static BufferedImage PLAYER_LEFT2 = getTexture(16, 2, 14);
	
	public static BufferedImage PLAYER_RIGHT = getTexture(16, 2, 9);
	public static BufferedImage PLAYER_RIGHT_STAND = getTexture(16, 2, 11);
	public static BufferedImage PLAYER_RIGHT2 = getTexture(16, 2, 12);
	
	public static BufferedImage PLAYER_FRONT = getTexture(16, 2, 5);
	public static BufferedImage PLAYER_FRONT2 = getTexture(16, 2, 6);
	
	// mobs
	// sticker
	public static final BufferedImage STICKER_RIGHT = getTexture(16, 2, 0);
	public static final BufferedImage STICKER_NORMAL = getTexture(16, 2, 1);
	public static final BufferedImage STICKER_LEFT = getTexture(16, 2, 2);
	// lavalter
	public static final BufferedImage LAVALTER_RIGHT = getTexture(16, 2, 3);
	public static final BufferedImage LAVALTER_LEFT = getTexture(16, 2, 4);
	// blazer
	public static final BufferedImage BLAZER_RIGHT = getTexture(16, 1, 4);
	public static final BufferedImage BLAZER_LEFT = getTexture(16, 1, 5);
	// boss
	public static final BufferedImage BOSS_LOOK_LEFT = getPTexture(98, 2, 30, 29);
	public static final BufferedImage BOSS_LOOK_FRONT = getPTexture(130, 2, 30, 29);
	//public static final BufferedImage BOSS_LOOK_HIT = getPTexture(162, 2, 30, 29);
	public static final BufferedImage BOSS_LOOK_HIT = getPTexture(225, 2, 30, 29);
	public static final BufferedImage BOSS_LOOK_RIGHT = getPTexture(194, 2, 30, 29);
	
	// boss life gui
	public static final BufferedImage BOSS_LIFE_GUI_GREEN_LEFT = getTexture(16, 4, 6);
	public static final BufferedImage BOSS_LIFE_GUI_GREEN_MID = getTexture(16, 4, 7);
	public static final BufferedImage BOSS_LIFE_GUI_GREEN_RIGHT = getTexture(16, 4, 8);
	
	public static final BufferedImage BOSS_LIFE_GUI_RED_LEFT = getTexture(16, 5, 6);
	public static final BufferedImage BOSS_LIFE_GUI_RED_MID = getTexture(16, 5, 7);
	public static final BufferedImage BOSS_LIFE_GUI_RED_RIGHT = getTexture(16, 5, 8);
	
	// COCTUS
	public final static BufferedImage COCTUS_BOSS = getPTexture(160, 48, 30, 32);
	public final static BufferedImage COCTUS_BOSS_HIT = getPTexture(193, 48, 30, 32);
	
	// coctus projectile
	public final static BufferedImage COCTUS_PROJECTILE = getPTexture(162, 116, 10, 10);
	
	// VOLCANO
	public final static BufferedImage VOLCANO_BOSS_FULL = getP2Texture(2, 2, 49, 58);
	public final static BufferedImage VOLCANO_BOSS_ALONE = getP2Texture(102, 2, 49, 58);
	public final static BufferedImage VOLCANO_BOSS_DRAGONE = getP2Texture(52, 2, 49, 58);
	public final static BufferedImage VOLCANO_BOSS_DRAGOONE = getP2Texture(153, 2, 49, 58);
	
	public final static BufferedImage VOLCANO_BOSS_FULL_HIT = getP2Texture(52, 211, 49, 58);
	public final static BufferedImage VOLCANO_BOSS_ALONE_HIT = getP2Texture(101, 211, 49, 58);
	public final static BufferedImage VOLCANO_BOSS_DRAGONE_HIT = getP2Texture(2, 211, 49, 58);
	public final static BufferedImage VOLCANO_BOSS_DRAGOONE_HIT = getP2Texture(151, 211, 49, 58);
	
	// DRAGONS
	public final static BufferedImage DRAGOONE_RIGHT = getP2Texture(0, 64, 22, 27);
	public final static BufferedImage DRAGOONE_LEFT = getP2Texture(77, 64, 22, 27);
	public final static BufferedImage DRAGOONE_RIGHT_HIT = getP2Texture(20, 92, 22, 27);
	public final static BufferedImage DRAGOONE_LEFT_HIT = getP2Texture(98, 92, 22, 27);
	
	
	public final static BufferedImage DRAGONE_RIGHT = getP2Texture(50, 64, 22, 27);
	public final static BufferedImage DRAGONE_LEFT = getP2Texture(27, 64, 22, 27);
	public final static BufferedImage DRAGONE_RIGHT_HIT = getP2Texture(71, 92, 22, 27);
	public final static BufferedImage DRAGONE_LEFT_HIT = getP2Texture(47, 92, 22, 27);
	
	public final static BufferedImage DRAGOONE_BOMB = getP2Texture(5, 98, 5, 5);
	public final static BufferedImage DRAGONE_BOMB = getP2Texture(11, 98, 5, 5);
	public final static BufferedImage DRAGOONE_BOMB_RED = getP2Texture(5, 104, 5, 5);
	public final static BufferedImage DRAGONE_BOMB_RED = getP2Texture(11, 104, 5, 5);
	
	public final static BufferedImage VOLCANO_PROJECTILE = getP2Texture(2, 113, 13, 13);
	
	
	// projectiles
	public static final BufferedImage PROJECTILE_ORANGE_TOP = getTexture(16, 3, 0);
	public static final BufferedImage PROJECTILE_ORANGE_RIGHT = getTexture(16, 3, 1);
	public static final BufferedImage PROJECTILE_ORANGE_DOWN = getTexture(16, 3, 2);
	public static final BufferedImage PROJECTILE_ORANGE_LEFT = getTexture(16, 3, 3);
	
	public static final BufferedImage PROJECTILE_RED_TOP = getTexture(16, 4, 0);
	public static final BufferedImage PROJECTILE_RED_RIGHT = getTexture(16, 4, 1);
	public static final BufferedImage PROJECTILE_RED_DOWN = getTexture(16, 4, 2);
	public static final BufferedImage PROJECTILE_RED_LEFT = getTexture(16, 4, 3);
	
	public static final BufferedImage PROJECTILE_YELLOW_TOP = getTexture(16, 5, 0);
	public static final BufferedImage PROJECTILE_YELLOW_RIGHT = getTexture(16, 5, 1);
	public static final BufferedImage PROJECTILE_YELLOW_DOWN = getTexture(16, 5, 2);
	public static final BufferedImage PROJECTILE_YELLOW_LEFT = getTexture(16, 5, 3);
	
	public static final BufferedImage PROJECTILE_GREEN_TOP = getTexture(16, 6, 0);
	public static final BufferedImage PROJECTILE_GREEN_RIGHT = getTexture(16, 6, 1);
	public static final BufferedImage PROJECTILE_GREEN_DOWN = getTexture(16, 6, 2);
	public static final BufferedImage PROJECTILE_GREEN_LEFT = getTexture(16, 6, 3);
	
	public static final BufferedImage PROJECTILE_BOSS_TOP = getPTexture(101, 51, 6, 9);
	public static final BufferedImage PROJECTILE_BOSS_RIGHT = getPTexture(116, 53, 9, 6);
	public static final BufferedImage PROJECTILE_BOSS_DOWN = getPTexture(134, 51, 6, 9);
	public static final BufferedImage PROJECTILE_BOSS_LEFT = getPTexture(148, 53, 9, 6);
	
	// map indications
	public static final BufferedImage CENTER = getTexture(16, 7, 0);
	public static final BufferedImage SPAWN_IND_RIGHT = getTexture(16, 7, 3);
	public static final BufferedImage SPAWN_IND_DOWN = getTexture(16, 7, 4);
	public static final BufferedImage SPAWN_IND_LEFT = getTexture(16, 7, 5);
	public static final BufferedImage SPAWN_IND_TOP = getTexture(16, 7, 6);
	
	public static final BufferedImage SAND_FLOWER = getPTexture(65, 209, 17, 15);
	public static final BufferedImage LAVA = getPTexture(96, 224, 15, 15);
	public static final BufferedImage FIRE = getPTexture(82, 98, 11, 11);
	public static final BufferedImage CACTUS = getPTexture(96, 96, 17, 16);
	
	// heart
	public static final BufferedImage LIFE = getTexture(16, 7, 1);
	
	// bonus
	public static final BufferedImage X2 = getTexture(16, 8, 0);
	public static final BufferedImage SMALL_LIFE = getTexture(16, 7, 2);
	public static final BufferedImage DOUBLE_GUN = getTexture(16, 8, 2);
	public static final BufferedImage SPEED_BOOST = getTexture(16, 8, 3);
	public static final BufferedImage TIME_BONUS = getTexture(16, 8, 4);
	public static final BufferedImage DROP_UP = getTexture(16, 8, 5); // TODO or getTexture(16, 8, 1);
	public static final BufferedImage BOSS_ENTER = getPTexture(130, 130, 15, 12);
	
	// game title
	public static final BufferedImage GAME_TITLE = scale2(getTitleTexture(), 2, 2);
	
	// terrains
	public static final BufferedImage CLASSIC_TERRAIN = getTerrain(Terrain.CLASSIC);
	public static final BufferedImage DESERT_TERRAIN = getTerrain(Terrain.DESERT);
	public static final BufferedImage VOLCAN_TERRAIN = getTerrain(Terrain.VOLCAN);
	public static final BufferedImage RANDOM_TERRAIN = getRandomTerrainImage();
	
	
	private static BufferedImage getRandomTerrainImage(){
		try {
			return ImageIO.read(new FileInputStream("./resources/textures/question_mark.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static BufferedImage getTerrain(Terrain terrain){
		BufferedImage image;
		
		switch(terrain){
			case CLASSIC:
				try {
					image = ImageIO.read(new FileInputStream("./resources/textures/classic_terrain.png"));
					return image;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case DESERT:
				try {
					image = ImageIO.read(new FileInputStream("./resources/textures/desert_terrain.png"));
					return image;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
				}
				break;
			case VOLCAN:
				try {
					image = ImageIO.read(new FileInputStream("./resources/textures/volcano_terrain.png"));
					return image;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
				}
				break;
		}
		return null;
	}

	public static BufferedImage getTexture(int width, int tileSetRow, int tileSetCol){
		return getTexture(width, width, tileSetRow, tileSetCol);
	}
	
	protected static BufferedImage getTexture(int width, int height, int tileSetRow, int tileSetCol){
		BufferedImage tileImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		try {
			//tileImage = ImageIO.read(new FileInputStream("resources/textures/res_data.png"));
			tileImage = ImageIO.read(new FileInputStream("./resources/textures/res_data.png"));
			tileImage = tileImage.getSubimage(width * tileSetCol, height * tileSetRow,
					width, height);
			
			int w = tileImage.getWidth();
			int h = tileImage.getHeight();
			
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
			         int argb = tileImage.getRGB(x, y);
			         if (argb == Color.MAGENTA.getRGB()){
			              tileImage.setRGB(x, y, 0);
			         }
			    }
			}
			return tileImage;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static BufferedImage getPTexture(int xp, int yp, int width, int height){
		BufferedImage tileImage;
		try {
			// tileImage = ImageIO.read(Texture.class.getResourceAsStream("/textures/res_data.png"));
			tileImage = ImageIO.read(new FileInputStream("./resources/textures/res_data.png"));
			tileImage = tileImage.getSubimage(xp, yp, width, height);
			
			int w = tileImage.getWidth();
			int h = tileImage.getHeight();
			
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
			         int argb = tileImage.getRGB(x, y);
			         if (argb == Color.MAGENTA.getRGB()){
			              tileImage.setRGB(x, y, 0);
			         }
			    }
			}
			return tileImage;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static BufferedImage getP2Texture(int xp, int yp, int width, int height){
		BufferedImage tileImage;
		try {
			// tileImage = ImageIO.read(Texture.class.getResourceAsStream("/textures/volcano_data.png"));
			tileImage = ImageIO.read(new FileInputStream("./resources/textures/volcano_data.png"));
			tileImage = tileImage.getSubimage(xp, yp, width, height);
			
			int w = tileImage.getWidth();
			int h = tileImage.getHeight();
			
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
			         int argb = tileImage.getRGB(x, y);
			         if (argb == Color.MAGENTA.getRGB()){
			              tileImage.setRGB(x, y, 0);
			         }
			    }
			}
			return tileImage;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static BufferedImage getTitleTexture(){
		BufferedImage tileImage;
		try {
			//tileImage = ImageIO.read(new FileInputStream("resources/textures/res_data.png"));
			tileImage = ImageIO.read(new FileInputStream("./resources/textures/res_data.png"));
			tileImage = tileImage.getSubimage(0, 9 * 16,
					8 * 16, 19);
			
			int w = tileImage.getWidth();
			int h = tileImage.getHeight();
			
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
			         int argb = tileImage.getRGB(x, y);
			         if (argb == Color.MAGENTA.getRGB()){
			              tileImage.setRGB(x, y, 0);
			         }
			    }
			}
			return tileImage;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int getAlpha(int argb){
		return (argb >> 24) & 0xFF;
	}
	
	public static int getRed(int argb){
		return (argb >> 16) & 0xFF;
	}
	
	public static int getGreen(int argb){
		return (argb >> 8) & 0xFF;
	}
	
	public static int getBlue(int argb){
		return (argb >> 0) & 0xFF;
	}
	
	public static int getColor(int a, int r, int g, int b){
		return (a << 24) | (r << 16) | (g << 8) | b;
	}
	
	
	public static void turnRed(BufferedImage texture){
		for(int x = 0; x < texture.getWidth(); x++){
			for(int y = 0; y < texture.getHeight(); y++){
				
				// get argb
				int argb = texture.getRGB(x, y);
				
				// if not transparent
				if(!(getAlpha(argb) == 0)){
					int red = getRed(argb);
					
					red += RED_COMPONENT_MODIFICATION;
					if(red > 255){
						red = 255;
					}
					
					texture.setRGB(x, y, getColor(getAlpha(argb), red, getGreen(argb), getBlue(argb)));
					// Logs.println2("Set more red !");
				}
				else{
					texture.setRGB(x, y, argb);
				}
			}
		}
	}
	
	private static final int RED_COMPONENT_MODIFICATION = 50;
	
	public static void undoRed(BufferedImage texture){
		for(int x = 0; x < texture.getWidth(); x++){
			for(int y = 0; y < texture.getHeight(); y++){
				int argb = texture.getRGB(x, y);
				
				// if not transparent
				if(!(getAlpha(argb) == 0)){
					int red = getRed(argb);
					
					red -= RED_COMPONENT_MODIFICATION;
					if(red < 0){
						red = 0;
					}
					
					texture.setRGB(x, y, getColor(getAlpha(argb), red, getGreen(argb), getBlue(argb)));
				}
			}
		}
	}
	
	public static void turnRedd(BufferedImage texture){
		Logs.println2("texture width : " + texture.getWidth());
		Logs.println2("texture height : " + texture.getHeight());
		
		for(int x = 0; x < texture.getWidth(); x++){
			for(int y = 0; y < texture.getHeight(); y++){
				texture.setRGB(x, y, Color.WHITE.getRGB());
			}
		}
	}
	
	
	public static BufferedImage scale2(BufferedImage toScale, int widthFactor, int heightFactor){
		int newWidth = new Double(toScale.getWidth() * widthFactor).intValue();
		int newHeight = new Double(toScale.getHeight() * heightFactor).intValue();
		
		BufferedImage resized = new BufferedImage(newWidth, newHeight, toScale.getType());
		Graphics2D g = resized.createGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		
		g.drawImage(toScale, 0, 0, newWidth, newHeight, 0, 0, toScale.getWidth(),
			toScale.getHeight(), null);
		g.dispose();
		return resized;
	}
	
	public static BufferedImage scale(BufferedImage toScale, double scale){
		int w = toScale.getWidth();
		int h = toScale.getHeight();
		BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(scale, scale);
		AffineTransformOp scaleOp = 
		   new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		after = scaleOp.filter(toScale, after);
		return after;
	}
	
	public static BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
	public static BufferedImage copy(BufferedImage source){
	    BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
	    Graphics g = b.getGraphics();
	    g.drawImage(source, 0, 0, null);
	    g.dispose();
	    return b;
	}

}
