package com.enrico200165.weblistscraper.session;

import com.enrico200165.weblistscraper.page.PageProcessResult;
import com.enrico200165.weblistscraper.tools.ClientWrapper;


import com.enrico200165.weblistscraper.configs.SessionLimitsBase;
import com.enrico200165.weblistscraper.page.PageProcDescr;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SessionManager extends SessionManagerAbstr {

	public SessionManager(SessionLimitsBase sessLimPar, ClientWrapper cwPar) {
		super(sessLimPar,cwPar);
		entriesEsaminate = 0;
		rawEntriesEsaminate = 0;
	}

	@Override
	public boolean performSessionSpecific(SessionLimitsBase sl) {
		boolean ret = true;

		PageProcessResult res = null;
		for (PageProcDescr descr : pageProcDescrs) {
			res = descr.getProcessor().process(descr); // originary page

			while (res.result.isOk() && res.result.getContinua() && res.hasNexPage()) {
				// pages found by previous page (ex. following a "next" link)
				PageProcDescr descrOther = res.getpPDescr();
				res = descrOther.getProcessor().process(res.getpPDescr());
			}
			if (!res.result.isOk()) {
				log.error("errore elaborando pDescr: ");
				ret = false;
				break;
			}
		}
		if (ret) log.info("------------------------ OK FINITO  ---------------------------");
		return ret;
	}

	@Override
	public void initSession() {
	}

	@Override
	public void finalizeSession() {
		log.warn("normally a derivd class should override this method");
	}

	
	
	private static Logger log = LogManager.getLogger(SessionManager.class.getSimpleName());
}
