package toolbox;


public class TimeCounter {

	private int time;
	
	public TimeCounter(){
		time = 0;
	}
	
	public void increment(){
		try{
			time++;
		}
		catch(Exception e){
			// time > max int value ?
		}
	}
	
	public int getTime(){
		return time;
	}
}
