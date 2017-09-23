package toolbox;


public enum Color {
	ORANGE, YELLOW, GREEN, RED;
	
	public static Color copyColor(String color){
		return Color.valueOf(color); 
	}
}
