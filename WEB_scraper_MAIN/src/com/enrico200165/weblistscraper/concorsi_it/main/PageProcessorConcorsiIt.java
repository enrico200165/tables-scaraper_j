package com.enrico200165.weblistscraper.concorsi_it.main;

import com.enrico200165.weblistscraper.page.TableScraperABC;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;

import com.enrico200165.weblistscraper.concorsi_it.main.entities.ConcorsiDB;
import com.enrico200165.weblistscraper.configs.HostConfig;
import com.enrico200165.weblistscraper.configs.PageConfigABC;
import com.enrico200165.weblistscraper.page.PageProcessor;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;

public class PageProcessorConcorsiIt extends PageProcessor {

	public PageProcessorConcorsiIt(HostConfig hcPar, SessionManagerAbstr sess, PageConfigABC tcfgPar) {
		super( hcPar, sess, tcfgPar);
	}

	public void init(SessionManagerAbstr sesPar, ConcorsiDB dbPar) {
		this.sesMgr = sesPar;
		concDB = dbPar;
	}

	@Override
	public void initTableScraperSpecific(TableScraperABC tsPar) {

		assert (concDB != null);

		if (tsPar.getClass() == TableScraperConcorsiItGeoTable.class) {
			TableScraperConcorsiItGeoTable ts = (TableScraperConcorsiItGeoTable) tsPar;
			ts.init(sesMgr);
			return;
		} else if (tsPar.getClass() == TableScraperConcorsiItSearch.class) {
			TableScraperConcorsiItSearch ts = (TableScraperConcorsiItSearch) tsPar;
			ts.init(sesMgr);
			return;
		} else if (tsPar.getClass() == TableScraperConcorsiItSpecInf.class) {
			TableScraperConcorsiItSpecInf ts = (TableScraperConcorsiItSpecInf) tsPar;
			ts.init(sesMgr);
			return;
		}

		log.log(Level.SEVERE, "Type of table scaraper not managed, addMap it. EXITING");
		System.exit(1);
	}

	SessionManagerAbstr sesMgr;
	ConcorsiDB concDB;

	static Logger log=Logger.getLogger(PageProcessorConcorsiIt.class.getSimpleName());
}
