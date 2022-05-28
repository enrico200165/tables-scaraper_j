package com.enrico200165.weblistscraper.concorsi_it;

import com.enrico200165.weblistscraper.page.NextTablePageSelectorsABC;

/**
 * Contiene parametri per identificare il link alla prossima "pagina" di dati di una tabella
 * @author enrico
 *
 */
public class NextTablePageSelectorsConcorsiIt extends NextTablePageSelectorsABC {
	
	public NextTablePageSelectorsConcorsiIt() {
		super();
		
		
		//#paginazione2 > li:nth-child(3) > a:nth-child(2)
		this.titleRegex = "Pagina Successiva";
		this.urlRegex = ".*/regione/lazio/page/.*";
		this.idRegex = null;
		this.classeRegex = null;
		this.testoRegex = null;
	}
}
