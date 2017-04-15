package com.enrico200165.utils.various;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;


public class UtlTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	// @Test
	public void test() {
		String t = Utl.tStamp();
		log.info(t);
		t = Utl.todayDateStamp();
		log.info(t);		
	}
	
	@Test public void testPropertiesEV() {

		PropertiesEV p = new PropertiesEV("V:/data/pers_data/00_importantissimi/keys/programming/fortest.properties");
		
		log.info(p.get("proprieta1"));
		log.info(p.get("proprieta2"));
		
	}
	
	
	
	private static org.apache.log4j.Logger log = Logger.getLogger(UtlTest.class);

}
