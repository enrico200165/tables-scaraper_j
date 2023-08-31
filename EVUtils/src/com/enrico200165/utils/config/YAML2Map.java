package com.enrico200165.utils.config;

import com.enrico200165.utils.various.Utl;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class YAML2Map {

    /**
     * Reads the ID (name) of an element, contained within it as the value of ID
     *
     * @param
     * @return
     */
    public static String getNameFromChild(Map.Entry<String, Object> element) {
        Map<String, Object>  values = (Map<String, Object>) element.getValue();
        String name = (String) values.get("ID"); // todo use constant
        return name;
    }


    public static Map<String, Object> YAML2Map(String fpath) {

        String content = null;
        log.info("will parse config file: "+fpath);
        try  {  content = new String(Files.readAllBytes(Paths.get(fpath))); }
        catch (IOException e) {
            log.log(Level.SEVERE,"working dir"+System.getProperty("user.dir") + e.toString());
        }

        Map<String, Object> propertiesMap = (new Yaml()).load(content);

        log.log(Level.SEVERE, "Completed yaml file parsing");

        return propertiesMap;
    }


    static Logger log=Logger.getLogger(Utl.class.getSimpleName());
}
