package com.enrico200165.weblistscraper.configs.site_japanguide;

import com.enrico200165.weblistscraper.configs.SessionLimitsBase;

public class SessionLimitsJapanguide extends SessionLimitsBase {

	@Override
	public int getMaxEntriesRead() {
		return 100;
	}

	public int getMaxNewProspectsSaves() {
		return 0;
	}

	public int getMaxContactsExec() {
//		return 20;
		return 1; // unlimited
	}

	@Override
	public int getMaxProcess() {
		return 0;
	}

	@Override
	public int getMaxHTTPCalls() {
		return 0;
	}
}
