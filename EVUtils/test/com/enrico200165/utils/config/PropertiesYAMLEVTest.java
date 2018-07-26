package com.enrico200165.utils.config;

import com.enrico200165.utils.various.Utl;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        "    dummy_test:"+" dummy_val\n"+
        "    just_testing:\n"+
        "        int_value:"+" 99\n"+
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

        log.info(Utl.tStamp()+"start basic reading");

        String content = null;
        try  {
            content = new String(Files.readAllBytes(Paths.get(fpath)));
            //log.info(content);

        } catch (IOException e) {
            e.printStackTrace();
        }



        Yaml yaml = new Yaml();
        // root element, tasks
        Map<String,Object> prop = (Map<String,Object>) yaml.load(fcontent);

        // just studying types inside it
//        for (Type t: data.getClass().getTypeParameters()) {
//            System.out.println(t.getTypeName());
//            if (t instanceof ParameterizedType) {
//                Type elementType = ((ParameterizedType) t).getActualTypeArguments()[0];
//            }
//        }

        try {
            log.info(PropertiesYAMLEV.getStr(prop,"dummy_test"));
            for (String k : prop.keySet()) {

                Map<String,Object> entry = (Map<String,Object>) prop.get(k);
                log.info("analyzing key"+k);
                if (PropertiesYAMLEV.getType(entry) == "task_list") {
                    // todo EV# implement
                    log.info("found task list object, should implement parsing it");
                } else if (PropertiesYAMLEV.getType(entry) == "tasks") {

                }
            }
        } catch (Exception_YAMLCfg_WrongType e) {
            log.error(e);
        }

        log.info("Completed yaml file parsing");




    }



    @Test public void testJacksonYAML() {

        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        System.out.flush();
        PropertiesYAMLEV p = new PropertiesYAMLEV("user.yaml");
        //p.properties();

    }


    static String fcontent = "";
    static  String fpath = "test.yaml";

    private static org.apache.log4j.Logger log = Logger.getLogger(PropertiesYAMLEVTest.class);

}
