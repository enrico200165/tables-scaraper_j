package com.enrico200165.weblistscraper.marketingDB;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by enrico on 3/1/2016.
 */
public class DBManagerMKTTest {

    static String HOME = "japanguide";
    static String PERSIST_UNIT = "html";


    @Before
    public void setUp() throws Exception {
        ch = new DBManagerMKT(PERSIST_UNIT);

    }


    public List<ProspectCoreData> generateProspCoreData(int n, String homeID) {
        List<ProspectCoreData> prospects = new ArrayList<ProspectCoreData>();


        for (int i = 0; i < n; i++) {
            ProspectCoreData p = new ProspectCoreData();
            // NON SETTARE INTERNAL id E' GESTITO DA JPA, SE SETTATO CONFLITTI
            //  p.setInternalID(i);
            p.setUserID("webuserid_" + i);
            p.setHomeID(homeID);
            prospects.add(p);
        }
        return prospects;
    }

    /**
     * Must be stored to set the internalID
     *
     * @param n
     * @return
     */
    public List<ProspectABC> generateProspectsABC(int n) {
        List<ProspectABC> prospects = new ArrayList<ProspectABC>();

        List<ProspectCoreData> cds = generateProspCoreData(n, HOME);
        ch.storeProspCoreData(cds, new Date(), false, true);

        for (ProspectCoreData pd : cds) {
            prospects.add(new ProspectVanilla(pd));
        }
        // ch.dumpProsp(prospects);
        return prospects;
    }


    void dumpPABC(List<ProspectABC> prospects) {
        for (ProspectABC p : prospects) {
            log.info(p.dump());
        }
    }


    @Test
    public void testSelectProspectHomes() throws Exception {

        List<String> homes = ch.selectProspectHomes();
        for (String s : homes) {
            System.out.println(s);
        }

    }

    @Test
    public void testStoreProspects() throws Exception {

        int n = 100;

        List<ProspectCoreData> prospects = generateProspCoreData(n, HOME);

        int nrStored = ch.storeProspCoreData(prospects, null, false, true);
        log.info("stored nr prospects: " + nrStored);

        prospects = ch.selectProspects(HOME);
        ch.dump(prospects);
    }


    @Test
    public void testGetNrOfRows() {
        Long nr = 0l;

        int n = 8;

        List<ProspectCoreData> prospects = generateProspCoreData(n, HOME);

        int nrStored = ch.storeProspCoreData(prospects, null, false, true);
        log.info("stored : " + nrStored + " returned: " + ch.getNrofProspForHome(HOME));
    }


    @Test
    public void testSelectProspects() throws Exception {
        int n = 100;

        int stored = ch.storeProspCoreData(generateProspCoreData(n, HOME), null, false, true);

        List<ProspectCoreData> prospects = ch.selectProspects(PERSIST_UNIT);
        for (ProspectCoreData p : prospects) {
            System.out.println(p.getUserID());
        }

    }


    @Test
    public void testSelectProspectsMult() throws Exception {

        List<ProspectCoreData> prospects = ch.selectProspects(HOME);
        for (ProspectCoreData p : prospects) {
            System.out.println(p.dump());
        }
    }


    @Test
    public void testFindProspectByInternalID() throws Exception {

    }

    @Test
    public void testStoreProspect() throws Exception {

    }


    //====================================================================================


    Contact genContact(ProspectABC p) {
        Contact contact = new Contact(p, p.it().getChannelType(),"testChannelVendor",
                p.it().getHomeID(), "testPOST", "html contact message");
        return contact;
    }


    List<Contact> genContsForProsp(ProspectABC prospect, int nr) {
        ArrayList<Contact> contacts = new ArrayList<Contact>();

        for (int i = 0; i < nr; i++) {
            Contact c = genContact(prospect);
            contacts.add(c);
        }
        // dumpContacts(contacts);
        return contacts;
    }


    List<Contact> genProspContacts(int nrProspects, int nrContsForProsp, boolean store) {

        List<ProspectABC> prospects = generateProspectsABC(nrProspects);
        ArrayList<Contact> contacts = new ArrayList<Contact>();

        for (ProspectABC p : prospects) {
            contacts.addAll(genContsForProsp(p, nrContsForProsp));
        }
        //ch.dumpProsp(prospects);
        // log.info("Prospects in DB: "+ch.getNrofProspForHome(HOME));
        // dumpContacts(contacts);

        if (store) {
            // gia fatto prima ch.storeProspects(prospects, new Date(),false,true);
            for (Contact c : contacts) {
                ch.storeContact(c, false, true);
            }
        }

        return contacts;
    }


    @Test
    public void testStoreContact() throws Exception {

        List<ProspectABC> prospects = generateProspectsABC(10);
        ch.dumpProsp(prospects);
        List<Contact> contacts = new ArrayList<Contact>();

        for (ProspectABC p : prospects) {
            List<Contact> curProspContacts = genContsForProsp(p, 5);
            contacts.addAll(curProspContacts);
        }
        ch.dumpContacts(contacts);

    }


    @Test
    public void testGetNrofContInDates() {
        int nrContacts = 5;
        int contPerProspect = 5;
        List<Contact> contacts = genProspContacts(nrContacts, contPerProspect, true);

        // ch.dumpContacts(contacts);

        int nr = ch.getNrofContInDates(HOME, new Date(), new Date());
        log.info("contacts in date range: " + nr);

    }


    @Test
    public void testSelectContacts() {

        int nrContacts = 5;
        int contPerProspect = 5;
        genProspContacts(nrContacts, contPerProspect, true);

        List<Contact> contacts =  ch.selectContacts(HOME+"FALLOFALLIRE",null,null);
        ch.dumpContacts(contacts);
    }


    DBManagerMKT ch;

    private static Logger log = LogManager.getLogger(DBManagerMKTTest.class.getSimpleName());
}