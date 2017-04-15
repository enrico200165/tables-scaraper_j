package com.enrico200165.weblistscraper.configs;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by enrico on 15/04/2017.
 */
public class WEBTablesScraperConfig {

    WEBTablesScraperConfig(String fName) {
        // for now we trick, no real reading from files
        if (fName == null || fName.length() <0 ) {
            cfgFName = "config.yaml";
        } else {
            cfgFName = fName;
        }
        yaml = new Yaml();
    }

    Yaml read1() {
        try {
            InputStream input = new FileInputStream(new File(cfgFName));
            Yaml yaml = new Yaml();
            Tasks tasks = yaml.loadAs(input,Tasks.class);
            System.out.println(tasks);
            input.close();
            return yaml;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    Yaml read2() {
        try {
            InputStream is = new FileInputStream(new File(cfgFName));
            Yaml yaml = new Yaml();

            // @SuppressWarnings("unchecked")
            Map<String, ArrayList> entriesl1 = (Map<String, ArrayList>) yaml.load(is);
            for (Map.Entry<String, ArrayList> entryl1: entriesl1.entrySet()) {
                // log.info("l1 entry["+entryl1.getKey()+"]= "+entryl1.getValue());
                if (entryl1.getKey().equals("tasks")) {
                    for (Object task: entryl1.getValue()) {
                        if (task.getClass() != LinkedHashMap.class) {
                            log.warn("unexpected class: "+task.getClass().getName());
                        } else {
                            parseTask((LinkedHashMap) task);
                        }
                    }
                    continue;
                }
                log.error("unexpected entry: "+entryl1.getKey());
            }
            return yaml;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    boolean parseTask(Object taskPar) {

        // log.info("task class: "+task.getClass().getName());
        LinkedHashMap task = (LinkedHashMap) taskPar;

        for (Object oe: task.entrySet()) {
            Map.Entry e = (Map.Entry) oe;
            log.info("task: "+e.getKey()+e.getClass().getName()+" value class: "+e.getValue().getClass());
            for (Object hostsO : (ArrayList) e.getValue()) {
                log.info(hostsO.getClass().getName());
                Map hosts = (Map) hostsO;
                // log.info("hosts : "+hosts.getClass());
                for (Object hostO: hosts.entrySet()) {
                    Map.Entry host = (Map.Entry) hostO;
                    log.info("host : "+host.getClass());
                    parseHost(host);
                }
            }
        }
        return true;
    }

    boolean parseHost(Object hPar) {
        Map.Entry host = (Map.Entry) hPar;
        // log.info("entry: "+host.getKey()+"->"+host.getValue());
        Map hostChildren = (Map) host.getValue();
        for (Object oe: hostChildren.entrySet()) {
            Map.Entry e = (Map.Entry) oe;
            log.info("entry: "+e.getKey()+"->"+e.getValue());
        }


        log.info(hPar.getClass());

//        for (Object eo: hPar.entrySet()) {
//            Map.Entry e= (Map.Entry) eo;
//            log.info("key : "+e.getKey());
//        }
        return true;
    }



    boolean parseHosts() {
        return true;
    }





    public static void main(String[] v) {
        // System.out.println(System.getProperty("user.dir"));
        WEBTablesScraperConfig cfg = new WEBTablesScraperConfig(null);
        Yaml y = cfg.read2();
        // System.out.println(y.toString());
    }

    Yaml yaml;
    String cfgFName;

    private static org.apache.log4j.Logger log = Logger.getLogger(WEBTablesScraperConfig.class);

}
