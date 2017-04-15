package com.enrico200165.weblistscraper.concorsi_it;



import java.util.*;

import org.apache.log4j.Logger;

import com.enrico200165.weblistscraper.concorsi_it.entities.Concor;
import com.enrico200165.weblistscraper.concorsi_it.entities.ConcorsiDB;
import com.enrico200165.weblistscraper.configs.PageConfigABC;
import com.enrico200165.weblistscraper.page.EntryProcessorABC;
import com.enrico200165.weblistscraper.page.TableScraperABC;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;

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

	private static org.apache.log4j.Logger log = Logger.getLogger(TableScraperConcorsiBase.class);

}
