package com.ff.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import com.ff.datatypes.NFLTeam;
import com.ff.datatypes.Player;
import com.ff.retrievers.DataRetrievalCBS;
import com.ff.retrievers.DataRetrievalNFL;
import com.ff.retrievers.DataRetrievalNerd;
import com.ff.utils.DatabaseUtils;
import com.ff.utils.FileReader;

public class DataRetrievalService {

	
	
	private DatabaseUtils _db;
	private FileReader _reader;
	private DataRetrievalNFL _nflAPI;
	private DataRetrievalCBS _cbsAPI;
	private DataRetrievalNerd _nerdAPI;
	
	public DataRetrievalService(){
		
		loadProperties();
		
		//set up DB connector
		_db = new DatabaseUtils();	
		//set up file reader
		_reader = new FileReader();
		// set up the data retrievers
		_nflAPI = new DataRetrievalNFL();
		_cbsAPI = new DataRetrievalCBS();
		_nerdAPI = new DataRetrievalNerd();
		

	}
	
	public void initialize(){
		
		//############ RUN ONLY ONCE ############
		//ArrayList<NFLTeam> teams = _nerdAPI.getProTeams();
		
		//_db.populateNFLTeams(teams);
		//ArrayList<Player> players = _nerdAPI.getProPlayers(_reader.rookieTextFile());
		//System.out.println("final size: " + players.size());
		//_db.populateNFLPlayers(players);
		
		//##################### ID MAPPING #####################
		//_nerdAPI.idMapInitializer(_db, "QB");
		//_nerdAPI.idMapInitializer(_db, "RB");
		//_nerdAPI.idMapInitializer(_db, "WR");
		//_nerdAPI.idMapInitializer(_db, "TE");
		//_nerdAPI.idMapInitializer(_db, "K");
		//_nerdAPI.idMapInitializer(_db, "DEF");
		
		//_cbsAPI.idMapInitializer("DST");
		
		//_nflAPI.idMapInitializer("QB");
		//_nflAPI.idMapInitializer("RB");
		//_nflAPI.idMapInitializer("WR");
		//_nflAPI.idMapInitializer("TE");
		//_nflAPI.idMapInitializer("K");
		//_nflAPI.idMapInitializer("DEF");
		
		//##################### RANKINGS #####################
		//_db.addRanking(_cbsAPI.getRankingByPos("QB"));
		//_db.addRanking(_cbsAPI.getRankingByPos("RB"));
		//_db.addRanking(_cbsAPI.getRankingByPos("WR"));
		//_db.addRanking(_cbsAPI.getRankingByPos("TE"));
		//_db.addRanking(_cbsAPI.getRankingByPos("K"));
		//_db.addRanking(_cbsAPI.getRankingByPos("DST"));
		
		//_db.addRanking(_nflAPI.getRanking());
		
		//_db.addRanking(_nerdAPI.getRanking());
		
		//_db.populateNFLPlayers(_reader.defenseTextFile());
		
		//_reader.importCustomRankingFile("Rookie-QB-11132014.txt").printPlayerRanking();
	}
	
	public static void main(String[] args){
		
		DataRetrievalService drs = new DataRetrievalService();
		drs.initialize();
		
	}
	
	public void loadProperties(){
		InputStream input = null;
		Properties prop = new Properties();
		
    	try {
        
    		String filename = System.getProperty("user.dir") + "/config/config.properties";
    		
    		input = new FileInputStream(filename);

    		//load a properties file from class path, inside static method
    		prop.load(input);
                //get the property value and print it out
               // System.out.println(prop.getProperty("database"));
    	       // System.out.println(prop.getProperty("dbuser"));
    	       // System.out.println(prop.getProperty("dbpassword"));
    	       // System.out.println(prop.getProperty("nerd_api_key"));
 
    	} catch (IOException ex) {
    		ex.printStackTrace();
        } finally{
        	if(input!=null){
        		try {
				input.close();
				
				System.setProperty("database", prop.getProperty("database"));
				System.setProperty("dbuser", prop.getProperty("dbuser"));
				System.setProperty("dbpassword", prop.getProperty("dbpassword"));
				System.setProperty("nerd_api_key", prop.getProperty("nerd_api_key"));
				
			} catch (IOException e) {
				e.printStackTrace();
			}
        	}
        }
	}

}
