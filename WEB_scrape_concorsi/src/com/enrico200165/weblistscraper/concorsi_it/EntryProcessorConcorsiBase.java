package com.enrico200165.weblistscraper.concorsi_it;

import com.enrico200165.weblistscraper.concorsi_it.entities.Concor;
import com.enrico200165.weblistscraper.configs.PageConfigABC;
import com.enrico200165.weblistscraper.page.EntryProcessorABC;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import com.enrico200165.weblistscraper.tools.ClientWrapper;
import com.enrico200165.weblistscraper.tools.ResponseWrapper;
import com.enrico200165.weblistscraper.tools.Result;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public abstract class EntryProcessorConcorsiBase extends EntryProcessorABC {

	public EntryProcessorConcorsiBase(SessionManagerAbstr smPar, PageConfigABC tCfgpar) {
		super(smPar, tCfgpar);
	}


	abstract public boolean fillEntrySpecific(Concor conc, Element entryRawHTML, String fullURL, Object otherParams, boolean excludeIt);

	private boolean fillEntry(Concor conc, Element entryRawHTML, String fullURL, Object otherParams, boolean excludeIt) {
//		log.info("internal URL " + conc.getInfoInternURL());
		String URLPaginaConc = conc.getInfoInternURL();

		ClientWrapper cw = smgr.getClientWrapper();
		ResponseWrapper rw = cw.simpleGET(URLPaginaConc);

		Document paginaConcorso = Jsoup.parse(rw.getResponseHTML());

		String selector = "table.sintesi";
		Element table = null;
		Elements tables = paginaConcorso.select("table.sintesi");
		if (tables != null && tables.size() ==1 && tables.get(0) != null) {
			table = tables.get(0);
			conc.setCopiaSintesi(table.html());
		} else {
			log.error("unable to find table at: " + URLPaginaConc + " using selector: "+selector);
			return false;
		}

		Element testoCompleto = paginaConcorso.select("#testo_completo").get(0);
		conc.setCopiaTestoCompleto(testoCompleto.html());

		Elements tds = table.select("tr > td");
		for (int i = 0; i < tds.size(); i++) {
			if (tds.get(i).text().equals("Tipologia")) {
				conc.setTipologia(tds.get(i + 1).text());
				//				log.info("Tipologia = "+tds.get(i+1).text());
			} else if (tds.get(i).text().matches(".*egion.*")) {
				conc.setRegione(tds.get(i + 1).text());
				//				log.info("Regione = "+tds.get(i+1).text());
			} else if (tds.get(i).text().matches(".*rovincia.*")) {
				conc.setProvincia(tds.get(i + 1).text());
				//				log.info("Regione = "+tds.get(i+1).text());
			} else if (tds.get(i).text().matches(".*omune.*")) {
				conc.setComune(tds.get(i + 1).text());
				//				log.info("Comune = "+tds.get(i+1).text());
			} else if (tds.get(i).text().matches("Data .cadenza.*")) {
				conc.setScadenzaFromString(tds.get(i + 1).text());
				//				log.info("scadenza = "+tds.get(i+1).text());
			} else {
			}
		}
		return true;
	}

	public Result processWebEntry(Element entryRawHTML, String fullURL, Object otherParams, boolean excludeIt) {
		Result res = new Result();
		Concor conc = new Concor();

		conc.setAnnotations((String) otherParams);

		// varia a seconda delle varie liste
		fillEntrySpecific(conc, entryRawHTML, fullURL, otherParams, excludeIt);

		fillEntry(conc, entryRawHTML, fullURL, otherParams, excludeIt);

		if (!excludeIt) {
			excludeIt = pageConfig.getEntryExcludeFilter(smgr).excludeTypedEntry(conc);
			if (excludeIt) {
				log.debug("");
			}
		}
				
		conc.setExcluded(excludeIt);
		// ### Memorizzo ###
		if (conc.isExcluded()) {
			((SessionManagerConcorsi)smgr).incrExcluded();
			((SessionManagerConcorsi)smgr).concEsclusi.add(conc);
		} else {
			((SessionManagerConcorsi)smgr).incrIncluded();
			((SessionManagerConcorsi)smgr).getDBMgr().storeConcor(conc, Concor.IDFieldName, true, true);
		}
		return res;
	}

	private static org.apache.log4j.Logger log = Logger.getLogger(EntryProcessorConcorsiBase.class);
}
