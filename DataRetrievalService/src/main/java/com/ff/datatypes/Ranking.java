package com.ff.datatypes;

import java.util.HashMap;

public class Ranking {
	
	public int _id;
	public String _source_details;
	public String _source_type;
	public java.sql.Date _date;
	public String _type;
	public String _position;
	
	public HashMap<Integer, Integer> _rankingsList;
	
	public Ranking(){
		_rankingsList = new HashMap<Integer, Integer>();
	}
	
	public void printPlayerRanking(){
		System.out.println("############################################");
		System.out.println("Source:   " + _source_type);
		System.out.println("Date:     " + _date.toString());
		System.out.println("Type:     " + _type);
		System.out.println("Position: " + _position);
		System.out.println("############################################");
		for(int i = 0; i < _rankingsList.size(); i++){
			System.out.println(i + ". : " + _rankingsList.get(i));
		}
	}
}
