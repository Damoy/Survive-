package toolbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class Save {
	
	
	public static void saveScore(int score){
		List<Integer> scores = SListToI(loadScores());
		scores.add(score);

		FileWriter fw;
		try {
			fw = new FileWriter(new File("./data/scores.txt"), false);
			PrintWriter pw = new PrintWriter(fw);
			writeIListInFile(scores, pw);
			pw.close();
			Logs.println2("Scores saved !");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void writeIListInFile(List<Integer> ints, PrintWriter pw){
		for(Integer i : ints){
			pw.println(i.toString());
		}
	}
	
	private static FileReader getScoresFileReader(){
		try {
			FileReader fr = new FileReader("./data/scores.txt");
			return fr;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Loading the scores
	 * @return all lines of the score file
	 */
	public static ArrayList<String> loadScores(){
		createFile("./data/scores.txt");
		ArrayList<String> scoresInfos = new ArrayList<String>();

		FileReader fileReader = getScoresFileReader();
		
		if(fileReader == null){
			Logs.println2("LOAD SCORES FILE READER NULL");
		}
		
		String line;
		try(BufferedReader br = new BufferedReader(fileReader);) {
			line = br.readLine();
			while(line != null){
				scoresInfos.add(line);
				line = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return scoresInfos;
	}
	
	
	private static void createFile(String path){
		try{
			File file = (new File(path));
			if(file.exists()) return;
			
		    PrintWriter writer = new PrintWriter(path, "UTF-8");
		    writer.println("0");
		    writer.close();
		}
		catch (IOException e) {
		
		}
	}
	
	
	public static List<Integer> SListToI(List<String> list){
		List<Integer> iList = new ArrayList<>();
		try{
			for(String s : list){
				iList.add(Integer.valueOf(s));
			}
		}
		catch(Exception e){
			Logs.println2("Converting list problem !");
			return null;
		}
		return iList;
	}
	
	
}
