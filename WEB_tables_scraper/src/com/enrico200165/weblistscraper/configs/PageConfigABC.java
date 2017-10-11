package com.enrico200165.weblistscraper.configs;

import com.enrico200165.weblistscraper.page.EntryProcessorABC;
import com.enrico200165.weblistscraper.page.TableScraperABC;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import com.enrico200165.weblistscraper.tools.EntryExcludeFilter;
import com.enrico200165.weblistscraper.tools.EntryIncludeFilter;
import org.apache.log4j.Logger;

import com.enrico200165.weblistscraper.page.NextTablePageSelectorsABC;
import com.enrico200165.weblistscraper.tools.EntryCanActOnFilter;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Tage = table/page SEMBREREBBE che fornisce tutto ciò che serve a elaborare una pagina: - indirizzi (host-config) - selettori - oggetti
 * specifici
 * 
 * @author enrico
 *
 */
public abstract class PageConfigABC {

	public PageConfigABC(HostConfig hcPar, TableScraperABC ts, EntryCanActOnFilter entryCanActOnPar, ChannelIFC channelInfoPar) {
		super();
		this.hConfig = hcPar;
		this.tableScraper = ts;
		this.channelInfo = channelInfoPar;
		this.entryCanActOn = entryCanActOnPar;
	}

	public ChannelIFC getChannelInfo() {
		return this.channelInfo;
	}

	public HostConfig getHostConfig() {
		return this.hConfig;
	}

	public EntryCanActOnFilter getContactProspectFilter() {
		return entryCanActOn;
	}

	// -- END Forwarding to gloabl config

	static EntryExcludeFilter entryExcludeFilter = null;

	public EntryExcludeFilter getEntryExcludeFilter(SessionManagerAbstr smPar) {
		if (entryExcludeFilter == null) {
			entryExcludeFilter = getEntryExcludeFilterSpecific(smPar);
		}
		return entryExcludeFilter;
	}

	public abstract EntryExcludeFilter getEntryExcludeFilterSpecific(SessionManagerAbstr smPar);

	static EntryIncludeFilter entryIncludeFilter = null;

	public EntryIncludeFilter getEntryIncludeFilter(SessionManagerAbstr smPar) {
		if (entryIncludeFilter == null) {
			entryIncludeFilter = getEntryIncludeFilterSpecific(smPar);
		}
		return entryIncludeFilter;
	}

	public abstract EntryIncludeFilter getEntryIncludeFilterSpecific(SessionManagerAbstr smPar);

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
		log.warn("patch per rimediare a una dipendenza circolare dovuta a design assurdo");
		tablePageScraper.setPageConfig(this);
		return tablePageScraper;
	}

	// table&entry specific /processor

	abstract public EntryProcessorABC getEntryProcObject(String configID);

	abstract public EntryProcessorABC getEntryProc(String configID);

	static NextTablePageSelectorsABC nextTablePageSelectorsABC = null;

	public NextTablePageSelectorsABC getNextTablePageSelectors() {
		if (nextTablePageSelectorsABC == null) {
			nextTablePageSelectorsABC = getNextTablePageSelectorsSpecific();
		}
		return nextTablePageSelectorsABC;
	}



	// ##############################################################################
	// ### NEW PART READ FROM CONFIGURATION
	// #############################################################################


	Yaml read(String cfgFName) {
		try {
			if (cfgFName == null || cfgFName.length() < 1) {
				cfgFName = "V:\\data\\pers_dev\\data_dbs\\web_scraper\\italianpenpals.org.yaml";
			}
			InputStream is = new FileInputStream(new File(cfgFName));
			Yaml yaml = new Yaml();

			// @SuppressWarnings("unchecked")
			Map<String, ArrayList> entriesl1 = (Map<String, ArrayList>) yaml.load(is);
			for (Map.Entry<String, ArrayList> entryl1: entriesl1.entrySet()) {
				// log.info("l1 entry["+entryl1.getKey()+"]= "+entryl1.getValue());
				if (entryl1.getKey().equals("tasks")) {
					for (Object task: entryl1.getValue()) {
						if (task.getClass() != LinkedHashMap.class) {
							log.warn("unexpected class: "+task.getClass().getName());
						} else {
							parseTask((LinkedHashMap) task);
						}
					}
					continue;
				}
				log.error("unexpected entry: "+entryl1.getKey());
			}
			return yaml;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


	boolean parseTask(Object taskPar) {

		// log.info("task class: "+task.getClass().getName());
		LinkedHashMap task = (LinkedHashMap) taskPar;

		for (Object oe: task.entrySet()) {
			Map.Entry e = (Map.Entry) oe;
			log.info("task: "+e.getKey()+e.getClass().getName()+" value class: "+e.getValue().getClass());
			for (Object hostsO : (ArrayList) e.getValue()) {
				log.info(hostsO.getClass().getName());
				Map hosts = (Map) hostsO;
				// log.info("hosts : "+hosts.getClass());
				for (Object hostO: hosts.entrySet()) {
					Map.Entry host = (Map.Entry) hostO;
					log.info("host : "+host.getClass());
					parseHost(host);
				}
			}
		}
		return true;
	}

	boolean parseHost(Object hPar) {
		Map.Entry host = (Map.Entry) hPar;
		// log.info("entry: "+host.getKey()+"->"+host.getValue());
		Map hostChildren = (Map) host.getValue();
		this.hConfig = new HostConfigVanilla();
		for (Object oe: hostChildren.entrySet()) {
			Map.Entry e = (Map.Entry) oe;
			String key = (String) e.getKey();
			if (key.equals("baseHostURI")) {
				hConfig.setBaseHostURIStr((String)e.getValue());
			} else if (key.equals("loginFName")) {
				log.error("add implementation of "+key);
			} else if (key.equals("tables")) {
				log.error("add implementation of "+key);
			} else if (key.equals("baseHostURI")){
				log.error("add implementation of "+key);
			} else {
				log.error("UNKNOWN KEY: "+key);
			}
		}
		log.info(hPar.getClass());
		return true;
	}


	boolean parseHosts() {
		return true;
	}



	public static void main(String[] v) {
	    // System.out.println(System.getProperty("user.dir"));
        PageConfigABC cfg = new PageConfigVanilla(null,null,null,null,null);
		cfg.read(null);
		// System.out.println(y.toString());
	}

	Yaml yaml;
	String cfgFName;

	// temporanei, da rimuoverere
	HostConfig hConfig;


	// ### end new part #############################################################

	protected abstract NextTablePageSelectorsABC getNextTablePageSelectorsSpecific();

	protected TableScraperABC tableScraper;
	protected ChannelIFC channelInfo;

	protected EntryCanActOnFilter entryCanActOn;


	private static org.apache.log4j.Logger log = Logger.getLogger(PageConfigABC.class);
}
