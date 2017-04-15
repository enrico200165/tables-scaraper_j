package com.enrico200165.weblistscraper.configs.site_italianpenpals;

import com.enrico200165.weblistscraper.page.NextTablePageSelectorsABC;

/**
 * Contiene parametri per identificare il link alla prossima "pagina" di dati di una tabella
 * @author enrico
 *
 */
public class NextTablePageSelectorsItalianPenPals extends NextTablePageSelectorsABC {
	
	public NextTablePageSelectorsItalianPenPals() {
		super();
		this.urlRegex = ".*page=.*";
		this.idRegex = null;
		this.classeRegex = null;
		this.testoRegex = ".*next.*";
	}
}
