package com.enrico200165.weblistscraper.tools;

import java.io.IOException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import org.apache.log4j.Logger;

public class RequestFilterNull implements ClientRequestFilter {

	public void filter(ClientRequestContext requestContext) throws IOException {
	}

	private static org.apache.log4j.Logger log = Logger.getLogger(RequestFilterNull.class);

}