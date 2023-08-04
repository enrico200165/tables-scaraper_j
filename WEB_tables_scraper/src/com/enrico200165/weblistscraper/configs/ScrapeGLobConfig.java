package com.enrico200165.weblistscraper.configs;

import com.enrico200165.weblistscraper.page.EntryProcessorABC;
import com.enrico200165.weblistscraper.page.NextTablePageSelectorsABC;
import com.enrico200165.weblistscraper.page.TableScraperABC;
import com.enrico200165.weblistscraper.tools.*;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Level;
//import com.enrico200165.utils.config.PropertiesYAMLEV;


/**
 * Read config from YAML
 * 
 * @author enrico
 *
 */

public class ScrapeGLobConfig extends PageConfigABC {


    public ScrapeGLobConfig(String cfgFName, HostConfig hcPar, TableScraperABC ts
            , EntryCanActOnFilter entryCanActOnPar, ChannelIFC channelInfoPar) {
        super(null,null,null,null);

		this.sessionLimits = new SessionLimitsBase();
        this.tableScraper = ts;
        this.entryCanActOn = entryCanActOnPar;
    }


    // Constants
    static final String TYPE_NOT_FOUND = "type_not_found";
    static final String TYPE_KEY       = "type";
    static final String TASK_KEY       = "task";
    static final String ID_KEY         = "ID";
    static final String HOST_KEY       = "host";
    static final String HOST_URI       = "baseHostURI";
    static final String LOGIN_KEY      ="login";
    static final String CHANNEL_KEY    = "channel";
    static final String TABLE_KEY      = "table";


	// logging methods to easily build composite key for printou
    static String fullKey(String father, String key) {
	    return father+"."+key;
    }
    static String fullKey(String father, String key, String id) {
        return fullKey(father,key)+"["+id+"]";
    }

    // jsoup selector for the table in a page
    public  String TableSelectCSS() {
        return null;
    }

    // jsoup selector for the entries in a table
    public  String EntrySelectCSS() {
        return null;
    }

    public EntryIncludeFilter getInclFilter() { return inclFilter; }

    public EntryExcludeFilter getExclFilter() { return exclFilter; }




    // ------- DIRTY MIGRATION METHODS, TO BE REMOVED ------------

	public ChannelIFC getChannelInfo() {
		return this.channelInfo;
	}


	public EntryCanActOnFilter getContactProspectFilter() {
		return entryCanActOn;
	}

	// -- END Forwarding to gloabl config

	static EntryExcludeFilter entryExcludeFilter = null;
	public EntryExcludeFilter getEntryExcludeFilter() {
		if (entryExcludeFilter == null) {
			entryExcludeFilter = getEntryExcludeFilterSpecific();
		}
		return entryExcludeFilter;
	}

	public  EntryExcludeFilter getEntryExcludeFilterSpecific() {
		return null;
	}
	static EntryIncludeFilter entryIncludeFilter = null;
	public EntryIncludeFilter getEntryIncludeFilter() {
		if (entryIncludeFilter == null) {
			entryIncludeFilter = getEntryIncludeFilterSpecific();
		}
		return entryIncludeFilter;
	}

	public  EntryIncludeFilter getEntryIncludeFilterSpecific(){
		return null;
	}


    public TableScraperABC getTableScraperObject(){
		 return null;
	 }

	public TableScraperABC getTableScraper() {
		if (tablePageScraper == null) {
			tablePageScraper = getTableScraperObject();
		}
		log.log(Level.WARNING,  "patch per rimediare a una dipendenza circolare dovuta a design assurdo");
		tablePageScraper.setPageConfig(this);
		return tablePageScraper;
	}

	// table&entry specific /processor

	 public EntryProcessorABC getEntryProcObject(String configID){
		 return null;
	 }

	 public EntryProcessorABC getEntryProc(String configID){
		 return null;
	 }

	static NextTablePageSelectorsABC nextTablePageSelectorsABC = null;

	public NextTablePageSelectorsABC getNextTablePageSelectors() {
		if (nextTablePageSelectorsABC == null) {
			nextTablePageSelectorsABC = getNextTablePageSelectorsSpecific();
		}
		return nextTablePageSelectorsABC;
	}


    public SessionLimitsBase getSessionLimits() { return sessionLimits;  }
    public void setSessionLimits(SessionLimitsBase sessionLimits) { this.sessionLimits = sessionLimits;  }

    protected  NextTablePageSelectorsABC getNextTablePageSelectorsSpecific(){
		return null;
	}

	// ----- functionoids non facilmente sostituiti da configurazione -----
    EntryIncludeFilter inclFilter;
    EntryExcludeFilter exclFilter;

    EntryHTMLIncludeFilter htmlInclFilter;
	EntryHTMLExcludeFilter htmlExclFilter;

    // ------ dati sostituibili o sostituiti da configurazione yaml ------
	protected ChannelIFC channelInfo;
    SessionLimitsBase sessionLimits;

	private static Logger log = LogManager.getLogManager().getLogger(ScrapeGLobConfig.class.getSimpleName());
}
