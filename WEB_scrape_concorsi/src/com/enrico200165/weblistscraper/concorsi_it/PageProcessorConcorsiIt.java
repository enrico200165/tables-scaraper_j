package com.enrico200165.weblistscraper.concorsi_it;

import com.enrico200165.weblistscraper.page.TableScraperABC;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import org.apache.log4j.Logger;

import com.enrico200165.weblistscraper.concorsi_it.entities.ConcorsiDB;
import com.enrico200165.weblistscraper.configs.HostConfigABC;
import com.enrico200165.weblistscraper.configs.PageConfigABC;
import com.enrico200165.weblistscraper.page.PageProcessor;

public class PageProcessorConcorsiIt extends PageProcessor {

	public PageProcessorConcorsiIt(HostConfigABC hcPar, SessionManagerAbstr sess, PageConfigABC tcfgPar) {
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

		log.error("Type of table scaraper not managed, addMap it. EXITING");
		System.exit(1);
	}

	SessionManagerAbstr sesMgr;
	ConcorsiDB concDB;

	private static org.apache.log4j.Logger log = Logger.getLogger(PageProcessorConcorsiIt.class);
}
