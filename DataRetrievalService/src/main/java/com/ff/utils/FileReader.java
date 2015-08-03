package com.ff.utils;

import java.io.BufferedReader;
import java.util.ArrayList;

import com.ff.datatypes.Player;
import com.ff.datatypes.Position;
import com.ff.datatypes.Ranking;

public class FileReader {
	
	public FileReader(){
		
	}
	
	
	public ArrayList<String> rookieTextFile(){
		ArrayList<String> output = new ArrayList<String>();
		
		String filePath = System.getProperty("user.dir") + "/datafiles/rookies.txt";
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
	
	public ArrayList<Player> defenseTextFile(){
		ArrayList<Player> output = new ArrayList<Player>();
		
		String filePath = System.getProperty("user.dir") + "/datafiles/defenses.txt";
		//Line looks like.....  'Arizona Cardinals	ARI'
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
		      
		      System.out.println("Defense: " + firstname + " " + lastname + " " + team);
		      
		      Player player = new Player();
		      player.firstName = firstname;
		      player.lastName = lastname;
		      player.team = team;
		      player.rookieFlag = false;
		      player.position = Position.DST;
		      player.displayName = lastname;
		      player.college = "";
		      player.dob = "";
		      player.experience = 0;
		      
		      output.add(player);
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
	
	public Ranking importCustomRankingFile(String filename){
		Ranking myRank = new Ranking();
		
		String filePath = System.getProperty("user.dir") + "/datafiles/rookies.txt";
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
		      //output.add(firstname+lastname+team+position);
		    }
		    reader.close();
		   // return output;
		  }
		  catch (Exception e)
		  {
		    System.err.format("Exception occurred trying to read '%s'.", filePath);
		    e.printStackTrace();
		    return null;
		  }
		
		
		
		
		myRank._source_type = "Custom";
		myRank._source_details = "";
		//myRank._date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		myRank._type = "Normal";
		myRank._position = "All";
		
		//myRank._rankingsList.put(i, myID);
		
		return myRank;
	}
}
