package com.enrico200165.web.italianpenpals;

import com.enrico200165.silverpop.xmlapi.XMLMessages;
import com.enrico200165.utils.net.SftpHelper;
import com.enrico200165.utils.net.http.Utils;
import com.enrico200165.utils.various.PropertiesEV;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainSilverpopItalianPP {

    public MainSilverpopItalianPP() {
    }

    public static void main(String[] args) {

        Utils.setFiddler(true);

        PropertiesEV p = new PropertiesEV("V:/data/pers_data/00_importantissimi/keys/programming/silverpop.properties");

        Silverpop sp = new Silverpop(p);

        ItalianPenPalsSite italianPenPalsSite = new ItalianPenPalsSite();

        (new MainSilverpopItalianPP()).perform(sp, italianPenPalsSite);

    }

    public void nascondi(Silverpop session, ItalianPenPalsSite italianPenPalsSite) {
        boolean downloadUsers = true;
        if (downloadUsers) {
            italianPenPalsSite.downloadUsers();
        }

        System.exit(0);
        // --- upload map file ---
        boolean uploadMapFile = true;
        if (uploadMapFile) {
            SftpHelper sftp = new SftpHelper();
            sftp.upload("ipp_map_file.xml", "upload/");
            SftpHelper.output(sftp.ls("upload/*"));
        }

        if (3 == 5) {
            // --- ottengo lista database
            String ris = session.APIRequest(XMLMessages.getLists(), false, false);
            Document doc = Jsoup.parse(ris);
            Elements lists = doc.select("LIST");
            for (Element l : lists) {
                String listID = l.select("id").get(0).text();
                log.info(listID);
                String list = session.APIRequest(XMLMessages.getListMetaData(listID), false, true);
                log.info(list);
            }
        }

    }

    boolean FTPUploadUsers(String fname) {
        SftpHelper sftp = new SftpHelper();
        sftp.upload("ipp_map_file.xml", "upload/");
        sftp.upload(fname, "upload/");
        SftpHelper.output(sftp.ls("upload/*"));
        return true;
    }

    boolean FTPDownload(String fname) {
        SftpHelper sftp = new SftpHelper();
        sftp.download("download/" + fname, fname);
        return true;
    }

    public void perform(Silverpop session, ItalianPenPalsSite italianPenPalsSite) {


        italianPenPalsSite.downloadUsers();

        // nascondi(session,italianPenPalsSite );

		/*session.getOauthToken();*/
        try {
            //			session.APIRequest(XMLMessages.getJobStatus("3331464"), false, true);
//			session.APIRequest(XMLMessages.RawRecipientDataExport(null), false, true);


            session.APIRequest(XMLMessages.WebTrackingDataExport(null), false, true);
            String fname = session.getRespElemValue("FILE_PATH");

            for (int i = 0; i < 30; i++) {
                log.info("waiting ... " + i);
                Thread.sleep(500);
            }
            log.info("### FTP ###");
            FTPDownload(fname);

            boolean uploadMapFile = false;
            if (uploadMapFile) {
                FTPUploadUsers(JUNKFNAME);
                String respXML = session.APIRequest(XMLMessages.ImportList(IPPID, MAP_FILE_NAME, JUNKFNAME), false, true);
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            session.logout();
        }
    }


    void testSQLITE() {
        {
            // register the driver
            String sDriverName = "org.sqlite.JDBC";

            // now we set up a set of fairly basic string variables to use in the body of the code proper
            String sTempDb = "hello.db";
            String sJdbc = "jdbc:sqlite";
            String sDbUrl = sJdbc + ":" + sTempDb;
            // which will produce a legitimate Url for SqlLite JDBC :
            // jdbc:sqlite:hello.db
            int iTimeout = 30;
            String sMakeTable = "CREATE TABLE dummy (id numeric, response text)";
            String sMakeInsert = "INSERT INTO dummy VALUES(1,'Hello from the database')";
            String sMakeSelect = "SELECT response from dummy";
            Connection conn = null;
            try {
                Class.forName(sDriverName);
                // create a database connection
                conn = DriverManager.getConnection(sDbUrl);
                Statement stmt = conn.createStatement();
                try {
                    stmt.setQueryTimeout(iTimeout);
                    stmt.executeUpdate(sMakeTable);
                    stmt.executeUpdate(sMakeInsert);
                    ResultSet rs = stmt.executeQuery(sMakeSelect);
                    try {
                        while (rs.next()) {
                            String sResult = rs.getString("response");
                            log.debug(sResult);
                        }
                    } finally {
                        try {
                            rs.close();
                        } catch (Exception ignore) {
                        }
                    }
                } finally {
                    try {
                        stmt.close();
                    } catch (Exception ignore) {
                    }
                }
            } catch (Exception e) {

            } finally {
                try {
                    conn.close();
                } catch (Exception ignore) {
                }
            }

        }

    }

    final static String IPPID = "49165";
    final static String MAP_FILE_NAME = "ipp_map_file.xml";
    String JUNKFNAME = "italianpenpals_users20160111_103103.tsv";

    private static org.apache.log4j.Logger log = Logger.getLogger(MainSilverpopItalianPP.class);

}
