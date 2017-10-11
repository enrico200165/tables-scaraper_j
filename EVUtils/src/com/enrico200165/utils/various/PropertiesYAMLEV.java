package com.enrico200165.utils.various;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.io.File;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class PropertiesYAMLEV {


    public PropertiesYAMLEV(String pathPar) {
        path = pathPar;
    }

    public Object readProperties() {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        PropertiesYAMLUser user = null;
        try {
            user = mapper.readValue(new File(this.path), PropertiesYAMLUser.class);
            System.out.println(ReflectionToStringBuilder.toString(user, ToStringStyle.MULTI_LINE_STYLE));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return user;
    }



    public String get(String key) {
        return properties.get(key);
    }

    Map<String, String> properties;
    Properties prop;
    String path;

    private static org.apache.log4j.Logger log = Logger.getLogger(PropertiesYAMLEV.class);
}
