package toolbox;import java.util.List;

import entities.mobs.Mob;
import items.Bonus;

public class WorldSave {
	
	//public Player playerSave;
	public List<Mob> mobsSave;
	public List<Bonus> allBonusSave;
	// public Timer gameTimerSave;
	
	//public WorldSave(List<Mob> mobsSave, List<Bonus> allBonusSave, Timer gameTimerSave){
		public WorldSave(List<Mob> mobsSave, List<Bonus> allBonusSave){
		this.mobsSave = Mob.cloneList(mobsSave);
		this.allBonusSave = Bonus.cloneList(allBonusSave);
		
		// Logs.println2("timer null ? : " + gameTimerSave);
		// this.gameTimerSave = new Timer(gameTimerSave);
	}

}
