package com.enrico200165.weblistscraper.page;

import com.enrico200165.weblistscraper.configs.PageConfigABC;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;

import com.enrico200165.weblistscraper.configs.HostConfig;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;/**
 * @author enrico
 * 
 *         ipotesi a posteriori: Elabora una pagina, eventualmente contenente una tabella, fa questo leggendo il descrittore della pagina ed
 *         invocando le operazioni (GET, POST ...) in esso contenute
 *
 */
public abstract class PageProcessorAbstr {

	public PageProcessorAbstr(HostConfig hostConfigPar, SessionManagerAbstr sesMgrPar) {
		super();
		sesMgr = sesMgrPar;
		hostConfig = hostConfigPar;
	}

	abstract public void initTableScraperSpecific(TableScraperABC ts);

	public void initTableScraperCommon(SessionManagerAbstr smgr, TableScraperABC ts) {
		ts.setSessionManager(smgr);
		initTableScraperSpecific(ts);
	}

	public abstract PageProcessResult process(PageProcDescr p);

	public PageConfigABC getTableOrPageConfig() {
		return this.tableOrPageConfig;
	}

	public void setTableOrPageConfig(PageConfigABC par) {
		this.tableOrPageConfig = par;
	}

	protected PageConfigABC tableOrPageConfig;
	protected SessionManagerAbstr sesMgr;

	HostConfig hostConfig;

	private static Logger log = LogManager.getLogManager().getLogger(PageProcessorAbstr.class.getSimpleName());
}
