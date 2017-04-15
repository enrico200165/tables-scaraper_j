package com.enrico200165.weblistscraper.configs.site_italianpenpals;

import com.enrico200165.weblistscraper.marketingDB.*;

public class ProspectItalianPenPals extends ProspectABC {


    public ProspectItalianPenPals(ProspectCoreData prospectCoreData) {
        super(prospectCoreData);
    }

    @Override
    public long updateAndGetScore() {
        return 0;
    }
}
