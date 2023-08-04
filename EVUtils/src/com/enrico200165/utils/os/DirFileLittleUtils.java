package com.enrico200165.utils.os;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;

import java.util.Map;


public class DirFileLittleUtils {


    public static String getEnvVar(String varName) {
        return getEnvVar(varName, false);
    }

    public static String getEnvVar(String varName, boolean stopOnErr) {
        String value = System.getenv().get(varName);
        if (value == null && stopOnErr) {
            log.log(Level.SEVERE, "Environment variable not found: "+varName);
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
    private static Logger log = LogManager.getLogManager().getLogger(DirFileLittleUtils.class.getSimpleName());
}
