package com.enrico200165.utils.various;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;

import java.io.File;
import java.util.Map;
public class EnvMap {
	EnvMap() {
	}

	public static Map<String, String> getEnvMap() {
		if (singletonEnvMap == null) {
			singletonEnvMap = System.getenv();
		}
		return singletonEnvMap;
	}

	public static String getEnv(String var) {
		return getEnvMap().get(var);
	}

	
	
	public static boolean envDirExists(String dir, boolean reportError) {
		if (!getEnvMap().containsKey(dir)) {
			return false;
		}
		File f = new File(dir);
		if (!f.exists()) {
			if (reportError) {
				log.log(Level.SEVERE, "not existing dir: " + dir);
			}
			return false;
		}
		return f.isDirectory();
	}

	static Map<String, String> singletonEnvMap;
	static Logger log=Logger.getLogger(EnvMap.class.getSimpleName());
}
