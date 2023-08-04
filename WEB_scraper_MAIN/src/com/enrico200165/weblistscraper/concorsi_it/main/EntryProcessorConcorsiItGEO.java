package com.enrico200165.weblistscraper.concorsi_it.main;

import com.enrico200165.weblistscraper.common.WEBUtils;
import com.enrico200165.weblistscraper.concorsi_it.main.entities.Concor;
import com.enrico200165.weblistscraper.configs.PageConfigABC;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;import org.jsoup.nodes.Element;

import java.net.URI;


public class EntryProcessorConcorsiItGEO extends  EntryProcessorConcorsiBase {

	public EntryProcessorConcorsiItGEO(SessionManagerAbstr smPar, PageConfigABC tCfgpar) {
		super(smPar,tCfgpar);
	}


	@Override
	public boolean fillEntrySpecific(Concor conc, Element entryRawHTML, String fullURL, Object otherParams, boolean excludeIt) {

//		log.info("raw Element: \n" + entryRawHTML);

		conc.appendToAnn(" GEO-List");
		
		String titolo = WEBUtils.evSelAttrTxt(entryRawHTML, "tr > td:nth-child(1) > a", "title");
		conc.setTitle(titolo);

		// --- Siste (host only) ----
		conc.setSite(pageConfig.getHostConfig().getHost());

		// --- table URL ---
		conc.setTableUrl(fullURL);

		Element e = entryRawHTML.select("tr > td:nth-child(2) > a").get(0);
		if (e != null) {
			URI uri = pageConfig.getHostConfig().getBaseHostURI();
			String intUrl = uri.getScheme() + "://" + uri.getAuthority();
			intUrl += e.attr("href");
			conc.setInfoInternURL(intUrl);
		} else {
			log.log(Level.WARNING,  "unable to read internal URL");
		}

		// --- summary
		conc.setSommario(WEBUtils.evSelTxt(entryRawHTML, "tr > td:nth-child(2)"));

		return true;
	}
	
	
	private static Logger log = LogManager.getLogManager().getLogger(EntryProcessorConcorsiItGEO.class.getSimpleName());
}
