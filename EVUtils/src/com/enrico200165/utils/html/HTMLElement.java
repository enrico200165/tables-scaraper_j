package com.enrico200165.utils.html;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HTMLElement extends HTMLGenElement {

	public HTMLElement(String name) {
		super(name);
	}

	public HTMLElement(String name, String content) {
		super(name,content);
	}
	
	public void addElement(SPAN e) {
		if (stringContent.length() > 0) {
			log.error("mischiati due tipi di contenuto");
			System.exit(1);
		}
		this.subElements.add(e);
	}
	
	public static HTMLElement buildElement(String name, String class1, String class2,String value) {
		HTMLElement s = new HTMLElement(name);
		if (class1 != null && class1.length() > 0)
			s.addClass(class1);
		if (class2 != null && class2.length() > 0)
			s.addClass(class2);
		s.addHTMLContent(value);
		return s;
	}

	
	private static Logger log = LogManager.getLogger(HTMLElement.class.getSimpleName());
}
