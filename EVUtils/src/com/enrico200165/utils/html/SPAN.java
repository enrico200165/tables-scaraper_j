package com.enrico200165.utils.html;

public class SPAN extends HTMLGenElement {

	public SPAN(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public static SPAN buildElement(String name, String class1, String class2,String value) {
		SPAN s = new SPAN(name);
		if (class1 != null && class1.length() > 0)
			s.addClass(class1);
		if (class2 != null && class2.length() > 0)
			s.addClass(class2);
		s.addHTMLContent(value);
		return s;
	}

	
}
