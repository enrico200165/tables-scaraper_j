package com.enrico200165.utils.various;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesEV {

	public PropertiesEV(String pathPar) {
		prop = new Properties();
		properties = new HashMap<String,String>();
		
		
		try {
			prop.load(new FileInputStream(pathPar));
		} catch (IOException e) {
			log.error("unable to read/find prop file: "+pathPar + "\nam in: "+System.getProperty("user.dir"));
			System.exit(1);
		}

		for (String key : prop.stringPropertyNames()) {
			String value = prop.getProperty(key);
//			log.debug(key + " => " + value);
			properties.put(key, value);
		}
		prop = null;
	}

	public String get(String key) {
		return properties.get(key);
	}
	
	Map<String,String> properties;
	Properties prop;
	String path;

	private static org.apache.log4j.Logger log = Logger.getLogger(PropertiesEV.class);
}
