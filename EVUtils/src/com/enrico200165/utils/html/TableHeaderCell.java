package com.enrico200165.utils.html;

public class TableHeaderCell extends HTMLGenElement {

	public TableHeaderCell(String htmlTxt) {
		super("th");
		this.addHTMLContent(htmlTxt);
	}

	public TableHeaderCell setWidthPx(int px) {
		addAttribute("width", ""+px+"px");
		return this;
	}

	public TableHeaderCell setWidthPx(double px) {
		addAttribute("width", ""+(int)px+"px");
		return this;
	}

	
}
