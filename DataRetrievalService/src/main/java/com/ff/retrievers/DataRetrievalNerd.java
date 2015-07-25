package com.ff.retrievers;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ff.datatypes.NFLTeam;
import com.ff.datatypes.Player;

public class DataRetrievalNerd extends DataRetrieval implements IDataRetriever{

	public DataRetrievalNerd(){
		_apiURL = "http://www.fantasyfootballnerd.com";
	}

	public ArrayList<NFLTeam> getProTeams(){
		//first build up bye weeks
		HashMap<String, Integer> byeWeekMap = getByeWeeks();
		
		String output = _restUtils.getrequest(_apiURL + "/service/nfl-teams/json/jkbt9qb2pfh3/");
		System.out.println(output);

		JSONObject jsonObject = new JSONObject(output);
		JSONArray pro_teams = jsonObject.getJSONArray("NFLTeams");

		ArrayList<NFLTeam> team_list = new ArrayList<NFLTeam>();		

		for(int i = 0; i < pro_teams.length(); i++)
		{
			String abbr = pro_teams.getJSONObject(i).getString("code");
			String fullname = pro_teams.getJSONObject(i).getString("fullName");

			//get bye week
			int bye = byeWeekMap.get(abbr);
			
			NFLTeam temp = new NFLTeam();
			temp._abbr = abbr;
			temp._name = fullname;
			temp._byeWeek = bye;
			team_list.add(temp);
		}
		
		return team_list;
	}

	//Used by the "populateTeams" method
	public HashMap<String, Integer> getByeWeeks(){
		String output = _restUtils.getrequest(_apiURL + "/service/byes/json/jkbt9qb2pfh3/");
		//System.out.println(output);

		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		JSONObject jsonObject = new JSONObject(output);
		System.out.println(jsonObject.length());
		System.out.println(jsonObject.toString());
		
		//go through all 16 weeks of the season, looking for Bye Weeks (typically 4-11)
		for(int i = 1; i < 16; i++){
			try{			
				JSONArray jsonObjectBYE = jsonObject.getJSONArray("Bye Week "+i);

				for(int j = 0; j < jsonObjectBYE.length(); j++){
					String abbrev = jsonObjectBYE.getJSONObject(j).getString("team");
					System.out.println("       abbrev = " + abbrev);
					int bye = jsonObjectBYE.getJSONObject(j).getInt("byeWeek");
					System.out.println("       bye = " + bye);
					
					map.put(abbrev, bye);
				}
				
			}
			catch (Exception ex){
				//System.err.println(ex);
				//ignore
				System.out.println("Week " + i + " is not a bye week.");
			}	
		}		
		return map;
	}

	public ArrayList<Player> getProPlayers() {
		// TODO Auto-generated method stub
		return null;
	}
}
