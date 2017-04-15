package com.enrico200165.weblistscraper.configs.site_italianpenpals;

import com.enrico200165.weblistscraper.marketingDB.*;
import org.jsoup.nodes.Element;

import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import com.enrico200165.weblistscraper.tools.EntryIncludeFilter;

public class EntryIncludeFilterItalianPenpals extends EntryIncludeFilter {

	EntryIncludeFilterItalianPenpals(SessionManagerAbstr smpar) {
		super(smpar);
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