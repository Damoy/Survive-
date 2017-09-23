package toolbox;

public enum Side {
	NULL, TOP, DOWN, LEFT, RIGHT;


	public static Side getRandomSide(){
		switch(Maths.irand(4)){
			case 0:
				return Side.LEFT;
			case 1:
				return Side.RIGHT;
			case 2:
				return Side.TOP;
			case 3:
				return Side.DOWN;
		}
		return Side.DOWN;
	}
	
	public static Side getRandomLRSide(){
		switch(Maths.irand(2)){
		case 0:
			return Side.LEFT;
		case 1:
			return Side.RIGHT;
		}
		return Side.RIGHT;
	}
	
	public static Side copySide(String side){
		return Side.valueOf(side);
	}

}


