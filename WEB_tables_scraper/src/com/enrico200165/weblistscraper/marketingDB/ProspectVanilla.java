package com.enrico200165.weblistscraper.marketingDB;

/**
 * Created by enrico on 3/1/2016.
 */
public  class ProspectVanilla extends ProspectABC {

    public ProspectVanilla(ProspectCoreData prospectCoreData) {
        super(prospectCoreData);
    }



    public long updateAndGetScore() {
        return prospectCoreData.getScoreActOnThis();
    }

    public String dump() {
        return prospectCoreData.dump();
    }

}
