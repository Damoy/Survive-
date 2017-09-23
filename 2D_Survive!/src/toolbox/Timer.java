package toolbox;


public class Timer {

	private long startTime;
	private long nowTime;
	private int delay;
	
	private int timePassed;
	private int timeLeft;
	
	private int secs;
	
	private boolean stopped;
	private long timeStopped;
	private long exactTimeStopped;
	
	/**
	 * Generates a new timer
	 * @param delay the max time of the timer in seconds
	 */
	public Timer(int delay){
		this(delay, true);
	}
	
	public Timer(int delay, boolean init){
		if(init) init();
		this.delay = delay;
		timePassed = 0;
		stopped = false;
		secs = 0;
		
		timeStopped = -1;
		exactTimeStopped = -1;
	}
	
	public Timer(Timer timer){
		if(timer != null){
			this.startTime = timer.startTime;
			this.nowTime = timer.nowTime;
			this.delay = timer.delay;
			this.timePassed = timer.timePassed;
			this.timeLeft = timer.timeLeft;
			this.secs = timer.secs;
			stopped = false;
		}
		else{
			Logs.println2("Timer given was null !");
		}
	}
	
	public void init(){
		if(stopped) return;
		startTime = System.currentTimeMillis();
		nowTime = startTime;
	}
	
	public void add(int secs){
		if(stopped) return;
		this.secs += secs;
	}
	
	public void increment(){
		if(stopped) return;
		if(timePassed >= delay){
			timeLeft = 0;
			return;
		}
		
		if(secs != 0){
			nowTime = System.currentTimeMillis() - secs * 1000;
			timePassed = (int) ((nowTime - startTime) / 1000.0);
			
//			if(timeStopped == -1){
//				timePassed = (int) ((nowTime - startTime) / 1000.0);
//			}
//			else{
//				timePassed = (int) ((nowTime - startTime + timeStopped) / 1000.0);
//			}
			
			timeLeft = delay - timePassed;
		}
		else{
			nowTime = System.currentTimeMillis();
			timePassed = (int) ((nowTime - startTime) / 1000.0);
			
//			if(timeStopped == -1){
//				timePassed = (int) ((nowTime - startTime) / 1000.0);
//			}
//			else{
//				timePassed = (int) ((nowTime - startTime + timeStopped) / 1000.0);
//			}
			
			timeLeft = delay - timePassed;
		}

	}
	
	
	public void reset(int delay){
		// 	if(stopped) return;
		startTime = System.currentTimeMillis();
		nowTime = startTime;
		this.delay = delay;
		timePassed = 0;
		timeStopped = exactTimeStopped = -1;
	}
	
	
	public int getTimeLeft(){
		return timeLeft;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getNowTime() {
		return nowTime;
	}

	public void setNowTime(long nowTime) {
		this.nowTime = nowTime;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getTimePassed() {
		return timePassed;
	}

	public void setTimePassed(int timePassed) {
		this.timePassed = timePassed;
	}

	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}
	
	public boolean isStop(){
		return stopped;
	}
	
	public void launch(){
		if(!stopped) return;
		
		if(timeStopped == -1){
			timeStopped = 0;
		}
		
		if(exactTimeStopped == -1 || exactTimeStopped == 0){
			throw new IllegalStateException("Timer class ; stop() should be used before launch() !");
		}
		
		timeStopped = Math.abs(System.currentTimeMillis() - exactTimeStopped); // +=
		secs += (timeStopped / 1000);
		stopped = false;
	}
	
	
	public void stop(){
		if(stopped) return;
		
		if(exactTimeStopped == -1){
			exactTimeStopped = 0;
		}
		
		exactTimeStopped = System.currentTimeMillis();
		stopped = true;
	}
	
	
	
	
}
