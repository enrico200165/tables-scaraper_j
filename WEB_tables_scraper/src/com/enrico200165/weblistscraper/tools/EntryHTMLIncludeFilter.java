package com.enrico200165.weblistscraper.tools;


import com.enrico200165.weblistscraper.marketingDB.ProspectCoreData;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;

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

    private static Logger log = Logger.getLogger(EntryHTMLIncludeFilter.class);
}
