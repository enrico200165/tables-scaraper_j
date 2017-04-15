package com.enrico200165.weblistscraper.configs.site_japanguide;

import com.enrico200165.utils.str_regex.*;
import com.enrico200165.weblistscraper.common.*;
import com.enrico200165.weblistscraper.configs.PageConfigABC;
import com.enrico200165.weblistscraper.marketingDB.Contact;
import com.enrico200165.weblistscraper.marketingDB.ProspectCoreData;
import com.enrico200165.weblistscraper.page.EntryProcessorCommunity;
import com.enrico200165.weblistscraper.session.SessionManagerAbstr;
import com.enrico200165.weblistscraper.tools.*;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class EntryProcessorJapanguide extends EntryProcessorCommunity {

    public EntryProcessorJapanguide(SessionManagerAbstr smPar, ClientWrapper cwPar,
                                    PageConfigABC tCfgpar) {
        super(smPar, tCfgpar);
        this.cw = cwPar;
        patternDigits = Pattern.compile("[^0-9]*([0-9]*).*");
    }

    /**
     * Parses ad JSoup entry to fill a prospect object
     */
    @Override
    protected Result FillEntry(Element entry, ProspectCoreData prospectw) {
        Result res = new Result();


        // pre-elaborazione per semplificare le altre
        String tabellaEntryOK = "center > table:nth-child(1) > tbody";
        Element tabella = entry.select(tabellaEntryOK).get(0);
        if (tabella == null) {
            log.error("non riesco a elaborare entry");
            return res.setRc(Result.RC.ERROR);
        }

        //log.info(tabella);

        // -- riga 1 della tabella ----
        Element nameAndAgeEl = tabella.select("tbody > tr:nth-child(1) > td:nth-child(1) > font > b").get(0);
        if (nameAndAgeEl != null) {
            String nameAndAge = nameAndAgeEl.text();

            String name = null;
            if (nameAndAge.contains("(")) {
                name = nameAndAge.split("\\(")[0];
            } else {
                name = nameAndAge;
            }
            name = name.trim();
            prospectw.setUserID(name);

            // quick and dirty not to use heavy regez
            if (nameAndAge.contains("(")) {
                String ageStr = nameAndAge.split("\\(")[1];
                ageStr = ageStr.split("\\)")[0];
                int age = Integer.parseInt(ageStr);
                prospectw.setAge(age);
            } else {
                log.warn("age not found in: " + nameAndAge);
            }
        }

        {
            Element genderPicture = tabella.select("tbody > tr:nth-child(1) > td:nth-child(2) > font > img").get(0);
            String imageSrc = genderPicture.attr("src");
            if (imageSrc.endsWith("gender_f.gif")) {
                prospectw.setSex("f");
            } else if (imageSrc.endsWith("gender_m.gif")) {
                prospectw.setSex("m");
            } else {
                prospectw.setSex("-");
            }
        }

        Element nation = tabella.select("tbody > tr:nth-child(2) > td > table > tbody > tr > td > font > table > tbody > tr:nth-child(2) > td:nth-child(2) > font").get(0);
        log.info(nation.text());
        prospectw.setMotherCountry(nation.text());


        Element languages = tabella.select("tbody > tr:nth-child(2) > td > table > tbody > tr > td > font > table > tbody > tr:nth-child(3) > td:nth-child(2) > font").get(0);
        log.info(languages.text());
        if (languages != null) {
            String motherTongue = languages.text().split("<->")[0].trim();
            String targetLanguage = languages.text().split("<->")[1].trim();
            prospectw.setLanguageLearning(targetLanguage);
            prospectw.setMotherTongue(motherTongue);
        } else {
            log.warn("Languages info not found");
        }

        Element postMsgURL = tabella.select("tbody > tr:nth-child(3) > td:nth-child(2) > font > a").get(0);
        String url = postMsgURL.attr("href");
        prospectw.setPrivateMessageURL(postMsgURL.attr("href"));
        log.info(prospectw.getPrivateMessageURL());

        Element adText = tabella.select("tbody > tr:nth-child(2) > td > table > tbody > tr > td > p > font").get(0);
        log.info(adText.text());
        prospectw.setProfile(adText.text());

        // log.info("score: " + prospectw.updateAndGetScore());


        // da esaminare

        {
            Element adDate = tabella.select("tbody > tr:nth-child(3) > td:nth-child(1) > font").get(1);
            if (adDate != null) {
                String dateStr = adDate.text();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/M/dd");
                try {

                    java.util.Date date = formatter.parse(dateStr);
                    prospectw.setPostedOnHome(date);
                    log.info(prospectw.getPostedOnHome());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        Element interessi = tabella.select("tbody > tr:nth-child(2) > td > table > tbody > tr > td > font > table > tbody > tr:nth-child(1) > td:nth-child(2) > font").get(0);
        if (interessi != null) {
            prospectw.setHobbiesList(interessi.text());
            log.info(prospectw.getHobbiesList());
        }

        // --- campi fissi che non richiedono parsing

        prospectw.setHomeID("japanguide");
        prospectw.setChannelType("web");
        // prospectw.setMainEmail("");
        // prospectw.setAdOrProfileEntryUrl("");

        return res;

    }


    @Override
    protected boolean fillContact(Contact contact, ProspectCoreData prospect) {
        contact.setProspectID(prospect.getUserID());
        contact.setProspectFK(prospect.getInternalID());

        // --- channel info ---
        contact.setChannelFields(new ChannelConfigJapanguide());

        // --- campaign info ---
        if (StringUtils.nullOrEmpty(contact.getCampaignName())) {
            contact.setCampaignName("acquisition");
        }

        if (StringUtils.nullOrEmpty(contact.getContacterIdentity())) {
            contact.setContacterIdentity("armandoitaly1990");
        }

        /*
        if (StringUtils.nullOrEmpty(contact.())) {
        }
        */
        contact.setContactType(ContactType.NONE);
        contact.setContactExecIDNumber(0);
        contact.setContactMail("armandoitaly1990@gmail.com");
        contact.setContactDateNow();
        contact.setUserContactCommand("POST");
        contact.setUserContactKey("unused");

        return true;
    }

    boolean analyzeForm(ResponseWrapper rw, List<NameValuePairString> hiddenFormFields) {
        // --- analisi della pagina contente la form, specifica al sito
        // da fiddler vedo questi parametri:
        // aMESSAGE=Anche+io+sono+Italiano%2C+proviamo+ad+aiutarci+con+lo+studio+del+Giapponese%3F&aMES=send
        Document doc = rw.getJSoupDocument();
        Elements formsTentative = doc.select("#section_mainContent > center:nth-child(4) > table > tbody > tr > td > font > center > form"); // per debug, dovrebbe essere uo
        Element loginForm = formsTentative.get(0);
        if (loginForm == null) {
            log.error("failed to get form to analyze at: " + rw.dump());
            return false;
        }
        // log.info("login form OUTER HTML\n" + loginForm.outerHtml());
        Elements inputFields = loginForm.select("input");
        // display all
        for (Element e : inputFields) {
            String attrName = e.attr("name");
            hiddenFormFields.add(new NameValuePairString(attrName, e.val()));
        }

        log.info("\n------------- form fields found --------------------"
                + "\n" + WEBUtils.dumpNameValuePairsList(hiddenFormFields));

        return true;
    }

    /*
     * (non-Javadoc) contactParam è un parametro generico qui usato per la URL della pagina contenente la form */
    @Override
    protected boolean performContact(Contact contact, ProspectCoreData prospect, String contactParam) {
        log.info("CONTATTO: " + prospect.getUserID());

        // --- follow the link to message the contact (to the page  containing the form
        ResponseWrapper rw = cw.simpleGET(prospect.getPrivateMessageURL(), 0);
        if (rw.it().getStatus() != 200) {
            log.warn("some problem, maybe not blocking, getting page with login form: " + rw.it().getStatus());
            return false;
        }

        // --- in the page containing the form, should post now

        // --- scopri campi nascosti
        List<NameValuePairString> hiddentFormFields = new ArrayList<NameValuePairString>();
        analyzeForm(rw, hiddentFormFields);

        // campi che setto io
        ArrayList<NameValuePairString> formFields = new ArrayList<NameValuePairString>();
        formFields.add(new NameValuePairString("aMESSAGE", "anche io sono italiano, proviamo ad aiutarci"));

        // campi da rimuovere
        ArrayList<String> removeTheseFields = new ArrayList<String>();
        removeTheseFields.add("");

        WEBUtils.setFormParams(formFields, null /*replaceVals*/,/*removeList*/ removeTheseFields, hiddentFormFields);
        log.info(WEBUtils.dumpNameValuePairsList(formFields));


        log.error("evito il post, rimuovere");
        if (3 > (10-9) || this.cw != null)
            return true;

        rw = cw.simplePOST(prospect.getPrivateMessageURL(), formFields, null, 0);
        rw.analyzeResponse(false, cw);
        if (rw.it().getStatus() == 200)
            return true;
        else {
            log.error("post of Message to " + prospect.getPrivateMessageURL() + " failed: " + rw.dump());
            return false;
        }
    }


    Pattern patternDigits;

    private static org.apache.log4j.Logger log = Logger.getLogger(EntryProcessorJapanguide.class);

}
