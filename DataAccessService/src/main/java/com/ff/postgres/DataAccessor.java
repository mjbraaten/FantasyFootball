package com.ff.postgres;

import java.sql.*;
import java.util.*;

import com.ff.datatypes.NFLTeam;

public class DataAccessor {

	  public static void main(String[] args){
		  new DataAccessor();
	  }
	  
	  public DataAccessor() 
	  {
	    Connection conn = null;

	    // connect to the database
	    conn = connectToDatabaseOrDie();

	    // get the data
	    printTeams(retrieveNFLTeams(conn));
	    
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
	  
	  private List<NFLTeam> retrieveNFLTeams(Connection conn)
	  {
	    try{
	    	List<NFLTeam> NFLTeams = new ArrayList<NFLTeam>();
	    	Statement st = conn.createStatement();
	    	ResultSet rs = st.executeQuery("SELECT abbrev, name, byeweek, outdoorstadium FROM nflteams ORDER BY abbrev");
	    	while (rs.next()){
	    		NFLTeam team = new NFLTeam();
	    		team._abbrev = rs.getString("abbrev");
	    		team._name = rs.getString ("name");
	    		team._byeWeek = rs.getInt ("byeweek");
	    		team._outdoorStadium = rs.getBoolean("outdoorstadium");
	        
	    		//Add team to list
	    		NFLTeams.add(team);
	    	}
	    	rs.close();
	    	st.close();
	    	return NFLTeams;
	    }
	    catch (SQLException se) {
	    	System.err.println("Threw a SQLException creating the list of NFL teams.");
	    	System.err.println(se.getMessage());
	    	return null;
	    }
	  }
	  
	  private List<NFLTeam> retrievePlayer(Connection conn, long playerID)
	  {
	    try{
	    	List<NFLTeam> NFLTeams = new ArrayList<NFLTeam>();
	    	Statement st = conn.createStatement();
	    	ResultSet rs = st.executeQuery("SELECT abbrev, name, byeweek, outdoorstadium FROM nflteams ORDER BY abbrev");
	    	while (rs.next()){
	    		NFLTeam team = new NFLTeam();
	    		team._abbrev = rs.getString("abbrev");
	    		team._name = rs.getString ("name");
	    		team._byeWeek = rs.getInt ("byeweek");
	    		team._outdoorStadium = rs.getBoolean("outdoorstadium");
	        
	    		//Add team to list
	    		NFLTeams.add(team);
	    	}
	    	rs.close();
	    	st.close();
	    	return NFLTeams;
	    }
	    catch (SQLException se) {
	    	System.err.println("Threw a SQLException creating the list of NFL teams.");
	    	System.err.println(se.getMessage());
	    	return null;
	    }
	  }
	  

	  
	  
	  
	  
	  
	  private void printTeams(List<NFLTeam> teams) {
	    Iterator it = teams.iterator();
	    while (it.hasNext())
	    {
	      NFLTeam team = (NFLTeam)it.next();
	      System.out.println("abbrev: " + team._abbrev + " Name: " + team._name);
	    }
	  }
}
