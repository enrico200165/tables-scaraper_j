package com.enrico200165.weblistscraper.concorsi_it;

import com.enrico200165.weblistscraper.marketingDB.ProspectCoreData;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import com.enrico200165.weblistscraper.tools.EntryIncludeFilter;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class EntryIncludeFilterConcorsiIt extends EntryIncludeFilter {

	public EntryIncludeFilterConcorsiIt(SessionManagerAbstr smpar) {
		super(smpar);
	}

	@Override
	public boolean includeEntrySpecific(Element entry) {
		// Al momento nulla da includere a livello di entry
		return false;
	}

	@Override
	public boolean includeProspectSpecific(ProspectCoreData prospect) {
		return true;
		// return justToTest(prospect);
	}

	// ########## (INTERNAL) LIBRARY FUNCTIONS #########
	public boolean justToTest(ProspectConcorsiIt prospect) {
		return (prospect.it().getUserID().length() > 7);
	}

	
	ArrayList<String> jobLocation; 
	void buildJjobLocation() {
		jobLocation = new ArrayList<String>();
	}

	
}