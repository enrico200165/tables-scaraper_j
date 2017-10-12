package com.enrico200165.utils.config;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Properties;
import java.io.File;


public class PropertiesYAMLEV {


    public PropertiesYAMLEV(String pathPar) {
        path = pathPar;
    }

    public Object readProperties() {

        return null;
    }



    public String get(String key) {
        return properties.get(key);
    }

    Map<String, String> properties;
    Properties prop;
    String path;

    private static org.apache.log4j.Logger log = Logger.getLogger(PropertiesYAMLEV.class);
}
