package com.ff.retrievers;

import java.util.ArrayList;
import java.util.HashMap;

import com.ff.datatypes.NFLTeam;
import com.ff.datatypes.Player;
import com.ff.datatypes.Ranking;

public interface IDataRetriever {
	
	ArrayList<NFLTeam> getProTeams();
	HashMap<String, Integer> getByeWeeks();
	ArrayList<Player> getProPlayers(ArrayList<String> rookiesList);
	Ranking getRanking();
	Ranking getRankingByPos(String position);
}
