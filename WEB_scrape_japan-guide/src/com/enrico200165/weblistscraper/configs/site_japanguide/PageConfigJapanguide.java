package com.enrico200165.weblistscraper.configs.site_japanguide;

import com.enrico200165.weblistscraper.configs.ChannelIFC;
import com.enrico200165.weblistscraper.configs.HostConfig;
import com.enrico200165.weblistscraper.configs.PageConfigABC;
import com.enrico200165.weblistscraper.tools.EntryCanActOnFilter;
import com.enrico200165.weblistscraper.page.EntryProcessorABC;
import com.enrico200165.weblistscraper.page.NextTablePageSelectorsABC;
import com.enrico200165.weblistscraper.page.TableScraperABC;
import com.enrico200165.weblistscraper.tools.EntryExcludeFilter;
import com.enrico200165.weblistscraper.tools.EntryIncludeFilter;
import org.apache.log4j.Logger;

public class PageConfigJapanguide extends PageConfigABC {

	public PageConfigJapanguide(HostConfig hcPar, TableScraperABC ts, EntryCanActOnFilter entryCanActOnPar, ChannelIFC channelInfoPar) {
		super( hcPar,  ts,  entryCanActOnPar,  channelInfoPar);
	}

	@Override
	public EntryExcludeFilter getEntryExcludeFilterSpecific() {
		return new EntryExcludeFilterJapanguide();
	}

	@Override
	public EntryIncludeFilter getEntryIncludeFilterSpecific() {
		return new EntryIncludeFilterJapanguide();
	}

	@Override
	public NextTablePageSelectorsABC getNextTablePageSelectorsSpecific() {
		return new NextTablePageSelectorsJapanGuide();
	}

	@Override
	public String TableSelectCSS() {
		return "#section_mainContent";
		//"#section_mainContent > center:nth-child(8) > table"
	}

	@Override
	public String EntrySelectCSS() {
		// return "center > table"; // contiene tabelle nidificate, quindi deve esser figlia di primao livello
		return "center"; // contiene tabelle nidificate, quindi deve esser figlia di primao livello
	}

	boolean localTestRun;

	@Override
	public EntryProcessorABC getEntryProcObject(String configID) {
		return new EntryProcessorJapanguide(null, null,this);
	}

	public TableScraperABC getTableScraperObject() {
		return tableScraper;
	}

	public EntryProcessorABC getEntryProc(String configID) {

		/* if (configID.equals("")) { if (entryProcSpec == null) { entryProcSpec = new
		 * EntryProcessorConcorsiItSpecInf(cfg.getSessionManagerAbstr(), getClientWrapper(cl), this); } return entryProcSpec; } else {
		 * log.error("getEntryProc(String configID) non gestito: " + configID); System.exit(1); return null; } */
		log.error("da riscrivere, esco");
		return null;
	}

	private static org.apache.log4j.Logger log = Logger.getLogger(PageConfigJapanguide.class);
}
