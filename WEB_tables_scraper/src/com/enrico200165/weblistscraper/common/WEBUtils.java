package com.enrico200165.weblistscraper.common;

import com.enrico200165.cookies.CookieStoreEV;
import com.enrico200165.utils.str_regex.NameValuePairString;
import com.enrico200165.weblistscraper.tools.ClReqFilterCookies;
import com.enrico200165.weblistscraper.tools.ClientRespFilterEV;
import com.enrico200165.weblistscraper.tools.ResponseWrapper;
import com.enrico200165.weblistscraper.tools.Result;
import com.enrico200165.weblistscraper.tools.Result.RC;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class WEBUtils {

    // in realta questa dovrebbe essere molto più complicata, in particolare per
    // gestire ? & = #
    public static final String pathCharRegex = "[\\w\\d\\-_\\.\\?&=#;]";

    public static final String pathSegmentRegex = "(" + pathCharRegex + "+" + ")";

    public static final String pathElemRegex = "/" + pathSegmentRegex;
    // singolo elemento della parte in più di un sottodominio rispetto al dominio
    // in  host1.cluster1.google.com è host1. o cluster1. (punto incluso)
    public static final String pathRegex = "/|(" + pathElemRegex + ")+/?";
    public static final String tildePathRegex1 = "~" + "(pathSegmentRegex)?";
    public static final String tildePathRegex2 = "~" + pathSegmentRegex + "(" + pathElemRegex + ")*/?";

    // delay to simulate human
    public static String callDelayProperty = "call_delay";

    // buttate dentro senza verifica
    // da rivedere probabilmente non corrette
    public static final String domainCharRegex = "[\\w-]";
    // la parte fra i . in host1.google.com  host1 google com
    public static final String domainWordRegex = domainCharRegex + "+";
    // singolo elemento della parte in più di un sottodominio rispetto al dominio
    // in  host1.cluster1.google.com è host1. o cluster1. (punto incluso)
    public static final String subDomainOneLeftRegex = domainWordRegex + "\\.";
    public static final String subDomainLeftRegex = "(" + subDomainOneLeftRegex + ")" + "+";




    public static String evSelTxt(Element entry, String sel) {

        Elements els = entry.select(sel);
        if (els.size() == 1 && els.get(0) != null) {
            return els.get(0).text();
        }
        log.error("no match per: " + sel);
        return null;
    }

    public static String evSelAttrTxt(Element entry, String sel, String attr) {

        Elements els = entry.select(sel);
        if (els.size() == 1 && els.get(0).attr(attr) != null) {
            return els.get(0).attr(attr);
        }

        log.error("unable to find attribute " + attr + " in " + entry);
        return null;
    }



    public static HttpCookie fromNewCookie(NewCookie c) {

        HttpCookie cookie = new HttpCookie(c.getName(), c.getValue());

        cookie.setComment(c.getComment());
        cookie.setCommentURL("not available");
        cookie.setDiscard(false);
        cookie.setDomain(c.getDomain());
        cookie.setMaxAge(c.getMaxAge());
        cookie.setPath(c.getPath());
        cookie.setPortlist("not available");
        cookie.setSecure(c.isSecure());
        cookie.setVersion(c.getVersion());
        cookie.setHttpOnly(c.isHttpOnly());

        return cookie;
    }

    public static String cookieToString(NewCookie cookie, String fieldDelim) {
        String s = "";
        // s += cookie.toString(); // per ora prendiamo il default
        s += "name=" + cookie.getName();
        s += fieldDelim + "path=" + cookie.getPath();
        s += fieldDelim + "domain=" + cookie.getDomain();
        s += fieldDelim + "comment=" + cookie.getComment();
        s += fieldDelim + "max age=" + cookie.getMaxAge();
        s += fieldDelim + "expiry date=" + cookie.getExpiry();

        s += fieldDelim + "version=" + cookie.getVersion();
        s += fieldDelim + "value=" + cookie.getValue();

        return s;
    }


    static ClReqFilterCookies clReqFilterCookies = null;

    static public ClReqFilterCookies getClReqFilterCookies() {
        if (clReqFilterCookies == null) {
            clReqFilterCookies = new ClReqFilterCookies(getCookieStoreEV());
        }
        return clReqFilterCookies;
    }

    static ClientResponseFilter respFilter = null;

    static public ClientResponseFilter getClRespFilter() {
        if (respFilter == null) {
            respFilter = new ClientRespFilterEV(getCookieStoreEV());
        }
        return respFilter;
    }

    static CookieStoreEV cookieStore = null;

    public static CookieStoreEV getCookieStoreEV() {
        if (cookieStore == null) {
            cookieStore = new CookieStoreEV();
        }
        return cookieStore;
    }

    static Client client = null;

    public static Client createClient(ClientRequestFilter reqF, ClientResponseFilter crespF, boolean recreate) {

        if (client != null && recreate) {
            log.warn("ricreo client");
        }

        if (client == null || recreate) {

            client = ClientBuilder.newClient();

            if (reqF == null)
                client.register(getClReqFilterCookies());
            else
                client.register(reqF);

            if (crespF == null)
                client.register(getClRespFilter());
            else
                client.register(crespF);

            client.property("enrico", "ottimo");
        } else {
            log.warn("Client jax-rs già creato");
        }
        return client;
    }


    static public List<NameValuePairString> formFieldsFromString(String fromFiddler) {
        List<NameValuePairString> fields = new ArrayList<NameValuePairString>();

        String[] pairs = fromFiddler.split("&");
        for (String couple : pairs) {
            String name;
            String value;
            name = couple.split("=")[0];
            value = couple.split("=")[1];
            fields.add(new NameValuePairString(name, value));
        }
        return fields;
    }


    public static String dumpNameValuePairsList(List<NameValuePairString> list) {
        String ret = "";

        for (NameValuePairString nvp : list) {
            ret += "\"" + nvp.getKey() + "\" = \"" + nvp.getValue() + "\"";
            ret += "\n";
        }
        return ret;
    }


    public static boolean getFormFields(ResponseWrapper rw, List<NameValuePairString> hiddenFormFields, String formSelector) {
        // --- analisi della pagina contente la form, specifica al sito
        Document doc = rw.getJSoupDocument();
        Elements els = doc.select(formSelector); // per debug, dovrebbe essere uo
        if (els == null || els.size() <= 0) {
            log.error("unable to find form at selector: " + formSelector);
            System.exit(1);
            return false;
        }
        Element loginForm = els.get(0);
        if (loginForm == null) {
            log.error("failed to get form to analyze at: " + rw.dump());
            System.exit(1);
        }
        // log.info("login form OUTER HTML\n" + loginForm.outerHtml());
        Elements inputFields = loginForm.select("input");
        // display all
        for (Element e : inputFields) {
            String type = e.attr("type");
            if (type.equals("submit")) {
                continue;
            }
            String attrName = e.attr("name");
            hiddenFormFields.add(new NameValuePairString(attrName, e.val()));
            log.debug("captured form input: " + attrName + " = " + e.val());
        }
        return false;
    }

    /**
     * Used to set form fields
     *
     * @param formFields
     * @param replaceVals
     * @param removeList
     * @param addList
     * @return
     */
    static public boolean setFormParams(List<NameValuePairString> formFields, List<NameValuePairString> replaceVals,
                                        List<String> removeList, List<NameValuePairString> addList) {


        // campi da rimuovere
        if (removeList != null) for (NameValuePairString nvp : formFields) {
            for (String removeKey : removeList) {
                if (removeKey.equals(nvp.getKey())) {
                    formFields.remove(nvp);
                    continue;
                }
            }
        }

        // replace values
        if (replaceVals != null) for (NameValuePairString nvp : formFields) {
            for (NameValuePairString repl : replaceVals) {
                if (nvp.getKey().equals(repl.getKey())) {
                    nvp.setValue(repl.getValue());
                }
            }
        }

        // addMap name value pairs
        if (addList != null) for (NameValuePairString nvp : addList) {
            formFields.add(nvp);
        }

        // rimuoviamo campi con chiave nulla, non so perchè ma ne pesco
        ArrayList<NameValuePairString> temp = new ArrayList<NameValuePairString>();
        for (NameValuePairString nvp : formFields) {
            if (nvp.getKey().length() > 0) {
                temp.add(nvp);
            } else {
                log.info("beccata chiave nulla, valore: " + nvp.getValue());
            }
        }
        formFields.clear();
        formFields.addAll(temp);

        return true;
    }

    static public Result textSelList(Element elsPar, ArrayList<String> jsoupSelectors) {
        Result res = new Result();
        Elements els;
        Elements elsTemp;

        els = elsPar.select(jsoupSelectors.remove(0));
        for (String sel : jsoupSelectors) {
            elsTemp = els.select(sel);
            if (elsTemp != null) els = elsTemp;
        }

        if (els == null || els.size() != 1) {
            log.error("jsoup selector on elements does not match 1");
            System.exit(1);
            return res.setContinua(false);
        }
        return res.setRetStr(els.get(0).text());
    }

    static public Result text(Element elsPar, String jsoupSelector) {
        return text(elsPar, jsoupSelector, true);
    }

    static public Result text(Element elsPar, String jsoupSelector, boolean exitIfNotFound) {
        Result res = new Result();
        Elements els = elsPar.select(jsoupSelector);
        if (els == null || els.size() != 1) {
            res.setRc(RC.NOT_FOUND);
            if (exitIfNotFound) {
                res.setRc(RC.ERROR);
                res.setErrorMessage("jsoup selector on elements does not match: " + jsoupSelector + "\n" + elsPar.html());
                log.error(res.getErrorMessage());
            }
            return res.setContinua(false).setRetStr("");
        }
        return res.setRetStr(els.get(0).text());
    }

    public static String dumpResponse(Response r, boolean detailed, String fieldDelim) {
        String s = "[Response obiect] ";
        s += "status=" + r.getStatus();
        s += fieldDelim + "statusInfo=" + r.getStatusInfo();
        s += fieldDelim + "location=" + r.getLocation();

        if (!detailed) return s;

        MultivaluedMap<String, String> headersStrings = r.getStringHeaders();
        Iterator<String> it = headersStrings.keySet().iterator();
        s += "\n---- headers ---";
        while (it.hasNext()) {
            String theKey = it.next();
            s += "\n" + theKey + ":";
            List<String> valuesList = headersStrings.get(theKey);
            for (String value : valuesList) {
                s += value + ",";
            }
        }

        return s;
    }

    static public void waitBeforePageLoad(int seconds, boolean testRun) {

        if (testRun || seconds == -1) return;
        if (seconds == 0) seconds = 3;

        Random rand = new Random();
        long mseconds = rand.nextInt((seconds - 1) * 1000) + 1000;
        mseconds += 1500; // sempre almeno 1.5 secs
        try {
            Thread.sleep(mseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // forse errato a causa di domainWordRegex che ora accetta  - dappertutto
    // ex. ---- o -a-
    public static boolean isDomainCorrect(String dom) {

        if (dom.equals("")) {
            // log.warn("empty domain");
            return true;
        }

        return dom.matches("\\.?" + subDomainLeftRegex + domainWordRegex);
    }

    public static boolean isSubDomainOf(String sub, String dom, boolean acceptEqual) {

        if (!isDomainCorrect(sub) || !isDomainCorrect(dom)) {
            log.error("exiting invalid domain(s): " + dom + " altro " + sub);
            System.exit(1);
        }
        if (acceptEqual && sub.equals(dom)) return true;

        if (!sub.endsWith(dom)) return false;
        boolean ret = sub.matches(subDomainLeftRegex + dom);

        return ret;
    }

    public static boolean isPathCorrect(String path) {
        boolean ret = false;
        ret = path.matches(pathRegex);
        ret = ret || path.matches(tildePathRegex1);
        ret = ret || path.matches(tildePathRegex2);
        return ret;
    }

    public static boolean isSubPathOf(String sub, String path, boolean acceptEqual) {

        if (!(isPathCorrect(sub) && isPathCorrect(path))) {
            log.error("exiting invalid path(s): " + sub + " altro " + path);
            isPathCorrect(path);
            isPathCorrect(sub);
            System.exit(1);
            return false;
        }

        if (path.endsWith("/") && path.length() > 1) path = path.substring(0, path.length() - 1);
        if (sub.endsWith("/") && sub.length() > 1) sub = sub.substring(0, sub.length() - 1);

        // da qui path corretti ed eventualmente normalizzati

        if (acceptEqual) {
            if (sub.equals(path)) return true;
        }

        if (sub.length() <= path.length()) return false;

        String[] subElems = sub.split("/");
        String[] pathElems = path.split("/");

        for (int i = 0; i < subElems.length && i < pathElems.length; i++) {
            if (!subElems[i].equals(pathElems[i])) return false;
        }

        return true;
    }

    private static Logger log = LogManager.getLogger(WEBUtils.class.getSimpleName());
}
