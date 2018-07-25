package com.enrico200165.utils.config;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Properties;


class Exception_YAMLCfg_WrongType extends Exception {

    public Exception_YAMLCfg_WrongType(String ex_type, String foundType ,String k) {
        this.key = k;
        this.expected_type = ex_type;
        this.found_type = foundType;
    }

    public String toString() { return "wrong type in YAMLfor key: "+key+" expected: "+expected_type+" found: "+found_type; }

    public String expected_type;
    public String found_type;
    public String key;
}


public class PropertiesYAMLEV {


    public static final String TYPE_NOT_FOUND = "type_not_found";
    public static final String TYPE_KEY = "type";

    public PropertiesYAMLEV(String pathPar) {
        path = pathPar;
    }


    static String getType( Map< String , Object> entry)
    // type is the value of child element "type"
    {
        // get child element
        Object val = entry.get(TYPE_KEY);
        if (val == null || !(val instanceof java.lang.String ))
            return TYPE_NOT_FOUND;
        return val.toString();
    }


    static String getStr( Map< String , Object> entry, String key) throws Exception_YAMLCfg_WrongType
    // type is the value of child element "type"
    {
        // get child element
        Object val = entry.get(key);
        if (val == null) throw new Exception_YAMLCfg_WrongType("String", "null",key);
        if (!(val instanceof java.lang.String )) throw new Exception_YAMLCfg_WrongType("String", val.getClass().toString(),key);

            return val.toString();
    }




    public String get(String key) {
        return properties.get(key);
    }

    Map<String, String> properties;
    Properties prop;
    String path;

    private static org.apache.log4j.Logger log = Logger.getLogger(PropertiesYAMLEV.class);
}
