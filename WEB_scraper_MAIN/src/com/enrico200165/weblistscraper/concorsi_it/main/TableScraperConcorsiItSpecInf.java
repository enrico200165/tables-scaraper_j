package com.enrico200165.weblistscraper.concorsi_it.main;

import com.enrico200165.weblistscraper.configs.PageConfigABC;
import com.enrico200165.weblistscraper.page.EntryProcessorABC;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import com.enrico200165.weblistscraper.tools.Result;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * @author enrico
 * 
 *         Verificare: gestisce il parsing di una tabella
 *
 */
public class TableScraperConcorsiItSpecInf extends TableScraperConcorsiBase {

	public TableScraperConcorsiItSpecInf(SessionManagerAbstr smgrPar, PageConfigABC tcfgPar, EntryProcessorABC ep) {
		super(smgrPar,tcfgPar,ep);
	}


	@Override
	protected void initEntryProcessor(EntryProcessorABC entryProc) {
	}

	
	@Override
	public boolean tableNotFoundOK(Result res) {
		return true;
	}

	static Logger log=Logger.getLogger(TableScraperConcorsiItSpecInf.class.getSimpleName());
}
