package com.ff.retrievers;

import java.util.ArrayList;
import java.util.HashMap;

import com.ff.datatypes.NFLTeam;
import com.ff.datatypes.Player;

public class DataRetrievalNFL extends DataRetrieval implements IDataRetriever{

	public DataRetrievalNFL(){
		_apiURL = "http://api.fantasy.nfl.com";
	}
	
	public void getPlayerData(){
		//String url = "http://api.fantasy.nfl.com/v1/players/editordraftranks";
		String output = _restUtils.getrequest(_apiURL + "/v1/players/editordraftranks");
		System.out.println(output);
	}

	public ArrayList<NFLTeam> getProTeams() {
		// TODO Auto-generated method stub
		return null;
	}

	public HashMap<String, Integer> getByeWeeks() {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<Player> getProPlayers() {
		// TODO Auto-generated method stub
		return null;
	}
}
