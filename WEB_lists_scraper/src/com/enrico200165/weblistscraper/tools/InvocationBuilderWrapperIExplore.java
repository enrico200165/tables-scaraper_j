package com.enrico200165.weblistscraper.tools;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Level;


public class InvocationBuilderWrapperIExplore extends InvocationBuilderWrapper {

	public InvocationBuilderWrapperIExplore() {
		super();
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
		// chrome
		// bld.header("Connection", "keep-alive");
		// bld.header("Accept",
		// "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		// bld.header("Accept-Encoding", "gzip, deflate, sdch");
		// "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.130 Safari/537.36");
		// bld.header("Accept-Language", "it-IT,it;q=0.8,en-US;q=0.6,en;q=0.4");

		// IE

		// bld.header("Accept", "text/html, application/xhtml+xml, */*");
		// bld.header("Accept-Language", "it-IT");
		// bld.header("Accept-Encoding", "gzip, deflate");
		// bld.header("DNT", "1");
		// bld.header("Connection", "Keep-Alive");
	}

	protected InvocationBuilderWrapperIExplore adjustHeader(String name) {
		String newVal = headers.get(name);
		if (newVal != null && newVal.length() > 0)
			this.bld.header(name, newVal);
		else {
			log.log(Level.SEVERE, "header non trovato: " + name);
			System.exit(1);
		}
		return this;
	}

	@Override
	public InvocationBuilderWrapper onSet() {
		adjustHeader("User-Agent");
		return this;
	}

	static Logger log=Logger.getLogger(InvocationBuilderWrapperIExplore.class.getSimpleName());
}