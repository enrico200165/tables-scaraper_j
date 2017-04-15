package com.enrico200165.utils.html;

public class BR extends HTMLGenElement {

	public BR() {
		super("br");
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getHTMLMarkUp(int level) {
		return "<br />";
	}

	
}
