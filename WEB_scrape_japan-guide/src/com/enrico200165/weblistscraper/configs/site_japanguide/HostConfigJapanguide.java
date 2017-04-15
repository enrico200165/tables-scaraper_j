package com.enrico200165.weblistscraper.configs.site_japanguide;

import com.enrico200165.weblistscraper.configs.HostConfigABC;

import java.net.URI;
import java.net.URISyntaxException;

public class HostConfigJapanguide extends HostConfigABC {

	public HostConfigJapanguide(String loginPropertyFile) {
		super(loginPropertyFile);
		
		
		try {
			this.baseHostURI = new URI("http" /*scheme*/, 
					 null/*userInfo*/, 
					 "www.japan-guide.com" /*host*/,
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
	public String getUserName() {
		return "armandoitaly1990@gmail.com";
	}

	@Override
	public String getPassword() {
		return "ocirne00";
	}

	@Override
	public String getLoginFormAction() {
		return  "s://login/";
	}

	@Override
	public String getLoginPageURL() {
		return this.loginFormURL;
	}

	@Override
	public String getLFormSelector() {

		//return "form[action=login.html]";
		return "form[action=https://www.japan-guide.com/login/]";

	}

	@Override
	public String getHost() {
		return "www.japan-guide.com";
	}
}
