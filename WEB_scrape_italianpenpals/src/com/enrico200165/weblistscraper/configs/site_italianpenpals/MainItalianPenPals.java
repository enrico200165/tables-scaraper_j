package com.enrico200165.weblistscraper.configs.site_italianpenpals;

import com.enrico200165.cookies.CookieStoreEV;
import com.enrico200165.utils.net.http.Utils;
import com.enrico200165.utils.various.Utl;
import com.enrico200165.weblistscraper.common.*;
import com.enrico200165.weblistscraper.configs.ChannelIFC;
import com.enrico200165.weblistscraper.configs.HostConfigABC;
import com.enrico200165.weblistscraper.configs.PageConfigABC;
import com.enrico200165.weblistscraper.configs.drupal7.FormManagerLoginDrupal7;
import com.enrico200165.weblistscraper.page.EntryProcessorABC;
import com.enrico200165.weblistscraper.page.PageProcDescr;
import com.enrico200165.weblistscraper.page.PageProcessor;
import com.enrico200165.weblistscraper.page.TableScraperABC;
import com.enrico200165.weblistscraper.session.SessionManager;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import com.enrico200165.weblistscraper.tools.*;
import org.apache.log4j.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientRequestFilter;

public class MainItalianPenPals {

	public static void main(String[] args) {

		Utl.checks();

		Utils.setFiddler(true);

		// ############## PARTE GENERICA ################
		// -- parte di interfacciamento
		HostConfigABC hostConfigIPP = new  HostConfigItalianPenPals("V:\\data\\pers_dev\\data_dbs\\italianpenpals_login_enrico.properties");
		CookieStoreEV cs = new CookieStoreEV();
		ClientRequestFilter crqf= new ClReqFilterCookies(cs);
		ClientRespFilterEV cref = new ClientRespFilterEV(cs);
		Client cl = WEBUtils.createClient(crqf, cref, false);
		ClientWrapper cw = new ClientWrapper(cl,new InvocationBuilderWrapperIExplore(),hostConfigIPP,
				1, new FormManagerLoginDrupal7(hostConfigIPP));
		

		// ############## configurazione specifica ##############
		ChannelIFC channelInfoIPP =  new ChannelConfigItalianPenPalsUsersTable();
		EntryCanActOnFilter entryCanActOn = new EntryCanAcOntFilterItalianPenPals();

		SessionManagerAbstr sm = new SessionManager(new SessionLimitsItalianPenPals(), cw);
		
		EntryProcessorABC entryProcessorIPP = new EntryProcessorItalianPenPals(sm,cw,null);
		TableScraperABC tableScraperIPP = new  TableScraperItalianPenPals(sm,null,entryProcessorIPP);
		PageConfigABC pageConfig = new PageConfigItalianPenPals(hostConfigIPP,tableScraperIPP,entryCanActOn ,channelInfoIPP);

		entryProcessorIPP.setPageConfig(pageConfig);
		
		PageProcessor pageProcessor = new PageProcessor(hostConfigIPP, sm, pageConfig);


		PageProcDescr homePage = new PageProcDescr(hostConfigIPP, pageConfig, pageProcessor,"http://italianpenpals.org",WebPageAction.GET);
		homePage.setAuthenticate(false);
		// sm.addMap(homePage);


		
		
		PageProcDescr tablePage = new PageProcDescr( hostConfigIPP,pageConfig, pageProcessor,"http://italianpenpals.org/content/pen-pals-global-list",WebPageAction.GET_SCRAPE);
		
		
		tablePage.setAuthenticate(true);
		sm.add(tablePage);
		sm.performSession(new SessionLimitsItalianPenPals());

	}

	private static org.apache.log4j.Logger log = Logger.getLogger(MainItalianPenPals.class);

}
