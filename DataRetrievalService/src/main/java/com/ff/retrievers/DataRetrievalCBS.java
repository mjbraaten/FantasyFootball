package com.ff.retrievers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ff.datatypes.NFLTeam;
import com.ff.datatypes.Player;
import com.ff.utils.DatabaseUtils;

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
	
	public void idMapInitializer(DatabaseUtils db, String inPos) {
		ArrayList<String> validPositions = new ArrayList<String>();
		validPositions.add("QB");
		validPositions.add("RB");
		validPositions.add("WR");
		validPositions.add("TE");
		validPositions.add("K");
		
		ArrayList<Player> players = new ArrayList<Player>();
		
		String output = _restUtils.getrequest(_apiURL + "/fantasy/players/list?version=3.0&SPORT=football&response_format=json");
		JSONObject jsonObject = new JSONObject(output);
		JSONObject jsonBody = jsonObject.getJSONObject("body");
		JSONArray jsonObjectPlayer = jsonBody.getJSONArray("players");

		for(int i = 0; i < jsonObjectPlayer.length(); i++){
			String position = jsonObjectPlayer.getJSONObject(i).getString("position");

			if(validPositions.contains(position)){

				String lastname = jsonObjectPlayer.getJSONObject(i).getString("lastname");
				String firstname = jsonObjectPlayer.getJSONObject(i).getString("firstname");
				String team = jsonObjectPlayer.getJSONObject(i).getString("pro_team");
				String cbsID = jsonObjectPlayer.getJSONObject(i).getString("id");

				List<Integer> matchingIDs = db.findPlayerIDs(lastname, firstname, position, team);
				if(matchingIDs.size() >= 1){
					db.insertNewID(matchingIDs.get(0), "cbs_id", Integer.parseInt(cbsID));
					//System.out.println("Found: " + lastname + ", " + firstname + " : " + position + " : " + team);
				}
				else
				{
					System.err.println("Error finding player: " + firstname + " " + lastname);
				}
			}
		}
	}
}
