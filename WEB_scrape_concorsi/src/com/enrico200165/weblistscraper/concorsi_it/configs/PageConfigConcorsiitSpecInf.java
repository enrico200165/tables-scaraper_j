package com.enrico200165.weblistscraper.concorsi_it.configs;

import com.enrico200165.weblistscraper.concorsi_it.EntryExcludeFilterConcorsiIt;
import com.enrico200165.weblistscraper.concorsi_it.EntryIncludeFilterConcorsiIt;
import com.enrico200165.weblistscraper.concorsi_it.EntryProcessorConcorsiItSpecInf;
import com.enrico200165.weblistscraper.concorsi_it.NextTablePageSelectorsConcorsiIt;
import com.enrico200165.weblistscraper.configs.*;
import com.enrico200165.weblistscraper.page.NextTablePageSelectorsABC;
import com.enrico200165.weblistscraper.page.EntryProcessorABC;
import com.enrico200165.weblistscraper.page.TableScraperABC;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import com.enrico200165.weblistscraper.tools.EntryCanActOnFilter;
import com.enrico200165.weblistscraper.tools.EntryExcludeFilter;
import com.enrico200165.weblistscraper.tools.EntryIncludeFilter;

public class PageConfigConcorsiitSpecInf extends com.enrico200165.weblistscraper.concorsi_it.configs.PageConfigConcorsi {

	public PageConfigConcorsiitSpecInf(HostConfig hcPar, TableScraperABC ts, EntryCanActOnFilter entryCanActOnPar, ChannelIFC chann) {
		super(hcPar, ts,entryCanActOnPar,chann);
	}

	@Override
	public String TableSelectCSS() {
//		return "#pagine_interne > ul";
		return "#datagrid > table";

	}

	@Override
	public String EntrySelectCSS() {
//		return "li > div";
		return "tr";
	}

	@Override
	public EntryIncludeFilter getEntryIncludeFilterSpecific(SessionManagerAbstr smPar) {
		return new EntryIncludeFilterConcorsiIt(smPar);
	}

	@Override
	public EntryExcludeFilter getEntryExcludeFilterSpecific(SessionManagerAbstr smPar) {
		return new EntryExcludeFilterConcorsiIt(smPar);
	}

	static NextTablePageSelectorsABC netPageSel = null;

	@Override
	public NextTablePageSelectorsABC getNextTablePageSelectorsSpecific() {
		if (netPageSel == null) {
			netPageSel = new NextTablePageSelectorsConcorsiIt();
		}
		return netPageSel;
	}

	public TableScraperABC getTableScraperObject() {
//		return new TableScraperConcorsiItSpecInf(null,this, null);
		return tableScraper;
	}

	@Override
	public EntryProcessorABC getEntryProcObject(String configID) {
		return new EntryProcessorConcorsiItSpecInf(null,  this);
	}

}
