package com.enrico200165.utils.html;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


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
	
	private static Logger log = LogManager.getLogger(A.class.getSimpleName());
}
