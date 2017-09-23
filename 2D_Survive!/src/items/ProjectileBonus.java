package items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import entities.mobs.Mob;
import gameTester.Game;
import graphics.Texture;
import toolbox.Color;
import toolbox.Maths;

public class ProjectileBonus extends Bonus{
	
	private Color color;
	private BufferedImage[] textures = new BufferedImage[4];
	private float speed;
	private boolean canRender = true;
	
	
	public ProjectileBonus(float x, float y){
		super(Texture.PROJECTILE_GREEN_DOWN, x, y);
		setBonus();
	}

	
	private void setBonus(){
		color = getRandomP();
		setSpeed(color);
		setTextures(color);
		setBonusPoints(color);
	}
	
	
	public void setSpeed(Color color){
		switch(color){
		case ORANGE:
			speed = 2f;
			break;
		case YELLOW:
			speed = 6f;
			break;
		case GREEN:
			speed = 8f;
			break;
		case RED:
			speed = 4f;
		}
	}
	
	public static float getSpeed(Color color){
		switch(color){
			case ORANGE:
				return 2f;
			case YELLOW:
				return 6f;
			case GREEN:
				return 8f;
			case RED:
				return 4f;
		}
		return 4f;
	}
	
	
	private void setTextures(Color color){
		switch(color){
		case ORANGE:
			textures[0] = Texture.PROJECTILE_ORANGE_TOP;
			textures[1] = Texture.PROJECTILE_ORANGE_RIGHT;
			textures[2] = Texture.PROJECTILE_ORANGE_DOWN;
			textures[3] = Texture.PROJECTILE_ORANGE_LEFT;
			texture = Texture.PROJECTILE_ORANGE_RIGHT;
			break;
		case YELLOW:
			textures[0] = Texture.PROJECTILE_YELLOW_TOP;
			textures[1] = Texture.PROJECTILE_YELLOW_RIGHT;
			textures[2] = Texture.PROJECTILE_YELLOW_DOWN;
			textures[3] = Texture.PROJECTILE_YELLOW_LEFT;
			texture = Texture.PROJECTILE_YELLOW_RIGHT;
			break;
		case GREEN:
			textures[0] = Texture.PROJECTILE_GREEN_TOP;
			textures[1] = Texture.PROJECTILE_GREEN_RIGHT;
			textures[2] = Texture.PROJECTILE_GREEN_DOWN;
			textures[3] = Texture.PROJECTILE_GREEN_LEFT;
			texture = Texture.PROJECTILE_GREEN_RIGHT;
			break;
		case RED:
			textures[0] = Texture.PROJECTILE_RED_TOP;
			textures[1] = Texture.PROJECTILE_RED_RIGHT;
			textures[2] = Texture.PROJECTILE_RED_DOWN;
			textures[3] = Texture.PROJECTILE_RED_LEFT;
			texture = Texture.PROJECTILE_RED_RIGHT;
		}
	}
	
	
	private void setBonusPoints(Color color){
		switch(color){
		case ORANGE:
			bonusPoints = 100 + Maths.irand(50);
			break;
		case RED:
			bonusPoints = 150 + Maths.irand(50);
			break;
		case YELLOW:
			bonusPoints = 200 + Maths.irand(50);
			break;
		case GREEN:
			bonusPoints = 300 + Maths.irand(50);
		}
	}
	
	
	@Override
	public void update(Game game, List<Mob> mobs) {
	}
	
	
	@Override
	public void render(Graphics2D g) {
		if(canRender){
			g.drawImage(textures[1], (int) x, (int) y, null);
		}
	}
	
	
	private Color getRandomP(){
		switch(Maths.irand(4)){
			case 0:
				return Color.ORANGE;
			case 1:
				return Color.YELLOW;
			case 2:
				return Color.GREEN;
			case 3:
				return Color.RED;
		}
		return null;
	}

	public float getSpeed(){
		return speed;
	}
	
	public BufferedImage[] getTextures(){
		return textures;
	}
	
	
	public Color getColor(){
		return color;
	}


	@Override
	public Bonus clone() {
		ProjectileBonus clone = new ProjectileBonus(x, y);
		clone.bonusPoints = bonusPoints;
		clone.box = box;
		clone.canRender = canRender;
		clone.color = color;
		clone.speed = speed;
		clone.texture = texture;
		clone.textures = textures;
		return clone;
	}
	
	

}
