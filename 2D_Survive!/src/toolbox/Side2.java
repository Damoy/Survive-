package toolbox;

public enum Side2 {
	NULL, TOP_LEFT, BOT_LEFT, TOP_RIGHT, BOT_RIGHT, TOP, LEFT, RIGHT, BOT;
	
	public static Side2 copy(String side){
		return Side2.valueOf(side);
	}
}
