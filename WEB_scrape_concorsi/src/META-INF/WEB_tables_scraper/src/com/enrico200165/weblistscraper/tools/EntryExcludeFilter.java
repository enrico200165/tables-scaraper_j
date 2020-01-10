package com.enrico200165.weblistscraper.tools;

import org.jsoup.nodes.Element;

import com.enrico200165.weblistscraper.marketingDB.ProspectCoreData;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;

public abstract class EntryExcludeFilter extends FilterBase {

	public EntryExcludeFilter(SessionManagerAbstr smpar) {
		super(smpar);
	}

	abstract public boolean exclude(Element entry,  StringBuffer comments);
	
	public boolean excludeHTMLEntry(Element entry, StringBuffer comments) {
		boolean ret = exclude(entry,comments);
		if (ret)
			incrTrueEntry();
		else
			incrFalseEntry();
		return ret;
	}

	abstract public boolean excludeTypedEntry(Object castMeInDerivedFilter);
	
	
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

}
