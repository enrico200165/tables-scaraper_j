package com.enrico200165.weblistscraper.configs.site_italianpenpals;

import com.enrico200165.weblistscraper.configs.SessionLimitsBase;

public class SessionLimitsItalianPenPals extends SessionLimitsBase {


	public int maxNewProspectsSaves() {
		return 0;
	}

	public int maxContactsExec() {
//		return 20;
		return 0; // unlimited
	}

}
