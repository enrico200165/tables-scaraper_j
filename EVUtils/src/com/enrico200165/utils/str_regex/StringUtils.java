package com.enrico200165.utils.str_regex;

import com.enrico200165.utils.various.Utl;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;



import java.util.ArrayList;


public class StringUtils {
	public static String escapeForDelimited(String s, String sep, String replacement) {
		if (s != null) {
			if (replacement != null && replacement.length() > 0) {
				s = s.replaceAll(sep, replacement);
			}
		} else {
			s = "";
		}
		return s;
	}


	static public String dQuoteIt(String s) {
		return "\""+s+"\"";
	}

	public static boolean nullOrEmpty(String s) {
		return s == null || s.length() <= 0;
	}


	public static String safeTruncate(String s, int maxChars) {
		if (s == null) {
			log.log(Level.SEVERE, "setting null or wrong length string, ESCO");
			System.exit(1);
			return null;
		}
		if (s.length() >= maxChars) {
			s = s.substring(0, maxChars-1);
		}
		return s;
	}


	public static boolean safeEquals(String s1, String s2) {
		boolean ret = false;

		if (s1 == null) {
			if (s2 == null) {
				return true;
			} else {
				return false;
			}
		} else {
			if (s2 == null) {
				return false;
			} else {
				return s1.equals(s2);
			}
		}

	}




	public static boolean str2int(String s, Integer i) {
		i = Utl.NOT_INITIALIZED_INT;
		if (s == null || s.length() <= 0) {
			log.log(Level.SEVERE, "string null or empty, cannot convert to int");
			return false;
		}
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			i = Utl.NOT_INITIALIZED_INT;
			log.log(Level.SEVERE, "errore parsing string \"" + s + "\" into int ", e.toString());
			return false;
		}
		return true;
	}

	public static int intFromStringAt1(String s) {
		int ret = Utl.NOT_INITIALIZED_INT;
		if (s == null || s.length() < 2) {
			log.log(Level.SEVERE, "string null or too short");
			return ret;
		}
		try {
			ret = Integer.parseInt(s.substring(1));
		} catch (NumberFormatException e) {
			ret = Utl.NOT_INITIALIZED_INT;
			log.log(Level.SEVERE, e.getMessage());
		}
		return ret;
	}

	
	public static String arrayListToString(ArrayList<String> al, String delimiter) {
		String s = "";
		if (al == null) {
			log.log(Level.SEVERE, "");
			System.exit(-1);
		}
		boolean first = true;
		for (String e : al) {
			if (first) {
				first = false;
			} else {
				s += delimiter;
			}
			s += e;
		}
		return s;
	}
	
	
	private static Logger log = LogManager.getLogManager().getLogger(StringUtils.class.getSimpleName());
}
