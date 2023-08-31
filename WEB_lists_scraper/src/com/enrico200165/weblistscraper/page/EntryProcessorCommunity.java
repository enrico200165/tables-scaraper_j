package com.enrico200165.weblistscraper.page;

import com.enrico200165.weblistscraper.common.ContactType;
import com.enrico200165.weblistscraper.configs.PageConfigABC;
import com.enrico200165.weblistscraper.marketingDB.Contact;
import com.enrico200165.weblistscraper.marketingDB.DBManagerMKT;
import com.enrico200165.weblistscraper.marketingDB.ProspectCoreData;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import com.enrico200165.weblistscraper.tools.ClientWrapper;
import com.enrico200165.weblistscraper.tools.Result;
import com.enrico200165.weblistscraper.tools.Result.RC;
import org.jsoup.nodes.Element;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public abstract class EntryProcessorCommunity extends EntryProcessorABC {

    public EntryProcessorCommunity(SessionManagerAbstr smPar, PageConfigABC tCfgPar) {
        super(smPar, tCfgPar);

        if (tCfgPar == null)
            log.info("tCfgPar == null");

        log.log(Level.WARNING,  "maybe cw should be passed as parameter? don't know");
        this.cw = smPar.getClientWrapper();
        storeProspect = false;
        contactType = ContactType.SIMULATED;
    }

    abstract protected Result FillEntry(Element entry, ProspectCoreData prospect);

    abstract protected boolean fillContact(Contact contact, ProspectCoreData prospect);

    abstract protected boolean performContact(Contact contact, ProspectCoreData prospect, String contactParam);


    public void setDBManagerMKT(DBManagerMKT ch) {
        this.DBManagerMKT = ch;
    }


    /* Process a JSoup entry, typically it will be a table/list entry */
    @Override
    public Result processWebEntry(Element entryRawHTML, String formURL, Object otherParam, boolean excludeIt) {

        Result res = new Result();
        ProspectCoreData entryW = new ProspectCoreData();

        // --- su HTML entity
        FillEntry(entryRawHTML, entryW);
        // "exclude" filter
        if (pageConfig.getEntryExcludeFilter().exclude(entryW)) {
            log.info("filter excluded prospect: " + entryW.IDsToString());
            return res;
        }
        // "include" filter
        if (!pageConfig.getEntryIncludeFilter().includeProspect(entryW)) {
            log.info("\"include\" filter did not include prospect: " + entryW.IDsToString());
            return res;
        }

        // --- store prospect
        if (isStoreProspect() && !entryW.fromDB() && smgr.getSessionLimits().canSaveProspect(true)) {
            entryW.store(DBManagerMKT, /* skip if present */  new Date(), true, true);
            // the long prospect ID is set by the automatic synch, deactivated just after
        } else {
            log.log(Level.WARNING,  "cannot save prospect " + " max exceeded: " + smgr.getSessionLimits().getSaves() + "/"
                    + smgr.getSessionLimits().getMaxNewProspectsSaves() + " no actions seem possible, EXITING, prospect=\n"
                    + entryW.IDsToString());
            return res.setContinua(false).setRc(RC.MUST_EXIT).setErrorMessage("max store contacts exceeded");
        }

        // ########## contatto il cliente ##########
        if (!smgr.getSessionLimits().canContactEntry()) {
            log.log( Level.FINE, "session limits says cannot contact prospect: " + entryW.toString());
            return res.setRc(RC.ACTION_NOT_ALLOWED).setErrorMessage("session limits says cannot contact prospect: " + entryW.toString());
        }

        if (getContactType() != ContactType.NONE) {
            // --- riempiamo l'entry del contatto
            Contact contact = new Contact();

            fillContact(contact, entryW);
            contact.setContactType(getContactType());
            // --- contact, if allowed
            if (!pageConfig.getContactProspectFilter().canAct(entryW, contact)) {
                log.info("filter says cannot contact prospect: " + entryW.toString());
                return res.setRc(RC.ACTION_NOT_ALLOWED).setErrorMessage("filter says cannot contact prospect: " + entryW.toString());
            }
            if (getContactType() == ContactType.CONTACTED) {
                if (performContact(contact, entryW, "non usato")) {
                    smgr.getSessionLimits().contactNrIncr();
                }
                DBManagerMKT.storeContact(contact, /* skip if present */true, /* commit now */true);
            }
        }
        return res;
    }


    public boolean isStoreProspect() {
        return storeProspect;
    }

    public void setStoreProspect(boolean storeProspect) {
        this.storeProspect = storeProspect;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType c) {
        this.contactType = c;
    }

    protected boolean storeProspect;
    protected ContactType contactType;

    protected DBManagerMKT DBManagerMKT;
    protected ClientWrapper cw;


    static Logger log=Logger.getLogger(EntryProcessorCommunity.class.getSimpleName());
}
