package com.enrico200165.weblistscraper.configs;

import com.enrico200165.utils.config.Exception_YAMLCfg_WrongType;
import com.enrico200165.utils.config.PropertiesYAMLEV;
import com.enrico200165.weblistscraper.page.EntryProcessorABC;
import com.enrico200165.weblistscraper.page.NextTablePageSelectorsABC;
import com.enrico200165.weblistscraper.page.TableScraperABC;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import com.enrico200165.weblistscraper.tools.EntryCanActOnFilter;
import com.enrico200165.weblistscraper.tools.EntryExcludeFilter;
import com.enrico200165.weblistscraper.tools.EntryIncludeFilter;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Read config from YAML
 * 
 * @author enrico
 *
 */

public class ConfigReader  extends PageConfigABC {


    public ConfigReader(String cfgFName, HostConfig hcPar, TableScraperABC ts, EntryCanActOnFilter entryCanActOnPar, ChannelIFC channelInfoPar) {
        super(null,null,null,null);

        this.hConfig = hcPar;
        this.tableScraper = ts;
        this.channelInfo = channelInfoPar;
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

	static void  dummy() {
		ConfigReader x = new ConfigReader(null,null
                ,null,null,null);
	}


	// logging methods to easily build composite key for printou
    static String fullKey(String father, String key) {
	    return father+"."+key;
    }
    static String fullKey(String father, String key, String id) {
        return fullKey(father,key)+"["+id+"]";
    }


    // ------- DIRTY MIGRATION METHODS, TO BE REMOVED ------------


    // ------- end DIRTY MIGRATION METHODS, TO BE REMOVED ---------

    private static LoginConfig parseLogin(Map.Entry<String, Object> task, String fatherKeyP) throws Exception_YAMLCfg_WrongType {

        String id = PropertiesYAMLEV.getNameChild(task); assert (id.length() > 0);

        LoginConfig lc = new LoginConfig();

        Map<String, Object> taskListChildren = (Map<String, Object>) task.getValue();
        for (Map.Entry<String, Object> e : taskListChildren.entrySet()) {

            String k = e.getKey(); Object value_o = (Object) e.getValue();
            log.info("analyzing key: " + fullKey(fatherKeyP, k));

            if (k.equals(ID_KEY)) {
            } else if (k.equals("loginFormURL")) {
                lc.loginFormURL = e.getValue().toString();
            } else if (k.equals("LoginFormSelector")) {
                lc.jsoupLoginFormSelector = e.getValue().toString();
            } else if (k.equals("userValue")) {
                lc.user = e.getValue().toString();
            }  else if (k.equals("passwordValue")) {
                lc.password = e.getValue().toString();
            } else if (k.equals("formAction")) {
                lc.loginFormAction = e.getValue().toString();
            }

            else {
                log.error("unmanaged key: " + fullKey(fatherKeyP,k,id));
                //System.exit(1);
            }
        }
        return lc;
    }



    private static HostConfig parseHost(Map.Entry<String, Object> task, String fatherKeyP)
            throws Exception_YAMLCfg_WrongType {

        String id = PropertiesYAMLEV.getNameChild(task); assert (id.length() > 0);

        HostConfig hc = new HostConfig();
        LoginConfig lc = null;

        Map<String, Object> taskListChildren = (Map<String, Object>) task.getValue();
        for (Map.Entry<String, Object> e : taskListChildren.entrySet()) {

            String k = e.getKey(); Object value_o = (Object) e.getValue();
            log.info("analyzing key: " + fullKey(fatherKeyP, k));

            if (k.equals(ID_KEY)) {
            } else if (k.equals(HOST_URI)) {
                try { hc.baseHostURI = new URI(e.getValue().toString()); }
                catch(Exception exc) { log.error(exc); }
            } else if (k.equals(LOGIN_KEY)) {
                lc = parseLogin(e,fullKey(fatherKeyP,k,id));
                hc.loginFormURL = lc.loginFormURL;
                hc.jsoupLoginFormSelector = lc.jsoupLoginFormSelector;
                hc.loginFormAction = lc.loginFormAction;
                hc.user = lc.user;
                hc.password = lc.password;
            } else {
                log.error("unmanaged key: " + fullKey(fatherKeyP,k,id));
                System.exit(1);
            }
        }

        return hc;
    }


    private static ChannelIFC parseChannel(Map.Entry<String, Object> entryPar, String fatherKeyP)
            throws Exception_YAMLCfg_WrongType {

        String id = PropertiesYAMLEV.getNameChild(entryPar); assert (id.length() > 0);

        ChannelIFC chann = new ChannelConfigVanilla();

        Map<String, Object> taskListChildren = (Map<String, Object>) entryPar.getValue();
        for (Map.Entry<String, Object> e : taskListChildren.entrySet()) {

            String k = e.getKey();
            Object value_o = (Object) e.getValue();
            log.info("analyzing key: " + fullKey(fatherKeyP, k));

            if (k.equals(ID_KEY)) {
            } else if (k.equals("name"))   { chann.setName(e.getValue().toString());
            } else if (k.equals("type"))   { chann.setType(e.getValue().toString());
            } else if (k.equals("vendor")) { chann.setVendor(e.getValue().toString());
            } else if (k.equals("item"))   { chann.setItem(e.getValue().toString());
            } else {
                log.error("unmanaged key: " + fullKey(fatherKeyP, k, id));
                System.exit(1);
            }
        }

        return chann;
    }


    private static NextTablePageSelectorsABC parseNextPageSel(Map.Entry<String, Object> entryPar, String fatherKeyP)
            throws Exception_YAMLCfg_WrongType {

        String id = PropertiesYAMLEV.getNameChild(entryPar); assert (id.length() > 0);

        NextTablePageSelectorsABC nextPageSel = new NextTablePageSelectorsABC();

        Map<String, Object> taskListChildren = (Map<String, Object>) entryPar.getValue();
        for (Map.Entry<String, Object> e : taskListChildren.entrySet()) {

            String k = e.getKey();
            Object value_o = (Object) e.getValue();
            log.info("analyzing key: " + fullKey(fatherKeyP, k));

            if (k.equals(ID_KEY)) {
            } else if (k.equals("title_regex"))  { nextPageSel.setTitleRegex(e.getValue().toString());
            } else if (k.equals("url_regex"))    { nextPageSel.setUrlRegex(e.getValue().toString());
            } else if (k.equals("id_regex"))     { nextPageSel.setIdRegex(e.getValue().toString());
            } else if (k.equals("class_regex"))  { nextPageSel.setClasseRegex(e.getValue().toString());
            } else if (k.equals("testo_regex"))  { nextPageSel.setTestoRegex(e.getValue().toString());
            } else {
                log.error("unmanaged key: " + fullKey(fatherKeyP, k, id));
                System.exit(1);
            }
        }

        return nextPageSel;
    }



    private PageConfigVanilla parseTable(Map.Entry<String, Object> entryPar, String fatherKeyP)
            throws Exception_YAMLCfg_WrongType {

        String id = PropertiesYAMLEV.getNameChild(entryPar); assert (id.length() > 0);

        PageConfigVanilla pg_cfg = new PageConfigVanilla("temporary" , null
        ,  null, null , null);

        Map<String, Object> taskListChildren = (Map<String, Object>) entryPar.getValue();
        for (Map.Entry<String, Object> e : taskListChildren.entrySet()) {

            String k = e.getKey();
            Object value_o = (Object) e.getValue();
            log.info("analyzing key: " + fullKey(fatherKeyP, k));

            if (k.equals(ID_KEY)) {
            } else if (k.equals("next_table_page_selector"))   {
                NextTablePageSelectorsABC sel = parseNextPageSel(e, fullKey(fatherKeyP,k));
                pg_cfg.setNextTablePageSelectors(sel);
            } else if (k.equals("table_url"))   {
                pg_cfg.setTableURl(e.getValue().toString());

            } else {
                log.error("unmanaged key: " + fullKey(fatherKeyP, k, id));
                System.exit(1);
            }
        }

        return pg_cfg;
    }




    private void parseTask(Map.Entry<String, Object> task, String fatherKeyP) throws Exception_YAMLCfg_WrongType {

	    String id = PropertiesYAMLEV.getNameChild(task);
	    assert(id.length() > 0);

        Map<String , Object> taskListChildren = (Map<String , Object> ) task.getValue();
        for (Map.Entry<String , Object> e : taskListChildren.entrySet()) {

            String k = e.getKey(); Object value_o = (Object) e.getValue();
            log.info("analyzing key: "+fatherKeyP+"."+e.getKey());

            // fetch the values is they are in these format
            Map<String , Object> values = null;
            if (value_o instanceof Map<?, ?>) {
                values = (Map<String , Object>) e.getValue();
            }
            if (k.equals(ID_KEY)) {}
            else if (k.equals(HOST_KEY)) {
                log.warn("temporary dirt workaround while restructuring, remove ASAP");
                this.hConfig = parseHost(e, fullKey(fatherKeyP, k, id));
            } else if (k.equals(CHANNEL_KEY)) {
                    log.warn("temporary dirt workaround while restructuring, remove ASAP");
                    this.channelInfo = parseChannel(e,fullKey(fatherKeyP,k,id));
            } else if (k.equals(TABLE_KEY)) {
                log.warn("temporary dirt workaround while restructuring, remove ASAP");
                parseTable(e,fullKey(fatherKeyP,k));
            } else { // non-structured values
                log.error("unmanaged key: "+fullKey(fatherKeyP,k,id));
                System.exit(1);
            }
        }

    }


    public  void parseTaskList(@NotNull Map.Entry<String, Object> tasksList, String fatherKeyP) throws Exception_YAMLCfg_WrongType {

        Map<String , Object> taskListChildren = (Map<String , Object> ) tasksList.getValue();
        for (Map.Entry<String , Object> e : taskListChildren.entrySet()) {

            String k = e.getKey(); Object value_o = (Object) e.getValue();

            if (k.equals(ID_KEY)) {
            }  else if (k.equals(TASK_KEY)) {
                parseTask(e,fullKey(fatherKeyP,k));
            } else {
                log.error("unmanaged key: "+k+" exiting");
                System.exit(1);
            }

        }
    }





    public void parseYAMLConfig(String fpath) {

        String content = null;

        try  {  content = new String(Files.readAllBytes(Paths.get(fpath))); }
        catch (IOException e) { log.error("working dir"+System.getProperty("user.dir"),e);  }

        Map<String, Object> prop = (Map<String, Object>) (new Yaml()).load(content);

        try {
            for (Map.Entry<String, Object> entry : prop.entrySet()) {

                String k = entry.getKey(); Object value_o = entry.getValue();

                if (k.equals("task_list")) { parseTaskList(entry ,k); continue; }
                if (k.equals("ID")) { log.info("ID:"+PropertiesYAMLEV.getNameChild(entry)); continue; }

                log.error("unmanaged key: "+k+" exiting");
                System.exit(1);
            }
        } catch (Exception_YAMLCfg_WrongType e) {
            log.error(e);
        } catch (Exception e) {
            log.error(e);
        }

        log.info("Completed yaml file parsing");


    }




	public ChannelIFC getChannelInfo() {
		return this.channelInfo;
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

	public  EntryExcludeFilter getEntryExcludeFilterSpecific(SessionManagerAbstr smPar) {
		return null;
	}

	static EntryIncludeFilter entryIncludeFilter = null;

	public EntryIncludeFilter getEntryIncludeFilter(SessionManagerAbstr smPar) {
		if (entryIncludeFilter == null) {
			entryIncludeFilter = getEntryIncludeFilterSpecific(smPar);
		}
		return entryIncludeFilter;
	}

	public  EntryIncludeFilter getEntryIncludeFilterSpecific(SessionManagerAbstr smPar){
		return null;
	}

	// jsoup selector for the table in a page
	public  String TableSelectCSS() {
		return null;
	}

	// jsoup selector for the entries in a table
	public  String EntrySelectCSS() {
		return null;
	}

	// table specific scraper/processor
	TableScraperABC tablePageScraper = null;

	 public TableScraperABC getTableScraperObject(){
		 return null;
	 }

	public TableScraperABC getTableScraper() {
		if (tablePageScraper == null) {
			tablePageScraper = getTableScraperObject();
		}


		log.warn("patch per rimediare a una dipendenza circolare dovuta a design assurdo");
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




	protected  NextTablePageSelectorsABC getNextTablePageSelectorsSpecific(){
		return null;
	}

	protected TableScraperABC tableScraper;
	protected ChannelIFC channelInfo;

	protected EntryCanActOnFilter entryCanActOn;


	private static Logger log = Logger.getLogger(ConfigReader.class);
}
