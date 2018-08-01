package com.enrico200165.weblistscraper.concorsi_it;

import com.enrico200165.cookies.CookieStoreEV;
import com.enrico200165.utils.net.http.Utils;
import com.enrico200165.utils.various.Utl;
import com.enrico200165.weblistscraper.common.WEBUtils;
import com.enrico200165.weblistscraper.concorsi_it.configs.ChannelConfigConcorsiItUsersTable;
import com.enrico200165.weblistscraper.concorsi_it.configs.PageConfigConcorsiitSpecInf;
import com.enrico200165.weblistscraper.concorsi_it.configs.SessionLimitsConcorsiIt;
import com.enrico200165.weblistscraper.configs.*;
import com.enrico200165.weblistscraper.page.EntryProcessorABC;
import com.enrico200165.weblistscraper.page.PageProcDescr;
import com.enrico200165.weblistscraper.page.PageProcessor;
import com.enrico200165.weblistscraper.page.TableScraperABC;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import com.enrico200165.weblistscraper.tools.*;
import org.apache.log4j.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientRequestFilter;

public class MainConcorsiIt {

	public static void main(String[] args) {

		Utl.checks();

		log.info("Working dir: "+System.getProperty("user.dir"));
		int dummy;

		Utils.setFiddler(false);



        // --- Configurazione ---

		// here the new configuration
		ConfigReader cfg_reader = null;
		if (true) {
			String yaml_path = "../../../data_dev/web_scraper/concorsi_it.yaml";
			cfg_reader = new ConfigReader(null, null, null, null, null);
			cfg_reader.parseYAMLConfig(yaml_path);
		}

        // --- Cliente e connettività  ----------------------------
        CookieStoreEV cs = new CookieStoreEV();

		ClientRequestFilter cReqFilt = new ClReqFilterCookies(cs);
		ClientRespFilterEV cResFilt = new ClientRespFilterEV(cs);
		Client cl = WEBUtils.createClient(cReqFilt, cResFilt, false);
		ClientWrapper cw = new ClientWrapper(cl, new InvocationBuilderWrapperIExplore(), cfg_reader.getHostConfig(), 1,
				new FormManagerDummy(cfg_reader.getHostConfig()));
		SessionManagerAbstr sm = new SessionManagerConcorsi(cw);




		// --- business  -----------------------

		boolean piuvisti = true;
		boolean geografia = false;
		boolean specialista = true;
		boolean ricerche = false;

		if (piuvisti) {
			//http://concorsi.it/nc/top

			ChannelIFC channConfig = new ChannelConfigConcorsiItUsersTable();

			EntryCanActOnFilter entryCanActOn = new EntryCanActOnFilterConcorsiIt();

			EntryProcessorABC entryProcSpecInf = new EntryProcessorConcorsiItSpecInf(sm, null);
			TableScraperABC tableSCraperSpecInf = new TableScraperConcorsiItSpecInf(sm, null, entryProcSpecInf);
			PageConfigABC pageCfgPiuVisti = new PageConfigConcorsiitSpecInf(cfg_reader.getHostConfig()
                    ,tableSCraperSpecInf, entryCanActOn,
					channConfig);
			PageProcessor pageProcessorPiuVisti = new PageProcessor(cfg_reader.getHostConfig(), sm, pageCfgPiuVisti);
			PageProcDescr tablePage = new PageProcDescr(cfg_reader.getHostConfig(), pageCfgPiuVisti, pageProcessorPiuVisti,
					"https://concorsi.it/regione/lazio", WebPageAction.GET_SCRAPE);

			// --- settaggi per rimediare a dipendenze circolari ---
			entryProcSpecInf.setPageConfig(pageCfgPiuVisti);
			tableSCraperSpecInf.setPageConfig(pageCfgPiuVisti);
			
			tablePage.setAuthenticate(false);
			sm.add(tablePage);
		}

		/* if (geografia) { // ########################## GEOGRAFIA ################################### // --- lazio ---
		 * 
		 * EntryProcessorABC entryProcGeo = new EntryProcessorConcorsiItGEO(sm, null);
		 * 
		 * 
		 * 
		 * PageConfigABC tcfgGeografia = null; // forward decl per evitare problema TableScraperABC tableSCraperGeo = new
		 * TableScraperConcorsiItGeoTable(sm, tcfgGeografia, entryProcGeo); tcfgGeografia = new
		 * PageConfigConcorsiGeo(hostConfigConcorsiIt,tableSCraperGeo);
		 * 
		 * 
		 * PageProcessor pageLoad = new PageProcessor( hostConfigConcorsiIt, sm, tcfgGeografia);
		 * 
		 * tablePageTemp = new PageProcDescr(gCfg, hostConfigConcorsiIt, null, pageLoad, null, null, "http://www.concorsi.it/regione/lazio",
		 * WebPageAction.SCRAPE); tablePageTemp.setAuthenticate(false); sm.addMap(tablePageTemp);
		 * 
		 * // --- Abruzzo --- tablePageTemp = new PageProcDescr(gCfg, hostConfigConcorsiIt, null, new PageProcessor( hostConfigConcorsiIt,
		 * sm, tcfgGeografia), null, null, "http://www.concorsi.it/regione/abruzzo", WebPageAction.SCRAPE);
		 * tablePageTemp.setAuthenticate(false); sm.addMap(tablePageTemp);
		 * 
		 * // --- teramo tablePageTemp = new PageProcDescr(gCfg, hostConfigConcorsiIt, null, new PageProcessor( hostConfigConcorsiIt, sm,
		 * tcfgGeografia), null, null, "http://www.concorsi.it/provincia/teramo", WebPageAction.SCRAPE);
		 * tablePageTemp.setAuthenticate(false); sm.addMap(tablePageTemp);
		 * 
		 * // --- Pescara tablePageTemp = new PageProcDescr(gCfg, hostConfigConcorsiIt, null, new PageProcessor( hostConfigConcorsiIt, sm,
		 * tcfgGeografia), null, null, "http://www.concorsi.it/provincia/pescara", WebPageAction.SCRAPE);
		 * tablePageTemp.setAuthenticate(false); sm.addMap(tablePageTemp);
		 * 
		 * // --- chieti tablePageTemp = new PageProcDescr(gCfg, hostConfigConcorsiIt, null, new PageProcessor( hostConfigConcorsiIt, sm,
		 * tcfgGeografia), null, null, "http://www.concorsi.it/provincia/chieti", WebPageAction.SCRAPE);
		 * tablePageTemp.setAuthenticate(false); sm.addMap(tablePageTemp);
		 * 
		 * // --- L'aquila tablePageTemp = new PageProcDescr(gCfg, hostConfigConcorsiIt, null, new PageProcessor( hostConfigConcorsiIt, sm,
		 * tcfgGeografia), null, null, "http://www.concorsi.it/provincia/l%27aquila", WebPageAction.SCRAPE);
		 * tablePageTemp.setAuthenticate(false); sm.addMap(tablePageTemp); }
		 * 
		 * if (specialista) { // ########### Specialista Informatico ################
		 * 
		 * PageConfigABC tcfgSpecInf = new PageConfigConcorsiitSpecInf(hostConfigConcorsiIt);
		 * 
		 * 
		 * tablePageTemp = new PageProcDescr(gCfg, hostConfigConcorsiIt, null, new PageProcessor( hostConfigConcorsiIt, sm, tcfgSpecInf),
		 * null, null, "http://concorsi.it/concorsi/specialista-informatico.html", WebPageAction.SCRAPE);
		 * tablePageTemp.setAuthenticate(false); sm.addMap(tablePageTemp); }
		 * 
		 * if (ricerche) { // ########### Ricerche, Altro Formato ################
		 * 
		 * // informatica
		 * 
		 * 
		 * 
		 * 
		 * PageConfigABC tcfgSearch = new PageConfigConcorsiSearch(hostConfigConcorsiIt); tablePageTemp = new PageProcDescr(gCfg,
		 * hostConfigConcorsiIt, null, new PageProcessor( hostConfigConcorsiIt, sm, tcfgSearch), null, null,
		 * "http://concorsi.it/risultati/?ric=informatic", WebPageAction.SCRAPE); tablePageTemp.setAuthenticate(false);
		 * sm.addMap(tablePageTemp);
		 * 
		 * tablePageTemp = new PageProcDescr(gCfg, hostConfigConcorsiIt, null, new PageProcessor( hostConfigConcorsiIt, sm, tcfgSearch),
		 * null, null, "http://concorsi.it/risultati/?ric=elaborazione", WebPageAction.SCRAPE); tablePageTemp.setAuthenticate(false);
		 * sm.addMap(tablePageTemp); } //
		 * 
		 * ------------------------------------------------------------------------------- */

		SessionLimitsBase sessLim = new SessionLimitsConcorsiIt();

		sm.performSession(sessLim);

	}

	private static org.apache.log4j.Logger log = Logger.getLogger(MainConcorsiIt.class);

}
