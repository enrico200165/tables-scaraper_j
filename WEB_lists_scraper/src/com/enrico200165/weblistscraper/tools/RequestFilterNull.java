package com.enrico200165.weblistscraper.tools;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;

public class RequestFilterNull implements ClientRequestFilter {

	public void filter(ClientRequestContext requestContext) throws IOException {
	}

	static Logger log=Logger.getLogger(RequestFilterNull.class.getSimpleName());

}