package com.enrico200165.weblistscraper.concorsi_it.configs;

import com.enrico200165.weblistscraper.configs.HostConfigABC;
import org.apache.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;

public class HostConfigConcorsiIt extends HostConfigABC {


	public HostConfigConcorsiIt() {
		super("none");
		
		try {
			this.baseHostURI = new URI("http" /*scheme*/, 
					 null/*userInfo*/, 
					 "concorsi.it" /*host*/, 
					 80 /*port*/, 
					 null /*path*/, 
					 null /*query*/, 
					 null/*fragment*/);
			log.debug(baseHostURI);
			user = "enrico";
			password = "ocirne00";
			
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
	@Override
	public String getHostNoport() {
		return baseHostURI.getHost();
	}
	

	@Override
	public String getLoginFormAction() {
		return  "/user";
	}

	@Override
	public String getLoginPageURL() {
		return "user";
	}

	@Override
	public String getLFormSelector() {
		return "#user-login";
		// return "#user-login-form";
	}

	@Override
	public String getHost() {
		return baseHostURI.getHost();
	}

	private static org.apache.log4j.Logger log = Logger.getLogger(HostConfigConcorsiIt.class);

}
