package com.enrico200165.utils.various;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
				log.error("not existing dir: " + dir);
			}
			return false;
		}
		return f.isDirectory();
	}

	static Map<String, String> singletonEnvMap;
	private static Logger log = LogManager.getLogger(EnvMap.class.getSimpleName());
}
