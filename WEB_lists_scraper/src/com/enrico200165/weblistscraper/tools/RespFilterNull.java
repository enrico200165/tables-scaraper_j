package com.enrico200165.weblistscraper.tools;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;
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

	static Logger log=Logger.getLogger(RespFilterNull.class.getSimpleName());
}