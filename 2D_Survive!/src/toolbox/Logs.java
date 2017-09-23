package toolbox;

import java.util.List;

public class Logs {

	public static void println(@SuppressWarnings("unused") String s){
		//System.out.println(s);
	}
	
	public static void println2(String s){
		// System.out.println(s);
	}
	
	
	public static void print(String s){
		System.out.print(s);
	}
	
	public static void printList(List<String> list){
		for(String s : list){
			println2(s);
		}
	}
	
	public static void printIList(List<Integer> list){
		for(Integer i : list){
			println2(i.toString());
		}
	}
}
