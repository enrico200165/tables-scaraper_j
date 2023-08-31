package com.enrico200165.weblistscraper.tools;

import com.enrico200165.weblistscraper.marketingDB.ProspectCoreData;

import org.jsoup.nodes.Element;


import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Level;


public class EntryExcludeFilterVanilla extends EntryExcludeFilter {

	public EntryExcludeFilterVanilla() {
	}

	public boolean exclude(Element entry,  StringBuffer comments) {
		log.log(Level.SEVERE, "da implementare");
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
		log.log(Level.SEVERE, "da implementare");
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

	static Logger log=Logger.getLogger(EntryExcludeFilterVanilla.class.getName());
}
