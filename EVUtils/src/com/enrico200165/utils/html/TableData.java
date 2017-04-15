package com.enrico200165.utils.html;

public class TableData extends HTMLGenElement {

	public TableData(String htmlTxt) {
		super("td");
		this.addHTMLContent(htmlTxt);
	}

}
