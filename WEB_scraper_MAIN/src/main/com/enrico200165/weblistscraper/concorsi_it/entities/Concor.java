package main.com.enrico200165.weblistscraper.concorsi_it.entities;

import com.enrico200165.utils.html.A;
import com.enrico200165.utils.html.DIV;
import com.enrico200165.utils.html.HR;
import com.enrico200165.utils.html.Table;
import com.enrico200165.utils.net.security.HashGeneratorUtils;
import com.enrico200165.utils.str_regex.StringUtils;
import com.enrico200165.weblistscraper.entities.EntityBase;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Models the basic common fields of a project that will be stored on DB Every Web site or channel will have a different "ProspectCoreData", with
 * different fields, that will be modeled by another class, *probably* - derived by this to provide polimorphism - probably with all fields
 * transient and including an objetc of this class, to use, manually the persistence of the basic prospect (and aboid getting into the
 * complexity of the persistence of class and superclass Etc.
 * 
 * @author enrico
 *
 */

public class Concor extends EntityBase {

	public static final String IDFieldName = "infoInternURL";

	public Concor() {
		dateReported = null;
		annotations = "";

	}

	public String getAnnotations() {
		return annotations;
	}

	public void setAnnotations(String annotations) {
		this.annotations = annotations;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getTableUrl() {
		return tableUrl;
	}

	public void setTableUrl(String tableUrl) {
		this.tableUrl = tableUrl;
	}

	public Date getDateRecorded() {
		return dateRecorded;
	}

	public void setDateRecorded(Date dateRecorded) {
		this.dateRecorded = dateRecorded;
	}

	public String getInternalID() {
		return internalID;
	}

	public void setInternalID(String internalID) {
		this.internalID = internalID;
	}

	public String getSommario() {
		return sommario;
	}

	public void setSommario(String sommario) {
		this.sommario = sommario;
	}

	public String getInfoExtURL() {
		return infoExtURL;
	}

	public void setInfoExtURL(String infoExtURL) {
		this.infoExtURL = infoExtURL;
	}

	public String getOtherInfo() {
		return otherInfo;
	}

	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}

	public Date getDateReported() {
		return dateReported;
	}

	public void setDateReported(Date dateReported) {
		this.dateReported = dateReported;
	}

	public String getTipologia() {
		return tipologia;
	}

	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}

	public String getTipologiaContratto() {
		return tipologiaContratto;
	}

	public void setTipologiaContratto(String tipologiaContratto) {
		this.tipologiaContratto = tipologiaContratto;
	}

	public int getPosti() {
		return posti;
	}

	public void setPosti(int posti) {
		this.posti = posti;
	}

	public String getEnte() {
		return ente;
	}

	public void setEnte(String ente) {
		this.ente = ente;
	}

	public String getRegione() {
		return regione;
	}

	public void setRegione(String regione) {
		this.regione = regione;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public Date getScadenza() {
		return scadenza;
	}

	public void setScadenza(Date scadenza) {
		this.scadenza = scadenza;
	}

	public String getCopiaSintesi() {
		return copiaSintesi;
	}

	public void setCopiaSintesi(String copiaSintesi) {
		this.copiaSintesi = copiaSintesi;
	}

	public static String getIdfieldname() {
		return IDFieldName;
	}

	public String getTitle() {
		return title;
	}

	public String getInfoInternURL() {
		return infoInternURL;
	}

	public String getFullDescr() {
		return fullDescr;
	}

	public String getCopiaTestoCompleto() {
		return copiaTestoCompleto;
	}

	public String html() {
		String html = "";
		html = (new HR()).getHTMLMarkUp(1);

		// title
		html += (new DIV()).addHTMLContent(getTitle()).getHTMLMarkUp(1);

		html += (new DIV()).addHTMLContent(" annotations: " + getAnnotations()).getHTMLMarkUp(1);

		A a = new A(getInfoInternURL(), "descrizione interna");
		a.addAttribute("target", "_blank");
		String anc = (new DIV()).addChild(a).getHTMLMarkUp(0);
		html += anc;

		{
			// --- location		
			String location = "<b>Sede</b>: " + getRegione() + " " + getProvincia() + " " + getComune();
			html += (new DIV()).addHTMLContent(location).getHTMLMarkUp(1);
		}

		// -- copio html originale
		//		html += (new DIV()).addHTMLContent(getCopiaTestoCompleto()).getHTMLMarkUp(1);

		String table = (new Table("")).addHTMLContent(getCopiaSintesi()).getHTMLMarkUp(1);
		html += (new DIV()).addHTMLContent(table).getHTMLMarkUp(1);

		return html;
	}

	String annotations;

	// --- Indirizzo ---
	String site;
	String tableUrl;

	@Temporal(TemporalType.TIMESTAMP)
	Date dateRecorded;

	// --- Contenuti ---
	@Id
	String internalID;

	@Column(length = 4096)
	String title;

	public void setTitle(String t) {
		t = StringUtils.safeTruncate(t, 4096);
		title = t;
		internalID = HashGeneratorUtils.genSHA256NoExcp(t);
	}

	@Column(length = 4096)
	String sommario;

	@Column(length = 1024)
	String infoInternURL;

	public void setInfoInternURL(String u) {
		this.infoInternURL = StringUtils.safeTruncate(u, 1024);
	}

	public void appendToAnn(String s) {
		// per non mancare eventuali operazioni DB
		setAnnotations(getAnnotations() + s);
	}

	public void setScadenzaFromString(String scad) {
		try {
			Date result;
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ITALIAN);
			result = df.parse(scad);
			this.scadenza = result;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.error(e);
			System.exit(1);
		}
	}

	@Column(length = 512)
	String infoExtURL;

	@Column(length = 4096)
	String fullDescr;

	public void setFullDescr(String s) {
		fullDescr = StringUtils.safeTruncate(s, 4096);
	}

	String otherInfo;

	@Temporal(TemporalType.TIMESTAMP)
	Date dateReported; // a report HTML, null se non esportato

	// --- scheda di sintesi

	String tipologia;
	String tipologiaContratto;
	int posti;
	String ente;
	String regione;
	String provincia;
	String comune;

	@Temporal(TemporalType.TIMESTAMP)
	Date scadenza;

	@Column(length = 4096)
	String copiaSintesi; // copio l'html della tabela di sintesi tale e qual

	@Column(length = 4096)
	String copiaTestoCompleto;
	public void setCopiaTestoCompleto(String t) {
		copiaTestoCompleto = StringUtils.safeTruncate(t, 4096);
	}

	// --- fine scheda di sintesi ---

	private static Logger log = LogManager.getLogger(Concor.class.getSimpleName());
}