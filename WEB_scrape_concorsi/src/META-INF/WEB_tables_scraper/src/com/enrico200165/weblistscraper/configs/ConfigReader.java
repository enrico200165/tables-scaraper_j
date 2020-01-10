package com.enrico200165.weblistscraper.configs;

import com.enrico200165.utils.config.Exception_YAMLCfg_WrongType;
import com.enrico200165.utils.config.YAML2Map;
import com.enrico200165.weblistscraper.page.NextTablePageSelectorsABC;
import com.enrico200165.weblistscraper.tools.EntryExcludeFilter;
import com.enrico200165.weblistscraper.tools.EntryExcludeFilterVanilla;
import com.enrico200165.weblistscraper.tools.EntryIncludeFilter;
import com.enrico200165.weblistscraper.tools.EntryIncludeFilterVanilla;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.Map;

//import com.enrico200165.utils.config.PropertiesYAMLEV;





/**
 * Read config from YAML
 * 
 * @author enrico
 *
 */

public class ConfigReader   {


    public ConfigReader() {
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


    // --- now necessary,  in the future to be removed as these will build the full config

    // jsoup selector for the table in a page
    public  String TableSelectCSS() {
        return null;
    }

    private static LoginConfig parseLogin(Map.Entry<String, Object> task, String fatherKeyP) throws Exception_YAMLCfg_WrongType {

        String id = YAML2Map.getNameFromChild(task); assert (id.length() > 0);

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

        String id = YAML2Map.getNameFromChild(task); assert (id.length() > 0);

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

        String id = YAML2Map.getNameFromChild(entryPar); assert (id.length() > 0);

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

        String id = YAML2Map.getNameFromChild(entryPar); assert (id.length() > 0);

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


    private static SessionLimitsBase parseSessionLimits(Map.Entry<String, Object> entryPar, String fatherKeyP)
            throws Exception_YAMLCfg_WrongType {

        String id = YAML2Map.getNameFromChild(entryPar); assert (id.length() > 0);

        SessionLimitsBase limits = new SessionLimitsBase();

        Map<String, Object> taskListChildren = (Map<String, Object>) entryPar.getValue();
        for (Map.Entry<String, Object> e : taskListChildren.entrySet()) {

            String k = e.getKey();
            Object value_o = (Object) e.getValue();
            log.info("analyzing key: " + fullKey(fatherKeyP, k));

            if (k.equals(ID_KEY)) {
            } else if (k.equals("max_entries_read"))  {
                limits.setMaxEntriesRead(Integer.parseInt(e.getValue().toString()));
            } else if (k.equals("max_new_prospects_saves"))    {
                limits.setMaxNewProspectsSaves(Integer.parseInt(e.getValue().toString()));
            } else if (k.equals("max_contacts_exec"))     {
                limits.setMaxContactsExec(Integer.parseInt(e.getValue().toString()));
            } else if (k.equals("max_process"))  {
                limits.setMaxProcess(Integer.parseInt(e.getValue().toString()));
            } else if (k.equals("max_HTTP_calls"))  {
                limits.setMaxHTTPCalls(Integer.parseInt(e.getValue().toString()));
            } else {
                log.error("unmanaged key: " + fullKey(fatherKeyP, k, id));
                System.exit(1);
            }
        }

        return limits;
    }


    private EntryIncludeFilter parseEntryIncludeFilter(Map.Entry<String, Object> entryPar, String fatherKeyP)
            throws Exception_YAMLCfg_WrongType {

        String id = YAML2Map.getNameFromChild(entryPar); assert (id.length() > 0);

        EntryIncludeFilter inclFilter = new EntryIncludeFilterVanilla(null);

        Map<String, Object> taskListChildren = (Map<String, Object>) entryPar.getValue();
        for (Map.Entry<String, Object> e : taskListChildren.entrySet()) {

            String k = e.getKey(); Object value_o = (Object) e.getValue();
            log.info("analyzing key: " + fullKey(fatherKeyP, k));

            if (k.equals(ID_KEY)) {
            } else {
                log.error("unmanaged key: " + fullKey(fatherKeyP, k, id));
                System.exit(1);
            }
        }

        return inclFilter;
    }


    private EntryExcludeFilter parseEntryExcludeFilter(Map.Entry<String, Object> entryPar, String fatherKeyP)
            throws Exception_YAMLCfg_WrongType {

        String id = YAML2Map.getNameFromChild(entryPar); assert (id.length() > 0);

        EntryExcludeFilter  filter = new EntryExcludeFilterVanilla(null);

        Map<String, Object> taskListChildren = (Map<String, Object>) entryPar.getValue();
        for (Map.Entry<String, Object> e : taskListChildren.entrySet()) {

            String k = e.getKey(); Object value_o = (Object) e.getValue();
            log.info("analyzing key: " + fullKey(fatherKeyP, k));

            if (k.equals(ID_KEY)) {
            } else {
                log.error("unmanaged key: " + fullKey(fatherKeyP, k, id));
                System.exit(1);
            }
        }

        return filter;
    }




    private PageConfigVanilla parseTable(Map.Entry<String, Object> entryPar, String fatherKeyP)
            throws Exception_YAMLCfg_WrongType {

        String id = YAML2Map.getNameFromChild(entryPar); assert (id.length() > 0);

        PageConfigVanilla pg_cfg = new PageConfigVanilla("temporary" , null,
                null, null , null, this.gCfg);

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
            } else if (k.equals("session_limits"))   {
                SessionLimitsBase sessLim = parseSessionLimits(e, fullKey(fatherKeyP,k));
            } else if (k.equals("entry_cont_include_filter"))   {
                gCfg.inclFilter = parseEntryIncludeFilter(e,fullKey(fatherKeyP, k));
            } else if (k.equals("entry_cont_exclude_filter"))   {
                gCfg.exclFilter = parseEntryExcludeFilter(e,fullKey(fatherKeyP, k));
            }  else {
                log.error("unmanaged key: " + fullKey(fatherKeyP, k, id));
                System.exit(1);
            }
        }

        return pg_cfg;
    }




    private void parseTask(Map.Entry<String, Object> task, String fatherKeyP) throws Exception_YAMLCfg_WrongType {

	    String id = YAML2Map.getNameFromChild(task);
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
                gCfg.hConfig = parseHost(e, fullKey(fatherKeyP, k, id));
            } else if (k.equals(CHANNEL_KEY)) {
                    log.warn("temporary dirt workaround while restructuring, remove ASAP");
                gCfg.channelInfo = parseChannel(e,fullKey(fatherKeyP,k,id));
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


    /**
     *
     * @param fpath
     */
    public ScrapeGLobConfig parseYAMLConfig(String fpath) {

        this.gCfg = new ScrapeGLobConfig("dummyName", null, null, null, null);;
        Map<String, Object> prop = YAML2Map.YAML2Map(fpath);

        try {
            for (Map.Entry<String, Object> entry : prop.entrySet()) {

                String entry_type = entry.getKey();

                if (entry_type.equals("task_list")) {
                    parseTaskList(entry ,entry_type);
                    continue;
                }
                log.error("unmanaged entry type/key: "+entry_type+" exiting");
                System.exit(1);
            }
        } catch (Exception_YAMLCfg_WrongType e) {
            log.error(e);
        } catch (Exception e) {
            log.error(e);
        }

        log.info("Completed yaml file parsing");

        return gCfg;
    }


    // todo qui solo per semplificare, dovrebbe essere locale
    ScrapeGLobConfig gCfg = null; // new ScrapeGLobConfig();

    private static Logger log = Logger.getLogger(ConfigReader.class);
}
