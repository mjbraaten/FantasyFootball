package com.ff.retrievers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ff.datatypes.NFLTeam;
import com.ff.datatypes.Player;

public class DataRetrievalCBS extends DataRetrieval implements IDataRetriever{
	
	public DataRetrievalCBS(){
		_apiURL = "http://api.cbssports.com";
	}

	public ArrayList<NFLTeam> getProTeams() {
		String output = _restUtils.getrequest(_apiURL + "/fantasy/pro-teams?version=3.0&SPORT=football&response_format=json");
		System.out.println(output);
		
		JSONObject jsonObject = new JSONObject(output);
		JSONObject body = jsonObject.getJSONObject("body");
		JSONArray pro_teams = body.getJSONArray("pro_teams");
		
		ArrayList<NFLTeam> team_list = new ArrayList<NFLTeam>();		
		
		for(int i = 0; i < pro_teams.length(); i++)
		{
			String abbr = pro_teams.getJSONObject(i).getString("abbr");
			String nickname = pro_teams.getJSONObject(i).getString("nickname");
			String name = pro_teams.getJSONObject(i).getString("name");
			String fullname = name + " " + nickname;
			
			NFLTeam temp = new NFLTeam();
			temp._abbr = abbr;
			temp._name = fullname;
			team_list.add(temp);
		}
		
		return team_list;
	}

	public HashMap<String, Integer> getByeWeeks() {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<Player> getProPlayers(ArrayList<String> rookiesList) {
		// TODO Auto-generated method stub
		return null;
	}
}
