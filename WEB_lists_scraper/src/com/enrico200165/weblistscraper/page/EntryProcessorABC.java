package com.enrico200165.weblistscraper.page;

import com.enrico200165.weblistscraper.configs.PageConfigABC;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import com.enrico200165.weblistscraper.tools.Result;
import org.jsoup.nodes.Element;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Level;

/**
 * Elabora una riga di una tabella
 * 
 * Specifico alla *struttura* delle entries di una tabella
 * non necessariamente specifico alla singola tabella dato che più tabelle su un sito 
 * posson avere stessa struttura (e elaborazione diversa o uguale)
 * 
 * Dovrebbe contenere l'eventuale gestione del DB specifica alla entries
 * (lo storage manager con JPA dentro)
 * 
 * @author enrico
 *
 */
public abstract class EntryProcessorABC {

	public EntryProcessorABC(SessionManagerAbstr smPar, PageConfigABC pageConfigPar) {
		super();
		pageConfig = pageConfigPar;
		smgr = smPar;
	}

	
	public void setSmgr(SessionManagerAbstr smgr) {
		log.log(Level.WARNING,  "this exists because because of a design problem (circular dependency) cannot set it in the constructor, where anyway there is a parameter");
		this.smgr = smgr;
	}


	public void setPageConfig(PageConfigABC tCfg) {
		log.log(Level.WARNING,  "this exists because because of a design problem (circular dependency) cannot set it in the constructor, where anyway there is a parameter");
		this.pageConfig = tCfg;
	}


	abstract public Result processWebEntry(Element entryRawHTML, String formURL,
										   Object otherParams, boolean excludeIt);

	protected SessionManagerAbstr smgr;
	protected PageConfigABC pageConfig;
		
	static Logger log=Logger.getLogger(EntryProcessorABC.class.getSimpleName());
}
