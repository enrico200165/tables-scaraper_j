package com.enrico200165.weblistscraper.configs.site_italianpenpals;

import com.enrico200165.weblistscraper.configs.PageConfigABC;
import com.enrico200165.weblistscraper.marketingDB.DBManagerMKT;
import com.enrico200165.weblistscraper.page.EntryProcessorABC;
import com.enrico200165.weblistscraper.page.TableScraperABC;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import com.enrico200165.weblistscraper.tools.Result;
import org.apache.log4j.Logger;

public class TableScraperItalianPenPals extends TableScraperABC {

	public TableScraperItalianPenPals(SessionManagerAbstr smgrPar, PageConfigABC tcfgPar, EntryProcessorABC ep) {
		super(smgrPar,tcfgPar,ep);
		if (ep == null)
			log.info("entryProcessor == null");
	}

	


	@Override
	public boolean finalizeTableScraping() {
		return false;
	}


	
	@Override
	public boolean initStorage() {
		getContactHistory(/* String persistence_unit_name*/);
		return true;
	}

	static DBManagerMKT DBManagerMKT = null;
	public DBManagerMKT getContactHistory(/* String persistence_unit_name*/) {
		if (DBManagerMKT == null) {
			DBManagerMKT = new DBManagerMKT("marketing");
		}
		return DBManagerMKT;
	}




	@Override
	protected void initEntryProcessor(EntryProcessorABC entryProc) {
		EntryProcessorItalianPenPals p = (EntryProcessorItalianPenPals) entryProc;
		p.setDBManagerMKT(getContactHistory());
	}




	@Override
	public boolean tableNotFoundOK(Result res) {
		log.error("da implementare, esco");
		System.exit(1);
		return false;
	}
	
	private static org.apache.log4j.Logger log = Logger.getLogger(TableScraperItalianPenPals.class);
}
