package com.enrico200165.weblistscraper.tools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import java.io.IOException;

public class RespFilterNull implements ClientResponseFilter {

	public void filter(ClientRequestContext requestContext) throws IOException {
	}


	@Override
	public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
		// TODO Auto-generated method stub

	}

	private static Logger log = LogManager.getLogger(RespFilterNull.class.getName());
}