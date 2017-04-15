package com.enrico200165.weblistscraper.tools;


import com.enrico200165.weblistscraper.marketingDB.*;
import org.jsoup.nodes.Element;

import com.enrico200165.weblistscraper.session.SessionManagerAbstr;

public abstract class EntryIncludeFilter extends FilterBase {

	public EntryIncludeFilter(SessionManagerAbstr smpar) {
		super(smpar);
		// TODO Auto-generated constructor stub
	}

	abstract public boolean includeEntrySpecific(Element entry);

	public boolean includeHTMLEntry(Element entry) {
		boolean ret = false;
		if (ret = includeEntrySpecific(entry)) {
			incrTrueEntry();
		} else {
			incrFalseEntry();
		}
		return ret;
	}

	abstract public boolean includeProspectSpecific(ProspectCoreData prospectCastIt);

	public boolean includeProspect(ProspectCoreData prospectCastIt) {
		boolean ret;
		if (ret = includeProspectSpecific(prospectCastIt)) {
			incrTrueProspect();
		} else {
			incrFalseProspect();
		}
		return ret;
	}

}
