package toolbox;

public class Infos {

	private final static String BOSS = "Boss inside !";
	private final static String KILL_MOBS = "Kill the mobs !";
	private final static String MAD = "Madjounby !";
	private final static String LAVA = "Care to lava !";
	private final static String MAGIC = "Yes, it's Magic !";
	private final static String VOLCANOP = "Volcanop !";
	private final static String COCTUS = "Coctus !";
	private final static String DRAGONS = "Is it a dragon ?";
	
	private final static String[] MENU_INFOS = new String[]{
			BOSS, KILL_MOBS, MAD, LAVA, MAGIC, COCTUS, VOLCANOP, DRAGONS
	};

	
	public static String MENU_INFO = generateMenuInfo();
	
	public final static void setMenuInfo(){
		MENU_INFO = generateMenuInfo();
	}
	
	public final static String generateMenuInfo(){
		return MENU_INFOS[Maths.irand(MENU_INFOS.length)];
	}
	
}
