package com.ff.service;

import java.util.ArrayList;

import com.ff.datatypes.NFLTeam;
import com.ff.retrievers.DataRetrievalCBS;
import com.ff.retrievers.DataRetrievalNFL;
import com.ff.retrievers.DataRetrievalNerd;
import com.ff.utils.DatabaseUtils;

public class DataRetrievalService {

	private DatabaseUtils _db;
	private DataRetrievalNFL _nflAPI;
	private DataRetrievalCBS _cbsAPI;
	private DataRetrievalNerd _nerdAPI;
	
	public DataRetrievalService(){
		//setup DB connector
		_db = new DatabaseUtils();	
		// set up the data retrievers
		_nflAPI = new DataRetrievalNFL();
		_cbsAPI = new DataRetrievalCBS();
		_nerdAPI = new DataRetrievalNerd();
	}
	
	public void initialize(){
		//TEST
		//_nflAPI.getPlayerData();
		//_cbsAPI.getTeams();
		//ArrayList<NFLTeam> teams = _nerdAPI.getProTeams();
		//_db.populateNFLTeams(teams);
	}
	
	public static void main(String[] args){
		
		DataRetrievalService drs = new DataRetrievalService();
		drs.initialize();
		
	}

}
