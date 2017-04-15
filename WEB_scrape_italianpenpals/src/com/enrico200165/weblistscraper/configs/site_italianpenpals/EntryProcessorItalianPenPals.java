package com.enrico200165.weblistscraper.configs.site_italianpenpals;

import com.enrico200165.utils.str_regex.*;
import com.enrico200165.weblistscraper.common.*;
import com.enrico200165.weblistscraper.configs.PageConfigABC;
import com.enrico200165.weblistscraper.marketingDB.Contact;
import com.enrico200165.weblistscraper.marketingDB.ProspectCoreData;
import com.enrico200165.weblistscraper.page.EntryProcessorCommunity;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import com.enrico200165.weblistscraper.tools.*;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntryProcessorItalianPenPals extends EntryProcessorCommunity {

	public EntryProcessorItalianPenPals(SessionManagerAbstr smPar, ClientWrapper cwPar,
										PageConfigABC tCfgpar) {
		super(smPar,tCfgpar);
		patternDigits = Pattern.compile("[^0-9]*([0-9]*).*");
		if (this.cw == null) 
			log.info("this.cw == null");
	}

	/**
	 * Parses ad JSoup entry to fill a prospect object
	 */
	@Override
	protected Result FillEntry(Element entry, ProspectCoreData prospectw) {
		Result res = new Result();
		if ((res = WEBUtils.text(entry, "td a.username")).isOk()) {
			prospectw.setUserID(res.getRetStr());
			// --- estraggo e seguo il link alla pagina del profileo ----
			String profilePageURI = entry.select("td a.username").attr("href");
			log.trace("profile url: " + profilePageURI);
			ResponseWrapper rw = cw.simpleGET(profilePageURI, 0);

			// --- parse entry content
			Document doc = Jsoup.parse(rw.getResponseHTML());
			// Element entryContent = doc.select(".content .l-edge").get(0);
			Element entryContent = doc.select(".panel-flexible-inside.panels-flexible-user_layout_verticale_00-inside").get(0);
			// qui dovrei controllare/gestire fallimento 

			// --- fields ---
			if ((res = WEBUtils.text(entryContent, ".pane-user-name")).isOk()) {
				prospectw.setUserID(res.getRetStr());
				log.debug("name: " + prospectw.getUserID());
			}
			if ((res = WEBUtils.text(entryContent, ".pane-user-field-self-introduction-short", false)).isOk()) {
				prospectw.setProfile(res.getRetStr());
				log.debug("self introduction short: " + prospectw.getProfile());
			}
			if ((res = WEBUtils.text(entryContent, ".pane-user-field-userlanguage", false)).isOk()) {
				prospectw.setLanguageLearning(res.getRetStr());
				log.debug("userlanguage: " + prospectw.getLanguageLearning());
			}
			if ((res = WEBUtils.text(entryContent, ".pane-user-field-country", false)).isOk()) {
				prospectw.setMotherCountry(res.getRetStr());
				log.debug("pane-user-field-country: " + prospectw.getMotherCountry());
			}
			if ((res = WEBUtils.text(entryContent, ".pane-user-field-age", false)).isOk()) {
				Matcher matcher = patternDigits.matcher(res.getRetStr());
				if (matcher.matches()) {
					String m = matcher.group(1);
				}
				int age = Integer.parseInt(matcher.group(1));
				prospectw.setAge(age);
				log.debug("pane-user-field-age: " + prospectw.getAge());
			}

			// URL to the post message
			Elements prmsgChunk = entryContent.select("a.privatemsg-send-link");
			if (prmsgChunk != null && prmsgChunk.size() == 1 && prmsgChunk.attr("href") != null && prmsgChunk.attr("href").length() > 0) {
				prospectw.setPrivateMessageURL(prmsgChunk.attr("href"));
				log.info(prospectw.getPrivateMessageURL());
			}

			prospectw.setProspectRecordwsTStampNow();
		}
		return res;
	}

	@Override
	protected boolean fillContact(Contact contact, ProspectCoreData prospect) {
		contact.setProspectID(prospect.getUserID());
		contact.setProspectFK(prospect.getInternalID());

		// --- channel info ---
		contact.setChannelFields(pageConfig.getChannelInfo());
		// --- campaign info ---
		contact.setCampaignName("acquisition");
		contact.setContacterIdentity("armando");
		// azioni contatto cliente, tipicamente una POST
		contact.setContactContent("Hi, I am Italian");
		contact.setContactType(getContactType());
		contact.setContactExecIDNumber(9999);
		contact.setContactMail("unknown@nowhere.iknow.com");
		contact.setContactDateNow();
		contact.setUserContactCommand("???POST???");
		contact.setUserContactKey("???usercontactkey???");
		return true;
	}

	
	
	
	
	

	/*
	 * (non-Javadoc) contactParam è un parametro generico qui usato per la URL della pagina contenente la form */
	@Override
	protected boolean performContact(Contact contact, ProspectCoreData prospect, String contactParam) {
		log.info("CONTATTO: " + prospect.getUserID());

		// qui abbiamo due URL, sto usando la form action per passare
		ResponseWrapper rw = cw.simpleGET(prospect.getPrivateMessageURL(), 0);
		if (rw.it().getStatus() != 200) {
			log.warn("some problem, maybe not blocking, getting page with login form: " + rw.it().getStatus());
			return false;
		}

		List<NameValuePairString> formFields = new ArrayList<NameValuePairString>();
		WEBUtils.getFormFields(rw, formFields,"#privatemsg-new");
		ArrayList<NameValuePairString> replace = new ArrayList<NameValuePairString>();
		replace.add(new NameValuePairString("subject", "language xchange"));
		replace.add(new NameValuePairString("recipient", "armando"));
		ArrayList<NameValuePairString> add = new ArrayList<NameValuePairString>();
		add.add(new NameValuePairString("body[value]", "system html"));
		ArrayList<NameValuePairString> remove = new ArrayList<NameValuePairString>();
		remove.add(new NameValuePairString("", "system html"));
		WEBUtils.setFormParams(formFields,replace /*replaceVals*/,/*removeList*/ null, add);		
		rw =  cw.simplePOST(prospect.getPrivateMessageURL(), formFields, null,0);
		rw.analyzeResponse(false, cw);
		if (rw.it().getStatus() == 200)
			return true; 
		else {
			log.error("post of Message to "+prospect.getPrivateMessageURL()+" failed: "+rw.dump());
			return false;
		}
	}

	Pattern patternDigits;
	
	private static org.apache.log4j.Logger log = Logger.getLogger(EntryProcessorItalianPenPals.class);

}
