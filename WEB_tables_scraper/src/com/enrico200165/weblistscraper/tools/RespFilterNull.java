package com.enrico200165.weblistscraper.tools;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;

import org.apache.log4j.Logger;

public class RespFilterNull implements ClientResponseFilter {

	public void filter(ClientRequestContext requestContext) throws IOException {
	}

	private static org.apache.log4j.Logger log = Logger.getLogger(RespFilterNull.class);

	@Override
	public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
		// TODO Auto-generated method stub
		
	}

}