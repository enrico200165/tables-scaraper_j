package com.enrico200165.weblistscraper.configs;

public class HostConfigVanilla extends HostConfig {

	public HostConfigVanilla() {
	}

	public HostConfigVanilla(String loginPropertiesFile) {
		super(loginPropertiesFile);
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
	public String getJsoupLoginFormSelector() {
		return "#user-login";
		// return "#user-login-form";
	}

	@Override
	public String getHost() {
		return baseHostURI.getHost();
	}
}
