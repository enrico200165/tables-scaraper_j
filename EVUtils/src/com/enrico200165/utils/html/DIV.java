package com.enrico200165.utils.html;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;

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
			log.log(Level.SEVERE, "mischiati due tipi di contenuto");
		}
		this.subElements.add(e);
	}
	private static Logger log = LogManager.getLogManager().getLogger(DIV.class.getName());
}
