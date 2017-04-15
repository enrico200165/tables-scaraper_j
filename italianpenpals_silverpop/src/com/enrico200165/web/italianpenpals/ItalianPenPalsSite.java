package com.enrico200165.web.italianpenpals;

import com.enrico200165.cookies.CookieStoreEV;
import com.enrico200165.weblistscraper.common.*;
import com.enrico200165.weblistscraper.configs.drupal7.FormManagerLoginDrupal7;
import com.enrico200165.weblistscraper.configs.site_italianpenpals.HostConfigItalianPenPals;
import com.enrico200165.weblistscraper.tools.*;
import org.apache.log4j.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.Response;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by enrico on 10/02/2016.
 */
public class ItalianPenPalsSite {

    public ItalianPenPalsSite() {

        hostConfigIPP = new HostConfigItalianPenPals("V:\\data\\pers_dev\\data_dbs\\italianpenpals_login_enrico.properties");

         CookieStoreEV cs = new CookieStoreEV();
        ClientRequestFilter crqf = new ClReqFilterCookies(cs);
        ClientRespFilterEV cref = new ClientRespFilterEV(cs);
        Client cl = WEBUtils.createClient(crqf, cref, true);
        final FormManagerLoginDrupal7 fmPar = new FormManagerLoginDrupal7(hostConfigIPP);
        cw = new ClientWrapper(cl, new InvocationBuilderWrapperIExplore(), hostConfigIPP,1, fmPar);
    }

    static public String writeWebResponseToFile(String defaultFullPath, Response response) {

        BufferedWriter writer = null;
        try {
            //create a temporary file
            String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

            if (defaultFullPath == null || defaultFullPath.length() <= 0)
                defaultFullPath = "italianpenpals_users" + timeLog + ".tsv";
            File logFile = new File(defaultFullPath);

            // This will output the full path where the file will be written to...
            log.debug(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write(response.readEntity(String.class));
            writer.close();
            return defaultFullPath;
        } catch (Exception e) {
            log.error("errore", e);
            return null;
        }
    }


    public boolean downloadUsers() {
        if (cw.authenticate(hostConfigIPP)) {
            rw = cw.simpleGET("http://italianpenpals.org/users_export");
            drupalUsersFName = writeWebResponseToFile(null, rw.it());
            if (drupalUsersFName == null) {
                log.error("download or write to file of list of users failed");
                return false;
            } else {
                log.info("Lisa utenti IPP in: "+drupalUsersFName);
                return true;
            }
        } else {
            log.error("Drupal Authentication failed");
            return false;
        }
    }


    HostConfigItalianPenPals hostConfigIPP;
    String drupalUsersFName;
    final static String listURL = "http://italianpenpals.org/users_export";

    ClientWrapper cw;
    ResponseWrapper rw;

    private static org.apache.log4j.Logger log = Logger.getLogger(ItalianPenPalsSite.class);
}
