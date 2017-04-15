package com.enrico200165.weblistscraper.configs.site_japanguide;

import com.enrico200165.cookies.CookieStoreEV;
import com.enrico200165.utils.net.http.Utils;
import com.enrico200165.utils.str_regex.*;
import com.enrico200165.utils.various.Utl;
import com.enrico200165.weblistscraper.common.*;
import com.enrico200165.weblistscraper.configs.ChannelIFC;
import com.enrico200165.weblistscraper.configs.HostConfigABC;
import com.enrico200165.weblistscraper.configs.PageConfigABC;
import com.enrico200165.weblistscraper.configs.drupal7.FormManagerLoginDrupal7;
import com.enrico200165.weblistscraper.marketingDB.DBManagerMKT;
import com.enrico200165.weblistscraper.page.*;
import com.enrico200165.weblistscraper.session.SessionManager;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import com.enrico200165.weblistscraper.tools.*;
import org.apache.log4j.Logger;
import org.jsoup.*;
import org.jsoup.nodes.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.*;
import java.util.*;

public class MainJapanguide {

    public static void main(String[] args) {

        log.info(Utl.getVMOptions());
        Utl.checks();

        Utils.setFiddler(true);
        try {
            perform();
        } catch (Exception e) {
            log.info("", e);
        }
    }

    public static void perform() {
        // -- parte di interfacciamento
        HostConfigABC hostConfig = new HostConfigJapanguide("V:\\data\\pers_dev\\data_dbs\\japanguide_login_armandoitaly1990.properties");
        CookieStoreEV cs = new CookieStoreEV();
        ClientRequestFilter crqf = new ClReqFilterCookies(cs);
        ClientRespFilterEV cref = new ClientRespFilterEV(cs);

        Client cl = WEBUtils.createClient(crqf, cref, false);
        InvocationBuilderWrapper ibw = new InvocationBuilderWrapperIExplore();

        ClientWrapper cw = new ClientWrapper(cl, ibw, hostConfig,
                1, new FormManagerLoginDrupal7(hostConfig));

        SessionManagerAbstr sm = new SessionManager(new SessionLimitsJapanguide(), cw);


        // --- parte funzionale
        ChannelIFC channelInfoIPP = new ChannelConfigJapanguide();
        EntryCanActOnFilter entryCanActOnFilter = new EntryCanActFilterJapanguide();

        DBManagerMKT ch = new DBManagerMKT("marketing");
        EntryProcessorCommunity entryProcessor = new EntryProcessorJapanguide(sm, cw, null);
        entryProcessor.setContactType(ContactType.SIMULATED);
        entryProcessor.setStoreProspect(true);
        entryProcessor.setDBManagerMKT(ch);
        TableScraperABC tableScraper = new TableScraperJapanguide(sm, null, entryProcessor);
        PageConfigABC pageConfig = new PageConfigJapanguide(hostConfig, tableScraper, entryCanActOnFilter, channelInfoIPP);
        entryProcessor.setPageConfig(pageConfig);


        tableScraper.setPageConfig(pageConfig);
        PageProcessorAbstr pageProcessor = new PageProcessor(hostConfig, sm, pageConfig);

        boolean locale = true;
        if (locale) {

            // study form page da file anzichè senza scaricare
            try {
                //File input = new File("WEB_scrape_japan-guide/japanguide.html");
                File input = new File("V:\\data\\pers_dev\\sources\\intellij\\ev_tools\\ev_tools\\ev_tools\\WEB_scrape_japan-guide\\pagine_web\\japanguide.html");

                Document doc = Jsoup.parse(input, "UTF-8", "http://www.japan-guide.com/");
                PageProcDescr tablePage = new PageProcDescr(hostConfig, pageConfig, pageProcessor, "http://www.japan-guide.com/local/?aCAT=3", WebPageAction.GET_SCRAPE);
                tableScraper.scrape(tablePage, doc, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(1);
        }

        //home page


        PageProcDescr newHomePage = new PageProcDescr(hostConfig, pageConfig, pageProcessor, "http://www.japan-guide.com/", WebPageAction.GET);
        sm.add(newHomePage);

        PageProcDescr loginPage = new PageProcDescr(hostConfig, pageConfig, pageProcessor, "http://www.japan-guide.com/local/", WebPageAction.GET);
        sm.add(loginPage);
        loginPage.setAuthenticate(true);
        sm.add(loginPage);

        PageProcDescr LangExcgFormPage = new PageProcDescr(hostConfig, pageConfig, pageProcessor, "http://www.japan-guide.com/local/?aCAT=3", WebPageAction.GET);
        sm.add(LangExcgFormPage);


        // go to select location
        sm.add(new PageProcDescr(hostConfig, pageConfig, pageProcessor, "http://www.japan-guide.com/local/select_location.html?aCAT=3&aREF_COUNTRY=", WebPageAction.GET));
        // select japan
        sm.add(new PageProcDescr(hostConfig, pageConfig, pageProcessor, "http://www.japan-guide.com/local/select_location.html?aGEOTYPE=2&aID_COUNTRY=jp&aCAT=3", WebPageAction.GET));


        // --- ricerca ---
        String formFielsString = "aGENDER=f&aAGE1=14&aAGE2=120&aINTEREST=0&aNATIONALITY=jp&aLANGUAGE2=28&aLANGUAGE1=0&aGEOTYPE=2" +
                "&aID_COUNTRY=jp&aID_STATE=0&aID_CITY=0&aCAT=3&aMES=submit";
        List<NameValuePairString> formFields = WEBUtils.formFieldsFromString(formFielsString);
        FormManagerABC fm = null;
        PageProcDescr searchPage = new PageProcDescr(hostConfig, pageConfig, pageProcessor, "http://www.japan-guide.com/local/?aCAT=3", WebPageAction.POST_SCRAPE);
        searchPage.setFormsParams(formFields, "", "local/jp/", fm, ibw);
        sm.add(searchPage);


        sm.performSession(new SessionLimitsJapanguide()

        );
    }

    private static org.apache.log4j.Logger log = Logger.getLogger(MainJapanguide.class);
}
