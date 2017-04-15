package com.enrico200165.weblistscraper.configs.site_italianpenpals;

import com.enrico200165.weblistscraper.marketingDB.*;
import com.enrico200165.weblistscraper.tools.EntryCanActOnFilter;
import org.apache.log4j.Logger;

/**
 * Evauates whether we can contact a prospect Based on contact history, and *only* on that,
 * 
 * FOR NOW analyzing other things than contact history, or having a simple functionoid to decide which contact message, including none, send
 * to a prospect is avoided, I try to do basic steps that do/analyze only one thing
 * 
 * @author enrico
 *
 */
public class EntryCanAcOntFilterItalianPenPals extends EntryCanActOnFilter {

	@Override
	public boolean canAct(ProspectCoreData prospect, Contact contact) {
		boolean ret = true;
		ProspectItalianPenPals prospectI = new ProspectItalianPenPals(prospect);
		//
		ret =  myUser(prospectI) && !prospect.getUserID().equals("italianpenpals");
		
		if (prospect.getUserID().equals("enrico"))  {
			log.info("");
		}
		
		return ret;
	}

	// ########## (INTERNAL) LIBRARY FUNCTIONS ######### 
	public boolean myUser(ProspectItalianPenPals prospect) {
		return false || prospect.it().getUserID().equals("enrico") || prospect.it().getUserID().equals("armando")
				|| prospect.it().getUserID().equals("armand") || prospect.it().getUserID().equals("italianpenpals");
	}

	private static org.apache.log4j.Logger log = Logger.getLogger(EntryCanAcOntFilterItalianPenPals.class);

}
