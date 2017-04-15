package com.enrico200165.utils.html;

public class HR extends HTMLGenElement {

	public HR() {
		super("hr");
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getHTMLMarkUp(int level) {
		return "<hr />";
	}

	
}
