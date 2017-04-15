package com.enrico200165.weblistscraper.configs.site_japanguide;

import com.enrico200165.weblistscraper.configs.SessionLimitsBase;

public class SessionLimitsJapanguide extends SessionLimitsBase {

	@Override
	public int maxEntriesRead() {
		return 100;
	}

	public int maxNewProspectsSaves() {
		return 0;
	}

	public int maxContactsExec() {
//		return 20;
		return 1; // unlimited
	}

	@Override
	public int maxProcess() {
		return 0;
	}

	@Override
	public int maxHTTPCalls() {
		return 0;
	}
}
