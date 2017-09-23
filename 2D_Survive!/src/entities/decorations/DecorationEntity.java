package entities.decorations;

import java.awt.image.BufferedImage;

import entities.Entity;


public abstract class DecorationEntity extends Entity{

	public DecorationEntity(BufferedImage texture, float initX, float initY) {
		super(texture, initX, initY);
	}


}
