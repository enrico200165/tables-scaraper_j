package com.enrico200165.weblistscraper.configs;


import com.enrico200165.utils.config.Exception_YAMLCfg_WrongType;
import com.enrico200165.utils.config.YAML2Map;
import com.enrico200165.weblistscraper.page.NextTablePageSelectorsABC;
import com.enrico200165.weblistscraper.tools.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
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
    static final String CHANNEL_KEY          = "channel";
    static final String CFG_ENTRY_ID_KEY     = "channel";
    static final String HOST_KEY             = "host";
    static final String HOST_URI             = "baseHostURI";
    static final String ENTRY_TASK_LIST      = "task_list";
    static final String ENTRY_INCLUDE_FILTER = "entry_cont_include_filter";
    static final String ENTRY_EXCLUDE_FILTER = "entry_cont_exclude_filter";
    static final String ENTRY_HTML_INCLUDE_FILTER = "entry_html_include_filter";
    static final String ENTRY_HTML_EXCLUDE_FILTER = "entry_html_exclude_filter";
    static final String ENTRY_ID_KEY = "ID";
    static final String LOGIN_KEY            = "login";
    static final String TABLE_NEXT_SELECTOR  = "next_table_page_selector";
    static final String REGEXES_VERTICAL_KEY = "regexes_vertical";
    static final String SESSION_LIMITS_KEY   = "session_limits";
    static final String TABLE_KEY            = "table";
    static final String TABLE_SELECTOR_KEY   = "table_selector";
    static final String TASK_KEY             = "task";
    static final String TYPE_NOT_FOUND       = "type_not_found";
    static final String TYPE_KEY             = "type";
    static final String USER_FIELD_KEY       = "userField";
    static final String USER_VALUE_KEY       = "userValue";
    static final String PWD_FIELD_KEY       = "passwordField";
    static final String PWD_VALUE_KEY       = "passwordValue";

    private static List<String> parseRegexesVertical(Map.Entry<String, Object> entry, String fatherKeyP) throws Exception_YAMLCfg_WrongType {

        List<String> regexesList = new ArrayList<String>();
        Map<String, Object> taskListChildren = (Map<String, Object>) entry.getValue();
        for (Map.Entry<String, Object> e : taskListChildren.entrySet()) {

            String k = e.getKey(); Object value_o = (Object) e.getValue();
            log.debug("analyzing key: " + fullKey(fatherKeyP, k));

            if (k.equals("")) {
            } else {
                log.error("unmanaged key: " + fullKey(fatherKeyP,k));
                //System.exit(1);
            }
        }
        return regexesList;
    }




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

        String id = YAML2Map.getNameFromChild(task);
        assert (id.length() > 0);
        LoginConfig lc = new LoginConfig();

        Map<String, Object> taskListChildren = (Map<String, Object>) task.getValue();
        for (Map.Entry<String, Object> e : taskListChildren.entrySet()) {

            String k = e.getKey(); Object value_o = (Object) e.getValue();
            log.debug("analyzing key: " + fullKey(fatherKeyP, k));
            if (k.equals(ENTRY_ID_KEY)) {
            } else if (k.equals("loginFormURL")) {
                lc.loginFormURL = e.getValue().toString();
            } else if (k.equals("LoginFormSelector")) {
                lc.jsoupLoginFormSelector = e.getValue().toString();
            }  else if (k.equals(PWD_FIELD_KEY)) {
                lc.password_value = e.getValue().toString();
            }  else if (k.equals(PWD_VALUE_KEY)) {
                lc.password_value = e.getValue().toString();
            } else if (k.equals(USER_FIELD_KEY)) {
                lc.user_value = e.getValue().toString();
            } else if (k.equals(USER_VALUE_KEY)) {
                lc.user_value = e.getValue().toString();
            } else if (k.equals("formAction")) {
                lc.loginFormAction = e.getValue().toString();
            } else {
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
            log.debug("analyzing key: " + fullKey(fatherKeyP, k));

            if (k.equals(ENTRY_ID_KEY)) {
            } else if (k.equals(HOST_URI)) {
                try { hc.baseHostURI = new URI(e.getValue().toString()); }
                catch(Exception exc) { log.error(exc); }
            } else if (k.equals(LOGIN_KEY)) {
                lc = parseLogin(e,fullKey(fatherKeyP,k,id));
                hc.loginFormURL = lc.loginFormURL;
                hc.jsoupLoginFormSelector = lc.jsoupLoginFormSelector;
                hc.loginFormAction = lc.loginFormAction;
                hc.user = lc.user_value;
                hc.password = lc.password_value;
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
            log.debug("analyzing key: " + fullKey(fatherKeyP, k));

            if (k.equals(ENTRY_ID_KEY)) {
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
            log.debug("analyzing key: " + fullKey(fatherKeyP, k));

            if (k.equals(ENTRY_ID_KEY)) {
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


    private static com.enrico200165.weblistscraper.configs.SessionLimitsBase parseSessionLimits(Map.Entry<String, Object> entryPar, String fatherKeyP)
            throws Exception_YAMLCfg_WrongType {

        String id = YAML2Map.getNameFromChild(entryPar); assert (id.length() > 0);

        SessionLimitsBase limits = new SessionLimitsBase();

        Map<String, Object> taskListChildren = (Map<String, Object>) entryPar.getValue();
        for (Map.Entry<String, Object> e : taskListChildren.entrySet()) {

            String k = e.getKey();
            Object value_o = (Object) e.getValue();
            log.debug("analyzing key: " + fullKey(fatherKeyP, k));

            if (k.equals(ENTRY_ID_KEY)) {
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

        EntryIncludeFilter inclFilter = new EntryIncludeFilterVanilla();

        Map<String, Object> taskListChildren = (Map<String, Object>) entryPar.getValue();
        for (Map.Entry<String, Object> e : taskListChildren.entrySet()) {

            String k = e.getKey(); Object value_o = (Object) e.getValue();
            log.debug("analyzing key: " + fullKey(fatherKeyP, k));

            if (k.equals(ENTRY_ID_KEY)) {
            } else if (k.equals(REGEXES_VERTICAL_KEY)) {
                List<String> regexes = parseRegexesVertical(e, fatherKeyP);
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

        EntryExcludeFilter  filter = new EntryExcludeFilterVanilla();

        Map<String, Object> taskListChildren = (Map<String, Object>) entryPar.getValue();
        for (Map.Entry<String, Object> e : taskListChildren.entrySet()) {

            String k = e.getKey(); Object value_o = (Object) e.getValue();
            log.debug("analyzing key: " + fullKey(fatherKeyP, k));

            if (k.equals(ENTRY_ID_KEY)) {
            } else {
                log.error("unmanaged key: " + fullKey(fatherKeyP, k, id));
                System.exit(1);
            }
        }

        return filter;
    }


    private EntryHTMLIncludeFilter  parseEntryHTMLIncludeFilter(Map.Entry<String, Object> entryPar, String fatherKeyP)
            throws Exception_YAMLCfg_WrongType {

        String id = YAML2Map.getNameFromChild(entryPar); assert (id.length() > 0);

        EntryHTMLIncludeFilter inclFilter = new EntryHTMLIncludeFilter();

        Map<String, Object> taskListChildren = (Map<String, Object>) entryPar.getValue();
        for (Map.Entry<String, Object> e : taskListChildren.entrySet()) {

            String k = e.getKey(); Object value_o = (Object) e.getValue();
            log.debug("analyzing key: " + fullKey(fatherKeyP, k));

            if (k.equals(ENTRY_ID_KEY)) {
            } else if (k.equals(REGEXES_VERTICAL_KEY)) {
                List<String> regexes = parseRegexesVertical(e, fatherKeyP);
            } else {
                log.error("unmanaged key: " + fullKey(fatherKeyP, k, id));
                System.exit(1);
            }
        }

        return inclFilter;
    }

    private EntryHTMLExcludeFilter  parseEntryHTMLExcludeFilter(Map.Entry<String, Object> entryPar, String fatherKeyP)
            throws Exception_YAMLCfg_WrongType {

        String id = YAML2Map.getNameFromChild(entryPar); assert (id.length() > 0);

        EntryHTMLExcludeFilter exclFilter = new EntryHTMLExcludeFilter();

        Map<String, Object> taskListChildren = (Map<String, Object>) entryPar.getValue();
        for (Map.Entry<String, Object> e : taskListChildren.entrySet()) {

            String k = e.getKey(); Object value_o = (Object) e.getValue();
            log.debug("analyzing key: " + fullKey(fatherKeyP, k));

            if (k.equals(ENTRY_ID_KEY)) {
            } else if (k.equals(REGEXES_VERTICAL_KEY)) {
                List<String> regexes = parseRegexesVertical(e, fatherKeyP);
            } else {
                log.error("unmanaged key: " + fullKey(fatherKeyP, k, id));
                System.exit(1);
            }
        }

        return exclFilter;
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
            log.debug("analyzing key: " + fullKey(fatherKeyP, k));

            if (k.equals(ENTRY_ID_KEY)) {
            } else if (k.equals(TABLE_NEXT_SELECTOR))   {
                NextTablePageSelectorsABC sel = parseNextPageSel(e, fullKey(fatherKeyP,k));
                pg_cfg.setNextTablePageSelectors(sel);
            } else if (k.equals(HOST_KEY)) {
                gCfg.hConfig = parseHost(e, fullKey(fatherKeyP, k, id));
            } else if (k.equals("table_url"))   {
                pg_cfg.setTableURl(e.getValue().toString());
            } else if (k.equals("session_limits"))   {
                SessionLimitsBase sessLim = parseSessionLimits(e, fullKey(fatherKeyP,k));
            } else if (k.equals(ENTRY_INCLUDE_FILTER))   {
                gCfg.inclFilter = parseEntryIncludeFilter(e,fullKey(fatherKeyP, k));
            } else if (k.equals(ENTRY_EXCLUDE_FILTER))   {
                gCfg.exclFilter = parseEntryExcludeFilter(e,fullKey(fatherKeyP, k));
            } else if (k.equals(ENTRY_HTML_INCLUDE_FILTER))   {
                gCfg.htmlInclFilter = parseEntryHTMLIncludeFilter(e,fullKey(fatherKeyP, k));
            }  else if (k.equals(ENTRY_HTML_EXCLUDE_FILTER))   {
                gCfg.htmlExclFilter = parseEntryHTMLExcludeFilter(e,fullKey(fatherKeyP, k));
            }  else if (k.equals(TABLE_SELECTOR_KEY))   {
                gCfg.htmlExclFilter = parseEntryHTMLExcludeFilter(e,fullKey(fatherKeyP, k));
            } else if (k.equals(ENTRY_ID_KEY))   {
                log.warn("da implementare");
            } else {
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
            log.debug("analyzing key: "+fatherKeyP+"."+e.getKey());

            // fetch the values is they are in these format
            Map<String , Object> values = null;
            if (value_o instanceof Map<?, ?>) {
                values = (Map<String , Object>) e.getValue();
            }
            if (k.equals(ENTRY_ID_KEY)) {

            } else if (k.equals(CHANNEL_KEY)) {
                gCfg.channelInfo = parseChannel(e,fullKey(fatherKeyP,k,id));
            } else if (k.equals(TABLE_KEY)) {
                parseTable(e,fullKey(fatherKeyP,k));
            } else if (k.equals(SESSION_LIMITS_KEY)) {
                parseSessionLimits(e,fullKey(fatherKeyP,k));
            } else { // non-structured values
                log.error("unmanaged key: "+fullKey(fatherKeyP,k,id));
                System.exit(1);
            }
        }

    }


    public  void parseTaskList(Map.Entry<String, Object> tasksList, String fatherKeyP) throws Exception_YAMLCfg_WrongType {

        Map<String , Object> taskListChildren = (Map<String , Object> ) tasksList.getValue();
        for (Map.Entry<String , Object> e : taskListChildren.entrySet()) {

            String k = e.getKey(); Object value_o = (Object) e.getValue();

            if (k.equals(ENTRY_ID_KEY)) {
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

                if (entry_type.equals(ENTRY_TASK_LIST)) {
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

    private static Logger log = LogManager.getLogger(ConfigReader.class.getName());
}
