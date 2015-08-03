package com.ff.retrievers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ff.datatypes.NFLTeam;
import com.ff.datatypes.Player;
import com.ff.datatypes.Ranking;
import com.ff.utils.DatabaseUtils;

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

	public ArrayList<Player> getProPlayers(ArrayList<String> rookiesList) {
		// TODO Auto-generated method stub
		return null;
	}

	public void idMapInitializer(String inPos) {
		HashMap<String, Integer> positionMax = new HashMap();
		positionMax.put("QB", 250);
		positionMax.put("RB", 450);
		positionMax.put("WR", 650);
		positionMax.put("TE", 350);
		positionMax.put("K", 150);
		positionMax.put("DEF", 50);

		ArrayList<Player> players = new ArrayList<Player>();

		int max = positionMax.get(inPos);
		int currentCount = 0;
		int offset = 0;

		while(currentCount < max){
			offset = currentCount;
			currentCount += 50;

			System.out.println("Loop: " + offset + " - " + (offset+50));

			String resource = String.format("/players/advanced?format=json&count=%d&offset=%d&position=%s", 50, offset, inPos);
			String output = _restUtils.getrequest(_apiURL + resource);

			JSONObject jsonObject = new JSONObject(output);
			JSONArray jsonObjectPlayer = jsonObject.getJSONArray(inPos);

			for(int i = 0; i < jsonObjectPlayer.length(); i++){
				String position = jsonObjectPlayer.getJSONObject(i).getString("position");
				String lastname = jsonObjectPlayer.getJSONObject(i).getString("lastName");
				String firstname = jsonObjectPlayer.getJSONObject(i).getString("firstName");
				String team = jsonObjectPlayer.getJSONObject(i).getString("teamAbbr");
				String nflID = jsonObjectPlayer.getJSONObject(i).getString("id");

				List<Integer> matchingIDs = _dbUtils.findPlayerIDbyDetails(lastname, firstname, position);
				if(matchingIDs.size() >= 1){
					_dbUtils.insertNewID(matchingIDs.get(0), "nfl_id", Integer.parseInt(nflID));
					//System.out.println("Found: " + lastname + ", " + firstname + " : " + position + " : " + team);
				}
				else
				{
					System.err.println("Error finding player: " + lastname + ", " + firstname + " : " + position + " : " + team);
				}
			}
		}
	}

	public Ranking getRanking() {
		Ranking myRank = new Ranking();
		ArrayList<Integer> idList = new ArrayList<Integer>();
		boolean firstRun = true;

		int max = 200;
		int currentCount = 0;
		int offset = 0;

		while(currentCount < max){

			//System.out.println("Loop: " + offset + " - " + (offset+50));

			String resource = String.format("/v1/players/editordraftranks?format=json&count=50&offset=%d", offset);
			String output = _restUtils.getrequest(_apiURL + resource);
			JSONObject jsonObject = new JSONObject(output);

			if(firstRun){
				try {
					String lastRun = jsonObject.getString("lastRun");
					System.out.println("Last Run: " + lastRun);
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); 
					java.util.Date lastRunDate = df.parse(lastRun);
					System.out.println("LAST RUN DATE: " + lastRunDate.toString());
					myRank._date = new java.sql.Date(lastRunDate.getTime());
					
					myRank._source_type = "NFL_API";
					myRank._source_details = "EditorRankings";
					myRank._type = "Normal";
					myRank._position = "All";
					
					firstRun = false;
					
				} catch (ParseException e) {
					System.err.println("couldnt parse date");
					e.printStackTrace();
				}
			}

			JSONArray jsonObjectPlayers = jsonObject.getJSONArray("players");
			for(int i = 0; i < jsonObjectPlayers.length(); i++){

				int nflID = jsonObjectPlayers.getJSONObject(i).getInt("id");
				int myID = _dbUtils.findPlayerIDbyAPIID("nfl_id", nflID);
				
				if(myID == 0){
					String position = jsonObjectPlayers.getJSONObject(i).getString("position");
					String lastname = jsonObjectPlayers.getJSONObject(i).getString("lastName");
					String firstname = jsonObjectPlayers.getJSONObject(i).getString("firstName");
					String team = jsonObjectPlayers.getJSONObject(i).getString("teamAbbr");
					
					System.err.println("Could not find ID for: " + lastname + ", " + firstname + " " + position + " " + team + " nflID=" + nflID);
				}
				idList.add(myID);
				
				
			}
			System.out.println("Size = " + idList.size());
			
			currentCount += 50;
			offset = currentCount;
		}

		for(int j = 0; j < idList.size(); j++){
			myRank._rankingsList.put(j, idList.get(j));
		}
		
		return myRank;
	}

	public Ranking getRankingByPos(String position) {
		/*
		HashMap<String, Integer> positionMax = new HashMap();
		positionMax.put("QB", 50);
		positionMax.put("RB", 200);
		positionMax.put("WR", 200);
		positionMax.put("TE", 50);
		positionMax.put("K", 50);

		int max = positionMax.get(position);
		int currentCount = 0;
		int previousCount = 0;

		while(currentCount < max){
			previousCount = currentCount;
			currentCount += 50;

			System.out.println("Loop: " + previousCount + " - " + currentCount);

			String resource = String.format("/v1/players/editordraftranks?format=json&count=%d&offset=%d&position=%s", currentCount, previousCount, position);
			String output = _restUtils.getrequest(_apiURL + resource);

			JSONObject jsonObject = new JSONObject(output);
			String lastRun = jsonObject.getString("lastRun");
			System.out.println("Last Run: " + lastRun);

			JSONArray jsonObjectPlayers = jsonObject.getJSONArray("players");

			for(int i = 0; i < jsonObjectPlayers.length(); i++){
				//String position = jsonObjectPlayer.getJSONObject(i).getString("position");
				String lastname = jsonObjectPlayers.getJSONObject(i).getString("lastName");
				String firstname = jsonObjectPlayers.getJSONObject(i).getString("firstName");
				String team = jsonObjectPlayers.getJSONObject(i).getString("teamAbbr");
				String nflID = jsonObjectPlayers.getJSONObject(i).getString("id");

				List<Integer> matchingIDs = _dbUtils.findPlayerIDbyDetails(lastname, firstname, position, team);
				if(matchingIDs.size() >= 1){
					_dbUtils.insertNewID(matchingIDs.get(0), "nfl_id", Integer.parseInt(nflID));
					System.out.println("Found: " + lastname + ", " + firstname + " : " + position + " : " + team);
				}
				else
				{
					System.err.println("Error finding player: " + firstname + " " + lastname);
				}
			}
		}
		 */
		return null;
	}

}
