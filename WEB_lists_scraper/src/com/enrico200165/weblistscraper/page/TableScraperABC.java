package com.enrico200165.weblistscraper.page;

import com.enrico200165.weblistscraper.configs.PageConfigABC;
import com.enrico200165.weblistscraper.configs.SessionLimitsBase;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import com.enrico200165.weblistscraper.tools.EntryExcludeFilter;
import com.enrico200165.weblistscraper.tools.ResponseWrapper;
import com.enrico200165.weblistscraper.tools.Result;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Level;

abstract public class TableScraperABC {

    public TableScraperABC(SessionManagerAbstr smgrPar, PageConfigABC pageCfgPar, EntryProcessorABC epPar) {
        super();
        pageConfig = pageCfgPar;
        if (pageConfig == null) {
            log.info("breakpoint page config is null");
        }
        smgr = smgrPar;
        if (smgr == null) {
            log.info("breakpoint: session manager is null");
        }
        entryProcessor = epPar;
        if (entryProcessor == null)
            log.info("breakpoint entryprocessor is null");
    }


    public void init(EntryProcessorABC epPar) {
        this.entryProcessor = epPar;
    }

    public void setSessionManager(SessionManagerAbstr smPar) {
        log.log(Level.WARNING,  "esiste come patch al fatto che in alcuni casi quando viene creato "
                + " un session manager non è disponibile, va ridisegnato tutto");
        this.smgr = smPar;
    }


    /**
     * Consente di passare parametri eventualmente necessary all'enry processor
     *
     * @param entryProc
     */
    abstract protected void initEntryProcessor(EntryProcessorABC entryProc);

    abstract public boolean initStorage();

    abstract public boolean tableNotFoundOK(Result res);


    /**
     * Si limita a estrarre un JSoup document dalla risposta
     * @param ppDescr
     * @param rw
     * @param formURL
     * @return
     */
    public Result scrape(PageProcDescr ppDescr, ResponseWrapper rw, String formURL) {
        Result res = new Result();
        // get the table
        if (rw == null || rw.getJSoupDocument() == null) {
            log.log(Level.SEVERE, "not fount doc for page: " + rw.dump());
            return res.setRc(Result.RC.ERROR);
        }
        initEntryProcessor(entryProcessor);
        // -- nuovo codice ---
        Document page = rw.getJSoupDocument();
        return scrape(ppDescr, page, formURL);
    }



    public Result scrape(PageProcDescr UNUSED, Document page, String formURL) {

        Result res = new Result();
        initEntryProcessor(entryProcessor);

        // --- Table ---
        String tableSelectCSS = pageConfig.TableSelectCSS();
        log.info("scraping table: " + tableSelectCSS);

        Elements tables = page.select(tableSelectCSS);
        if (tables == null || tables.size() != 1) {
            if (tableNotFoundOK(res)) {
                return res;
            } else {
                log.log(Level.SEVERE, "not found table: \"" + pageConfig.TableSelectCSS() + "\" in page: " + page.html());
                return res.setRc(Result.RC.ERROR);
            }
        }
        Element table = page.select(tableSelectCSS).get(0);

        // --- Entries ---
        String entryCSS = pageConfig.EntrySelectCSS();
        Elements entriesInPage = table.select(entryCSS);
        if (entriesInPage == null || entriesInPage.size() <= 0) {
            log.log(Level.SEVERE, "not fount entries: \"" + entryCSS + "\" in html: " + table.html());
            return res.setRc(Result.RC.ERROR);
        }

        SessionLimitsBase sl = smgr.getSessionLimits();
        for (Element entry : entriesInPage) {
            // check if session limits allow to proceed
            if (!sl.canReadEntry(/* preIncrement */true)) {
                log.info("non posso fare scrape, esco, max exceeded: " + (sl.getReads() - 1) + "/" + sl.getReads());
                return res.setContinua(false);
            }
            log.log( Level.FINE, "read entry nr: " + sl.getReads());

            smgr.incrRawEntriesEsaminate();

            StringBuffer annotations = new StringBuffer();
            boolean excludeIt = false;
            if (!pageConfig.getEntryIncludeFilter().includeHTMLEntry(entry)) {
                EntryExcludeFilter excludeFilter = pageConfig.getEntryExcludeFilter();
                excludeIt = excludeFilter.excludeHTMLEntry(entry, annotations);
            } else {
                // log.info("inclusion forced by filter");
            }

            // some processors may need to process excluded entries
            if (!excludeIt) {
                entryProcessor.processWebEntry(entry, formURL, annotations.toString(), excludeIt);
            } else {
                log.info("Excluded HTML entry\n"+annotations.toString());
                log.log( Level.FINE, "entry content:\n"+entry.text());
            }
        }

        return res;
    }



    abstract public boolean finalizeTableScraping();


    public void setPageConfig(PageConfigABC pageConfig) {
        this.pageConfig = pageConfig;
    }


    public void setEntryProcessor(EntryProcessorABC entryProcessor) {
        this.entryProcessor = entryProcessor;
    }


    EntryProcessorABC entryProcessor;
    protected SessionManagerAbstr smgr;
    protected PageConfigABC pageConfig;

    static Logger log=Logger.getLogger(TableScraperABC.class.getSimpleName());
}