package com.enrico200165.weblistscraper.tools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;

public class RequestFilterNull implements ClientRequestFilter {

	public void filter(ClientRequestContext requestContext) throws IOException {
	}

	private static Logger log = LogManager.getLogger(RequestFilterNull.class.getSimpleName());

}