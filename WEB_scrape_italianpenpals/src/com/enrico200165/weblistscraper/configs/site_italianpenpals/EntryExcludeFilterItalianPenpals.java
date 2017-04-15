package com.enrico200165.weblistscraper.configs.site_italianpenpals;

import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import com.enrico200165.weblistscraper.tools.EntryExcludeFilter;
import org.jsoup.nodes.Element;

public class EntryExcludeFilterItalianPenpals extends EntryExcludeFilter {

	EntryExcludeFilterItalianPenpals(SessionManagerAbstr smpar) {
		super(smpar);
	}

	@Override
	public boolean exclude(Element entry, StringBuffer comments) {
		return false;
	}

	@Override
	public boolean excludeTypedEntry(Object prospectCastMe) {
		ProspectItalianPenPals prospect = (ProspectItalianPenPals) prospectCastMe;
		// return myUser(prospect);
		return false;
	}

	// ########## (INTERNAL) LIBRARY FUNCTIONS ######### 
	public boolean myUser(ProspectItalianPenPals prospect) {
		return false || prospect.it().getUserID().equals("enrico") || prospect.it().getUserID().equals("armando")
				|| prospect.it().getUserID().equals("armand") || prospect.it().getUserID().equals("italianpenpals");
	}

}
