package com.ff.utils;

import java.io.BufferedReader;
import java.util.ArrayList;

public class FileReader {
	
	public FileReader(){
		
	}
	
	
	public ArrayList<String> rookieTextFile(){
		ArrayList<String> output = new ArrayList<String>();
		
		String filePath = System.getProperty("user.dir") + "/references/rookies.txt";
		//Line looks like.....  'Bud Sasser STL, 6	WR'
		  try
		  {
		    BufferedReader reader = new BufferedReader(new java.io.FileReader(filePath));
		    String line;
		    while ((line = reader.readLine()) != null)
		    {	      
		      String[] paresedLine = line.split("[\\s,;\\n\\t]+");
		      
		      String firstname = paresedLine[0];
		      String lastname = paresedLine[1];
		      String team = paresedLine[2];
		      String byeweek = paresedLine[3];//not used
		      String position = paresedLine[4];
		      output.add(firstname+lastname+team+position);
		    }
		    reader.close();
		    return output;
		  }
		  catch (Exception e)
		  {
		    System.err.format("Exception occurred trying to read '%s'.", filePath);
		    e.printStackTrace();
		    return null;
		  }
	}
}
