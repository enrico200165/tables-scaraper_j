package com.enrico200165.utils.os;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;


public class DirFileLittleUtils {


    public static String getEnvVar(String varName) {
        return getEnvVar(varName, false);
    }

    public static String getEnvVar(String varName, boolean stopOnErr) {
        String value = System.getenv().get(varName);
        if (value == null && stopOnErr) {
            log.error("Environment variable not found: "+varName);
            System.exit(-1);
        }
        return value;
    }

    public static Map<String, String> getEnv() {
        if (envVars == null) {
            envVars = System.getenv();
        }
        return envVars;
    }

    static Map<String, String> envVars = null;
    private static Logger log = LogManager.getLogger(DirFileLittleUtils.class.getSimpleName());
}
