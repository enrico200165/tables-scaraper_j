package com.enrico200165.weblistscraper.tools;


import com.enrico200165.weblistscraper.marketingDB.ProspectCoreData;
import org.jsoup.nodes.Element;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class EntryHTMLIncludeFilter extends FilterBase {

    public EntryHTMLIncludeFilter() {
    }

    public boolean includeHTMLEntry(Element entry) {

        boolean ret = false;
        log.log(Level.SEVERE, "not implemented");
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
        log.log(Level.SEVERE, "not implemented");
        if (ret) {
            incrTrueProspect();
        } else {
            incrFalseProspect();
        }
        return ret;
    }

    private static Logger log = LogManager.getLogManager().getLogger(EntryHTMLIncludeFilter.class.getSimpleName());
}
