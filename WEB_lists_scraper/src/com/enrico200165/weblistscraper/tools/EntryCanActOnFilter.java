package com.enrico200165.weblistscraper.tools;

import com.enrico200165.weblistscraper.marketingDB.*;

/**
 * Based on contact history, and only on that, tess whether we can contact a prospect
 * 
 * FOR NOW analyzing  other things than contact history, or having a simple functionoid
 * to decide which contact message, including none, send to a prospect is avoided,
 * I try to do basic steps that do/analyze only one thing
 * @author enrico
 *
 */
public abstract class EntryCanActOnFilter {
	
	abstract public boolean canAct(ProspectCoreData prospect, Contact contact);

}
