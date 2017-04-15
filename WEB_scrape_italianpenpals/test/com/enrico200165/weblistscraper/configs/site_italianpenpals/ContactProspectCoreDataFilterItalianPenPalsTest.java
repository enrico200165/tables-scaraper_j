package com.enrico200165.weblistscraper.configs.site_italianpenpals;

import com.enrico200165.weblistscraper.marketingDB.*;

import com.enrico200165.weblistscraper.tools.EntryCanActOnFilter;
import org.junit.Test;

public class ContactProspectCoreDataFilterItalianPenPalsTest {

	@Test
	public void testCanContact() {
		
		EntryCanActOnFilter o = new EntryCanAcOntFilterItalianPenPals();
		
		ProspectCoreData prospect = new ProspectCoreData();
		prospect.setUserID("italianpenpals");
		assert(o.canAct(prospect, null));
		prospect.setUserID("enrico");
		assert(o.canAct(prospect, null));
		prospect.setUserID("xxx");
		assert(o.canAct(prospect, null));
	}

}
