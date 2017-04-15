package com.enrico200165.weblistscraper.configs.site_italianpenpals;

import com.enrico200165.weblistscraper.configs.HostConfigABC;

import java.net.URI;
import java.net.URISyntaxException;

public class HostConfigItalianPenPals extends HostConfigABC {


	public HostConfigItalianPenPals(String loginPropertiesFile) {
		super(loginPropertiesFile);
		
		try {
			this.baseHostURI = new URI("http" /*scheme*/, 
					 null/*userInfo*/, 
					 "italianpenpals.org" /*host*/, 
					 80 /*port*/, 
					 null /*path*/, 
					 null /*query*/, 
					 null/*fragment*/);
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
}
