package com.enrico200165.utils.config;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    private static Map.Entry<String, Object> tasksList;
    private static String fatherKeyP;

    public PropertiesYAMLEV(String pathPar) {
        path = pathPar;
    }


    static String getType( Map< String , Object> entry) {
    // type is the value of child element "type"
        Object val = entry.get(TYPE_KEY);
        if (val == null || !(val instanceof java.lang.String ))
            return TYPE_NOT_FOUND;
        return val.toString();
    }


    static String getStr( Map< String , Object> entry, String key)
            throws Exception_YAMLCfg_WrongType {
        Object val = entry.get(key);
        if (val == null) throw new Exception_YAMLCfg_WrongType("String", "null",key);
        if (!(val instanceof java.lang.String )) {
            throw new Exception_YAMLCfg_WrongType("String", val.getClass().toString(), key);
        }
        return val.toString();
    }
    static String getStr( Map.Entry< String , Object> entry, String key)
            throws Exception_YAMLCfg_WrongType {
        Object val = entry.getValue();
        if (val == null) throw new Exception_YAMLCfg_WrongType("String", "null",key);
        if (!(val instanceof java.lang.String )) {
            throw new Exception_YAMLCfg_WrongType("String", val.getClass().toString(), key);
        }
        return val.toString();
    }



    static int getInt( Map< String , Object> entry, String key) throws Exception_YAMLCfg_WrongType {
        // get child element
        String str = PropertiesYAMLEV.getStr(entry ,key);
        int result = Integer.parseInt(str);

        return result;
    }


    public String get(String key) {
        return properties.get(key);
    }


    private static void parseTask(@NotNull Map.Entry<String, Object> task, String fatherKeyP) throws Exception_YAMLCfg_WrongType {
        PropertiesYAMLEV.tasksList = task;
        PropertiesYAMLEV.fatherKeyP = fatherKeyP;

        Map<String , Object> taskListChildren = (Map<String , Object> ) task.getValue();
        for (Map.Entry<String , Object> e : taskListChildren.entrySet()) {

            String k = e.getKey(); String fatherKey = fatherKeyP+"."+e.getKey();
            Object value_o = (Object) e.getValue();
            log.info("analyzing key: "+fatherKey);

            if (value_o instanceof Map<?, ?>) {
                Map<String , Object> values = (Map<String , Object>) e.getValue();

                continue;
            } else { // non-structured values
                if (k.equals("type")) { // redundant, just to practise
                    if (PropertiesYAMLEV.getStr(e, "type").equals("task")) {
                    }
                }
                log.error("unmanaged key: "+fatherKey);
            }
        }

    }


    private static void parseTaskList(@NotNull Map.Entry<String, Object> tasksList, String fatherKeyP) throws Exception_YAMLCfg_WrongType {
        PropertiesYAMLEV.tasksList = tasksList;
        PropertiesYAMLEV.fatherKeyP = fatherKeyP;

        Map<String , Object> taskListChildren = (Map<String , Object> ) tasksList.getValue();
        for (Map.Entry<String , Object> e : taskListChildren.entrySet()) {

            String k = e.getKey(); String fatherKey = fatherKeyP+"."+e.getKey();
            Object value_o = (Object) e.getValue();
            log.info("analyzing key: "+fatherKey);

            if (value_o instanceof Map<?, ?>) {
                Map<String , Object> value = (Map<String , Object>) e.getValue();
                if (PropertiesYAMLEV.getType(value).equals("task")) {
                    parseTask(e,fatherKey);
                }
            } else {
                log.error("unmanaged key: "+fatherKey);
            }
        }
    }



    public static void main(String[] argv) {

        String fpath = "./EVUtils/test.yaml";
        String content = null;

        try  {  content = new String(Files.readAllBytes(Paths.get(fpath))); }
        catch (IOException e) { log.error("working dir"+System.getProperty("user.dir"),e);  }

        Map<String, Object> prop = (Map<String, Object>) (new Yaml()).load(content);

        try {
            for (Map.Entry<String, Object> entry : prop.entrySet()) {

                String k = entry.getKey(); Object value = entry.getValue();

                if (value instanceof Map<?, ?>) {
                    Map<String, Object> values = (Map<String, Object>) entry.getValue();
                    if (PropertiesYAMLEV.getType(values).equals("task_list")) {
                        parseTaskList(entry ,k); continue;
                    }
                } else {
                    // process simple values? listsother
                    continue;
                }
                log.error("unmanaged key: "+k);
            }
        } catch (Exception_YAMLCfg_WrongType e) {
            log.error(e);
        } catch (Exception e) {
            log.error(e);
        }

        log.info("Completed yaml file parsing");


    }



    Map<String, String> properties;
    Properties prop;
    String path;

    private static org.apache.log4j.Logger log = Logger.getLogger(PropertiesYAMLEV.class);
}
