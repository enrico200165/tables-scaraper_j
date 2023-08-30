package com.enrico200165.weblistscraper.concorsi_it.main.configs;

import com.enrico200165.weblistscraper.configs.ChannelIFC;
import com.enrico200165.weblistscraper.configs.HostConfig;
import com.enrico200165.weblistscraper.configs.PageConfigABC;
import com.enrico200165.weblistscraper.page.EntryProcessorABC;
import com.enrico200165.weblistscraper.page.NextTablePageSelectorsABC;
import com.enrico200165.weblistscraper.page.TableScraperABC;
import com.enrico200165.weblistscraper.tools.EntryCanActOnFilter;
import com.enrico200165.weblistscraper.tools.EntryExcludeFilter;
import com.enrico200165.weblistscraper.tools.EntryIncludeFilter;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;


/**
 * Tage = table/page
 *
 * @author enrico
 *
 */
public abstract class PageConfigConcorsi extends PageConfigABC {

    protected PageConfigConcorsi(HostConfig hcPar, TableScraperABC ts, EntryCanActOnFilter entryCanActOnPar, ChannelIFC chann) {
        super(hcPar, ts, entryCanActOnPar, chann);
    }

    public ChannelIFC getChannelInfo() {
        return this.channelInfo;
    }

    public EntryCanActOnFilter getContactProspectFilter() {
        return this.entryCanActOn;
    }

    // -- END Forwarding to gloabl config

    static EntryExcludeFilter entryExcludeFilter = null;

    public EntryExcludeFilter getEntryExcludeFilter() {
        if (entryExcludeFilter == null) {
            entryExcludeFilter = getEntryExcludeFilterSpecific();
        }
        return entryExcludeFilter;
    }

    public abstract EntryExcludeFilter getEntryExcludeFilterSpecific();

    static EntryIncludeFilter entryIncludeFilter = null;

    public EntryIncludeFilter getEntryIncludeFilter() {
        if (entryIncludeFilter == null) {
            entryIncludeFilter = getEntryIncludeFilterSpecific();
        }
        return entryIncludeFilter;
    }

    public abstract EntryIncludeFilter getEntryIncludeFilterSpecific();

    // jsoup selector for the table in a page
    public abstract String TableSelectCSS();

    // jsoup selector for the entries in a table
    public abstract String EntrySelectCSS();

    // table specific scraper/processor
    TableScraperABC tablePageScraper = null;

    abstract public TableScraperABC getTableScraperObject();

    public TableScraperABC getTableScraper() {
        if (tablePageScraper == null) {
            tablePageScraper = getTableScraperObject();
        }
        return tablePageScraper;
    }

    public void setTableScraper(TableScraperABC tsPar) {
        tablePageScraper = tsPar;
    }

    // table&entry specific /processor

    static EntryProcessorABC entryProcSearch = null;
    static EntryProcessorABC entryProcGeo = null;
    static EntryProcessorABC entryProcSpec = null;

    abstract public EntryProcessorABC getEntryProcObject(String configID);

    public EntryProcessorABC getEntryProc(String configID) {

        log.log(Level.WARNING,  "");
        return null;
    }

    static NextTablePageSelectorsABC nextTablePageSelectorsABC = null;

    public NextTablePageSelectorsABC getNextTablePageSelectors() {
        if (nextTablePageSelectorsABC == null) {
            nextTablePageSelectorsABC = getNextTablePageSelectorsSpecific();
        }
        return nextTablePageSelectorsABC;
    }

    protected abstract NextTablePageSelectorsABC getNextTablePageSelectorsSpecific();

    private static Logger log = LogManager.getLogManager().getLogger(PageConfigConcorsi.class.getSimpleName());

}
