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

	public PageConfigVanilla(String cfgFName, HostConfig hcPar, TableScraperABC ts
			,EntryCanActOnFilter entryCanActOnPar, ChannelIFC channelInfoPar,
							 ScrapeGLobConfig cfgRdrP) {
		super(null,null,null,null);
		this.hc = hcPar;
		this.channelInfo = channelInfoPar;


		this.tableScraper = ts;
		this.entryCanActOn = entryCanActOnPar;

		this.gCfg = cfgRdrP;
	}

	public ChannelIFC getChannelInfo() { return gCfg.getChannelInfo(); }
	public HostConfig getHostConfig() {
		return gCfg.getHostConfig();
	}
	public EntryExcludeFilter getEntryExcludeFilter(SessionManagerAbstr smPar) {
		return gCfg.getEntryExcludeFilter(smPar);
	}
	public EntryIncludeFilter getEntryIncludeFilter(SessionManagerAbstr smPar) {
		return gCfg.getEntryIncludeFilter(smPar);
	}

	public  EntryExcludeFilter getEntryExcludeFilterSpecific(SessionManagerAbstr smPar) {
		return null;
	}
	public  EntryIncludeFilter getEntryIncludeFilterSpecific(SessionManagerAbstr smPar){
		return null;
	}
	public NextTablePageSelectorsABC getNextTablePageSelectors() {
		return gCfg.getNextTablePageSelectors();
	}




	// jsoup selector for the table in a page
	public  String TableSelectCSS() {
		return null;
	}
	// jsoup selector for the entries in a table
	public  String EntrySelectCSS() {
		return null;
	}


	// -- Until here adjusted

	public EntryCanActOnFilter getContactProspectFilter() {
		return entryCanActOn;
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


	public void setNextTablePageSelectors(NextTablePageSelectorsABC sel) {
		this.nextTablePageSelectorsABC = sel;
	}


	public String getTableURl() {
		return tableURl;
	}

	public void setTableURl(String tableURl) {
		this.tableURl = tableURl;
	}


	protected  NextTablePageSelectorsABC getNextTablePageSelectorsSpecific(){
		return null;
	}


	// functionoids
	protected TableScraperABC tableScraper;
	protected EntryCanActOnFilter entryCanActOn;
	// already in YAML
	protected ChannelIFC channelInfo;
	protected HostConfig hc;
	String tableURl;
	ScrapeGLobConfig gCfg;

	private static Logger log = Logger.getLogger(PageConfigVanilla.class);
}
