package com.enrico200165.weblistscraper.session;

import java.util.ArrayList;

import com.enrico200165.weblistscraper.configs.PageConfigABC;
import com.enrico200165.weblistscraper.tools.ClientWrapper;
import org.apache.log4j.Logger;

import com.enrico200165.weblistscraper.configs.SessionLimitsBase;
import com.enrico200165.weblistscraper.page.PageProcDescr;

public abstract class SessionManagerAbstr {

    public SessionManagerAbstr(SessionLimitsBase sessLimPar, ClientWrapper cwPar) {
        super();
        sessLim = sessLimPar;
        setClientWrapper(cwPar);
        pageProcDescrs = new ArrayList<PageProcDescr>();
    }


    public SessionLimitsBase getSessionLimits() {
        return sessLim;
    }


    public void setSessLim(SessionLimitsBase sessLim) {
        this.sessLim = sessLim;
    }


    // --- inizialize session-type specific resources ---
    abstract public void initSession();

    // --- inizialize session-type specific resources ---
    abstract public void finalizeSession();

    /**
     * @return
     */
    public boolean performSession(SessionLimitsBase sLimits) {

        try {
            initSession();
            performSessionSpecific(sLimits);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finalizeSession();
        }
        printStatistics(null);
        return true;
    }


    public ClientWrapper getClientWrapper() {
        if (cw == null) {
            log.error("client wrapper è nullo");
        }
        return SessionManagerAbstr.cw;
    }


    public void printStatistics(PageConfigABC tableOrPageConfig) {

        // non più valido perchè una sessione può usare più di un PageConfigABC
        log.error("spostare statistiche in session, causando errore di compilazione riattivando codice sotto e rimuovendo il metodo di tage");

		/*

		String s = "\n";
		s += "exclude filter\n";
		s += "true-Entry=" + tableOrPageConfig.getEntryExcludeFilter().nrTrueEntry();
		s += " false-Entry=" + tableOrPageConfig.getEntryExcludeFilter().nrFalseEntry();
		s += "\ntrue-ProspectCoreData=" + tableOrPageConfig.getEntryExcludeFilter().nrTrueProspect();
		s += " false-ProspectCoreData=" + tableOrPageConfig.getEntryExcludeFilter().nrFalseProspect();

		s += "\n\n";
		s += "include filter";
		s += "\ntrue-Entry=" + tableOrPageConfig.getEntryIncludeFilter().nrTrueEntry();
		s += " false-Entry=" + tableOrPageConfig.getEntryIncludeFilter().nrFalseEntry();
		s += "\ntrue-ProspectCoreData=" + tableOrPageConfig.getEntryIncludeFilter().nrTrueProspect();
		s += " false-ProspectCoreData=" + tableOrPageConfig.getEntryIncludeFilter().nrFalseProspect();
		log.info("filter statistics\n" + s);

		// Entry is "excluded" first, so only Entries not excluded go to ProspectCoreData exclude fileter
		log.info("this should be 0, is: "
				+ (tableOrPageConfig.getEntryExcludeFilter().nrFalseEntry() - tableOrPageConfig.getEntryExcludeFilter().nrTrueProspect() - 
						tableOrPageConfig.getEntryExcludeFilter().nrFalseProspect()));

		log.info("this should be 0, is " + (tableOrPageConfig.getEntryExcludeFilter().nrFalseProspect() // prospects che "sopravvivono all'esclude 
				- tableOrPageConfig.getEntryIncludeFilter().nrTrueProspect() - tableOrPageConfig.getEntryIncludeFilter().nrFalseProspect()));

*/
    }


    abstract boolean performSessionSpecific(SessionLimitsBase sLimits);


    static ClientWrapper cw = null;


    public boolean setClientWrapper(ClientWrapper par) {
        if (cw != null) {
            log.error("client wrapper è già impostato, IGNORO");
//			System.exit(1);
            return false;
        }
        cw = par;
        return true;
    }


    public SessionManagerAbstr add(PageProcDescr ppd) {
        this.pageProcDescrs.add(ppd);
        return this;
    }

    public int getEntriesEsaminate() {
        return entriesEsaminate;
    }

    public void incrEntriesEsaminate() {
        this.entriesEsaminate++;
    }

    public int getRawEntriesEsaminate() {
        return rawEntriesEsaminate;
    }

    public void incrRawEntriesEsaminate() {
        this.rawEntriesEsaminate++;
    }

    int entriesEsaminate;
    int rawEntriesEsaminate;

    SessionLimitsBase sessLim;

    ArrayList<PageProcDescr> pageProcDescrs;

    private static org.apache.log4j.Logger log = Logger.getLogger(SessionManagerAbstr.class);
}
