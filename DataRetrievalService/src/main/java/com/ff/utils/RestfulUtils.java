package com.ff.utils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/*
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
*/

public class RestfulUtils {

	public final String getrequest(String url){				
		String response = ClientBuilder.newClient().target(url).request(MediaType.APPLICATION_JSON).get(String.class);
		return response;
	}
}
