package com.enrico200165.weblistscraper.concorsi_it;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.enrico200165.utils.str_regex.StringUtils;
import com.enrico200165.weblistscraper.tools.EntryExcludeFilter;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;

import com.enrico200165.utils.str_regex.RgH;
import com.enrico200165.weblistscraper.concorsi_it.entities.Concor;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;

public class EntryExcludeFilterConcorsiIt extends EntryExcludeFilter {

	public EntryExcludeFilterConcorsiIt(SessionManagerAbstr smPar) {
		super(smPar);
		excludePatterns = new ArrayList<Pattern>();
		buildPatterns();
	}

	@Override
	public boolean exclude(Element entry, StringBuffer annotations) {
		String htmlEntry = entry.html();
		return excludeTextual(htmlEntry, annotations);
	}

	public boolean excludeTextual(String text, StringBuffer annotations) {
		boolean exclude = false;

		text = toOneLine(text);
		text = text.toLowerCase(Locale.ITALY);

		for (Pattern p : excludePatterns) {
			Matcher m = p.matcher(text);
			// log.info("matching: " + m.toString());
			if (m.matches()) {
				exclude = true;
				for (int i = 1; i <= m.groupCount(); i++) {
					log.info("matched: " + m.group(i));
					annotations.append("<br />nmatched: <b>" + m.group(i) + "</b>");
					if (exclude && annotations.length() <= 0) log.debug("breakpoint");
				}
			}
		}
		return exclude;
	}

	String toOneLine(String s) {
		return s.replace("\n", "").replace("\r", "");
	}

	@Override
	public boolean excludeTypedEntry(Object castMe) {
		Concor conc = (Concor) castMe;
		boolean excludeIt = false;

		// if present regione must be near
		String regione = conc.getRegione();
		if (!StringUtils.nullOrEmpty(regione)) {
			excludeIt = excludeIt || !RgH.matchAny(regione.toLowerCase(),new String[]  {"abruzzo","lazio","marche"} );
			if (excludeIt) {
				conc.appendToAnn("regione: "+regione);
			}
		}

		if (excludeIt) {
			log.debug("escluso "+conc.toString());
		}
		return excludeIt;
	}

	boolean myMatch(String s, String regex) {
		boolean ret = false;
		s = toOneLine(s.toLowerCase());
		ret = s.matches(RgH.j(regex));
		return ret;
	}

	void buildPatterns() {

		// --- tipo lavoro / -----------------------------
		addPattern(RgH.j("tempo determinato"));
		addPattern(RgH.j("tempo pieno e determinato"));
		addPattern(RgH.j("bors. di studi"));
		addPattern(RgH.j("formazione lavoro"));
		addPattern(RgH.j("stage"));
		addPattern(RgH.j("assegn. di ricerca"));
		addPattern(RgH.j("dottorato di ricerca"));
		addPattern(RgH.j("professore universitario"));
		addPattern(RgH.j("ricercatore universitario"));
		addPattern(RgH.j("ricercatore a tempo"));
		addPattern(RgH.j("dirigent. medic."));

		addPattern(RgH.j("di durata [\\w]+nale"));
		
		
		
		
		// --- tipo annuncio -----------------------
		
		addPattern(RgH.j("revoca del concorso"));
		addPattern(RgH.j("annullamento del bando"));
		 
		addPattern(RgH.j("avviso di pubblicazione"));
		addPattern(RgH.j("avvis. [\\w]* di mobilit"));
		
		
		addPattern(RgH.j("approvazione degli atti"));

		addPattern(RgH.j(".omina della commissione"));

		addPattern(RgH.j("approvazione delle graduatorie"));
		addPattern(RgH.j("graduatoria dei vincitori"));
		addPattern(RgH.j("graduatoria generale di merito"));
		addPattern(RgH.j("graduatoria di merito"));
		addPattern(RgH.j("graduatoria finale"));

		addPattern(RgH.j("diario dell. prov."));

		addPattern(RgH.j("pubblicazione della graduatoria"));

		addPattern(RgH.j("sospensione della procedura "));
		addPattern(RgH.j("cancellazione da.? Registr"));
		addPattern(RgH.j("iscrizion. ne. [Rr]egistro"));
		addPattern(RgH.j("passaggio diretto .ra pubblich"));
		addPattern(RgH.j("revoca "));
		addPattern(RgH.j("di mobilit"));

		// ----------------------------------------------------
		addPattern(RgH.j("[Mm]ilitare volontario"));
		addPattern(RgH.j("[Aa]lliev. [Uu]fficial"));
		// ----------------------------------------------------
		addPattern(RgH.j("riservat[oie] ai figli"));
		addPattern(RgH.j("riservat[oie] agli orfani"));
		addPattern(RgH.j("riservat[oie] ai? dipendent."));
		addPattern(RgH.j("riservat. ai? soggett. disabil."));
		addPattern(RgH.j("riservat. esclusiv."));

		addPattern(RgH.j("riservat. a[\\w]* categ[\\w]* protett[\\w]*"));
		addPattern(RgH.j("riservato alle categorie dei volontari"));

		
	}

	void addPattern(String par) {
		String norm = "";
		String[] words = par.split(" ");

		for (int i = 0; i < words.length; i++) {
			if (words[i].matches(" +")) continue;
			
			if (i < (words.length - 1)) norm += words[i] + " +";
			else norm += words[i];
		}
//		log.info("pattern is: " + norm);

		Pattern patt = Pattern.compile(norm);
		excludePatterns.add(patt);
	}

	//	Matcher m = datePatt.matcher(dateStr);
	ArrayList<String> employerTypeRegex; // università

	ArrayList<Pattern> excludePatterns; // università

	private static org.apache.log4j.Logger log = Logger.getLogger(EntryExcludeFilterConcorsiIt.class);
}
