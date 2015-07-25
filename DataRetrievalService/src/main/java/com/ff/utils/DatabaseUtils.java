package com.ff.utils;

import java.sql.*;
import java.util.*;

import com.ff.datatypes.NFLTeam;
import com.ff.retrievers.DataRetrievalCBS;
import com.ff.retrievers.DataRetrievalNFL;
import com.ff.retrievers.DataRetrievalNerd;

public class DatabaseUtils {
	  private Connection _conn = null;
	  
	  public DatabaseUtils() 
	  {
		//connect to the database
		  _conn = connectToDatabaseOrDie();
	  }
	  
	  private Connection connectToDatabaseOrDie()
	  {
	    Connection conn = null;
	    try {
	    	Class.forName("org.postgresql.Driver");
	    	//DriverManager.registerDriver(new org.postgresql.Driver());
	    	String url = "jdbc:postgresql://localhost/FantasyFootball";
	    	conn = DriverManager.getConnection(url,"postgres", "pirates");
	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	      System.exit(2);
	    } 
	    catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(2);
		}
	    return conn;
	  }

	  public boolean populateNFLTeams(List<NFLTeam> inTeams)
	  {
		PreparedStatement pst = null;
	    try{  	
	    	for (NFLTeam team : inTeams){
	    		String stm = "INSERT INTO nflteams(name, abbr, outdoorstadium, byeweek) VALUES(?, ?, ?, ?)";
	            pst = _conn.prepareStatement(stm);
	            pst.setString(1, team._name);  
	            pst.setString(2, team._abbr);  
	            pst.setBoolean(3, team._outdoorStadium);     
	            pst.setInt(4, team._byeWeek);
	            pst.executeUpdate();
	    	}
	    	pst.close();
	    	return true;
	    }
	    catch (SQLException se) {
	    	System.err.println("Threw a SQLException populating NFL teams.");
	    	System.err.println(se.getMessage());
	    	return false;
	    }
	  }
	  
}
