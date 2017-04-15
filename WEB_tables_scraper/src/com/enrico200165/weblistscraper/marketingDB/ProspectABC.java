package com.enrico200165.weblistscraper.marketingDB;

/**
 * ASSUNZIONE: data are the same for all Prospect, only behaviour changes
 * - derived classes should NOT add data
 * - to keep this viaual and not bloat code do NOT create wrapper functions, use it()
 * Created by enrico on 3/1/2016.
 */
public abstract class ProspectABC {

    public ProspectABC(ProspectCoreData prospectCoreData) {
        super();
        this.prospectCoreData = prospectCoreData;
    }


    //public long getID() {
//        return prospectCoreData.getInternalID();
  //  }

    public void setCoreDataObject(ProspectCoreData coreData) {
        this.prospectCoreData = coreData;
    }

    public ProspectCoreData it() {
        return this.prospectCoreData;
    }

    abstract public long updateAndGetScore();

    public String dump() {
        return prospectCoreData.dump();
    }

    protected ProspectCoreData prospectCoreData;
}
