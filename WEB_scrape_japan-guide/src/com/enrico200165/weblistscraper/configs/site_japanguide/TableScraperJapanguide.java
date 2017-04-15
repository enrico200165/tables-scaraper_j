package com.enrico200165.weblistscraper.configs.site_japanguide;

import com.enrico200165.weblistscraper.configs.PageConfigABC;
import com.enrico200165.weblistscraper.marketingDB.DBManagerMKT;
import com.enrico200165.weblistscraper.page.EntryProcessorABC;
import com.enrico200165.weblistscraper.page.TableScraperABC;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import com.enrico200165.weblistscraper.tools.Result;
import org.apache.log4j.Logger;

public class TableScraperJapanguide extends TableScraperABC {

	public TableScraperJapanguide(SessionManagerAbstr smgrPar, PageConfigABC tpPar, EntryProcessorABC epPar) {
		super(smgrPar,tpPar,epPar);
	}

	@Override
	public boolean finalizeTableScraping() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean initStorage() {
		getContactHistory(/* String persistence_unit_name */);
		return true;
	}

	static DBManagerMKT DBManagerMKT = null;

	public DBManagerMKT getContactHistory(/* String persistence_unit_name */) {
		if (DBManagerMKT == null) {
			DBManagerMKT = new DBManagerMKT("marketing");
		}
		return DBManagerMKT;
	}

	@Override
	protected void initEntryProcessor(EntryProcessorABC entryProc) {
		this.setEntryProcessor(entryProc);
	}

	@Override
	public boolean tableNotFoundOK(Result res) {
		log.error("da impelemntare, esco");
		System.exit(1);
		return false;
	}

	private static org.apache.log4j.Logger log = Logger.getLogger(TableScraperJapanguide.class);
}
