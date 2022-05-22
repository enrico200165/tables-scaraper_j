package com.enrico200165.cookies;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;


public class HTTPCookieJPATest {

    public HTTPCookieJPATest() {
        super();
    }


    static List<HttpCookieJPA> generateCookies(String name, int nrCookies, int nrDomains, int nrPaths) {

        return generateCookies(name, nrCookies, "domain", true, true, nrDomains, "/path1", true, true, nrPaths);
    }


    static List<HttpCookieJPA> generateCookies(String name, int nrCookies,
                                               String domain, boolean changeDomain, boolean growDomain, int nrDomains,
                                               String path, boolean changePath, boolean growPath, int nrPaths) {

        ArrayList<HttpCookieJPA> cookies = new ArrayList<HttpCookieJPA>();

        for (int nr = 0; nr < nrCookies; nr++) {
            for (int dom = 0; dom < nrDomains; dom++) {
                for (int paths = 0; paths < nrPaths; paths++) {
                    String curDomain = domain + nrDomains;
                    String curPath = path + nrPaths;
                    String curName = name + nr;
                    String finalName = curName + nr + "_" + domain + dom + "_" + path + paths;
                    String curVal = "val_" + finalName;


                    {
                        HttpCookie coreCookie = new HttpCookie(finalName, curVal);
                        coreCookie.setDomain(curDomain);
                        coreCookie.setPath(curPath);
                        coreCookie.setMaxAge(3600 * 3);
                        HttpCookieJPA cookie = new HttpCookieJPA(coreCookie);
                        cookies.add(cookie);
                    }
                }
            }
        }
        return cookies;
    }


    List<HttpCookieJPA> generateCookies2() {

        List<HttpCookieJPA> cookies = new ArrayList<HttpCookieJPA>();

        domains = new ArrayList<String>();

        domains.add("google.com");
        domains.add("google.it");
        domains.add("www.google.com");
        domains.add("");
        domains.add("com");

        this.paths = new ArrayList<String>();
        paths.add("/");
        paths.add("/user/register");
        paths.add("/user/view");

        dates = new ArrayList<Date>();
        long currentDate = (new Date()).getTime();
        log.info("check date: " + new

                Date(currentDate)

        );

        int nr = 0;
        this.cookies = new ArrayList<HttpCookieJPA>();
        for (
                String domain
                : domains)

        {
            for (String path : paths) {
                for (int i = 0; i < 10; i++) {
                    long seconds = 3600 * (i - 5);
                    {
                        HttpCookie coreCookie = new HttpCookie("cookie_" + nr, "val_" + nr);
                        coreCookie.setDomain(domain);
                        coreCookie.setPath(path);
                        coreCookie.setMaxAge(3600 * 3);
                        HttpCookieJPA cookie = new HttpCookieJPA(coreCookie);
                        cookies.add(cookie);
                    }
                    nr++;
                }
            }
        }
        return cookies;
    }


    @Test
    public void testIdentical() {

        HttpCookieJPA c1 = generateCookies("cookie", 1, 1, 1).get(0);
        HttpCookieJPA c2 = generateCookies("cookie", 1, 1, 1).get(0);
        assertTrue(c1.isIdentical(c2));

        c1.setMaxage(1l);
        assertFalse(c1.isIdentical(c2));

        long secs = c1.getMaxage();
        c1.setMaxage((new Date()).getTime());
        long secs2 = c1.getMaxage();
        log.info("secs prima e dopo: " + secs + " " + secs2);
    }


    @Test
    public void test() {

        List<HttpCookieJPA> cookies = generateCookies("name", 3, 3, 3);

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("cookiesGlobalStore");

        EntityManager em = factory.createEntityManager();

        for (HttpCookieJPA c : cookies) {
            EntityTransaction tx = em.getTransaction();
            if (!tx.isActive()) tx.begin();
            em.persist(c);
            tx.commit();
            /*
            em.detach(c);
            log.debug("memorizzato:\n" + c.toString());
            */
        }
        em.close();

    }


    @SuppressWarnings("deprecation")
    @Test
    public void testExpiryDate() {

        List<HttpCookieJPA> cookies = generateCookies("name", 2, 1, 1);

        HttpCookieJPA c1 = cookies.get(0);
        HttpCookieJPA c2 = cookies.get(1);

        Date testDate = new java.util.Date();

        // stessa data attraverso i due metodi/campi
        c1.setMaxage(testDate.getTime() / 1000);
        c2.setExpiryDate(testDate);
        assertEquals(c1.getMaxage(), c2.getMaxage());
        log.info(c1.getExpiryDate() + " " + c2.getExpiryDate());
        log.info("\n" + c1.getExpiryDate().getTime() + " \n" + c2.getExpiryDate().getTime());
        long diff = c1.getExpiryDate().getTime() - c2.getExpiryDate().getTime();
        Assert.assertTrue(diff <= 999 && diff >= -999);
    }

    public static void main(String[] s) {
        HTTPCookieJPATest tester = new HTTPCookieJPATest();
        tester.testExpiryDate();
        tester.test();
    }

    ArrayList<String> domains;
    ArrayList<String> paths;
    ArrayList<Date> dates;

    ArrayList<HttpCookieJPA> cookies;

    private static Logger log = LogManager.getLogger(HTTPCookieJPATest.class.getSimpleName());
}
