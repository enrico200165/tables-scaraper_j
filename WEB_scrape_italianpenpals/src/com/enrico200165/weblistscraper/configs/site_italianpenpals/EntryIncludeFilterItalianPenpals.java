package com.enrico200165.weblistscraper.configs.site_italianpenpals;

import com.enrico200165.weblistscraper.marketingDB.ProspectCoreData;
import com.enrico200165.weblistscraper.tools.EntryIncludeFilter;
import org.jsoup.nodes.Element;

public class EntryIncludeFilterItalianPenpals extends EntryIncludeFilter {

	EntryIncludeFilterItalianPenpals() {
	}

	@Override
	public boolean includeEntrySpecific(Element entry) {
		// Al momento nulla da includere a livello di entry
		return true;
	}

	@Override
	public boolean includeProspectSpecific(ProspectCoreData prospectCastIt) {
		return true;
		// return justToTest(prospect);
	}

	// ########## (INTERNAL) LIBRARY FUNCTIONS #########
	public boolean justToTest(ProspectItalianPenPals prospect) {
		return (prospect.it().getUserID().length() > 7);
	}

}