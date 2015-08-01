package com.ff.retrievers;

import java.util.ArrayList;
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

	public void idMapInitializer(DatabaseUtils db, String inPos) {
		HashMap<String, Integer> positionMax = new HashMap();
		positionMax.put("QB", 250);
		positionMax.put("RB", 450);
		positionMax.put("WR", 650);
		positionMax.put("TE", 350);
		positionMax.put("K", 150);

		ArrayList<Player> players = new ArrayList<Player>();

		int max = positionMax.get(inPos);
		int currentCount = 0;
		int previousCount = 0;

		while(currentCount < max){
			previousCount = currentCount;
			currentCount += 50;

			System.out.println("Loop: " + previousCount + " - " + currentCount);

			String resource = String.format("/players/advanced?format=json&count=%d&offset=%d&position=%s", currentCount, previousCount, inPos);
			String output = _restUtils.getrequest(_apiURL + resource);

			JSONObject jsonObject = new JSONObject(output);
			JSONArray jsonObjectPlayer = jsonObject.getJSONArray(inPos);

			for(int i = 0; i < jsonObjectPlayer.length(); i++){
				String position = jsonObjectPlayer.getJSONObject(i).getString("position");
				String lastname = jsonObjectPlayer.getJSONObject(i).getString("lastName");
				String firstname = jsonObjectPlayer.getJSONObject(i).getString("firstName");
				String team = jsonObjectPlayer.getJSONObject(i).getString("teamAbbr");
				String nflID = jsonObjectPlayer.getJSONObject(i).getString("id");

				List<Integer> matchingIDs = db.findPlayerIDbyDetails(lastname, firstname, position, team);
				if(matchingIDs.size() >= 1){
					db.insertNewID(matchingIDs.get(0), "nfl_id", Integer.parseInt(nflID));
					System.out.println("Found: " + lastname + ", " + firstname + " : " + position + " : " + team);
				}
				else
				{
					System.err.println("Error finding player: " + firstname + " " + lastname);
				}
			}
		}
	}

	public Ranking getRanking() {
		// TODO Auto-generated method stub
		return null;
	}

	public Ranking getRankingByPos(String position) {
		// TODO Auto-generated method stub
		return null;
	}

}
