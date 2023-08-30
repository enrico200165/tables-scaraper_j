package com.enrico200165.weblistscraper.session;

import com.enrico200165.weblistscraper.page.PageProcessResult;
import com.enrico200165.weblistscraper.tools.ClientWrapper;


import com.enrico200165.weblistscraper.configs.SessionLimitsBase;
import com.enrico200165.weblistscraper.page.PageProcDescr;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Level;

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
				log.log(Level.SEVERE, "errore elaborando pDescr: ");
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
		log.log(Level.WARNING,  "normally a derivd class should override this method");
	}

	
	
	private static Logger log = LogManager.getLogManager().getLogger(SessionManager.class.getSimpleName());
}
