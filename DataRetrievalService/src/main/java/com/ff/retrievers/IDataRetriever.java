package com.ff.retrievers;

import java.util.ArrayList;
import java.util.HashMap;

import com.ff.datatypes.NFLTeam;
import com.ff.datatypes.Player;

public interface IDataRetriever {
	
	ArrayList<NFLTeam> getProTeams();
	HashMap<String, Integer> getByeWeeks();
	ArrayList<Player> getProPlayers();
}
