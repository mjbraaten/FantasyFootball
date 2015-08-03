package com.ff.utils;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import com.ff.datatypes.NFLTeam;
import com.ff.datatypes.Player;
import com.ff.datatypes.Ranking;
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

	public boolean populateNFLPlayers(ArrayList<Player> inPlayers)
	{
		PreparedStatement pst = null;
		try{  	
			for (Player player : inPlayers){

				String stm = "INSERT INTO players(displayname, firstname, lastname, position, team, college, experience, rookieflag, dob) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
				pst = _conn.prepareStatement(stm);
				pst.setString(1, player.displayName);  
				pst.setString(2, player.firstName);  
				pst.setString(3, player.lastName);     
				pst.setString(4, player.position.getValue());
				pst.setString(5, player.team);  
				pst.setString(6, player.college);  
				pst.setInt(7, player.experience);     
				pst.setBoolean(8, player.rookieFlag);

				try{
					if(player.dob.equals("0000-00-00")){		            
						pst.setDate(9, null);
					}
					else{
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						Date parsed = format.parse(player.dob);
						java.sql.Date sql = new java.sql.Date(parsed.getTime());		            
						pst.setDate(9, sql);
					}
				}
				catch(Exception ex){
					pst.setDate(9, null);
					System.err.println(ex);
				}		            
				pst.executeUpdate();
			}
			pst.close();



			return true;
		}
		catch (SQLException se) {
			System.err.println("Threw a SQLException populating NFL players.");
			System.err.println(se.getMessage());
			return false;
		}
	}

	public int findPlayerIDbyAPIID(String api, int apiID)
	{
		int myID = 0;
		try{	    	
			String str = "SELECT id FROM player_id_map WHERE " + api + " = ? ";
			PreparedStatement st = _conn.prepareStatement(str);
			st.setInt(1, apiID);     	
			ResultSet rs = st.executeQuery();

			while (rs.next()){
				myID = rs.getInt("id");
			}
			rs.close();
			st.close();

			return myID;
		}
		catch (SQLException se) {
			System.err.println("Threw a SQLException finding player ID by API ID.");
			System.err.println(se.getMessage());
			return myID;
		}
	}

	public List<Integer> findPlayerIDbyDetails(String lastname, String firstname, String position)
	{
		ResultSet rs;
		PreparedStatement st;
		
		try{
			List<Integer> matchingIDs = new ArrayList<Integer>();

			if((position.equals("DEF"))){
				String str = "SELECT id FROM players WHERE lastname like ? and firstname like ?";
				st = _conn.prepareStatement(str);
				st.setString(1, lastname);  
				st.setString(2, firstname);
				rs = st.executeQuery();
			}
			else if((position.equals("DST"))){
				String str = "SELECT id FROM players WHERE lastname like ? and position like ?";
				st = _conn.prepareStatement(str);
				st.setString(1, lastname);  
				st.setString(2, position);
				rs = st.executeQuery();
			}
			else{
				String str = "SELECT id FROM players WHERE lastname like ? and firstname like ? and position like ?";
				st = _conn.prepareStatement(str);
				st.setString(1, lastname);  
				st.setString(2, firstname);
				st.setString(3, position);
				rs = st.executeQuery();
			}

			//ResultSet rs = st.executeQuery();

			while (rs.next()){
				int id = rs.getInt("id");

				//Add team to list
				matchingIDs.add(id);
			}
			rs.close();
			st.close();
			return matchingIDs;
		}
		catch (SQLException se) {
			System.err.println("Threw a SQLException creating the list of NFL teams.");
			System.err.println(se.getMessage());
			return null;
		}
	}

	public boolean insertNewID(int id, String apiName, int apiID)
	{
		PreparedStatement pst = null;
		try{

			//check to see if it exists yet or not
			List<Integer> matchingIDs = new ArrayList<Integer>();
			String str = String.format("SELECT * FROM player_id_map WHERE id = '%d'", id);
			Statement st = _conn.createStatement();
			ResultSet rs = st.executeQuery(str);

			if(!rs.next()){
				System.out.println("insert row");
				String stm = String.format("INSERT INTO player_id_map(id, %s) VALUES(?, ?)", apiName);
				pst = _conn.prepareStatement(stm);
				pst.setInt(1, id);
				pst.setInt(2, apiID);
				pst.executeUpdate();
			}
			else
			{
				System.out.println("update row");
				String stm = String.format("UPDATE player_id_map SET %s = %d WHERE id = %d", apiName, apiID, id);
				pst = _conn.prepareStatement(stm);
				pst.executeUpdate();
			}

			pst.close();
			rs.close();
			st.close();
			return true;
		}
		catch (SQLException se) {
			System.err.println("Threw a SQLException creating the id mapping.");
			System.err.println(se.getMessage());
			return false;
		}
	}

	public boolean addRanking(Ranking inRanking)
	{
		PreparedStatement pst = null;
		try{  	
			String stm = "INSERT INTO player_rankings_map(date_of_ranking, source_type, source_details, rank_type, player_position) VALUES(?, ?, ?, ?, ?) RETURNING ranking_id";
			pst = _conn.prepareStatement(stm);
			pst.setDate(1, inRanking._date);  
			pst.setString(2, inRanking._source_type);  
			pst.setString(3, inRanking._source_details);     
			pst.setString(4, inRanking._type);
			pst.setString(5,inRanking._position);
			pst.execute();

			//get the ID you just created
			ResultSet last_ranking_updated = pst.getResultSet();
			int ranking_id;
			while(last_ranking_updated.next()){
				ranking_id = last_ranking_updated.getInt(1);
				System.out.println(ranking_id);

				//go through the players and insert into the table
				for(int i = 0; i < inRanking._rankingsList.size(); i++){
					stm = "INSERT INTO player_rankings(ranking_id, rank_number, player_id) VALUES(?, ?, ?)";
					pst = _conn.prepareStatement(stm);
					pst.setInt(1, ranking_id);  
					pst.setInt(2, i+1);  
					pst.setInt(3, inRanking._rankingsList.get(i));     
					pst.executeUpdate();
				}
			}
			pst.close();
			return true;
		}
		catch (SQLException se) {
			System.err.println("Threw a SQLException adding rankings.");
			System.err.println(se.getMessage());
			return false;
		}
	}

}
