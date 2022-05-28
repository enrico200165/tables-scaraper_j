package com.enrico200165.cookies;

import com.enrico200165.utils.str_regex.StringUtils;
import com.enrico200165.weblistscraper.common.WEBUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import java.net.HttpCookie;
import java.net.URI;
import java.util.Date;

/**
 * Wrapper around java.net.HttpCookieHttpCookie, that does not allow to set the name
 * NB so changing the name means changing the cookie
 * NB HttpCookieHttpCookie.get/setExpiry() is nonsense, wrongly defined in the API, ignoring
 * it and using a member of my wrapper class
 *
 * @author enrico
 */


@Entity
@IdClass(HttpCookieJPAKey.class)
public class HttpCookieJPA {

    public HttpCookieJPA() {
        // not to crash when built from JPA
        this.httpCookie = new HttpCookie("tempName", "tempValue");
        this.setOriginalCookie("from default constructor");
        expiryDate = null;
    }

    public HttpCookieJPA(HttpCookie cPar) {
        this.httpCookie = cPar;
        this.expiryDate = null;
    }

    @Override
    public boolean equals(Object other) {
        if (other.getClass() != HttpCookieJPA.class) {
            return false;
        }
        HttpCookieJPA otherCookie = (HttpCookieJPA) other;
        return this.httpCookie.equals(otherCookie.httpCookie);
    }


    public boolean isIdentical(HttpCookie other) {
        boolean ret = true;

        // name, domain, path
        ret = ret && httpCookie.equals(other);

        ret = ret && StringUtils.safeEquals(httpCookie.getComment(), other.getComment());

        ret = ret && StringUtils.safeEquals(httpCookie.getCommentURL(), other.getCommentURL());
        ret = ret && httpCookie.getDiscard() == other.getDiscard();
        ret = ret && httpCookie.getMaxAge() == other.getMaxAge();
        ret = ret && StringUtils.safeEquals(httpCookie.getPortlist(), other.getPortlist());
        ret = ret && httpCookie.getSecure() == other.getSecure();
        ret = ret && httpCookie.getVersion() == other.getVersion();
        ret = ret && httpCookie.isHttpOnly() == other.isHttpOnly();

        return ret;
    }


    public boolean isIdentical(HttpCookieJPA other) {
        boolean ret = true; // name, domain, path// name, domain, path
        ret = ret && isIdentical(other.httpCookie);
        return ret;
    }


    public HttpCookieJPA(NewCookie c, URI uri) {

        this.httpCookie = WEBUtils.fromNewCookie(c);

        this.setOriginalCookie(c.toString());

        // --- codice sotto probabilmente va rimosso, lo lascio perchè non dovrebbe fare danni

        // --- domain
        if (c.getDomain() != null && c.getDomain().length() > 0) {
            setDomain(c.getDomain());
        } else if (uri != null && uri.getHost() != null && uri.getHost().length() > 0) {
            setDomain(uri.getHost());
        } else {
            log.error("unable to get domain for cookie: " + c);
            System.exit(1);
        }

        setMaxage((long) c.getMaxAge());
        setExpiryDate(c.getExpiry());
    }


    public String getName() {
        return httpCookie.getName();
    }

    // complicato dal fatto che HttpCookie does not allow to set name sono costretto a rigenerare
    public void setName(String name) {

        if (httpCookie == null) { // dovrebbe accadere solo quando chiamati da JPA
            this.httpCookie = new HttpCookie(name, "da impostare");
            // httpCookie othe fields will be filled by JPA
        } else {
            // we can NOT set name in HTTPCookie, so we have to create a new one and copy into it values from the old ones
            HttpCookie newCookie = new HttpCookie(name, httpCookie.getValue());

            newCookie.setComment(httpCookie.getComment());
            newCookie.setCommentURL(httpCookie.getCommentURL());
            newCookie.setDiscard(httpCookie.getDiscard());
            newCookie.setDomain(httpCookie.getDomain());
            newCookie.setMaxAge(httpCookie.getMaxAge());
            newCookie.setPath(httpCookie.getPath());
            newCookie.setPortlist(httpCookie.getPortlist());
            newCookie.setSecure(httpCookie.getSecure());
            newCookie.setValue(httpCookie.getValue());
            newCookie.setVersion(httpCookie.getVersion());
            newCookie.setHttpOnly(httpCookie.isHttpOnly());

            this.httpCookie = newCookie;
        }
    }


    public String dump() {
        String ret = getName() + " " + getDomain() + " " + getPath() + " = " + getValue(); //+ getExpiryDate()
        return ret;
    }

    public Cookie toCookie() {
        Cookie cookie = new Cookie(getName(), getValue(), getPath(), getDomain(), getVersion());
        return cookie;
    }


    public boolean validate() {
        boolean ret = true;

        ret = ret && (getName().length() > 0);
        ret = ret && (getDomain().length() > 0);
        ret = ret && (getPath().length() > 0);

        if (!ret) {
            log.error("cookie NOT valid\n" + this.toString());
            System.exit(1);
        }
        return ret;
    }

    public String toString() {
        String s = "";
        s += getName() + "=" + getValue();
        // s += " expiry date = " + getExpiryDate();
        s += " domain = " + getDomain();
        s += " path = " + getPath();
        return s;
    }


    @Transient
    HttpCookie getHttpCookie() {
        return httpCookie;
    }


    public boolean isInSubdomainOf(String domain) {
        return WEBUtils.isSubDomainOf(this.getDomain(), domain, true);
    }

    // -------- ordinary Get/Set ------------

    public String getDomain() {
        return httpCookie.getDomain();
    }

    public HttpCookieJPA setDomain(String s) {
        httpCookie.setDomain(s);
        return this;
    }

    public String getComment() {
        return httpCookie.getComment();
    }

    public void setComment(String s) {
        httpCookie.setComment(s);
    }

    public String getCommentURL() {
        return httpCookie.getCommentURL();
    }

    public void setCommentURL(String s) {
        httpCookie.setCommentURL(s);
    }

    public boolean getDiscard() {
        return httpCookie.getDiscard();
    }

    public void setDiscard(boolean v) {
        httpCookie.setDiscard(v);
    }

    @Transient
    public long getMaxage() {

        boolean vecchioCalcolavaDiffFromNow = false;
        if (vecchioCalcolavaDiffFromNow) {
            long secsNow = (new Date()).getTime() / 1000;
            long dateSecsWhenExpires = getExpiryDate().getTime() / 1000;
            long maxAge = dateSecsWhenExpires - secsNow;
            System.exit(1); // non si dovrebbe mai passare qui
            return maxAge;
        } else {
            return httpCookie.getMaxAge();
        }

    }

    /**
     * @param seconds
     */
    @Transient
    public void setMaxage(Long seconds) {
        // NewCookie doc: A cookie can be unset by sending a new cookie with maximum age of 0
        // since it will overwrite any existing cookie and then be immediately discarded.
        // The default value of -1 indicates that the cookie will be discarded at the end of the browser/application session.
        httpCookie.setMaxAge(seconds);
    }


    public Date getExpiryDate() {
        return this.expiryDate;
    }

    public void setExpiryDate(Date d) {
        // NewCookie doc: ... null if no expiry date was set.
        //if (d == null) return;
        this.expiryDate = d;
    }


    @Transient
    public boolean isPermAndExpired() {

        // max age = 0 (must delete this cookie) and max age = -1 (session cookie, delete end of session)
        // must be manage outside of here or there will be false results, this only works for permanent cookies

        // --- prima cerco di usare Max-Age, come da raccomandazioni internet
        Date nowDate = new Date();
        long nowMilliSecs = nowDate.getTime();

        long writtenDateMilliSecs = lastUpdatedDate.getTime();
        long expiryDateInMillisecs = writtenDateMilliSecs + getMaxage() * 1000;

        if (expiryDateInMillisecs >= nowMilliSecs)
            return true;

        // here I shold manage expiry date
        if (getExpiryDate() == null) {
            return false;
        }
        long currentDateMilliSecsLong = (new Date()).getTime();
        long expiryDateMilliSecsLong = getExpiryDate().getTime();

        long timeToExpirySecs = (expiryDateMilliSecsLong - currentDateMilliSecsLong) / 1000;
        log.debug("cookie will expire in seconds: " + timeToExpirySecs + " = ore: " + timeToExpirySecs / 3600
                + " = giorni: " + timeToExpirySecs / 3600 / 24);

        return (expiryDateMilliSecsLong <= currentDateMilliSecsLong);
    }

    public String getPath() {
        return httpCookie.getPath();
    }

    public HttpCookieJPA setPath(String s) { httpCookie.setPath(s) ; return this; }

    public String getPortlist() {
        return httpCookie.getPortlist();
    }

    public void setPortlist(String s) {
        httpCookie.setPortlist(s);
    }

    public boolean getSecure() {
        return httpCookie.getSecure();
    }

    public void setSecure(boolean v) {
        httpCookie.setSecure(v);
    }

    public String getValue() {
        return httpCookie.getValue();
    }

    public HttpCookieJPA setValue(String v) { httpCookie.setValue(v); return this; }

    public int getVersion() {
        return httpCookie.getVersion();
    }

    public void setVersion(int v) {
        httpCookie.setVersion(v);
    }

    @Transient
    public boolean hasExpired() {
        return httpCookie.hasExpired();
    }

    @Transient
    public int hashCode() {
        return httpCookie.hashCode();
    }

    @Transient
    public boolean isHttpOnly() {
        return httpCookie.isHttpOnly();
    }

    @Transient
    public void setHttpOnly(boolean v) {
        httpCookie.setHttpOnly(v);
    }


    public String getSetterPageURL() {
        if (setterPageURL == null) {
            return "";
        }
        return setterPageURL;
    }

    public void setSetterPageURL(String setterPageURL) {
        this.setterPageURL = setterPageURL;
    }


    public Date getUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.lastUpdatedDate = updatedDate;
    }

    public String getOriginalCookie() {
        return originalCookie;
    }

    public void setOriginalCookie(String originalCookie) {
        this.originalCookie = originalCookie;
    }

    @Transient
    transient HttpCookie httpCookie;

    @Temporal(TemporalType.TIMESTAMP)
    Date expiryDate;

    @Temporal(TemporalType.TIMESTAMP)
    Date lastUpdatedDate;


    String originalCookie;


    String setterPageURL;


    @Id
    String name;

    // if put as ID there are cases when null and makes persistence fail
    // String domain;

    private static Logger log = LogManager.getLogger(HttpCookieJPA.class.getSimpleName());

}
