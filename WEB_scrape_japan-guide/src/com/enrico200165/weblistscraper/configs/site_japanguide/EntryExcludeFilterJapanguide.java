package com.enrico200165.weblistscraper.configs.site_japanguide;

import com.enrico200165.weblistscraper.marketingDB.ProspectCoreData;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import com.enrico200165.weblistscraper.tools.EntryExcludeFilter;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class EntryExcludeFilterJapanguide extends EntryExcludeFilter {

	EntryExcludeFilterJapanguide(SessionManagerAbstr smpar) {
		super(smpar);
	}

	@Override
	public boolean exclude(Element entry, StringBuffer comments) {
		Elements rows = null;

		// escludiamo tabella form di ricerca
		if (!entry.text().toLowerCase().contains("interests")) {
			return true;
		}

		return false;
	}

	@Override
	public boolean excludeTypedEntry(Object prospectCastMe) {
		ProspectCoreData prospect = (ProspectCoreData) prospectCastMe;
		// return myUser(prospect);
		return false;
	}

	// ########## (INTERNAL) LIBRARY FUNCTIONS ######### 
	public boolean myUser(ProspectJapanguide prospect) {
		return false || prospect.it().getUserID().equals("enrico") || prospect.it().getUserID().equals("armando")
				|| prospect.it().getUserID().equals("armand") || prospect.it().getUserID().equals("italianpenpals");
	}

	private static org.apache.log4j.Logger log = Logger.getLogger(EntryExcludeFilterJapanguide.class);

}
