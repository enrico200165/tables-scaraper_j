package com.enrico200165.weblistscraper.concorsi_it;

import com.enrico200165.weblistscraper.marketingDB.Contact;
import com.enrico200165.weblistscraper.marketingDB.ProspectCoreData;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
public class EntryCanActOnFilterConcorsiIt extends EntryCanActOnFilter {

	@Override
	public boolean canAct(ProspectCoreData prospectWrap, Contact contact) {
		return false;
	}

	// ########## (INTERNAL) LIBRARY FUNCTIONS ######### 
	public boolean myUser(ProspectConcorsiIt prospect) {
		return false || prospect.it().getUserID().equals("enrico") || prospect.it().getUserID().equals("armando")
				|| prospect.it().getUserID().equals("armand") || prospect.it().getUserID().equals("italianpenpals");
	}

	private static Logger log = LogManager.getLogger(EntryCanActOnFilterConcorsiIt.class.getName());

}
