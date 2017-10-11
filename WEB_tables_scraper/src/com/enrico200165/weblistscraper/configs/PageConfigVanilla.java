package com.enrico200165.weblistscraper.configs;

import com.enrico200165.weblistscraper.page.EntryProcessorABC;
import com.enrico200165.weblistscraper.page.NextTablePageSelectorsABC;
import com.enrico200165.weblistscraper.page.TableScraperABC;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import com.enrico200165.weblistscraper.tools.EntryCanActOnFilter;
import com.enrico200165.weblistscraper.tools.EntryExcludeFilter;
import com.enrico200165.weblistscraper.tools.EntryIncludeFilter;
import org.apache.log4j.Logger;

/**
 * Tage = table/page SEMBREREBBE che fornisce tutto ciò che serve a elaborare una pagina: - indirizzi (host-config) - selettori - oggetti
 * specifici
 * 
 * @author enrico
 *
 */
public class PageConfigVanilla extends PageConfigABC {

	public PageConfigVanilla(String cfgFName, HostConfig hcPar, TableScraperABC ts, EntryCanActOnFilter entryCanActOnPar, ChannelIFC channelInfoPar) {
		super(null,null,null,null);
		this.hc = hcPar;
		this.tableScraper = ts;
		this.channelInfo = channelInfoPar;
		this.entryCanActOn = entryCanActOnPar;
	}

	static void  dummy() {
		PageConfigVanilla x = new PageConfigVanilla(null,null
                ,null,null,null);
	}

	public ChannelIFC getChannelInfo() {
		return this.channelInfo;
	}

	public HostConfig getHostConfig() {
		return this.hc;
	}

	public EntryCanActOnFilter getContactProspectFilter() {
		return entryCanActOn;
	}

	// -- END Forwarding to gloabl config

	static EntryExcludeFilter entryExcludeFilter = null;

	public EntryExcludeFilter getEntryExcludeFilter(SessionManagerAbstr smPar) {
		if (entryExcludeFilter == null) {
			entryExcludeFilter = getEntryExcludeFilterSpecific(smPar);
		}
		return entryExcludeFilter;
	}

	public  EntryExcludeFilter getEntryExcludeFilterSpecific(SessionManagerAbstr smPar) {
		return null;
	}

	static EntryIncludeFilter entryIncludeFilter = null;

	public EntryIncludeFilter getEntryIncludeFilter(SessionManagerAbstr smPar) {
		if (entryIncludeFilter == null) {
			entryIncludeFilter = getEntryIncludeFilterSpecific(smPar);
		}
		return entryIncludeFilter;
	}

	public  EntryIncludeFilter getEntryIncludeFilterSpecific(SessionManagerAbstr smPar){
		return null;
	}

	// jsoup selector for the table in a page
	public  String TableSelectCSS() {
		return null;
	}

	// jsoup selector for the entries in a table
	public  String EntrySelectCSS() {
		return null;
	}

	// table specific scraper/processor
	TableScraperABC tablePageScraper = null;

	 public TableScraperABC getTableScraperObject(){
		 return null;
	 }

	public TableScraperABC getTableScraper() {
		if (tablePageScraper == null) {
			tablePageScraper = getTableScraperObject();
		}


		log.warn("patch per rimediare a una dipendenza circolare dovuta a design assurdo");
		tablePageScraper.setPageConfig(this);


		return tablePageScraper;
	}

	// table&entry specific /processor

	 public EntryProcessorABC getEntryProcObject(String configID){
		 return null;
	 }

	 public EntryProcessorABC getEntryProc(String configID){
		 return null;
	 }

	static NextTablePageSelectorsABC nextTablePageSelectorsABC = null;

	public NextTablePageSelectorsABC getNextTablePageSelectors() {
		if (nextTablePageSelectorsABC == null) {
			nextTablePageSelectorsABC = getNextTablePageSelectorsSpecific();
		}
		return nextTablePageSelectorsABC;
	}




	protected  NextTablePageSelectorsABC getNextTablePageSelectorsSpecific(){
		return null;
	}

	protected HostConfig hc;
	protected TableScraperABC tableScraper;
	protected ChannelIFC channelInfo;

	protected EntryCanActOnFilter entryCanActOn;


	private static Logger log = Logger.getLogger(PageConfigVanilla.class);
}
