package toolbox;

import java.util.List;
import java.util.Random;



public class Maths {
	
	public static Random random = new Random();
	
	public static int irand(int max){
		return random.nextInt(max);
	}
	
	public static int max(List<Integer> iList){
		if(iList == null) return 0;
		
		int max = iList.get(0);
		for(int i = 1; i < iList.size(); i++){
			if(iList.get(i) > max){
				max = iList.get(i);
			}
		}
		return max;
	}
	
	public static float frand(int low, int max){
		return (float) low + irand(max);
	}
	
	
	public static double drandR(double min, double max) {
	  double range = max - min;
	  double scaled = random.nextDouble() * range;
	  double shifted = scaled + min;
	  return shifted; // == (rand.nextDouble() * (max-min)) + min;
	}
	
	public static float frandR(float min, float max) {
	  float range = max - min;
	  float scaled = random.nextFloat() * range;
	  float shifted = scaled + min;
	  return shifted;
	}
	

}
