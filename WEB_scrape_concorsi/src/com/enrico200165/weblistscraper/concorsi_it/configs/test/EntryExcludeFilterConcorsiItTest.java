package com.enrico200165.weblistscraper.concorsi_it.configs.test;

import com.enrico200165.weblistscraper.tools.EntryExcludeFilter;
import com.enrico200165.weblistscraper.tools.EntryExcludeFilterVanilla;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class EntryExcludeFilterConcorsiItTest {

	Element buildJSoupElem(String html) {
		Element e = new  org.jsoup.nodes.Element(Tag.valueOf("p"), "http://example.com");
		e.append("<div>"+html+"</div>");
		return e;
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		filter = new EntryExcludeFilterVanilla();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExcludeElement() {
		Element el = null;
		StringBuffer comments = new StringBuffer();
		el = buildJSoupElem(" per la copertura a tempo determinato di un posto di te");
		log.info(filter.exclude(el,comments));
	}

	@Test
	public void testExcludeProspectSpecific() {
		// fail("Not yet implemented");
	}

	
	EntryExcludeFilter filter;
	
	private static org.apache.log4j.Logger log = Logger.getLogger(EntryExcludeFilterConcorsiItTest.class);

}
