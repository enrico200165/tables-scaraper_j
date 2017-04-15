package com.enrico200165.utils.html;

/**
 * Serve a creare TableData objects scrivendo solo una volta i settings che non
 * cambiano tai settings sono memorizzati come membri
 * 
 * @author enrico
 * 
 */
public class TableDataHelper {

	public TableDataHelper() {
		width = "";
	}

	public TableDataHelper setClass(String c) {
		if (c != null) {
			this.classe = c;
		}
		return this;
	}

	public TableData cTD(String htmlText) {
		TableData t = new TableData(htmlText);
		if (classe != null && classe.length() > 0) {
			t.addClass(classe);
		}
		if (width.length() > 0) {
			t.addAttribute("width", "" + width);
		}
		return t;
	}

	public void setWidthPx(int w) {
		width = "" + w + "px";
	}

	String classe;
	String width;
}
