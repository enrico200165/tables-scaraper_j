package com.enrico200165.cookies;


import com.enrico200165.weblistscraper.common.WEBUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class CookieStoreEV implements java.net.CookieStore {

    public CookieStoreEV(String persistUnit) {
        if (CookieStoreEV.em == null) {
            log.info("creo sqlite DB a " + persistUnit);
            EntityManagerFactory factory = Persistence.createEntityManagerFactory(persistUnit);
            em = factory.createEntityManager();
            log.info(persistUnit + " persistence unit, Entity manager created");
        } else {
            log.warn(persistUnit+" persistence unit: attempt to create entity manager already created");
        }
    }

    public CookieStoreEV() {
        this("cookiesGlobalStore");
    }

    public int addMap(Map<String, NewCookie> newCookies, URI uri) {

        if (newCookies == null || newCookies.size() <= 0) {
            log.warn("non saremmo dovuti entrare in questa funzione");
            return 0;
        }

        int nrCookiesAdded = 0;
        for (NewCookie cookie : newCookies.values()) {
            if (cookie.getName().length() > 0) {
                add(uri, cookie);
                nrCookiesAdded++;
                log.debug("NEW cookie: " + WEBUtils.cookieToString(cookie, " "));
            } else
                log.warn("\n" + "errore interno su NEW cookie: " + WEBUtils.cookieToString(cookie, " "));
        }
        return nrCookiesAdded;
    }


    public int addList(List<HttpCookieJPA> newCookies) {
        int nrCookiesAdded = 0;
        return nrCookiesAdded;
    }


    public int addCollection(Collection<HttpCookieJPA> cookies) {
        int nrCookiesAdded = 0;
        for (HttpCookieJPA cookie : cookies) {
            try {
                URI uri = new URI(cookie.getSetterPageURL());
                add(uri, cookie);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return nrCookiesAdded;
    }


    public void add(URI uri, NewCookie cookie) {
        HttpCookieJPA addMe = new HttpCookieJPA(cookie, uri);
        add(uri, addMe);
    }

    public void add(URI uri, HttpCookieJPA cookie) {

        EntityTransaction tx = null;

        if (uri != null) {
            String setterPage = uri.toString();
            cookie.setSetterPageURL(setterPage);
        } else {
            // log.warn("setter page uri null for cookie:\n"+cookie.toString());
        }
        cookie.setUpdatedDate(new Date());

        HttpCookieJPA giaPresente = find(cookie.getDomain(), cookie.getName());
        if (giaPresente != null) {
            log.debug("cookie gia presente vs nuovo: \n" + giaPresente.toString() + "\n" + cookie.toString());
            int nrRemoved = removeCookie(cookie.getDomain(), cookie.getName());
            assert (nrRemoved != 1);
        }

        if (cookie.getMaxage() == 0) {
            // it means that I must only delete old, done above, and not write the new
            return;
        } else {
            try {
                tx = em.getTransaction();
                if (!tx.isActive()) tx.begin();
                em.persist(cookie);
            } catch (Exception e) {
                log.error(e);
            } finally {
                if (tx != null) {
                    tx.commit();
                    em.detach(cookie);
                }
            }
        }
    }

    public List<Cookie> getCookies(String domain, String path) {
        log.debug("nr All cookies in store: " + size());
        List<HttpCookieJPA> jpa = getCookiesJPA(domain, path);
        List<Cookie> cookies = new ArrayList<Cookie>();
        for (HttpCookieJPA j : jpa) {
            Cookie cookie = new Cookie(j.getName(), j.getValue());
            //,j.getPath(),j.getDomain(),j.getVersion());
            cookies.add(cookie);
        }
        log.debug("per domain: " + domain + " path: " + path + " trovani cookie nr: " + jpa.size());
        return cookies;
    }

    public List<HttpCookieJPA> getCookiesJPA(String domain, String path) {

        EntityTransaction tx = null;
        List<HttpCookieJPA> allCookies = new ArrayList<HttpCookieJPA>();
        try {
            tx = em.getTransaction();
            boolean withinTx = tx.isActive();
            if (!withinTx) tx.begin();
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<HttpCookieJPA> query = criteriaBuilder.createQuery(HttpCookieJPA.class);
            Root<HttpCookieJPA> from = query.from(HttpCookieJPA.class);
            query.select(from);
            TypedQuery<HttpCookieJPA> q = em.createQuery(query);
            allCookies = q.getResultList();
        } catch (Exception e) {
            log.error("");
        } finally {
            if (tx != null && tx.isActive())
                tx.commit();
        }

        // filter out
        List<HttpCookieJPA> filteredCookies = new ArrayList<HttpCookieJPA>();
        for (HttpCookieJPA cookie : allCookies) {
            // log.info(cookie.toString());
            if (cookie.isPermAndExpired()) {
                log.debug("cookie " + cookie.getName() + " expired: " + cookie.getExpiryDate());
                continue;
            }
            if (domain != null) {
                // ok, must filter
                if (!WEBUtils.isSubDomainOf(cookie.getDomain(), domain, true)) {
                    log.info("discarded cookie with domain: " + cookie.getDomain() + " not subdomain of " + domain);
                    continue;
                }
            }

            if (path != null) {
                // send if the target path is subpath of cookie path
                if (WEBUtils.isSubPathOf(cookie.getPath(), path, false)) {
                    log.info("discarded cookie with path: " + cookie.getPath() + " because path is " + path);
                    continue;
                }
            }
            filteredCookies.add(cookie);
        }

        return filteredCookies;
    }

    public HttpCookieJPA find(String domain, String name) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<HttpCookieJPA> query = criteriaBuilder.createQuery(HttpCookieJPA.class);
        Root<HttpCookieJPA> from = query.from(HttpCookieJPA.class);

        log.error("removed domain for a but, put it back");
        /*Predicate andClause = criteriaBuilder.and(criteriaBuilder.equal(from.get("domain"), domain),
                criteriaBuilder.equal(from.get("name"), name));*/
        Predicate andClause = criteriaBuilder.and(criteriaBuilder.equal(from.get("name"), name));

        query.where(andClause);

        TypedQuery<HttpCookieJPA> q = em.createQuery(query);
        List<HttpCookieJPA> cookies = q.getResultList();

        assert (cookies.size() <= 1);

        if (cookies.size() == 1)
            return cookies.get(0);
        else
            return null;
    }

    /**
     * EV!!! implementazione probabilmente inefficiente, rifare con una query diretta
     *
     * @param domain
     * @param path
     * @return
     */
    public int size(String domain, String path) {
        List<HttpCookieJPA> cookies = getCookiesJPA(domain, path);
        return cookies.size();
    }

    public int size() {
        List<HttpCookieJPA> cookies = getCookiesJPA(null, null);
        return cookies.size();
    }

    @Override
    public boolean removeAll() {

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        int deletedCount = em.createQuery("DELETE FROM HttpCookieJPA").executeUpdate();
        log.debug("deted cookies: " + deletedCount);
        tx.commit();
        return true;
    }


    public int removeCookie(String exactDomain, String name) {
        int nrRemoved = 0;

        EntityTransaction tx = em.getTransaction();

        int deletedCount = 0;
        try {
            if (!tx.isActive()) tx.begin();
            Query query = em.createQuery("DELETE FROM HttpCookieJPA c" +
                    " WHERE" +
                    " c.name = :name" +
                    " AND c.domain = :domain");
            deletedCount = query
                    .setParameter("name", name)
                    .setParameter("domain", exactDomain)
                    .executeUpdate();
        } catch (Exception e) {
            log.error("beccata", e);
        } finally {
            if (tx.isActive()) tx.commit();
        }


        return deletedCount;
    }


    /**
     * Creata fare pulizia quando riceviamo una nuova pagina e, EV??? SE CAPISCO BENE icookie memorizzati mi servono solo in avvio EV?? devo
     * cancellare tutti i cookie del dominio, a prescindere dal path?
     *
     * @param domainLike
     * @param path
     * @return
     */
    public int removeCookies(String domainLike, String path) {
        int nrRemoved = 0;
        EntityTransaction tx = em.getTransaction();

        try {
            if (!tx.isActive()) tx.begin();
            List<HttpCookieJPA> cookies = getCookiesJPA(domainLike, path);
            for (HttpCookieJPA cookie : cookies) {
                if (cookie.isPermAndExpired()) {
                    assert (find(cookie.getDomain(), cookie.getName()) != null);
                    em.remove(cookie);
                    nrRemoved++;
                }
            }
        } catch (Exception e) {
            log.error("beccata", e);
        } finally {
            if (tx.isActive()) tx.commit();
        }

        log.debug("nr cookies removed: " + nrRemoved + " remain: " + size());
        return nrRemoved;
    }

    public String dump(boolean detailed) {
        String ret = "";
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<HttpCookieJPA> query = criteriaBuilder.createQuery(HttpCookieJPA.class);
        Root<HttpCookieJPA> from = query.from(HttpCookieJPA.class);
        query.select(from);

        TypedQuery<HttpCookieJPA> q = em.createQuery(query);
        List<HttpCookieJPA> cookies = q.getResultList();

        if (cookies.size() == 0) return null;

        for (HttpCookieJPA c : cookies) {
            ret += c.toString() + "\n";
        }

        return ret.substring(0, ret.length() - 2);
    }

    public void close() {
        em.close();
    }

    // --- immondizia dell'API originale che fa veramente schifo ---
    /* Retrieve cookies associated with given URI, or whose domain matches the given URI. Only cookies that have not expired are returned.
     * This is called for every outgoing HTTP request. Returns: an immutable list of HttpCookie, return empty list if no cookies match the
	 * given URI */
    @Override
    public List<HttpCookie> get(URI uri) {
        // TODO Auto-generated method stub
        return null;
    }

    /* Get all not-expired cookies in cookie store. Returns: an immutable list of http cookies; return empty list if there's no http cookie
     * in store */
    @Override
    public List<HttpCookie> getCookies() {
        List<HttpCookieJPA> cookiesJPA = getCookiesJPA(null, null);
        List<HttpCookie> cookies = new ArrayList<HttpCookie>();

        for (HttpCookieJPA c : cookiesJPA) {
            cookies.add(c.getHttpCookie());
        }
        return cookies;
    }

    /* Adds one HTTP cookie to the store. ... . A cookie to store may or may not be associated with an URI. If it is not associated with an
     * URI, the cookie's domain and path attribute will indicate where it comes from. If it is associated with an URI and its domain and
     * path attribute are not speicifed, given URI will indicate where this cookie comes from. If a cookie corresponding to the given URI
     * already exists, then it is replaced with the new one. */
    @Override
    public void add(URI uri, HttpCookie cookie) {

        if (cookie.getDomain() == null || cookie.getDomain().length() <= 0) {
            if (uri.getHost() != null && uri.getHost().length() > 0) {
                cookie.setDomain(uri.getHost());
            } else {
                log.error("no domain for cookie");
                System.exit(1);
            }
        }
        if (cookie.getPath() == null || cookie.getPath().length() <= 0) {
            if (uri.getPath() != null && uri.getPath().length() > 0) {
                cookie.setPath(uri.getPath());
            } else {
                log.warn("no path for cookie: " + cookie);
                cookie.setPath("/");
            }
        }


        HttpCookieJPA addMe = new HttpCookieJPA(cookie);
        add(uri, addMe);
    }

    /* Get all URIs which identify the cookies in this cookie store. Returns: an immutable list of URIs; return empty list if no cookie in
     * this cookie store is associated with an URI */
    @Override
    public List<URI> getURIs() {
        // TODO Auto-generated method stub
        return null;
    }

    /* Remove a cookie from store. Parameters: uri - the uri this cookie associated with. if null, the cookie to be removed is not
     * associated with an URI when added; if not null, the cookie to be removed is associated with the given URI when added. cookie - the
     * cookie to remove Returns: true if this store contained the specified cookie */
    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        // TODO Auto-generated method stub
        return false;
    }

    static EntityManager em = null;

    private static Logger log = LogManager.getLogger(CookieStoreEV.class.getSimpleName());
}
