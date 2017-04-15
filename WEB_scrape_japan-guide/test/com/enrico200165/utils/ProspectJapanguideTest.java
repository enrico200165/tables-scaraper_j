package com.enrico200165.utils;

import com.enrico200165.weblistscraper.configs.site_japanguide.ProspectJapanguide;
import com.enrico200165.weblistscraper.marketingDB.DBManagerMKT;
import com.enrico200165.weblistscraper.marketingDB.ProspectCoreData;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.List;

/**
 * Created by enrico on 02/03/2016.
 */
public class ProspectJapanguideTest {

    @Test
    public void testUpdateAndGetScore() throws Exception {

        DBManagerMKT ch = new DBManagerMKT("marketing");
        ProspectJapanguide updater = new ProspectJapanguide(null);

        List<ProspectCoreData> prospectsCoreData = ch.selectProspects("japanguide");

        for (ProspectCoreData p : prospectsCoreData) {
            updater.setCoreDataObject(p);
            updater.updateAndGetScore();
            log.info(updater.dump());
        }
    }


    private static Logger log = Logger.getLogger(ProspectJapanguideTest.class);

}