package com.enrico200165.weblistscraper.page;

public class NextTablePageSelectorsABC {

	
	

	public String getTitleRegex() {
		return titleRegex;
	}
	public void setTitleRegex(String v) {
		this.titleRegex = v;
	}


	public String getUrlRegex() {
		return urlRegex;
	}
	public void setUrlRegex(String urlRegex) {
		this.urlRegex = urlRegex;
	}

	public String getIdRegex() {
		return idRegex;
	}
	public void setIdRegex(String idRegex) {
		this.idRegex = idRegex;
	}

	public String getClasseRegex() {
		return classeRegex;
	}
	public void setClasseRegex(String classeRegex) {
		this.classeRegex = classeRegex;
	}

	public String getTestoRegex() {
		return testoRegex;
	}
	public void setTestoRegex(String testoRegex) {
		this.testoRegex = testoRegex;
	}


	public String getaElementSelector() {
		return aElementSelector;
	}
	public void setaElementSelector(String aElementSelector) {
		this.aElementSelector = aElementSelector;
	}

	protected String aElementSelector;
	protected String urlRegex;
	protected String idRegex; 
	protected String classeRegex; 
	protected String testoRegex;
	protected String titleRegex;



	
}