package com.enrico200165.weblistscraper.configs;

import com.enrico200165.weblistscraper.tools.EntryExcludeFilter;
import org.apache.log4j.Logger;

import com.enrico200165.weblistscraper.page.NextTablePageSelectorsABC;
import com.enrico200165.weblistscraper.page.EntryProcessorABC;
import com.enrico200165.weblistscraper.page.TableScraperABC;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import com.enrico200165.weblistscraper.tools.EntryCanActOnFilter;
import com.enrico200165.weblistscraper.tools.EntryIncludeFilter;

/**
 * Tage = table/page
 * 
 * @author enrico
 *
 */
public abstract class PageConfigConcorsi extends PageConfigABC {

	protected PageConfigConcorsi(HostConfigABC hcPar, TableScraperABC ts, EntryCanActOnFilter entryCanActOnPar, ChannelIFC chann) {
		super(hcPar, ts, entryCanActOnPar, chann);
	}

	public ChannelIFC getChannelInfo() {
		return this.channelInfo;
	}

	public EntryCanActOnFilter getContactProspectFilter() {
		return this.entryCanActOn;
	}

	// -- END Forwarding to gloabl config

	static EntryExcludeFilter entryExcludeFilter = null;

	public EntryExcludeFilter getEntryExcludeFilter(SessionManagerAbstr smPar) {
		if (entryExcludeFilter == null) {
			entryExcludeFilter = getEntryExcludeFilterSpecific(smPar);
		}
		return entryExcludeFilter;
	}

	public abstract EntryExcludeFilter getEntryExcludeFilterSpecific(SessionManagerAbstr smPar);

	static EntryIncludeFilter entryIncludeFilter = null;

	public EntryIncludeFilter getEntryIncludeFilter(SessionManagerAbstr smPar) {
		if (entryIncludeFilter == null) {
			entryIncludeFilter = getEntryIncludeFilterSpecific(smPar);
		}
		return entryIncludeFilter;
	}

	public abstract EntryIncludeFilter getEntryIncludeFilterSpecific(SessionManagerAbstr smPar);

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
		return tablePageScraper;
	}

	public void setTableScraper(TableScraperABC tsPar) {
		tablePageScraper = tsPar;
	}

	// table&entry specific /processor

	static EntryProcessorABC entryProcSearch = null;
	static EntryProcessorABC entryProcGeo = null;
	static EntryProcessorABC entryProcSpec = null;

	abstract public EntryProcessorABC getEntryProcObject(String configID);

	public EntryProcessorABC getEntryProc(String configID) {

		log.warn("");
		return null;
	}

	static NextTablePageSelectorsABC nextTablePageSelectorsABC = null;

	public NextTablePageSelectorsABC getNextTablePageSelectors() {
		if (nextTablePageSelectorsABC == null) {
			nextTablePageSelectorsABC = getNextTablePageSelectorsSpecific();
		}
		return nextTablePageSelectorsABC;
	}

	protected abstract NextTablePageSelectorsABC getNextTablePageSelectorsSpecific();

	private static org.apache.log4j.Logger log = Logger.getLogger(PageConfigConcorsi.class);

}
