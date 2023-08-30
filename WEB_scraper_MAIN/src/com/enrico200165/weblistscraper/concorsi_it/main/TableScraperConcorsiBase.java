package com.enrico200165.weblistscraper.concorsi_it.main;


import com.enrico200165.weblistscraper.concorsi_it.main.entities.Concor;
import com.enrico200165.weblistscraper.concorsi_it.main.entities.ConcorsiDB;
import com.enrico200165.weblistscraper.configs.PageConfigABC;
import com.enrico200165.weblistscraper.page.EntryProcessorABC;
import com.enrico200165.weblistscraper.page.TableScraperABC;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;
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

	private static Logger log = LogManager.getLogManager().getLogger(TableScraperConcorsiBase.class.getSimpleName());

}
