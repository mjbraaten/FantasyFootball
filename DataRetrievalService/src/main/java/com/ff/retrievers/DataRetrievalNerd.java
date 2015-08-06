package com.ff.retrievers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ff.datatypes.NFLTeam;
import com.ff.datatypes.Player;
import com.ff.datatypes.Position;
import com.ff.datatypes.Ranking;
import com.ff.utils.DatabaseUtils;

public class DataRetrievalNerd extends DataRetrieval implements IDataRetriever{

	public DataRetrievalNerd(){
		_apiURL = "http://www.fantasyfootballnerd.com";
	}

	public ArrayList<NFLTeam> getProTeams(){
		//first build up bye weeks
		HashMap<String, Integer> byeWeekMap = getByeWeeks();
		
		String output = _restUtils.getrequest(_apiURL + "/service/nfl-teams/json/" + System.getProperty("nerd_api_key") + "/");
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
		String output = _restUtils.getrequest(_apiURL + "/service/byes/json/" + System.getProperty("nerd_api_key") + "/");
		//System.out.println(output);

		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		JSONObject jsonObject = new JSONObject(output);
		
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

	public ArrayList<Player> getProPlayers(ArrayList<String> rookiesList) {
		ArrayList<Player> output = new ArrayList<Player>();
		
		output.addAll(getPlayersByPosition("QB",rookiesList));
		output.addAll(getPlayersByPosition("RB",rookiesList));
		output.addAll(getPlayersByPosition("WR",rookiesList));
		output.addAll(getPlayersByPosition("TE",rookiesList));
		output.addAll(getPlayersByPosition("K",rookiesList));
		return output;
	}
	
	public ArrayList<Player> getPlayersByPosition(String pos, ArrayList<String> rookiesList) {
		ArrayList<Player> players = new ArrayList<Player>();
		
		String output = _restUtils.getrequest(_apiURL + "/service/players/json/" + System.getProperty("nerd_api_key") + "/" + pos + "/");
		JSONObject jsonObject = new JSONObject(output);
		JSONArray jsonObjectPlayer = jsonObject.getJSONArray("Players");
		
		for(int i = 0; i < jsonObjectPlayer.length(); i++){
			Player currentPlayer = new Player();
			
			String displayname = jsonObjectPlayer.getJSONObject(i).getString("displayName");
			String lastname = jsonObjectPlayer.getJSONObject(i).getString("lname");
			String firstname = jsonObjectPlayer.getJSONObject(i).getString("fname");
			String team = jsonObjectPlayer.getJSONObject(i).getString("team");
			String position = jsonObjectPlayer.getJSONObject(i).getString("position");
			String dob = jsonObjectPlayer.getJSONObject(i).getString("dob");
			String college = jsonObjectPlayer.getJSONObject(i).getString("college");
			String nerdID = jsonObjectPlayer.getJSONObject(i).getString("playerId");
			
			boolean rookieFlag = false;
			int experience = 0;
			
			if(rookiesList.contains(firstname+lastname+team+position)){
				rookieFlag = true;
				experience = 0;
				System.out.println("Found a rookie: " + firstname + " " + lastname);
			}
			else{
				//get experience for pros
				experience = 99;
			}

			currentPlayer.displayName = displayname;
			currentPlayer.lastName = lastname;
			currentPlayer.firstName = firstname;
			currentPlayer.team = team;
			currentPlayer.position = Position.valueOf(pos);
			currentPlayer.college = college;
			currentPlayer.dob = dob;
			currentPlayer.rookieFlag = rookieFlag;
			currentPlayer.experience = experience;
			
			players.add(currentPlayer);
		}
		return players;
	}
	
	public void idMapInitializer(DatabaseUtils db, String inPos) {
		ArrayList<Player> players = new ArrayList<Player>();
		
		String output = _restUtils.getrequest(_apiURL + "/service/players/json/" + System.getProperty("nerd_api_key") + "/" + inPos + "/");
		JSONObject jsonObject = new JSONObject(output);
		JSONArray jsonObjectPlayer = jsonObject.getJSONArray("Players");
		
		for(int i = 0; i < jsonObjectPlayer.length(); i++){
			
			String lastname = jsonObjectPlayer.getJSONObject(i).getString("lname");
			String firstname = jsonObjectPlayer.getJSONObject(i).getString("fname");
			String team = jsonObjectPlayer.getJSONObject(i).getString("team");
			String position = jsonObjectPlayer.getJSONObject(i).getString("position");
			String nerdID = jsonObjectPlayer.getJSONObject(i).getString("playerId");

			List<Integer> matchingIDs = db.findPlayerIDbyDetails(lastname, firstname, position);
			if(matchingIDs.size() >= 1){
				db.insertNewID(matchingIDs.get(0), "nerd_id", Integer.parseInt(nerdID));
			}
		}
	}

	public Ranking getRanking() {

		Ranking myRank = new Ranking();
		
		String output = _restUtils.getrequest(_apiURL + "/service/draft-rankings/json/" + System.getProperty("nerd_api_key") + "/");
		System.out.println(output);

		JSONObject jsonObject = new JSONObject(output);
		JSONArray jsonObjectPlayer = jsonObject.getJSONArray("DraftRankings");
		
		myRank._source_type = "NERD_API";
		myRank._source_details = "";
		myRank._date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		myRank._type = "Normal";
		myRank._position = "All";
		
		for (int i = 0; i < jsonObjectPlayer.length(); i++){
			int nerd_id = jsonObjectPlayer.getJSONObject(i).getInt("playerId");
			int myID = _dbUtils.findPlayerIDbyAPIID("nerd_id", nerd_id);
			myRank._rankingsList.put(i, myID);
		}
		
		return myRank;
	}

	public Ranking getRankingByPos(String position) {
		// TODO Auto-generated method stub
		return null;
	}
}
