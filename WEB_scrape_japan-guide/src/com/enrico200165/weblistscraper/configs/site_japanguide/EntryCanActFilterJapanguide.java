package com.enrico200165.weblistscraper.configs.site_japanguide;

import com.enrico200165.weblistscraper.marketingDB.*;
import org.apache.log4j.Logger;

import com.enrico200165.weblistscraper.tools.EntryCanActOnFilter;

/**
 * Evauates whether we can contact a prospect Based on contact history, and *only* on that,
 * 
 * FOR NOW analyzing other things than contact history, or having a simple functionoid to decide which contact message, including none, send
 * to a prospect is avoided, I try to do basic steps that do/analyze only one thing
 * 
 * @author enrico
 *
 */
public class EntryCanActFilterJapanguide extends EntryCanActOnFilter {

	@Override
	public boolean canAct(ProspectCoreData prospect, Contact contact) {

		if (prospect.getUserID().toLowerCase().equals("amrando"))
			return true;

		// vecchio codice
		boolean ret = true;
		ret =  myUser(prospect) && !prospect.getUserID().equals("italianpenpals");

		return false;
	}

	// ########## (INTERNAL) LIBRARY FUNCTIONS ######### 
	public boolean myUser(ProspectCoreData prospect) {
		return false || prospect.getUserID().equals("enrico") || prospect.getUserID().equals("armando")
				|| prospect.getUserID().equals("armand") || prospect.getUserID().equals("italianpenpals");
	}

	private static org.apache.log4j.Logger log = Logger.getLogger(EntryCanActFilterJapanguide.class);

}
