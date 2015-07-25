package com.ff.retrievers;

import com.ff.utils.RestfulUtils;

public abstract class DataRetrieval {

	protected RestfulUtils _restUtils;
	protected String _apiURL;
	
	public DataRetrieval(){
		_restUtils = new RestfulUtils();
	}

}
