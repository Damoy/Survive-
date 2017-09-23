package entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import entities.mobs.Mob;
import gameTester.Game;
import gameTester.GamePanel;
import physics.AABB;

public abstract class Entity {

	protected BufferedImage texture;
	
	protected float x;
	protected float y;
	
	protected AABB box;
	
	public Entity(BufferedImage texture, float initX, float initY){
		this.texture = texture;
		this.x = initX;
		this.y = initY;
		try{
			this.box = new AABB(this);
		}
		catch(Exception e){
			
		}
	}
	
	public abstract void update(Game game, List<Mob> mobs);
	public abstract void render(Graphics2D g);

	
	public BufferedImage getTexture() {
		return texture;
	}

	public void setTexture(BufferedImage texture) {
		this.texture = texture;
	}
	
	public AABB getBox(){
		return box;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public void setPos(float x, float y){
		setX(x);
		setY(y);
	}
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Entity)){
			return false;
		}
		Entity e = (Entity) o;
		return (e.getX() == x && e.getY() == y);
	}
	
	public boolean isInScreen(){
		return !(x <= 0 || x >= GamePanel.WIDTH || y <= 0 || y >= GamePanel.HEIGHT);
	}
	
	public static void updateBoxPos(Entity boxToUpdate, float x, float y){
		boxToUpdate.getBox().update(x, y);
	}
	
	public static void updateBoxPosX(Entity boxToUpdate, float x){
		boxToUpdate.getBox().updateX(x);
	}
	
	public static void updateBoxPosY(Entity boxToUpdate, float y){
		boxToUpdate.getBox().updateY(y);
	}
	
	
}
