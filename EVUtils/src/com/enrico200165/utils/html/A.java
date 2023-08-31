package com.enrico200165.utils.html;

import java.util.logging.Logger;
import java.util.logging.LogManager;


public class A extends HTMLGenElement {

	public A(String u, String d) {
		super("a");
		this.url = u;
		this.description = d;
		
		addAttribute("href", url);
		addHTMLContent(d);
	}


	
	String url;
	String description;
	
	static Logger log=Logger.getLogger(A.class.getSimpleName());
}
