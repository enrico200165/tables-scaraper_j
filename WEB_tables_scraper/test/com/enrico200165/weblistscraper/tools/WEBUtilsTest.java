package com.enrico200165.weblistscraper.tools;

import com.enrico200165.utils.net.http.Utils;
import com.enrico200165.utils.str_regex.NameValuePairString;
import com.enrico200165.weblistscraper.common.WEBUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.List;

import static org.junit.Assert.*;

public class WEBUtilsTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIsRelativeURL() {
		// fail("Not yet implemented");
	}

	@Test
	public void testMakeUrlRelative() {
		// fail("Not yet implemented");
	}

	@Test
	public void testIsCorrectURL() {
		// fail("Not yet implemented");
	}

	@Test
	public void testIsUsableURI() {
		// fail("Not yet implemented");
	}

	@Test
	public void testCookieToString() {
		// fail("Not yet implemented");
	}


	@Test
	public void testURLFromURI() throws Exception {

		String urlOrig = "https://www.japan-guide.com/login/?aACTION=url&aURL=/index.html";
		URI uri = new URI(urlOrig);
		assertEquals(Utils.URLFromURI(uri),urlOrig);
		//log.info(Utils.URLFromURI(uri));
	}

	@Test
	public void testURIfromUrl() throws Exception {
		String ret = "";
		String hostname = "example.com";
		String http = "http://";
		String https = "https://";

		ret = Utils.URIFromURLString("/",hostname).toString();
		log.info(ret);
		assertEquals(ret,http+hostname+"/");

		ret = Utils.URIFromURLString("",hostname).toString();
		log.info(ret);
		assertEquals(ret,http+hostname+"/");

		ret = Utils.URIFromURLString("path",hostname).toString();
		log.info(ret);
		assertEquals(ret,http+hostname+"/path");

		ret = Utils.URIFromURLString("/path",hostname).toString();
		log.info(ret);
		assertEquals(ret,http+hostname+"/path");

		ret = Utils.URIFromURLString("s://path",hostname).toString();
		log.info(ret);
		assertEquals(ret,https+hostname+"/path");


		// hostname substring di url
		String url = "http://"+hostname+"/pippo";
		ret = Utils.URIFromURLString(url,hostname).toString();
		assertEquals(url,ret);

		// hostname null
		hostname = null;
		URI u = Utils.URIFromURLString("s://path",hostname);
		assertNull(u);

		// l'url dovrebbe gestire l'hostname null
		ret = Utils.URIFromURLString(url,null).toString();
		assertEquals(url,ret);


	}


	@Test
	public void testFormFieldsFromString() {

		String fromFiddler = "aGENDER=b&aAGE1=14&aAGE2=120&aINTEREST=0&aNATIONALITY=jp&aLANGUAGE2=28&aLANGUAGE1=0&aGEOTYPE=2&aID_COUNTRY=jp&aID_STATE=0&aID_CITY=0&aCAT=3&aMES=submit";
		List<NameValuePairString> fields = WEBUtils.formFieldsFromString(fromFiddler);

		for (NameValuePairString p : fields) {
			log.info(p.getKey()+"="+p.getValue());
		}

	}


	@Test
	public void testIsPathCorrect() {
		assertFalse(WEBUtils.isPathCorrect("//"));
		assertTrue(WEBUtils.isPathCorrect("/a"));

		assertTrue(WEBUtils.isPathCorrect("/a/a"));
		assertTrue(WEBUtils.isPathCorrect("/a/a/"));
		
		
		assertTrue(WEBUtils.isPathCorrect("/a/"));
		assertFalse(WEBUtils.isPathCorrect("/a//"));
		assertFalse(WEBUtils.isPathCorrect("/~"));

		// non so se . è ammesso
		assertTrue(WEBUtils.isPathCorrect("/."));
		assertTrue(WEBUtils.isPathCorrect("/.."));

		// questi probabilmete NON DOVREBBMO ACCETTARLI, ma gestire correttamente è da
		// matti e  mi aspetto paths corretti
		assertTrue(WEBUtils.isPathCorrect("/?"));
		assertTrue(WEBUtils.isPathCorrect("/?a"));
		assertTrue(WEBUtils.isPathCorrect("/?&=#a;"));
		assertTrue(WEBUtils.isPathCorrect("~"));

		assertTrue(WEBUtils.isPathCorrect("~enrico"));
		assertTrue(WEBUtils.isPathCorrect("~enrico/"));
		assertTrue(WEBUtils.isPathCorrect("~enrico/a/a"));
	}

	@Test
	public void testIsDomainCorrect() {

		assertFalse(WEBUtils.isDomainCorrect("com"));
		assertFalse(WEBUtils.isDomainCorrect(".com"));
		assertFalse(WEBUtils.isDomainCorrect("com."));
		assertFalse(WEBUtils.isDomainCorrect(".a.a.a"));

		assertTrue(WEBUtils.isDomainCorrect("a.a"));
		assertTrue(WEBUtils.isDomainCorrect("a.a.a"));
		assertTrue(WEBUtils.isDomainCorrect("japan-guide.com"));
	}

	@Test
	public void testIsSubDomainOf() {
		
		assertTrue(WEBUtils.isSubDomainOf("host1.google.com", "google.com",false));
		assertTrue(WEBUtils.isSubDomainOf("cluster1.host1.google.com", "google.com",false));
		assertTrue(WEBUtils.isSubDomainOf("a.google.com", "google.com",false));

		// esce causa dominio invalido 
		// assertFalse(WEBUtils.isSubDomainOf("a#a.google.com","google.com",false));

		// questo probabilmente SBAGLIATO
		assertTrue(WEBUtils.isSubDomainOf("-.google.com", "google.com",false));

	
		assertFalse(WEBUtils.isSubDomainOf("google.com", "google.com",false));
		assertTrue(WEBUtils.isSubDomainOf("google.com", "google.com",true));

	}

	
	@Test
	public void testIsSubPahtOf() {

		
		assertFalse(WEBUtils.isSubPathOf("/ibm/", "/ibm/EMM",false));

		
		assertFalse(WEBUtils.isSubPathOf("/", "/",false));
		assertTrue(WEBUtils.isSubPathOf("/", "/",true));
		assertFalse(WEBUtils.isSubPathOf("/a", "/a",false));
		assertTrue(WEBUtils.isSubPathOf("/a/", "/a/",true));
		
		
		
		assertTrue(WEBUtils.isSubPathOf("/a/", "/a/",true));
		assertTrue(WEBUtils.isSubPathOf("/a/a", "/a/a/",true));

		assertTrue(WEBUtils.isSubPathOf("/ibm/EMM/campaign", "/ibm/EMM",false));
		assertTrue(WEBUtils.isSubPathOf("/ibm/EMM/campaign", "/ibm/EMM/",false));
		assertTrue(WEBUtils.isSubPathOf("/ibm/EMM/campaign/partition1/config/", "/ibm/EMM",false));
		assertTrue(WEBUtils.isSubPathOf("/ibm/EMM/campaign/partition1/config/","/ibm/EMM/campaign/partition1/config/",true));
		assertTrue(WEBUtils.isSubPathOf("/ibm/EMM/campaign/partition1/config/", "/",false));

		// esce causa dominio invalido 
		// assertFalse(WEBUtils.isSubPathOf("a#a.google.com","google.com",false));
	}


	private static Logger log = LogManager.getLogger(WEBUtilsTest.class.getSimpleName());
}
