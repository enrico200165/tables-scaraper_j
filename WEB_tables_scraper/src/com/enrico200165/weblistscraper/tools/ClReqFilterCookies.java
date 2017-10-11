package com.enrico200165.weblistscraper.tools;

import com.enrico200165.cookies.CookieStoreEV;
import com.enrico200165.utils.net.http.Utils;
import com.enrico200165.weblistscraper.common.*;
import org.apache.log4j.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Random;

public class ClReqFilterCookies implements ClientRequestFilter {

	public ClReqFilterCookies(CookieStoreEV CSPar) {
		this.cookieStore = CSPar;
		defaultDelay = 5;
	}

	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {

		Client client = requestContext.getClient();
		//		Collection<String> props = client.getConfiguration().getPropertyNames();
		//		for (String prop : props) {
		//			log.info("property: "+prop+" value "+client.getConfiguration().getProperty(prop));
		//		}
		Integer delay = (Integer) client.getConfiguration().getProperty(WEBUtils.callDelayProperty);
		if (delay == null) {
			log.debug("delay is null, setting it to 2");
			delay = new Integer(2);
		}
		waitToSimulateHuman(delay);

		URI uri = requestContext.getUri();
		log.info("log da filter: " + requestContext.getMethod() + " " + uri);

		// addMap / if empty path
		if (uri.getPath() == null || uri.getPath().length() == 0) {
			requestContext.setUri(UriBuilder.fromUri(uri).path("/").build());
			uri = requestContext.getUri();
		}
		if (!Utils.isUsableURI(uri)) {
			log.error("URI not usable: " + uri);
			System.exit(1);
		}

		{ // --- Inviamo i cookies ---
			String domain = requestContext.getUri().getHost();
			String path = requestContext.getUri().getPath();

			List<Cookie> cookies = cookieStore.getCookies(domain, path);
			if (cookies != null && cookies.size() > 0) {

				log.debug(cookieStore.dump(true));

				// vecchio sistema abbandonato
				// requestContext.getHeaders().put("Cookie", cookies);
				StringBuilder strBuilder = new StringBuilder();
				for (Cookie cookie : cookies) {
					strBuilder.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
				}
				requestContext.getHeaders().add("Cookie", strBuilder.toString());
			}
		}

	}

	void waitToSimulateHuman(int seconds) {

		if (seconds == -1) return;
		if (seconds == 0) seconds = this.defaultDelay;

		Random rand = new Random();
		long mseconds = rand.nextInt((seconds - 1) * 1000) + 1000;
		mseconds += 1500; // sempre almeno 1.5 secs
		try {
			Thread.sleep(mseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	int defaultDelay; // wait before loading page

	CookieStoreEV cookieStore;

	private static org.apache.log4j.Logger log = Logger.getLogger(ClReqFilterCookies.class);
}
