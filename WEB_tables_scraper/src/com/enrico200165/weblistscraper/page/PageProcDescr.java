package com.enrico200165.weblistscraper.page;

import com.enrico200165.utils.str_regex.*;
import com.enrico200165.weblistscraper.common.*;
import com.enrico200165.weblistscraper.configs.HostConfigABC;
import com.enrico200165.weblistscraper.configs.PageConfigABC;
import com.enrico200165.weblistscraper.tools.*;
import org.apache.log4j.Logger;

import java.net.*;
import java.util.*;

/**
 * Lavora usando PageProcessor e PageConfig, forse dovrebbero essere fuse (risulterebbe entità complessa?)
 * A grandi linee adesso la situazione è questa:
 * Questa classe decrive:
 * - quale pagina processare (URL Etc.)
 * - come
 * Page config descrive quali sono i markers dentro la pagina (per guidare il parsing di Jsoup)
 */
public class PageProcDescr {

	public PageProcDescr(HostConfigABC hostConfigPar, PageConfigABC tPar, PageProcessorAbstr processor, String url,
						 WebPageAction actionPar) {
		super();

		this.hostConfig = hostConfigPar;
		this.processor = processor;
		
		this.setUri(url);
		this.action = actionPar;
		httpCallDelay = 3;
		pageConfig = tPar;
	}

	public PageProcDescr( PageConfigABC tPar, PageProcessorAbstr processor, TableScraperABC ts, String tcPar, String url,
						  WebPageAction actionPar, int delay) {
		this(/*hostConfig*/null, tPar, processor,  url, actionPar);
		this.httpCallDelay = delay;
	}



	public PageConfigABC getPageConfig() {
		return pageConfig;
	}

	public void settCfg(PageConfigABC par) {
		this.pageConfig= par;
	}

	public PageProcessorAbstr getProcessor() {
		return processor;
	}

	public void setProcessor(PageProcessorAbstr processor) {
		this.processor = processor;
	}

	public boolean isAuthenticate() {
		return authenticate;
	}

	public void setAuthenticate(boolean authenticate) {
		this.authenticate = authenticate;
	}

	public String getURL() {
		String ret = WEBUtils.URLFromURI(this.uri);
		return ret;
	}

	public void setRelUri(String relUri) {
		this.relUri = relUri;
	}

	public WebPageAction getAction() {
		return action;
	}

	public void setAction(WebPageAction action) {
		this.action = action;
	}

	public int getHttpCallDelay() {
		return httpCallDelay;
	}

	public void setHttpCallDelay(int httpCallDelay) {
		this.httpCallDelay = httpCallDelay;
	}

	public static org.apache.log4j.Logger getLog() {
		return log;
	}

	public static void setLog(org.apache.log4j.Logger log) {
		PageProcDescr.log = log;
	}

	PageProcDescr cloneIt(String URL, WebPageAction actionPar, String tcPar) {
		PageProcDescr c = new PageProcDescr(this.hostConfig, this.pageConfig, processor, URL, actionPar);
		c.setUri(URL);
		return c;
	}

	public void setUri(String relUrl) {
		if (uri != null) {
			log.error("attempt to set URI");
			System.exit(1);
		}

		try {
			uri = new URI(relUrl);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		// tutto questo che segue va eliminato

		if (!WEBUtils.isRelativeURL(relUrl)) {
			relUrl = WEBUtils.makeUrlRelative(relUrl);
		}
		this.relUri = relUrl;
	}




	public int getHTTPCallDelay() {
		return this.httpCallDelay;
	}

	public void setHTTPCallDelay(int val) {
		this.httpCallDelay = val;
	}

	public void setTableScraper(TableScraperABC s) {
		tableScraper = s;
	}

	public TableScraperABC getTableScraper() {
		return tableScraper;
	}

	PageConfigABC pageConfig;
	HostConfigABC hostConfig;


	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}


	public List<NameValuePairString> getFormFields() {
		return formFields;
	}
	public String getFormSelector() {
		return formSelector;
	}
	public FormManagerABC getFormManager() {
		return formManager;
	}
	public String getLoginFormAction() {
		return loginFormAction;
	}
	public InvocationBuilderWrapper getIbw() {
		return ibw;
	}


	public void setFormsParams(List<NameValuePairString> formFields,String formSelector, String loginFormAction,
							   FormManagerABC formManager, InvocationBuilderWrapper ibw) {
        this.formFields = formFields;
        this.formSelector = formSelector;
        this.loginFormAction = loginFormAction;
		this.formManager = formManager;
        this.ibw = ibw;
	}


	URI uri;

	EntryProcessorABC ep;

	PageProcessorAbstr processor;
	TableScraperABC tableScraper;

	boolean authenticate;
	String relUri;
	WebPageAction action;
	int httpCallDelay;



	// --- for forms
	List<NameValuePairString> formFields = null;
	String formSelector;
	String loginFormAction;
	FormManagerABC formManager;
	InvocationBuilderWrapper ibw;


	private static org.apache.log4j.Logger log = Logger.getLogger(PageProcDescr.class);
}
