package com.enrico200165.weblistscraper.concorsi_it.entities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by enrico on 11/02/2016.
 */
public class ConcorsiDBTest {

    @Before
    public void setUp() throws Exception {
        concDB = new ConcorsiDB();
    }

    @After
    public void tearDown() throws Exception {
        concDB.enricoFlush();
    }

    @Test
    public void testFindConcorNotReported() throws Exception {

    }

    @Test
    public void testFindConcorByInternalID() throws Exception {

    }

    @Test
    public void testFindConcorByInternalID1() throws Exception {

    }

    @Test
    public void testStoreConcor() throws Exception {

        Concor con = new Concor();
        con.setInfoInternURL("pippo");
        con.setInternalID("dummyInternalIDValue");
        concDB.storeConcor(con,"internalID",false,true);
        assertEquals((long)concDB.getNrRows(),1);
    }

    @Test
    public void testDetachCollection() throws Exception {

    }

    @Test
    public void testUpdateReportedDate() throws Exception {

    }

    @Test
    public void testUpdateReportedDate1() throws Exception {

    }

    @Test
    public void testStoreConcor1() throws Exception {

    }

    ConcorsiDB concDB;
}