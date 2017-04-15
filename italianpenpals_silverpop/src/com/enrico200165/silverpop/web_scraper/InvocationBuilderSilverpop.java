package com.enrico200165.silverpop.web_scraper;

import java.util.Locale;

import javax.ws.rs.core.MediaType;

import com.enrico200165.weblistscraper.tools.InvocationBuilderWrapper;
import org.apache.log4j.Logger;

/*
 * 
 *  FORSE SULLA GESTIONE DEGLI HEADERS C'E' GROSSO CASINO
 * 
 */
public class InvocationBuilderSilverpop extends InvocationBuilderWrapper {

    public InvocationBuilderSilverpop(String oauthTokenPar) {
        super();
        setOauthToken(oauthTokenPar);
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
    }

    protected InvocationBuilderSilverpop adjustHeader(String name) {
        String newVal = headers.get(name);
        if (newVal != null && newVal.length() > 0)
            bld.header(name, newVal);
        else {
            log.error("header non trovato: " + name);
            System.exit(1);
        }
        return this;
    }


    @Override
    public InvocationBuilderWrapper onSet() {
        // adjustHeader("User-Agent");
        bld.accept(MediaType.TEXT_HTML_TYPE, MediaType.APPLICATION_XHTML_XML_TYPE, MediaType.WILDCARD_TYPE);
        bld.acceptLanguage(Locale.US);
        if (oauthToken != null && oauthToken.length() > 0) {
            setHeader("Authorization", "Bearer " + oauthToken);
        } else {
            // this should remove the header, if present
            setHeader("Authorization", null);
        }
        return this;
    }





    private static org.apache.log4j.Logger log = Logger.getLogger(InvocationBuilderSilverpop.class);
}