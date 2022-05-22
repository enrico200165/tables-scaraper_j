package com.enrico200165.weblistscraper.configs;

import com.enrico200165.weblistscraper.page.EntryProcessorABC;
import com.enrico200165.weblistscraper.page.NextTablePageSelectorsABC;
import com.enrico200165.weblistscraper.page.TableScraperABC;
import com.enrico200165.weblistscraper.tools.EntryCanActOnFilter;
import com.enrico200165.weblistscraper.tools.EntryExcludeFilter;
import com.enrico200165.weblistscraper.tools.EntryIncludeFilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Tage = table/page SEMBREREBBE che fornisce tutto ciò che serve a elaborare una pagina: - indirizzi (host-config) - selettori - oggetti
 * specifici
 *
 * @author enrico
 *
 */
public abstract class PageConfigABC {

	public PageConfigABC(HostConfig hcPar, TableScraperABC ts, EntryCanActOnFilter entryCanActOnPar
			, ChannelIFC channelInfoPar) {
		super();

		this.hConfig = hcPar;  // in YAML
		this.channelInfo = channelInfoPar; // in YAML

		this.tableScraper = ts;
		this.entryCanActOn = entryCanActOnPar;
	}

	public ChannelIFC getChannelInfo() {
		return this.channelInfo;
	}

	public HostConfig getHostConfig() {
		return this.hConfig;
	}

	public EntryCanActOnFilter getContactProspectFilter() {
		return entryCanActOn;
	}

	// -- END Forwarding to gloabl config

	static EntryExcludeFilter entryExcludeFilter = null;

	public EntryExcludeFilter getEntryExcludeFilter() {
		if (entryExcludeFilter == null) {
			entryExcludeFilter = getEntryExcludeFilterSpecific();
		}
		return entryExcludeFilter;
	}

	public abstract EntryExcludeFilter getEntryExcludeFilterSpecific();

	static EntryIncludeFilter entryIncludeFilter = null;

	public EntryIncludeFilter getEntryIncludeFilter() {
		if (entryIncludeFilter == null) {
			entryIncludeFilter = getEntryIncludeFilterSpecific();
		}
		return entryIncludeFilter;
	}

	public abstract EntryIncludeFilter getEntryIncludeFilterSpecific();

	// jsoup selector for the table in a page
	public abstract String TableSelectCSS();

	// jsoup selector for the entries in a table
	public abstract String EntrySelectCSS();

	// table specific scraper/processor
	TableScraperABC tablePageScraper = null;

	abstract public TableScraperABC getTableScraperObject();

	public TableScraperABC getTableScraper() {
		if (tablePageScraper == null) {
			tablePageScraper = getTableScraperObject();
		}
		log.warn("patch per rimediare a una dipendenza circolare dovuta a design assurdo");
		tablePageScraper.setPageConfig(this);
		return tablePageScraper;
	}

	// table&entry specific /processor

	abstract public EntryProcessorABC getEntryProcObject(String configID);

	abstract public EntryProcessorABC getEntryProc(String configID);

	static NextTablePageSelectorsABC nextTablePageSelectorsABC = null;

	public NextTablePageSelectorsABC getNextTablePageSelectors() {
		if (nextTablePageSelectorsABC == null) {
			nextTablePageSelectorsABC = getNextTablePageSelectorsSpecific();
		}
		return nextTablePageSelectorsABC;
	}





	String cfgFName;

	// temporanei, da rimuoverere
	HostConfig hConfig;


	// ### end new part #############################################################

	protected abstract NextTablePageSelectorsABC getNextTablePageSelectorsSpecific();

	protected TableScraperABC tableScraper;
	protected ChannelIFC channelInfo;

	protected EntryCanActOnFilter entryCanActOn;

	private static Logger log = LogManager.getLogger(PageConfigABC.class.getSimpleName());
}