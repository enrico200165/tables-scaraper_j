package com.enrico200165.weblistscraper.concorsi_it.main;

import com.enrico200165.utils.html.DIV;
import com.enrico200165.utils.html.H;
import com.enrico200165.utils.html.HTMLDisplayInBrowser;
import com.enrico200165.utils.various.Utl;
import com.enrico200165.weblistscraper.concorsi_it.main.entities.Concor;
import com.enrico200165.weblistscraper.concorsi_it.main.entities.ConcorsiDB;
import com.enrico200165.weblistscraper.configs.SessionLimitsBase;
import com.enrico200165.weblistscraper.session.SessionManager;
import com.enrico200165.weblistscraper.tools.ClientWrapper;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;

import java.util.ArrayList;
import java.util.List;

public class SessionManagerConcorsi extends SessionManager {

	public SessionManagerConcorsi(ClientWrapper cwPar , SessionLimitsBase sessionLimits) {
		super(sessionLimits, cwPar);
		concEsclusi = new ArrayList<Concor>();
		included = 0;
		excluded = 0;
	}

	// --- inizialize session-type specific resources ---
	@Override
	public void initSession() {
		log.log( Level.FINE, "SessionManagerConcorsi.intiSession");
		getDBMgr();
		included = 0;
		excluded = 0;
	}

		
	// --- inizialize session-type specific resources --- 
	@Override
	public void finalizeSession() {
		log.log( Level.FINE, "SessionManagerConcorsi.finalizeSession");

		List<Concor> concorsiIncluded = getDBMgr().findConcorNotReported();

		String html = "";
		html += new DIV("prodotto: "+Utl.tStamp()).getHTMLMarkUp(0);
		html+= new DIV("esaminati: "+getRawEntriesEsaminate()).getHTMLMarkUp(0);
		html+= new DIV("inclusi: "+getIncluded()).getHTMLMarkUp(0);
		html+= new DIV("exclusi: "+getExcluded()).getHTMLMarkUp(0);
		
		
		int i = 0;
		for (Concor c : concorsiIncluded) {
			i++;
			html += "<hr />"+i+"<br />"+c.html()+"<br />";					
		}
		
		
		concDB.updateReportedDate(concorsiIncluded);
		
		html+= new H(1,"esclusi").getHTMLMarkUp(1);

		for (Concor c : concEsclusi) {
			i++;
			html += "<hr />"+i+"<br />"+c.html()+"<br />";					
		}
		
		log.info("esaminate raw: "+getRawEntriesEsaminate());
		
		String dir = "";
		//String filename = "concorsi"+Utl.todayDateStamp();
		String filename = "concorsi"+Utl.todayDateStamp()+".html";		
		HTMLDisplayInBrowser.writeOut(dir+filename, html, true);
		HTMLDisplayInBrowser.displayLocFileBrowser(dir+filename);
	}



	protected ConcorsiDB getDBMgr() {
		if (concDB == null) {
			concDB = new ConcorsiDB();
		}
		return concDB;
	}

	
	void addToExcluded(Concor c) {
		concEsclusi.add(c);
	}
	
	
	public int getExcluded() {
		return excluded;
	}


	public void incrExcluded() {
		this.excluded++;
	}


	public int getIncluded() {
		return included;
	}


	public void incrIncluded() {
		this.included++;
	}


	
	ArrayList<Concor> concEsclusi;
	static ConcorsiDB concDB = null;
	int included;
	int excluded;
	
	private static Logger log = LogManager.getLogManager().getLogger(SessionManagerConcorsi.class.getSimpleName());
}
