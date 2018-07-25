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
import java.util.Map;


public class PropertiesYAMLEVTest {


    @AfterClass
    public static void tearDownAfterClass() {
    }


    @Before
    public void setUp() throws Exception {
        fcontent +=
        "tasks:\n"+
        "    type:"+" task_list\n"+
        "    concorsi:\n"+
        "        type:"+" task\n"+
        "        host:\n"+
        "            baseHostURI: http://concorsi.it"+"\n"+
        "            login:\n"+
        "                type: login"+"\n"+
        "                loginFormName: filenane"+"\n"+
        "                loginFormURL: /loginFormURL/hghgh"+"\n"+
        "                userFieldName: filenane"+"\n"+
        "                userNameValue: filenane"+"\n"+
        "                passwordFieldName: filenane"+"\n"+
        "                passwordValue: filenane"+"\n"+
        "            tables:\n"+
        "               type: tables\n"+
        "               list_values:\n"+
        "                   concorsi:\n"+
        "                       type: table"+"\n"+
        "                       tableUrl: concorsi\n"+
        "                       selector: myselector\n"+
        "                       htmlEntryIncl: [\"\",\"\"]\n"+
        "                       htmlEntryExcl: [\"\",\"\"]\n"+
        "                   concorsi2:\n"+
        "                       type2: table"+"\n"+
        "                       tableUrl: /concorsi\n"+
        "                       selector: myselector\n"+
        "                       htmlEntryIncl: [\"\",\"\"]"+"\n"+
        "                       htmlEntryExcl: [\"\",\"\"]"+"\n"+
        "";

        log.info("writing test config file: "+fpath+ " in: "+System.getProperty("user.dir"));
        PrintWriter pw = new PrintWriter(fpath);
        pw.println(fcontent);
        pw.flush();
        pw.close();
        return;
    }

    @After
    public void tearDown() {
    }

    @Test public void learnIt() {

        InputStream input = null;
        try {
            input = new FileInputStream(new File(fpath));
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

        log.info("yaml text:\n"+fcontent);
        InputStream input = null;
        try {
            input = new FileInputStream(new File(fpath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Yaml yaml = new Yaml();
        // log.info(yaml.load(input));
        Map<String,Object> prop = (Map<String,Object>) yaml.load(fcontent);
        for (String k : prop.keySet()) {
            log.info(k);
        }

        log.info("Completed yaml file parsing");
    }


    @Test public void testJacksonYAML() {

        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        System.out.flush();
        PropertiesYAMLEV p = new PropertiesYAMLEV("user.yaml");
        p.readProperties();

    }


    static String fcontent = "";
    static  String fpath = "test.yaml";;

    private static org.apache.log4j.Logger log = Logger.getLogger(PropertiesYAMLEVTest.class);

}
