package com.enrico200165.weblistscraper.page;

import com.enrico200165.utils.str_regex.NameValuePairString;
import com.enrico200165.weblistscraper.common.WEBUtils;
import com.enrico200165.weblistscraper.configs.HostConfig;
import com.enrico200165.weblistscraper.configs.PageConfigABC;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import com.enrico200165.weblistscraper.tools.*;

import javax.ws.rs.core.Form;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
public class PageProcessor extends PageProcessorAbstr {

    public PageProcessor(HostConfig hostCOnfigPar, SessionManagerAbstr sess, PageConfigABC tcfgPar) {
        super(hostCOnfigPar, sess);
        this.cw = sess.getClientWrapper();
        tcfg = tcfgPar;
        if (tcfgPar == null)
            log.log( Level.FINE, "per breakpoint");
    }


    @Override
    public void initTableScraperSpecific(TableScraperABC ts) {
        log.log(Level.SEVERE, "should not pass here or make class abstract");
    }

    @Override
    public PageProcessResult process(PageProcDescr pageProcDescr) {
        PageProcessResult res = new PageProcessResult();
        ResponseWrapper rw = null;
        res.result.setRc(Result.RC.OK);

        // trasferiamo nel client il delay, per uso più tardi dal client request filter
        cw.getClient().property(WEBUtils.callDelayProperty, new Integer(pageProcDescr.getHTTPCallDelay()));

        // log.info("Page: " +  hostConfig.getBaseHostURI()+pageProcDescr.getRelUri());
        switch (pageProcDescr.action) {
            case LOGIN: {
                if (cw.authenticate(hostConfig)) {
                } else {
                    log.log(Level.SEVERE, "could not authenticate");
                }
                break;
            }
            case GET: {
                rw = cw.getPage(hostConfig, pageProcDescr, 0, null);
                break;
            }
            case GET_SCRAPE: {
                rw = cw.getPage(hostConfig, pageProcDescr, 0, null);
                if (rw != null) {
                    scrapeFromRespWrapper(pageProcDescr, res, rw);
                } else {
                    // should only happen when authentication fail. We should exit as cannot manage that
                    res.result.setContinua(false).setRc(Result.RC.MUST_EXIT);
                }
                break;
            }

            case POST: {
                // load the page enclosing the form
                post(pageProcDescr);
                break;
            }
            case POST_SCRAPE: {
                rw = post(pageProcDescr);
                if (rw != null) {

                    rw.analyzeResponse(true, cw);
                    if (!(rw.it().getStatus() == 200 || rw.it().getStatus() == 302)) {
                        log.log(Level.SEVERE, "GESTIRE QUESTO ERRORE");
                    }
                    scrapeFromRespWrapper(pageProcDescr, res, rw);
                } else {
                    log.log(Level.SEVERE, "GESTIRE QUESTO ERRORE");
                }
                break;
            }

            default: {
                log.log(Level.SEVERE, "operazione non gestita attualmente (POST?) esco");
                System.exit(1);
                break;
            }
        }
        // here we shoud process contents, list of users
        if (!res.result.getContinua()) {
            return res;
        }
        if (!res.result.isOk()) {
            log.log(Level.SEVERE, "result is not OK:\n" + res);
            return res;
        }

        return res;
    }

    public ResponseWrapper post(PageProcDescr pageProcDescr) {
        ResponseWrapper rw;
        rw = cw.simpleGET(pageProcDescr.getUri(), 0);
        if (rw.it().getStatus() != 200) {
            log.log(Level.WARNING,  "some problem, getting page with form: " + rw.it().getStatus());
            return null;
        }

        // --- estrai campi (nascosti, e valori)dall form
        List<NameValuePairString> formFields = pageProcDescr.getFormFields();
        if (pageProcDescr.getFormSelector() != null && pageProcDescr.getFormSelector().length() > 0) {
            // aggiunge eventuali campi nascosti e relativi valori esaminando la form ?
            WEBUtils.getFormFields(rw, formFields, pageProcDescr.getFormSelector());
        }

        FormManagerABC fm = pageProcDescr.getFormManager();
        if (fm != null) {   // eventuali manipolazioni di campi (aggiunte, sostituzioni valori Etc
            List<NameValuePairString> replaceVals = new ArrayList<NameValuePairString>();
            fm.setFormFields(formFields, replaceVals, /* removeList */null, /* addList */null);
        }
        WEBUtils.setFormParams(formFields, null /* replaceVals */,/* removeList */null, /* addMap */null);
        Form form = new Form();
        for (NameValuePairString nvp : formFields) {
            form.param(nvp.getKey(), nvp.getValue());
            // log.info("added field to form: " + nvp.getKey() + " = " +
            // nvp.getValue());
        }
        log.log(Level.SEVERE, "#################### post sembra fare login, perchè? ###############");
        //final String loginFormAction = pageProcDescr.getLoginFormAction();
        final String loginFormAction = pageProcDescr.getURL();
        rw = cw.simplePOST(loginFormAction, formFields, ibw, 0);
        rw.analyzeResponse(true, cw);
        if (!(rw.it().getStatus() == 200 || rw.it().getStatus() == 302)) {
            log.log(Level.SEVERE, "");
        }
        return rw;
    }

    public void scrapeFromRespWrapper(PageProcDescr pageProcDescr, PageProcessResult res, ResponseWrapper rw) {
        TableScraperABC ts;
        if (pageProcDescr.getTableScraper() == null) {
            ts = tcfg.getTableScraper();
        } else {
            ts = pageProcDescr.getTableScraper();
        }

        // inizializzato in due pass per problemi
        initTableScraperCommon(sesMgr, ts);

        String url = pageProcDescr.getURL();
        res.result = ts.scrape(pageProcDescr, rw, url);

        // here we look for a "next" link
        String nextPage = rw.getLink(tcfg.getNextTablePageSelectors());
        if (nextPage != null) {
            // fill with the new pageProcessDescr
            res.pPDescr = new PageProcDescr(hostConfig, null, this, nextPage, WebPageAction.GET_SCRAPE);
        } else {
            ts.finalizeTableScraping();
        }
    }

    ClientWrapper cw;
    PageConfigABC tcfg;
    InvocationBuilderWrapper ibw;
    static Logger log=Logger.getLogger(PageProcessor.class.getSimpleName());

}
