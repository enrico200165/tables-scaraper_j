package com.enrico200165.utils.config;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class YAML2Map {

    /**
     * Reads the ID (name) of an element, contained within it as the value of ID
     *
     * @param
     * @return
     */
    public static String getNameFromChild(Map.Entry<String, Object> element) {
        Map values = (Map) element.getValue();
        String name = (String) values.get("ID"); // todo use constant
        return name;
    }


    public static Map<String, Object> YAML2Map(String fpath) {

        String content = null;
        log.info("will parse config file: "+fpath);
        try  {  content = new String(Files.readAllBytes(Paths.get(fpath))); }
        catch (IOException e) { log.error("working dir"+System.getProperty("user.dir"),e);  }

        Map<String, Object> propertiesMap = (Map<String, Object>) (new Yaml()).load(content);

        log.debug("Completed yaml file parsing");

        return propertiesMap;
    }


    private static Logger log = LogManager.getLogger(YAML2Map.class);
}
