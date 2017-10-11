package com.enrico200165.weblistscraper.configs.site_italianpenpals;

import com.enrico200165.weblistscraper.configs.ChannelIFC;
import com.enrico200165.weblistscraper.configs.HostConfig;
import com.enrico200165.weblistscraper.configs.PageConfigABC;
import com.enrico200165.weblistscraper.page.EntryProcessorABC;
import com.enrico200165.weblistscraper.page.NextTablePageSelectorsABC;
import com.enrico200165.weblistscraper.page.TableScraperABC;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import com.enrico200165.weblistscraper.tools.EntryCanActOnFilter;
import com.enrico200165.weblistscraper.tools.EntryExcludeFilter;
import com.enrico200165.weblistscraper.tools.EntryIncludeFilter;
import org.apache.log4j.Logger;

public class PageConfigItalianPenPals extends PageConfigABC {

	public PageConfigItalianPenPals(HostConfig hcPar, TableScraperABC ts, EntryCanActOnFilter entryCanActOnPar, ChannelIFC channPar) {
		super(hcPar,ts,entryCanActOnPar,channPar);
		if (ts == null)
			log.info("ts == null");
	}

	// --- TABLE ---
	@Override
	public String TableSelectCSS() {
		return ".views-table";
	}

	@Override
	public String EntrySelectCSS() {
		return "tbody tr";
	}

	@Override
	public EntryProcessorABC getEntryProcObject(String configID) {
		return new EntryProcessorItalianPenPals(null, null, this);
	}

	public TableScraperABC getTableScraperObject() {
		return tableScraper;
//		return new TableScraperItalianPenPals(cfg,null , this, null);
	}

	@Override
	public NextTablePageSelectorsABC getNextTablePageSelectorsSpecific() {
		return new NextTablePageSelectorsItalianPenPals();
	}

	@Override
	public EntryIncludeFilter getEntryIncludeFilterSpecific(SessionManagerAbstr smPar) {
		return new EntryIncludeFilterItalianPenpals(smPar);
	}

	@Override
	public EntryExcludeFilter getEntryExcludeFilterSpecific(SessionManagerAbstr smPar) {
		return new EntryExcludeFilterItalianPenpals(smPar);
	}

	public EntryProcessorABC getEntryProc(String configID) {

		/* if (configID.equals("")) { if (entryProcSpec == null) { entryProcSpec = new
		 * EntryProcessorConcorsiItSpecInf(cfg.getSessionManagerAbstr(), getClientWrapper(cl), this); } return entryProcSpec; } else {
		 * log.error("getEntryProc(String configID) non gestito: " + configID); System.exit(1); return null; } */
		log.error("da riscrivere, esco");
		return null;
	}

	// --- TABLE END ---

	private static org.apache.log4j.Logger log = Logger.getLogger(PageConfigItalianPenPals.class);

}
