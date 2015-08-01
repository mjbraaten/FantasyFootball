package com.ff.retrievers;

import com.ff.utils.DatabaseUtils;
import com.ff.utils.RestfulUtils;

public abstract class DataRetrieval {

	protected RestfulUtils _restUtils;
	protected DatabaseUtils _dbUtils;
	
	protected String _apiURL;
	
	public DataRetrieval(){
		_restUtils = new RestfulUtils();
		_dbUtils = new DatabaseUtils();	
	}

}
