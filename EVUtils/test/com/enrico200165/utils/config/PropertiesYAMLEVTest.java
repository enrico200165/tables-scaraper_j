package com.enrico200165.utils.config;

import com.enrico200165.utils.various.Utl;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;


public class PropertiesYAMLEVTest {

	String testFName = "test.yaml";

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		String fcontent = "";
		fcontent += 
		"tasks:" +"\n"+
		"    - concorsi:"+"\n"+
		"        - host:"+"\n"+
		"            baseHostURI: http://concorsi.it"+"\n"+
		"            loginFName: filenane"+"\n"+
		"            loginFormURL: /loginFormURL/hghgh"+"\n"+
		"            tables:"+"\n"+
		"                 - concorsi:"+"\n"+
		"                     tableUrl: \"/concorsi\""+"\n"+
		"                     selector: \"myselector\""+"\n"+
		"                     htmlEntryIncl: [\"\",\"\"]"+"\n"+
		"                     htmlEntryExcl: [\"\",\"\"]"+"\n"+
		"        - host:"+"\n"+
		"            url: http://concorsi.it"+"\n"+
		"            loginFName: filenane"+"\n"+
		"            tables:"+"\n"+
		"               - concorsi:"+"\n"+
		"                   tableUrl: \"/concorsi\""+"\n"+
		"                   selector: \"myselector\""+"\n"+
		"                   htmlEntryLIncl: [\"hinclude1\",\"hinclude2\"]"+"\n"+
		"                   htmlEntryLIncl: \"hincludeSingle\""+"\n"+
		"                   htmlEntryLExcl: [\"excl1\",\"excl2\"]"+"\n"+
		"                   htmlEntryExcl: \"exclude\""+"\n"+
		"                   entryIncl: \"hincl\""+"\n"+
		"                   entryIncl: [\"hincl1\",\"hincl2\"]"+"\n"+
		"    - italianpenpals:"+"\n"+
		"        - host:"+"\n"+
		"            url: http://concorsi.it"+"\n"+
		"            login:"+"\n"+
		"                user: enrico"+"\n"+
		"                password: pipppo"+"\n"+
		"            tables:"+"\n"+
		"               concorsi:"+"\n"+
		"                   url: \"/concorsi\""+"\n"+
		"                   selector: \"myselector\""+"\n"+
		""+"\n"+
		""+"\n"+
		""+"\n"+
		"  "+"\n"+
		"l1entry2: ciao"+"\n"+
		" "+"\n"+
		"l1entry3:"+"\n"+
		"    - l2entry: saluti";

		PrintWriter pw = new PrintWriter(testFName);
		pw.println(fcontent);
		pw.flush();
		pw.close();
		return ;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test public void learnIt() {

		InputStream input = null;
		try {
			input = new FileInputStream(new File(testFName));
		} catch (FileNotFoundException e) {
			log.error(e);
		}

		Yaml yaml = new Yaml();

		Object data = yaml.load(input);

		Type tt = data.getClass();

		System.out.println(data.getClass().getTypeName());
		System.out.println(data.getClass().getTypeName());

		for (Type t: data.getClass().getTypeParameters()) {
			System.out.println(t.getTypeName());
			if (t instanceof ParameterizedType) {
				Type elementType = ((ParameterizedType) t).getActualTypeArguments()[0];
			}
		}

		System.out.println(data);
		System.out.println(data.getClass());
		System.out.println(data.getClass().getName());

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
