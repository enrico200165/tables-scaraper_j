package com.enrico200165.weblistscraper.tools;


import org.jsoup.nodes.Element;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.enrico200165.weblistscraper.marketingDB.ProspectCoreData;


public class EntryHTMLIncludeFilter extends FilterBase {

    public EntryHTMLIncludeFilter() {
    }

    public boolean includeHTMLEntry(Element entry) {

        boolean ret = false;
        log.error("not implemented");
        if (ret) {
            incrTrueEntry();
        } else {
            incrFalseEntry();
        }
        return ret;
    }

    public boolean includeProspect(ProspectCoreData prospectCastIt) {
        boolean ret;

        ret = false;
        log.error("not implemented");
        if (ret) {
            incrTrueProspect();
        } else {
            incrFalseProspect();
        }
        return ret;
    }

    private static Logger log = LogManager.getLogger(EntryHTMLIncludeFilter.class.getName());
}
