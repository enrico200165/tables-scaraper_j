package com.enrico200165.utils.str_regex;

import java.util.ArrayList;
import java.util.List;

/**
 * Regex Helper
 * @author enrico
 *
 */
public class RgH {

	// adjust for java matching on all the string
	public static String j(String rgx) {
		return ".*("+rgx+").*";
	}


	static List<String> vettToArrList(String[] v) {
		ArrayList<String> al = new ArrayList<String>();
		for (String s: v) {
			al.add(s);
		}
		return al;
	}


	public static boolean matchAny(String text, String[] regexes) {
		return matchAny(text, vettToArrList(regexes));
	}
	public static boolean matchAny(String text, List<String> regexes) {
		return matchCount(text, regexes) > 0;
	}


	public static boolean matchAll(String text, String[] regexes) {
		return matchAll(text, vettToArrList(regexes));
	}
	public static boolean matchAll(String text, List<String> regexes) {
		return matchCount(text, regexes) == regexes.size() ;
	}


	static int matchCount(String text, List<String> regexes) {
	int n = 0;
		for (String r : regexes) {
			if (text.matches(r))
				n++;
		}
		return n;
	}

}
