package com.enrico200165.utils.html;

import java.util.logging.Logger;

public class H extends HTMLElement {

	public H(int level, String text) {
		super("h"+level,text);
	}

	public static H h(int level, String class1, String class2,String text) {
		H s = new H(level,text);
		if (class1 != null && class1.length() > 0)
			s.addClass(class1);
		if (class2 != null && class2.length() > 0)
			s.addClass(class2);
		s.addHTMLContent(text);
		return s;
	}

	
	private static Logger log = Logger.getLogger(H.class.getSimpleName());
}
