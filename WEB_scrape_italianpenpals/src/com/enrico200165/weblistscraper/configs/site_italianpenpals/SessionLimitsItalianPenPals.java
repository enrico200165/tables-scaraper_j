package com.enrico200165.weblistscraper.configs.site_italianpenpals;

import com.enrico200165.weblistscraper.configs.SessionLimitsBase;

public class SessionLimitsItalianPenPals extends SessionLimitsBase {

	@Override
	public int maxEntriesRead() {
		return 0;
	}

	public int maxNewProspectsSaves() {
		return 0;
	}

	public int maxContactsExec() {
//		return 20;
		return 0; // unlimited
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
