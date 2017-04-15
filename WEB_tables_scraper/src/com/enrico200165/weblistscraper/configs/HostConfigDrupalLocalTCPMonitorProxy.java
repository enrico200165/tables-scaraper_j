package com.enrico200165.weblistscraper.configs;

public class HostConfigDrupalLocalTCPMonitorProxy extends HostConfigABC {

	HostConfigDrupalLocalTCPMonitorProxy(String loginPropertyFile) {
		super(loginPropertyFile);
	}

	@Override
	public String getHostNoport() {
		return "http://drupaldev.local:9080";
	}

	@Override
	public String getLoginFormAction() {
		//	
		return "node?destination=node";
		//	return  "user";
	}

	@Override
	public String getLoginPageURL() {
		return "user";
	}

	@Override
	public String getLFormSelector() {
		return "user";
	}

	@Override
	public String getHost() {
		return null;
	}

}
