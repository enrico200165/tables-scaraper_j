package com.enrico200165.utils.html;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;

public class HTMLElement extends HTMLGenElement {

	public HTMLElement(String name) {
		super(name);
	}

	public HTMLElement(String name, String content) {
		super(name,content);
	}
	
	public void addElement(SPAN e) {
		if (stringContent.length() > 0) {
			log.log(Level.SEVERE, "mischiati due tipi di contenuto");
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

	
	private static Logger log = LogManager.getLogManager().getLogger(HTMLElement.class.getSimpleName());
}
