package com.enrico200165.weblistscraper.configs.site_japanguide;

import com.enrico200165.weblistscraper.page.NextTablePageSelectorsABC;

/**
 * Contiene parametri per identificare il link alla prossima "pagina" di dati di una tabella
 * @author enrico
 *
 */
public class NextTablePageSelectorsJapanGuide extends NextTablePageSelectorsABC {
	
	public NextTablePageSelectorsJapanGuide() {
		super();
		this.aElementSelector = "#section_mainContent > center  > table > tbody > tr > td:nth-child(3) > font > a";

//		this.urlRegex = "http://www.japan-guide.com/local/\\?aCAT=.*aGENDER.*aAGE1.*aAGE2.*aINTEREST.*aLANGUAGE1.*aLANGUAGE2.*aJOB.*aNATIONALITY.*aSUBCAT.*aMES=submit.*aPAGE=[0-9]+";
		this.urlRegex = null; // "http://www.japan-guide.com/local/\\?aCAT=.*aPAGE=[0-9]+";
		this.idRegex = null;
		this.classeRegex = null;
		this.testoRegex = "next";
	}
}
