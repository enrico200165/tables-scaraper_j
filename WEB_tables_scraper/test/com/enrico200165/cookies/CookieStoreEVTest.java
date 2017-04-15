package com.enrico200165.cookies;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CookieStoreEVTest {

	@Before
	public void setUp() throws Exception {
		defCookieStoreEV = new CookieStoreEV("cookiesGlobalStore");
	}
	

	@After
	public void tearDown() throws Exception {
        defCookieStoreEV.close();
	}


	static List<HttpCookieJPA> generateDefCookies(int nrCookies, int nrDomains, int nrPaths) {
		return generateCookies("cookie", nrCookies, "example.com", true, true, nrDomains,
				"/users/edit", true, true, nrPaths);
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
						HttpCookie coreCookie = new HttpCookie(finalName,curVal);
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




	@Test
	public void testAdd() throws Exception {
		int nrIniziale = defCookieStoreEV.size();
		log.debug("startup trovo nr cookies: "+nrIniziale);


		List<HttpCookieJPA> cookies = generateDefCookies(10,10,10);

		// html singolo cookie
		defCookieStoreEV.removeAll();
		HttpCookieJPA writtenCookie = cookies.get(1);
		log.debug("domain: "+writtenCookie.dump());
		defCookieStoreEV.add(null,writtenCookie);
		HttpCookieJPA readCookie = defCookieStoreEV.find(writtenCookie.getDomain(),writtenCookie.getName());
		log.debug("domain: "+readCookie.dump());


		defCookieStoreEV.addCollection(cookies);

		log.debug("adesso trovo nr cookies: "+defCookieStoreEV.size());

		defCookieStoreEV.removeAll();
		log.debug("dopo remove all nr cookies: "+defCookieStoreEV.size());

	}

	@Test
	public void testRemoveCookie() throws Exception {

		defCookieStoreEV.removeAll();
		List<HttpCookieJPA> cookies = generateDefCookies(1,1,1);

		HttpCookieJPA c = cookies.get(0);

		defCookieStoreEV.add(null,c);
		assertEquals(defCookieStoreEV.size(),1);

		defCookieStoreEV.removeCookie(c.getDomain(),c.getName());
		assertEquals(defCookieStoreEV.size(),0);
	}


	@Test
	public void testInsertion() {
        defCookieStoreEV.removeAll();
		List<HttpCookieJPA> cookies = generateDummyCookies();
		int nrInserted = 0;
		for (HttpCookieJPA cookie : cookies) {
            defCookieStoreEV.add(null, cookie);
			nrInserted++;
		}
		log.info("cookies inserted vs found in store: " + nrInserted + " " + defCookieStoreEV.size(null, null));
		assertEquals(nrInserted, defCookieStoreEV.size(null, null));
	}

	@Test
	public void testRetrieval() {

		String name = "name12345678";
		String domain = "google.com";

		HttpCookie cookie = new HttpCookie(name, "valore");

		cookie.setComment("comment");
		cookie.setCommentURL("http://comment.it");
		cookie.setDiscard(true);

		cookie.setDomain(domain);
		cookie.setHttpOnly(true);
		cookie.setMaxAge(60);
		cookie.setPath("/path");
		cookie.setPortlist("porta1 porta2");
		cookie.setSecure(true);
		cookie.setVersion(1);

        defCookieStoreEV.add(null, cookie);
		log.info("contents of cookie store: \n"+defCookieStoreEV.dump(true));
		HttpCookieJPA found = defCookieStoreEV.find(domain,name);
		assertNotNull(found);

		if (found != null) log.info("cookie found: " + found.toString());
		else log.info("cookie not found");
	}

	@Test
	public void testRemoval() {
		testInsertion();
		log.info("cookies in store before removal: " + defCookieStoreEV.size(null, null));

        defCookieStoreEV.removeCookies("google.com", null);

		log.info("cookies in store after removal: " + defCookieStoreEV.size(null, null));
		log.warn("");
	}

	public List<HttpCookieJPA> generateDummyCookies() {
		return generateDummyCookies("name", "value");
	}

	public List<HttpCookieJPA> generateDummyCookies(String name, String valueSeed) {
		ArrayList<String> domains;
		ArrayList<String> paths;
		ArrayList<Date> dates;

		domains = new ArrayList<String>();
		// domains.addMap("");
		domains.add("com");
		domains.add("google.com");
		domains.add("www.google.com");

		paths = new ArrayList<String>();
		paths.add("/");
		paths.add("/user/register");
		paths.add("/user/view");

		dates = new ArrayList<Date>();
		long currentDate = (new Date()).getTime();
		log.debug("check date: " + new Date(currentDate));

		ArrayList<HttpCookieJPA> cookies = new ArrayList<HttpCookieJPA>();
		int nr = 0;
		cookies = new ArrayList<HttpCookieJPA>();
		for (String domain : domains) {
			for (String path : paths) {
				for (int i = 0; i < 10; i++) {
					long seconds = 3600; // (i - 5);
					{
						HttpCookie coreCookie = new HttpCookie(name,"val_+nr");
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

	
	public static void main(String[] argv) {
		
	}
	
	
	
	CookieStoreEV defCookieStoreEV;

	private static org.apache.log4j.Logger log = Logger.getLogger(CookieStoreEVTest.class);

}
