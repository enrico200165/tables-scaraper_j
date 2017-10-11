package com.enrico200165.utils.various;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.io.File;


public class PropertiesYAMLEVTest {

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

		String fpath = "V:/data/pers_dev/data_dbs/web_scraper/config_test.yaml";
		PropertiesYAMLEV p = new PropertiesYAMLEV("dummy");
		log.info(p.get("proprieta1"));
		log.info(p.get("proprieta2"));
	}


	@Test public void testJacksonYAML() {

        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        System.out.flush();
        PropertiesYAMLEV p = new PropertiesYAMLEV("user.yaml");
        p.readProperties();

	}

	
	
	private static org.apache.log4j.Logger log = Logger.getLogger(PropertiesYAMLEVTest.class);

}
