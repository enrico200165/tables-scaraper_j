package com.enrico200165.weblistscraper.tools;


import com.enrico200165.weblistscraper.marketingDB.ProspectCoreData;
import org.jsoup.nodes.Element;


import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Level;


public class EntryIncludeFilterVanilla extends com.enrico200165.weblistscraper.tools.EntryIncludeFilter {

	public EntryIncludeFilterVanilla() {
	}

	public boolean includeEntrySpecific(Element entry) {
		log.log(Level.SEVERE, "to be implemented");
		return false;
	}

	public boolean includeHTMLEntry(Element entry) {
		boolean ret = false;
		if (ret = includeEntrySpecific(entry)) {
			incrTrueEntry();
		} else {
			incrFalseEntry();
		}
		return ret;
	}

	public boolean includeProspectSpecific(ProspectCoreData prospectCastIt) {
		log.log(Level.SEVERE, "to be implemented");
		return false;
	}

	public boolean includeProspect(ProspectCoreData prospectCastIt) {
		boolean ret;
		if (ret = includeProspectSpecific(prospectCastIt)) {
			incrTrueProspect();
		} else {
			incrFalseProspect();
		}
		return ret;
	}

	private static Logger log = LogManager.getLogManager().getLogger(EntryIncludeFilterVanilla.class.getSimpleName());

}
