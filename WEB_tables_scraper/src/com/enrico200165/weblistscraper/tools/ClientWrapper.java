package com.enrico200165.weblistscraper.tools;


import com.enrico200165.utils.str_regex.*;
import com.enrico200165.weblistscraper.common.*;
import com.enrico200165.weblistscraper.configs.HostConfig;
import com.enrico200165.weblistscraper.page.PageProcDescr;
import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientProperties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.enrico200165.utils.net.http.Utils;


public class ClientWrapper {

    public ClientWrapper(Client clientPar, InvocationBuilderWrapper ibwPar,
                         HostConfig hostCfgPar, int pageLoadDelayPar, FormManagerABC fmPar) {
        super();
        pageLoadDelay = pageLoadDelayPar;
        hostCfg = hostCfgPar;
        setHost(hostCfg.getHost());
        this.ibw = ibwPar;
        client = clientPar;
        baseHostURI = hostCfg.getBaseHostURI();
        fm = fmPar;
        reqFilter = WEBUtils.getClReqFilterCookies();
    }


    String fullURL(String relURI) {
        String ret = null;
        URI uri;
        try {
            uri = new URI(relURI);
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        ret = fullURL(uri).toString();
        return ret;
    }

    URI fullURL(URI relURI) {
        URI ret = null;

        if (relURI.getAuthority() != null && relURI.getAuthority().length() > 0) {
            log.debug("input URI was not relative: " + relURI.toString());
            return relURI;
        }

        try {
            // tutto sto casino perchè URI non ha i metodi set...
            // URIBuilder dovrebbe patchare questo, vedere in futuro
            String scheme = relURI.getScheme() == null ? baseHostURI.getScheme() : relURI.getScheme();
            String userInfo = relURI.getUserInfo() != null ? baseHostURI.getUserInfo() : relURI.getUserInfo();
            String host = hostCfg.getHost() != null ? baseHostURI.getHost() : relURI.getHost();
            int port = relURI.getPort() != -1 ? baseHostURI.getPort() : relURI.getPort();

            String path = relURI.getPath() != null ? relURI.getPath() : "/";
            if (!path.startsWith("/")) path = "/" + path;

            String query = relURI.getQuery() != null ? relURI.getQuery() : null;
            String fragment = relURI.getFragment() != null ? relURI.getFragment() : null;

            ret = new URI(scheme, userInfo, host, port, path, query, fragment);

        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;
    }

    ClReqFilterCookies getReqFilter() {
        return this.reqFilter;
    }

    public ResponseWrapper simpleGET(String relUri) {
        return simpleGET(relUri, pageLoadDelay);
    }

    public ResponseWrapper simpleGET(String relUri, int delayPar) {
        URI uri;
        try {
            int delay = (delayPar > 0) ? delayPar : pageLoadDelay;
            uri = new URI(relUri);
            return simpleGET(uri, delay);
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }


    public ResponseWrapper simpleGET(URI uri, int delay) {
        log.info("loading page, delay: " + delay + " URL: " + Utils.URLFromURI(uri));

        if (!Utils.isUsableURI(uri)) {
            log.error("URI not usable: " + uri);
            System.exit(1);
        }
        ResponseWrapper rw = new ResponseWrapper();

        baseTarget = this.client.target(uri);
        Invocation.Builder ib = baseTarget.request();
        ibw.setBuilder(ib);
        rw.setReponse(ibw.it().get());
        rw.analyzeResponse(false, this);
        return rw;
    }

    boolean analyzeHiddenFormFields(String pageURI, List<NameValuePairString> hiddenFormFields, String formSelector) {

        hiddenFormFields.clear();
        ResponseWrapper rw = simpleGET(pageURI, 0);
        if (rw.it().getStatus() != 200) {
            log.error("some problems with form: " + rw.it().getStatus());
            System.exit(1);
        }
        WEBUtils.getFormFields(rw, hiddenFormFields, formSelector);
        return false;
    }

    public ResponseWrapper simplePOST(String uriString, List<NameValuePairString> formFields,
                                      InvocationBuilderWrapper ibw, int delaySecs) {
        URI uri = Utils.URIFromURLString(uriString,this.getHost());
        if (uri == null) {
            log.error("ESCO: failed to create URI from string: " + uriString);
            System.exit(1);
        }
        return simplePOST(uri, formFields, ibw, delaySecs);
    }

    public ResponseWrapper simplePOST(URI uri, List<NameValuePairString> formFields, InvocationBuilderWrapper overrideIbw, int delaySecs) {

        if (null == formFields || formFields.size() <= 0) {
            log.error("form fields not set, aborting");
            System.exit(-1);
        }
        if (uri.getScheme() == null || uri.getScheme().length() <= 0) {
            String path = uri.getPath();
            path = fullURL(path);
            try {
                uri = new URI(path);
            } catch (URISyntaxException e) {
                log.error("", e);
                System.exit(1);
            }
        }

        Form form = new Form();
        for (NameValuePairString nvp : formFields) {
            form.param(nvp.getKey(), nvp.getValue());
        }

        baseTarget = client.target(uri);
        Invocation.Builder frmReqBld = baseTarget.request();
        frmReqBld.property(ClientProperties.FOLLOW_REDIRECTS, false);
        ibw.setBuilder(frmReqBld);
        frmReqBld.accept(MediaType.TEXT_HTML_TYPE, MediaType.TEXT_PLAIN_TYPE);
        ResponseWrapper rw = new ResponseWrapper(frmReqBld.post(Entity.form(form)));
        rw.analyzeResponse(true, this);
        if (rw.it().getStatus() != 200)
            log.debug("after POST " + baseTarget.getUri() + " status: " + rw.dump());
        else
            log.debug("OK POST " + baseTarget.getUri() + " " + rw.dump());
        return rw;
    }

    public boolean authenticate(HostConfig hcfg) {
        List<NameValuePairString> formFields = new ArrayList<NameValuePairString>();

        final String loginPageURL = hcfg.getLoginPageURL();
        ResponseWrapper rw = simpleGET(loginPageURL, 0);
        if (rw.it().getStatus() != 200) {
            log.warn("some problem, maybe not blocking, getting page with login form: " + rw.it().getStatus());
            return false;
        }
        WEBUtils.getFormFields(rw, formFields, hcfg.getJsoupLoginFormSelector());
        List<NameValuePairString> replaceVals = new ArrayList<NameValuePairString>();

        fm.setFormFields(formFields, replaceVals, /* removeList */null, /* addList */null);
        WEBUtils.setFormParams(formFields, replaceVals /* replaceVals */,/* removeList */null, /* addMap */null);
        Form form = new Form();
        for (NameValuePairString nvp : formFields) {
            form.param(nvp.getKey(), nvp.getValue());
            // log.info("added field to form: " + nvp.getKey() + " = " +
            // nvp.getValue());
        }
        final String loginFormAction = hcfg.getLoginFormAction();
        rw = simplePOST(loginFormAction, formFields, ibw, 0);
        rw.analyzeResponse(true, this);
        int status = rw.it().getStatus();
        return ( status == 200 || status == 302);
        // return rw;
    }

    public ResponseWrapper getPage(HostConfig hcfg, PageProcDescr ppd, int delay, ArrayList<NameValuePairString> expected) {
        //String forDebugFullURL = fullURL(ppd.getRelUri());
        //log.debug("load page: " + delay + " URL: " + forDebugFullURL);
        if (ppd.isAuthenticate()) {
            if (!authenticate(hcfg)) {
                log.error("authentication failed");
                return null;
            }
        }

        ResponseWrapper rw = simpleGET(ppd.getUri(), delay);
        if (rw.it().getStatus() != 200) {
            // --- get fallito, proviamo ad autenticare ----
            log.debug("failed to get: " + baseTarget.getUri() + " forse problema autenticazione, stato: " + rw.it().getStatus());
            if (!authenticate(hcfg)) {
                return null;
            }
            // -- dopo azione di recupero riprovo a caricare
            rw = simpleGET(ppd.getUri(), delay);
            if (rw.it().getStatus() != 200) {
                log.error("page load fallito anche dopo autenticazione: " + rw.it().getStatus());
            }
        } else {
            rw.checkContent(expected);
        }
        return rw;
    }


    public HostConfig getHostCfg() {
        return hostCfg;
    }


    public void setHostCfg(HostConfig hostCfg) {
        this.hostCfg = hostCfg;
    }


    public URI getBaseHostURI() {
        return baseHostURI;
    }


    public void setBaseHostURI(URI baseHostURI) {
        this.baseHostURI = baseHostURI;
    }


    public WebTarget getBaseTarget() {
        return baseTarget;
    }


    public void setBaseTarget(WebTarget baseTarget) {
        this.baseTarget = baseTarget;
    }


    public InvocationBuilderWrapper getIbw() {
        return ibw;
    }


    public void setIbw(InvocationBuilderWrapper ibw) {
        this.ibw = ibw;
    }


    public int getPageLoadDelay() {
        return pageLoadDelay;
    }


    public void setPageLoadDelay(int pageLoadDelay) {
        this.pageLoadDelay = pageLoadDelay;
    }


    public FormManagerABC getFm() {
        return fm;
    }


    public void setFm(FormManagerABC fm) {
        this.fm = fm;
    }


    public static org.apache.log4j.Logger getLog() {
        return log;
    }


    public static void setLog(org.apache.log4j.Logger log) {
        ClientWrapper.log = log;
    }


    public String getHost() {
        return this.hostCfg.getHost();
    }


    public void setReqFilter(ClReqFilterCookies reqFilter) {
        this.reqFilter = reqFilter;
    }


    public boolean setHost(String url) {
        if (url == null) {
            log.error("provided url is NULL");
            System.exit(1);
            return true;
        }
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            log.error("unable to extract host from URL: " + url);
            System.exit(1);
        }

        return true;
    }

    public Client getClient() {
        return client;
    }


    public void setClient(Client clientPar) {
        if (this.client != null) {
            log.error("ESCO, il client non dovrebbe mai essere sostituito, usa questo metodo solo se client null");
            System.exit(1);
        }
        client = clientPar;
    }


    HostConfig hostCfg;
    URI baseHostURI;
    WebTarget baseTarget;
    InvocationBuilderWrapper ibw;
    // ResponseWrapper rw;
    Client client;
    ClReqFilterCookies reqFilter;
    int pageLoadDelay;
    FormManagerABC fm;

    private static org.apache.log4j.Logger log = Logger.getLogger(ClientWrapper.class);


}
