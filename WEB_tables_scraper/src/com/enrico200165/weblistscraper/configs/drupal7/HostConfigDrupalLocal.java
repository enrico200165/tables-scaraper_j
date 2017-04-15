package com.enrico200165.weblistscraper.configs.drupal7;

import com.enrico200165.weblistscraper.configs.HostConfigABC;

public class HostConfigDrupalLocal extends HostConfigABC {

	public HostConfigDrupalLocal(String loginPropertyFile) {
		super(loginPropertyFile);
	}

	@Override
	public String getHostNoport() {
		return "http://drupaldev.local";
	}

	@Override
	public String getLoginFormAction() {
		// return  "node?destination=node";
		return "node/3?destination=node/1";
	}

	@Override
	public String getLoginPageURL() {
		return "user";
	}

	@Override
	public String getLFormSelector() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHost() {
		return null;
	}

}
