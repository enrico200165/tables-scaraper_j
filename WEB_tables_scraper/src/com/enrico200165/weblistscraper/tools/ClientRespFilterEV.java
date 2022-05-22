package com.enrico200165.weblistscraper.tools;

import com.enrico200165.cookies.CookieStoreEV;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.NewCookie;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class ClientRespFilterEV implements ClientResponseFilter {

    public ClientRespFilterEV(CookieStoreEV store) {
        Map<String, NewCookie> cr = new HashMap<String, NewCookie>();
        this.cookieStore = store;
    }

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {

        log.debug("status: " + responseContext.getStatus());
        log.debug("date: " + responseContext.getDate());
        log.debug("last-modified: " + responseContext.getLastModified());
        log.debug("location: " + responseContext.getLocation());
        String h = "\nheaders:\n";
        for (Entry<String, List<String>> header : responseContext.getHeaders().entrySet()) {
            h += "    " + header.getKey() + " :";
            for (String value : header.getValue()) {
                h += value + ", ";
            }
            h += "\n";
        }
        log.debug(h);
        log.debug("media-type: " + responseContext.getMediaType().getType());


        // --- set cookies ---
        Map<String, NewCookie> cookies = responseContext.getCookies();
        if (cookies.size() > 0) {
            // cookies precedenti vanno rimossi a fine sessione, ex. session cookies, max age -1 (da implementare)
            // o uno per uno, ex ricevo existing cookie max age 0
            log.debug("cookie size before " + cookieStore.size() + "\n" + cookieStore.dump(true));
            int nr = cookieStore.addMap(cookies, requestContext.getUri());
            log.debug(nr + " cookies ricevuti: \n" + cookieStore.dump(true));
        } else {
            log.debug("no cookies");
        }
    }

    public Map<String, NewCookie> getCookie() {
        return cr;
    }

    Map<String, NewCookie> cr;
    CookieStoreEV cookieStore;

    private static Logger log = LogManager.getLogger(ClientRespFilterEV.class.getSimpleName());
}
