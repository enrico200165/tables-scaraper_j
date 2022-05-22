package com.enrico200165.weblistscraper.concorsi_it;

import com.enrico200165.weblistscraper.common.WEBUtils;
import com.enrico200165.weblistscraper.concorsi_it.entities.Concor;
import com.enrico200165.weblistscraper.configs.PageConfigABC;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Element;

import java.net.URI;


public class EntryProcessorConcorsiItSpecInf extends EntryProcessorConcorsiBase {

	public EntryProcessorConcorsiItSpecInf(SessionManagerAbstr smPar, PageConfigABC tCfgpar) {
		super(smPar,tCfgpar);
	}


	@Override
	public boolean fillEntrySpecific(Concor conc,Element entryRawHTML, String fullURL, Object otherParams, boolean excludeIt) {
				
		assert (otherParams.getClass() == String.class);
//		log.info("raw Element: \n" + entryRawHTML);

		
		conc.appendToAnn(" SPEC-List");
		
//		String titolo = WEBUtils.evSelTxt(entryRawHTML, "h3 > b > a");
		String titolo = WEBUtils.evSelTxt(entryRawHTML, "tr>td:eq(0)>a");
		titolo = WEBUtils.evSelAttrTxt(entryRawHTML, "tr>td:eq(0)>a", "title");

		conc.setTitle(titolo);

		// --- Siste (host only) ----
		conc.setSite(pageConfig.getHostConfig().getHost());

		// --- table URL ---
		conc.setTableUrl(fullURL);

		// internal URL
		String relURL = WEBUtils.evSelAttrTxt(entryRawHTML, "tr > td:eq(1) > a", "href");
		if (relURL != null) {
			URI uri = pageConfig.getHostConfig().getBaseHostURI();
			String intUrl = uri.getScheme() + "://" + uri.getAuthority();
			intUrl += relURL;
			conc.setInfoInternURL(intUrl);
		} else {
			log.warn("unable to read internal URL");
			log.error("esco");
			System.exit(1);
			return false;
		}

		// --- summary
		conc.setSommario("da implementare");
		return true;
	}
	
	
	private static Logger log = LogManager.getLogger(EntryProcessorConcorsiItSpecInf.class.getSimpleName());
}
