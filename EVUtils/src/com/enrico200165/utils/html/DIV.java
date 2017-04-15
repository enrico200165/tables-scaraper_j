package com.enrico200165.utils.html;

import org.apache.log4j.Logger;

public class DIV extends HTMLGenElement {

	public DIV() {
		super("div");
		// TODO Auto-generated constructor stub
	}

	public DIV(String innerHTML) {
		super("div");
		addHTMLContent(innerHTML);
	}

	
	
	public void addElement(HTMLElement e) {
		if (stringContent.length() > 0) {
			log.error("mischiati due tipi di contenuto");
		}
		this.subElements.add(e);
	}
	private static org.apache.log4j.Logger log = Logger.getLogger(DIV.class);
}
