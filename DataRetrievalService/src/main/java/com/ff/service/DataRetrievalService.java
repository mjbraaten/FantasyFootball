package com.ff.service;

import java.util.ArrayList;

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
		//TEST
		//_nflAPI.getPlayerData();
		//_cbsAPI.getTeams();
		
		//_nerdAPI.idMapInitializer(_db, "QB");
		//_nerdAPI.idMapInitializer(_db, "RB");
		//_nerdAPI.idMapInitializer(_db, "WR");
		//_nerdAPI.idMapInitializer(_db, "TE");
		//_nerdAPI.idMapInitializer(_db, "K");
		
		//_cbsAPI.idMapInitializer(_db, "null");
		
		//_nflAPI.idMapInitializer(_db, "QB");
		//_nflAPI.idMapInitializer(_db, "RB");
		//_nflAPI.idMapInitializer(_db, "WR");
		//_nflAPI.idMapInitializer(_db, "TE");
		//_nflAPI.idMapInitializer(_db, "K");
		
		//############ RUN ONLY ONCE ############
		//ArrayList<NFLTeam> teams = _nerdAPI.getProTeams();
		
		//_db.populateNFLTeams(teams);
		//ArrayList<Player> players = _nerdAPI.getProPlayers(_reader.rookieTextFile());
		//System.out.println("final size: " + players.size());
		//_db.populateNFLPlayers(players);
		

	}
	
	public static void main(String[] args){
		
		DataRetrievalService drs = new DataRetrievalService();
		drs.initialize();
		
	}

}
