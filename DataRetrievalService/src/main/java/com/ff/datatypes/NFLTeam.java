package com.ff.datatypes;

public class NFLTeam {
	public String _name;
	public String _abbr;
	public int _byeWeek;
	public boolean _outdoorStadium;
	
	public NFLTeam(){
		
	}
	
	@Override
	public String toString(){
		return _abbr;
	}
}
