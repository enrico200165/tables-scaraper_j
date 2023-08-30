package com.enrico200165.weblistscraper.tools;

import com.enrico200165.cookies.CookieStoreEV;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.NewCookie;
import java.io.IOException;
import java.net.HttpCookie;
import java.net.URI;
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
            log.debug("cookie size before " + cookieStore.size() + "\n" + cookieStore.dump());

            // EVTODO
            int nr = 0;
            for (Map.Entry<String, NewCookie> entry : cookies.entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue());
                String key = entry.getKey();
                NewCookie nc = entry.getValue();
                HttpCookie hc = NewCookieToHttpCookie(nc);
                URI uri = requestContext.getUri() == null ? URI.create("http:://null.com") : requestContext.getUri();
                cookieStore.add(uri,hc);
                nr++;
            }
            log.debug(nr + " cookies ricevuti: \n" + cookieStore.dump());
        } else {
            log.debug("no cookies");
        }
    }

    HttpCookie NewCookieToHttpCookie(NewCookie newCookie) {
        HttpCookie cookie = new HttpCookie(newCookie.getName(), newCookie.getValue());
        cookie.setComment(newCookie.getComment());
        cookie.setDomain(newCookie.getDomain());
        cookie.setHttpOnly(newCookie.isHttpOnly());
        cookie.setMaxAge(newCookie.getMaxAge());
        cookie.setPath(newCookie.getPath());
        cookie.setSecure(newCookie.isSecure());
        cookie.setVersion(newCookie.getVersion());

        return cookie;
    }

    public Map<String, NewCookie> getCookie() {
        return cr;
    }

    Map<String, NewCookie> cr;
    CookieStoreEV cookieStore;

    private static Logger log = LogManager.getLogger(ClientRespFilterEV.class.getSimpleName());
}
