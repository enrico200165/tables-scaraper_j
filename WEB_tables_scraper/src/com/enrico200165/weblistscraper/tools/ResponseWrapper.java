package com.enrico200165.weblistscraper.tools;

import com.enrico200165.utils.str_regex.*;
import com.enrico200165.weblistscraper.page.NextTablePageSelectorsABC;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

public class ResponseWrapper {

    public ResponseWrapper(Response r) {
        super();
        this.response = r;
        validated = false;
    }

    public ResponseWrapper() {
        super();
        this.response = null;
    }

    boolean titleContains(String content) {
        return getTitle().contains(content);
    }

    public boolean analyzeResponse(boolean followRedirect, ClientWrapper clientw) {

        String statusString = response.getStatus() + " " + response.getStatusInfo();

        switch (response.getStatus()) {
            case 200: {
                break;
            }
            case 302: {
                String location = null;
                location = response.getHeaderString("Location");
                if (followRedirect) {
                    log.debug("follow redirect to: " + location);
                    clientw.simpleGET(location, 0);
                } else {
                    log.info("ignoring redirect to: " + location);
                }
                break;
            }
            case 403: {
                log.error(statusString);
                return false;
            }
            default: {
                log.info(statusString);
            }
        }
        return true;
    }

    String getTitle() {
        if (this.pageTitle == null || this.pageTitle.length() <= 0) {
            this.pageTitle = "";
            if (getJSoupDocument() != null) {
                Elements e = getJSoupDocument().select("title");
                if (e != null && e.size() > 0) {
                    Element t;
                    t = e.get(0);
                    if (t != null) this.pageTitle = t.text();
                }
            }
        }
        return this.pageTitle;
    }

    String getBody1stChars() {
        String body = "";
        if (getJSoupDocument() != null && getJSoupDocument().select("body").size() > 0
                && getJSoupDocument().select("body").get(0).text() != null)
            body = getJSoupDocument().select("body").get(0).text();
        return body;
    }

    public String getLink(NextTablePageSelectorsABC sel) {

        String url = null;
        Document doc = getJSoupDocument();

        {   // first let's try with a jsoup selector
            String linkSelector = sel.getaElementSelector();
            if (!StringUtils.nullOrEmpty(linkSelector)) {
                Elements links = doc.select(linkSelector);
                if (links.size() > 0) {
                    Element link = links.get(0);
                    if (link != null) {
                        url = link.attr("href");
                        return url;
                    }
                }
            }
        }


        Elements links = doc.select("a");
        if (links == null || links.size() == 0)
            return null;

        String urlRegex = sel.getUrlRegex();

        // all criteria provided (not null) must be valid (AND)
        // as soon as it's valid it returns
        for (Element link : links) {
            boolean found = true;

            url = link.attr("href");

            // solo per debug
            if (url.toLowerCase().contains("acat")) {
                // log.info("raw url: " + link.html());
            }

            if (urlRegex != null && urlRegex.length() > 0) {
                found = found && url.matches(sel.getUrlRegex());
            }
            if (sel.getIdRegex() != null && sel.getIdRegex().length() > 0) {
                found = found && link.id().matches(sel.getIdRegex());
            }
            if (sel.getClasseRegex() != null && sel.getClasseRegex().length() > 0) {
                found = found && sel.getClasseRegex().matches(link.className());
            }
            if (sel.getTestoRegex() != null && sel.getTestoRegex().length() > 0) {
                String text = link.text();
                found = found && text.matches(sel.getTestoRegex());
            }

            String title = link.attr("title");
            if (sel.getTitleRegex() != null && sel.getTitleRegex().length() > 0) {
                found = found && title.matches(sel.getTitleRegex());
            }

            if (found) {
                log.info("found next table link: " + link.attr("href"));
                log.error("rimuovere questo codice e ripristina normalità");
                // return link.attr("href");
                return url;
            }
        }
        return null;
    }

    public String dump() {
        String s = "";
        s += it().getStatus();
        s += " " + it().getStatusInfo();
        s += " title: " + getTitle();
        s += " body: " + getBody1stChars();
        return s;
    }

    /**
     * To validate if we got expected page Checks whether each JSoup selector (key) contains expected text (Value
     *
     * @param expected List of name value pairs, key is JSoup Selector, value is expected text
     * @return
     */
    public boolean checkContent(ArrayList<NameValuePairString> expected) {

        if (expected == null || expected.size() == 0) {
            this.validated = true;
            return this.validated;
        }

        Document doc = getJSoupDocument();
        if (doc == null) {
            this.validated = false;
            return validated;
        }

        for (NameValuePairString nvp : expected) {
            Elements e = getJSoupDocument().select(nvp.getKey());
            if (e == null || e.size() == 0) {
                this.validated = false;
                return validated;
            }
            String valueFound = e.get(0).text();
            if (valueFound == null || !valueFound.toLowerCase().contains(nvp.getValue().toLowerCase())) {
                this.validated = false;
                return validated;
            }
        }
        this.validated = true;
        return validated;
    }

    public Document getJSoupDocument() {
        if (this.doc == null) doc = Jsoup.parse(getResponseHTML());
        return this.doc;
    }

    public Response it() {
        return this.response;
    }

    public String getResponseHTML() {
        if (htmlPage == null) htmlPage = response.readEntity(String.class);
        return htmlPage;
    }

    public void setReponse(Response rPar) {
        reset();
        this.response = rPar;
    }

    void reset() {
        this.htmlPage = null;
        this.pageTitle = null; // salvato, uso frequente
        this.response = null;
        this.doc = null;
    }

    void close() {
        it().close();
        reset();
    }

    boolean validated;
    String htmlPage;
    String pageTitle; // salvato, uso frequente
    Response response;
    Document doc;

    private static org.apache.log4j.Logger log = Logger.getLogger(ResponseWrapper.class);
}
