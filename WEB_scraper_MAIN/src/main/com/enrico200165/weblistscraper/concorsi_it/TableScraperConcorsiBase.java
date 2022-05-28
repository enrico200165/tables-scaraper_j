package com.enrico200165.weblistscraper.concorsi_it;


import com.enrico200165.weblistscraper.concorsi_it.entities.Concor;
import com.enrico200165.weblistscraper.concorsi_it.entities.ConcorsiDB;
import com.enrico200165.weblistscraper.configs.PageConfigABC;
import com.enrico200165.weblistscraper.page.EntryProcessorABC;
import com.enrico200165.weblistscraper.page.TableScraperABC;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public abstract class TableScraperConcorsiBase extends TableScraperABC {

	public TableScraperConcorsiBase(SessionManagerAbstr smgrPar, PageConfigABC tcfgPar, EntryProcessorABC ep) {
		super(smgrPar,tcfgPar,ep);
	}

	public void init(SessionManagerAbstr smgrPar) {
		this.smgr = smgrPar;
	}

	@Override
	public boolean initStorage() {
		return false;
	}

	


	
	@Override
	public boolean finalizeTableScraping() {
		return true;
	}

	
	
	public ArrayList<Concor> getConcEsclusi() {
		return concEsclusi;
	}

	public void setConcEsclusi(ArrayList<Concor> concEsclusi) {
		this.concEsclusi = concEsclusi;
	}



	ConcorsiDB concDB;
	ArrayList<Concor> concEsclusi;

	private static Logger log = LogManager.getLogger(TableScraperConcorsiBase.class.getSimpleName());

}
