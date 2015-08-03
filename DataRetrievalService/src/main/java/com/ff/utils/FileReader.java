package com.ff.utils;

import java.io.BufferedReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.ff.datatypes.Player;
import com.ff.datatypes.Position;
import com.ff.datatypes.Ranking;

public class FileReader {
	
	DatabaseUtils _dbUtils;
	
	public FileReader(){
		_dbUtils = new DatabaseUtils();
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
		/*

		 HEADER	
			source_type	Custom
			source_details	College Players CBS
			date	
			type	Rookies
			position	
		PLAYERS	
			player 1	
			player 2
			...
			...
		 
		 */
		Ranking myRank = new Ranking();
		String filePath = System.getProperty("user.dir") + "/datafiles/rankings/" + filename;
		  try
		  {
		    BufferedReader reader = new BufferedReader(new java.io.FileReader(filePath));
		    
		    //ignore this line
		    String line_header = reader.readLine();
		    
		    String line_source_type = reader.readLine();
		    String[] parsed_source_type = line_source_type.split("[\\s,;\\n\\t]+");
		    
		    String line_source_details = reader.readLine();
		    String[] parsed_source_details = line_source_details.split("[\\s,;\\n\\t]+");
		    
		    String line_date = reader.readLine();
		    String[] parsed_source_date = line_date.split("[\\s,;\\n\\t]+");
		    
		    String line_type = reader.readLine();
		    String[] parsed_type = line_type.split("[\\s,;\\n\\t]+");
		    
		    String line_position = reader.readLine();
		    String[] parsed_position = line_position.split("[\\s,;\\n\\t]+");
		    
		    //ignore this line
		    String line_players = reader.readLine();
		    
		    //###########################################
		    // Filling out ranking
		    //###########################################
			myRank._source_type = parsed_source_type[1];
			myRank._source_details = parsed_source_details[1];
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 
			java.util.Date rankingDate = df.parse(parsed_source_date[1]);
			myRank._date = new java.sql.Date(rankingDate.getTime());
			myRank._type = parsed_type[1];
			myRank._position = parsed_position[1];	
		    
		    String line;
		    int count = 0;
		    while ((line = reader.readLine()) != null)
		    {	      
		      String[] paresedLine = line.split("[\\s,;\\n\\t]+");
		      
		      String firstname = paresedLine[0];
		      String lastname = paresedLine[1];
		      String team = paresedLine[2];
		      
		      //get ID
		      List<Integer> matchingIDs = _dbUtils.findPlayerIDbyDetails(lastname, firstname, myRank._position);
				if(matchingIDs.size() == 1){
					myRank._rankingsList.put(count, matchingIDs.get(0));
					count++;	 
				}
				else
				{
					System.err.println("Error finding player: " + firstname + " " + lastname + " matches= " + matchingIDs.size());
				}
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
		
		return myRank;
	}
}
