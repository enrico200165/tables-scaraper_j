package com.enrico200165.utils.various;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utl {

    public Utl() {
    }

    public static final int NOT_INITIALIZED_INT = -1;
    public static final String NOT_INITIALIZED_STRING = "EV-UNDEFINED";
    public static final String STRING_NOTAVAILABLE = "evna";

    public static int intFromString(String s) {
        int ret = NOT_INITIALIZED_INT;
        if (s == null || s.length() <= 0) {
            log.error("string null or empty, cannot convert to int");
            return ret;
        }
        try {
            ret = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            ret = NOT_INITIALIZED_INT;
            log.error(e.getMessage());
        }
        return ret;
    }

    public static int tokenLen(String s, boolean ignParent, boolean splitComma) {
        int len;

        Pattern parenPatt = Pattern.compile(" *\\([^)]*\\)");
        {
            Matcher parenMatcher = parenPatt.matcher(s);
            // se analizziamo le virgole dobbiamo analizzare le parentesi
            // per evitare di splittare virgole dentro una parentesi
            if (ignParent || splitComma) if (parenMatcher.lookingAt()) s = parenMatcher.replaceAll("");
        }
        if (splitComma) {
            // consideriamo la lunghezza dell'elemento maggiore
            String[] elems = s.split(" *, *");
            len = elems[0].length();
            for (String t : elems) {
                if (len < t.trim().length()) len = t.trim().length();
            }

        } else {
            // per significati come primitiva: moon, *part of the body ...
            String primitivesDelim = " *, *\\*"; // ", *\\*";
            Pattern primPattern = Pattern.compile(primitivesDelim);
            Matcher m = primPattern.matcher(s);
            if (m.find()) {
                Matcher paren = parenPatt.matcher(s);
                s = paren.replaceAll("");
                String elems[] = s.split(primitivesDelim);
                len = elems[0].length();
                for (String t : elems) {
                    if (t.trim().length() > len) len = t.trim().length();
                }
            } else {
                len = s.trim().length();
            }
        }

        return len;
    }

    public static String normalizeString(String s, String sep) {
        while (s.contains(sep + "null" + sep))
            s = s.replace(sep + "null" + sep, sep + sep);

        while (s.contains("  "))
            s = s.replace("  ", " ");

        if (!sep.equals(",")) {
            while (s.contains("," + sep))
                s = s.replace("," + sep, sep);
        }

        return s;
    }

    public static String comma2dot(String originale) {
        return originale.replace(",", ".");
    }

    public static String data(String dataCSV) {
        String d = "";
        d += dataCSV.substring(6, 10);
        d += dataCSV.substring(3, 5);
        d += dataCSV.substring(0, 2);

        return d;
    }

    static public String tStamp() {
        String s = "";

        Calendar c = Calendar.getInstance();
        s = String.format("%04d-%02d-%02d_%02d-%02d-%02d", /* 1900 + */c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE),
                c.get(Calendar.HOUR), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
        return s;
    }

    static public String todayDateStamp() {
        String s = "";

        Calendar c = Calendar.getInstance();
        s = String.format("%04d-%02d-%02d", /* 1900 + */c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DATE));
        return s;
    }

    static public String tStampCompact() {
        String s = "";
        Calendar c = Calendar.getInstance();
        s = String.format("%02d-%02d_%02d-%02d-%02d", c.get(Calendar.MONTH), c.get(Calendar.DATE), c.get(Calendar.HOUR),
                c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
        return s;
    }

    /**
     * check if dirs in env var really exist
     *
     * @param envVar
     * @return
     */
    public static boolean envVarDirsExist(String envVar) {
        String val = System.getenv(envVar);
        if (val == null || val.length() <= 0) {
            log.error("not existing env var: " + envVar);
            return false;
        }

        if (!val.matches(".*\\\\.*")) {
            // perchè?
            return true;
        }

        boolean ok = true;
        String[] dirs = val.split(";");
        for (String s : dirs) {
            if (s.endsWith("\\*")) s = s.substring(0, s.length() - 2);
            File dir = new File(s);
            if (!dir.exists()) {
                log.error("in env: " + envVar + "=" + val + "\n nonexisting dir: '" + s + "'");
                ok = false;
                return false;
            }
            if (!dir.isDirectory()) {
                if (!dir.isFile() && !dir.getName().endsWith(".jar") && !dir.getName().endsWith(".zip")) {
                    log.error("in env: " + envVar + "=" + val + "\nexisting but not a dir: " + s);
                    ok = false;
                } else {
                    log.debug("just to set a breakpoint");
                }
            }
        }

        return ok;
    }

    public static boolean checkEnvVarPaths() {
        boolean ok = true;
        ArrayList<String> envs = getEnvDirs();
        for (String envName : envs) {
            if (!ENVS_NOT_DIRS.contains(envName)) {
                if (!envVarDirsExist(envName)) {
                    log.error("variable: " + envName);
                    ok = false;
                }
            }
        }
        return ok;
    }

    public static ArrayList<String> getEnvDirs() {
        ArrayList<String> dirs = new ArrayList<String>();

        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            if (env.get(envName).matches(".*\\\\.*")) dirs.add(envName);
        }
        return dirs;
    }


    public static String getVMOptions() {
        String ret = "";
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> arguments = runtimeMxBean.getInputArguments();
        for (String option :arguments){
            ret += option +"\n";
        }
        return ret;
    }


    public static boolean checks() {
        boolean ret = true;


        System.out.println(System.getProperty("java.home"));
        System.out.println(System.getProperty("user.dir"));


        ret = duplInSingleEnvPath("libmecab.dll") && ret;
        ret = checkEnvVarPaths() && ret;
        ret = checkDuplFilesInEnvPaths(/* filename regex */"libmecab.dll") && ret;
        return ret;
    }

    public static boolean fileExistInEnvVarDirs(String fName, String envVar) {
        String val = System.getenv(envVar);
        if (val == null || val.length() <= 0) {
            log.error("not existing env var: " + envVar);
            return false;
        }
        return fileExistInDirsString(fName, val);
    }

    public static boolean fileExistInJavaProperty(String fName, String prop) {

        String val = System.getProperty(prop);
        if (val == null || val.length() <= 0) {
            log.error("not existing property: " + prop);
            return false;
        }
        return fileExistInDirsString(fName, val);
    }

    public static boolean fileExistInDirsString(String fName, String str) {
        boolean ok = false;
        String[] dirs = str.split(";");
        for (String s : dirs) {
            File curFile = new File(s + "\\" + fName);
            if (!curFile.exists()) {
                // log.info("in env: " + str + " file exists: " +
                // curFile.getAbsolutePath());
                return true;
            }
        }
        return ok;
    }

    public static boolean fileExistInPath(String fName) {
        return fileExistInEnvVarDirs(fName, "path");
    }

    public static boolean fileExists(String filePathName) {
        File f = new File(filePathName);
        if (!f.exists()) return false;
        return !f.isDirectory();
    }

    public static boolean checkDuplFilesInEnvPaths(String fileRegex) {

        Set<String> dirExamined = new HashSet<String>();
        boolean ok = true;
        Map<String, ArrayList<String>> filesAndDirs = new Hashtable<String, ArrayList<String>>();

        ArrayList<String> envs = getEnvDirs();
        for (String envVar : envs) {
            String[] dirsInVar = System.getenv(envVar).split(";");
            for (String dirStr : dirsInVar) {
                // singola directory o file
                File dir = new File(dirStr);
                if (!dir.isDirectory()) {
                    continue;
                } else {
                    if (!dirExamined.contains(dirStr))
                        dirExamined.add(dirStr);
                    else
                        continue;
                }
                String[] dirsList = dir.list();
                // non sicuro questo check sia corretto, messo in fretta senza ragionare
                if (dirsList == null) continue;
                for (String child : dirsList) {

                    if (fileRegex != null && !child.matches(".*" + fileRegex + ".*")) {
                        continue;
                    }

                    String pathname = dirStr + "\\\\" + child;
                    File curFile = new File(pathname);
                    if (!curFile.exists()) {
                        log.error("");
                        return false;
                    }
                    if (!curFile.isFile()) {
                        continue;
                    }
                    ArrayList<String> dirs;
                    if (!filesAndDirs.containsKey(child)) {
                        dirs = new ArrayList<String>();
                        filesAndDirs.put(child, dirs);
                    } else {
                        dirs = filesAndDirs.get(child);
                    }
                    dirs.add(dirStr);
                    if (dirs.size() > 1) {
                        ok = false;
                        log.info("file: " + child + " duplicated " + dirs.size() + " present in dirs:");
                        int i = 0;
                        for (String d : dirs) {
                            log.info(++i + "> " + d);
                            log.info("del " + d + "\\" + child);
                        }
                    }
                }
            }
        }

        return ok;
    }

    /**
     * Per tutte le variabili di ambiente che sembrano paths Controlla duplicati nelle directories di una singola variabile di ambiente
     *
     * @return
     */

    public static boolean duplInSingleEnvPath(String fileRegex) {

        boolean ok = true;

        ArrayList<String> envs = getEnvDirs();
        for (String envVar : envs) {
            String[] dirsInVar = System.getenv(envVar).split(";");
            for (String dirStr : dirsInVar) {
                // singola directory o file
                File dir = new File(dirStr);
                if (!dir.isDirectory()) {
                    continue;
                }

                Map<String, ArrayList<String>> filesAndDirs = new Hashtable<String, ArrayList<String>>();
                String[] dirsList = dir.list();
                if (dirsList == null) continue;
                for (String child : dirsList) {

                    if (fileRegex != null && !child.matches(".*" + fileRegex + ".*")) {
                        continue;
                    }

                    String pathname = dirStr + "\\\\" + child;
                    File curFile = new File(pathname);
                    if (!curFile.exists()) {
                        log.error("");
                        return false;
                    }
                    if (!curFile.isFile()) {
                        continue;
                    }
                    ArrayList<String> dirs;
                    if (!filesAndDirs.containsKey(child)) {
                        dirs = new ArrayList<String>();
                        filesAndDirs.put(child, dirs);
                    } else {
                        dirs = filesAndDirs.get(child);
                    }
                    dirs.add(dirStr);
                    if (dirs.size() > 1) {
                        ok = false;
                        log.info("file: " + child + " duplicated " + dirs.size() + " present in dirs:");
                        int i = 0;
                        for (String d : dirs) {
                            log.info(++i + "> " + d);
                        }
                    }
                }
                filesAndDirs = null;
            }
        }

        return ok;
    }

    private static final Set<String> ENVS_NOT_DIRS = Collections.unmodifiableSet(new HashSet<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
            add("LOGONSERVER");
            add("ComSpec");
            add("HOMEPATH");
            add("=::");
            add("IBM_JAVA_COMMAND_LINE");
        }
    });

    public static final String ENCODING_UTF8 = "UTF8";

    private static Logger log = LogManager.getLogger(Utl.class.getName());
}
