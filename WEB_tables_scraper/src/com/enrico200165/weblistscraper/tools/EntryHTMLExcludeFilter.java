package com.enrico200165.weblistscraper.tools;

import com.enrico200165.weblistscraper.marketingDB.ProspectCoreData;
import org.jsoup.nodes.Element;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntryHTMLExcludeFilter extends EntryExcludeFilter {

	public EntryHTMLExcludeFilter() {
	}

	public boolean exclude(Element entry,  StringBuffer comments) {
		log.error("da implementare");
		return false;
	}

	public boolean excludeHTMLEntry(Element entry, StringBuffer comments) {
		boolean ret = exclude(entry,comments);
		if (ret)
			incrTrueEntry();
		else
			incrFalseEntry();
		return ret;
	}

	public boolean excludeTypedEntry(Object castMeInDerivedFilter) {
		log.error("da implementare");
		return false;

	}


	// EV!!!quick and dirty, implementata per fretta, dovrebbe essere abstract
	public boolean excludeTextual(String text, StringBuffer annotations)  {
		return false;
	}


	public boolean exclude(ProspectCoreData entryW) {
		boolean ret = excludeTypedEntry(entryW);
		if (ret)
			incrTrueProspect();
		else
			incrFalseProspect();
		return ret;
	}

	private static Logger log = LogManager.getLogger(EntryHTMLExcludeFilter.class.getSimpleName());
}
