package physics;

import entities.Entity;

/**
 * The axis align bounding box,
 * used to detect collision between entities
 */
public class AABB {

    // the position
    private float x, y;
    // the sizes
    private int width, height;


    public AABB(Entity entity){
    	this(entity.getX(), entity.getY(),
    			entity.getTexture().getWidth(), entity.getTexture().getHeight());
    }

    /**
     * Generates a new AABB with the position
     * and the sizes given
     * @param x,y the position
     * @param height,width the sizes
     */
    public AABB(float x, float y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }


    public AABB(){
        this(0, 0, 0, 0);
    }



    /**
     * Set the box position only
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void update(float x, float y){
    	updateX(x);
    	updateY(y);
    }
    
    public void updateX(float x){
    	this.x = x;
    }
    
    public void updateY(float y){
    	this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    public String toString(){
    	return "[x:" + x + ",y:" + y + ",w:" + width + ",h:" + height + "]";
    }
}
