package com.enrico200165.weblistscraper.concorsi_it.configs;

import com.enrico200165.weblistscraper.concorsi_it.EntryExcludeFilterConcorsiIt;
import com.enrico200165.weblistscraper.concorsi_it.EntryIncludeFilterConcorsiIt;
import com.enrico200165.weblistscraper.concorsi_it.EntryProcessorConcorsiItSearch;
import com.enrico200165.weblistscraper.concorsi_it.NextTablePageSelectorsConcorsiIt;
import com.enrico200165.weblistscraper.concorsi_it.TableScraperConcorsiItSearch;
import com.enrico200165.weblistscraper.configs.ChannelIFC;
import com.enrico200165.weblistscraper.configs.HostConfigABC;
import com.enrico200165.weblistscraper.page.NextTablePageSelectorsABC;
import com.enrico200165.weblistscraper.page.EntryProcessorABC;
import com.enrico200165.weblistscraper.page.TableScraperABC;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import com.enrico200165.weblistscraper.tools.EntryCanActOnFilter;
import com.enrico200165.weblistscraper.tools.EntryExcludeFilter;
import com.enrico200165.weblistscraper.tools.EntryIncludeFilter;

public class PageConfigConcorsiSearch extends com.enrico200165.weblistscraper.configs.PageConfigConcorsi {

	public PageConfigConcorsiSearch(HostConfigABC hcPar, TableScraperABC ts, EntryCanActOnFilter entryCanActOnPar, ChannelIFC chann) {
		super(hcPar, ts, entryCanActOnPar,chann);
	}

	@Override
	public String EntrySelectCSS() {
		return "div.Mainfigura  > li";
	}

	@Override
	public String TableSelectCSS() {
		// originale return "#pagine_interne > div.figura";
		return "#pagine_interne > .figura";
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
		return new TableScraperConcorsiItSearch(null,this, null);
	}

	@Override
	public EntryProcessorABC getEntryProcObject(String configID) {
		return new EntryProcessorConcorsiItSearch(null, this);
	}

}
