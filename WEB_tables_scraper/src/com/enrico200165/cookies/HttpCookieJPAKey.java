package com.enrico200165.cookies;

import java.io.Serializable;
import javax.persistence.Entity;
import org.apache.log4j.Logger;

@Entity
public class HttpCookieJPAKey implements Serializable {

	// copiato tale e quale
	private static final long serialVersionUID = 343L;
	String name;
	String domain;

	public HttpCookieJPAKey(String n, String d) {
		this.name = n;
		this.domain = d;
	}

	public HttpCookieJPAKey() {
		name = "";
		domain = "";
	}

	public String toString() {
		String s = "";
		s += getDomain() + "/" + getName();
		return s;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDomain() {
		return this.domain;
	}

	public void setDomain(String s) {
		this.domain = s;
	}

	// Must have an equals method
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HttpCookieJPAKey) {
			HttpCookieJPAKey other = (HttpCookieJPAKey) obj;
			return other.name.equals(this.name) && other.domain.equals(this.domain);
		}
		return false;
	}

	// Must have a hashCode method
	@Override
	public int hashCode() {
		return domain.hashCode() + name.hashCode();
	}

	private static org.apache.log4j.Logger log = Logger.getLogger(HttpCookieJPAKey.class);

}
