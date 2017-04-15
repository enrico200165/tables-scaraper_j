package com.enrico200165.jsoup.quickcheck;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {
	public static void main(String[] args) {

		String html0 = "" + "<table><tbody>" + "<tr class=\"alt1\"> " + "<td><a title=\"Ente: REGIONE EMILIA ROMAGNA"
				+ "Titolo: AVVISO PUBBLICO PER LA COSTITUZIONE "
				+ "Posti: \" href=\"/ente/21172-regione-emilia-romagna.html\">REGIONE EMILIA ROMAGNA1</a></td> "
				+ "<td>BOLOGNA</td> <td><a title=\"Ente: REGIONE EMILIA ROMAGNA2"
				+ "Titolo: AVPER IL CONFERIMENTO DI INCARICHI PROFESSIONALI DI LAVORO AUTONOMO"
				+ "Posti: \" href=\"/scheda/139028-avviso-pubblico-per-la-costituzione-di-un-elenco"
				+ "-di-esperti-in-gestione-e-rendicontazione-progetti-europei-ed-internazionali-per-il-conferimento-di-incarichi-professionali-di-lavoro-autonomo.html\">AVVISO</a></td>"
				+ " <td align=\"right\">27653</td>  </tr>" + "</tbody></table>";

		String html1 = "<table><tr><td>pippo</td></tr></table>";

		Document doc = Jsoup.parse(html0);

//		Elements elements = doc.select("tr>td>a:eq(0)");

		Elements elements = doc.select("tr>td:eq(0)>a");
		
		
		
		for (Element e : elements) {
			System.out.println(" > "+e.id() + e.text());
		}

	}
}
