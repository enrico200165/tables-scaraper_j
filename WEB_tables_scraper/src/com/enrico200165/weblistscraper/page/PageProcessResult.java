package com.enrico200165.weblistscraper.page;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.enrico200165.weblistscraper.tools.Result;

public class PageProcessResult {
	PageProcessResult() {
		result = new Result();
		this.pPDescr = null;
	}

	/*
	 * if the page has generated another, ex. "next" link in a table
	 * then the field is not null
	 */
	public boolean hasNexPage() {
		return pPDescr != null;
	}

	public PageProcDescr getpPDescr() {
		return pPDescr;
	}

	public void setpPDescr(PageProcDescr pPDescr) {
		this.pPDescr = pPDescr;
	}


	public Result result;
	PageProcDescr pPDescr;

	private static Logger log = LogManager.getLogger(PageProcessor.class.getSimpleName());
}
