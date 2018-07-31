package com.enrico200165.web.italianpenpals;

import com.enrico200165.cookies.CookieStoreEV;
import com.enrico200165.silverpop.web_scraper.InvocationBuilderSilverpop;
import com.enrico200165.silverpop.xmlapi.XMLMessages;
import com.enrico200165.utils.str_regex.NameValuePairString;
import com.enrico200165.utils.str_regex.StringPersistent;
import com.enrico200165.weblistscraper.common.WEBUtils;
import com.enrico200165.weblistscraper.configs.HostConfig;
import com.enrico200165.weblistscraper.configs.drupal7.FormManagerLoginDrupal7;
import com.enrico200165.weblistscraper.tools.*;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.ws.rs.client.Client;
import java.util.ArrayList;
import java.util.List;

public class Silverpop {

	public Silverpop(java.util.Properties props) {

		properties = props;

		String fname = "V:\\data\\pers_dev\\data_dbs\\web_scraper\\host_italianpenpals.properties";
		CookieStoreEV cookieStore = new CookieStoreEV("cookiesGlobalStore");
		HostConfig hc = new HostConfig(fname);

		LAST_ACCESS_TOKEN.restore();
		ibw = new InvocationBuilderSilverpop(LAST_ACCESS_TOKEN.it());
		client = WEBUtils.createClient(new ClReqFilterCookies(cookieStore), new RespFilterNull(), false);
		cw = new ClientWrapper(client, ibw, hc, 0
				, new FormManagerLoginDrupal7(hc));

		ResponseWrapper rw = null;
		jsessionID = null;
		formFields = new ArrayList<NameValuePairString>();

	}

	public String getOauthToken() {

		String response = null;

		try {
			ibw.setOauthToken(null); // evitiamo old token che causa errore
			List<NameValuePairString> formFields = new ArrayList<NameValuePairString>();
			formFields.add(new NameValuePairString("grant_type", "refresh_token"));
			formFields.add(new NameValuePairString("client_id", properties.get("client_id").toString()));
			formFields.add(new NameValuePairString("client_secret", properties.get("client_secret").toString()));
			formFields.add(new NameValuePairString("refresh_token", properties.get("user_account_refresh_token").toString()));

			rw = cw.simplePOST("https://api0.silverpop.com/oauth/token", formFields, ibw, 0);
			response = rw.getResponseHTML();
		} catch (Exception e) {
			log.error("fallita richiesta di token", e);
			System.exit(1);
		}
		String temp = response.split("access_token\":\"")[1];
		temp = temp.split("\"")[0];
		this.LAST_ACCESS_TOKEN.setToken(temp);
		ibw.setOauthToken(LAST_ACCESS_TOKEN.it());
		log.debug(response);


		return response;
	}

	/* Non posso usare API per non avere recusione infinita o implementare contatori/flags */
	String login() {

		if (LAST_ACCESS_TOKEN.it().length() > 0 || true) {
			log.info("uso Oauth");
			return "";
		}

		List<NameValuePairString> loginFormFields = new ArrayList<NameValuePairString>();

		loginFormFields.add(new NameValuePairString("jsessionid", jsessionID));
		loginFormFields.add(new NameValuePairString("xml", XMLMessages.login("enrico.viali@it.ibm.com", "Ocir!?00si")));
		rw = cw.simplePOST(APIURL, loginFormFields, ibw, 0);
		String response = rw.getResponseHTML();
		jsessionID = response.split("<SESSIONID>")[1].split("</SESSIONID>")[0];
		return jsessionID;
	}

	String logout() {
		if (jsessionID != null && jsessionID.length() > 0) {
			String xmlLogoutString = XMLMessages.logout(null);
			return APIRequest(xmlLogoutString, false, false);
		} else {
			return "";
		}
	}

	//	String APIRequest(String xml, boolean dumpRequest, boolean dumpResponse) {
	//		return APIRequest(this.jsessionID, xml, dumpRequest, dumpResponse);
	//	}

	String APIRequest(String xml, boolean dumpRequest, boolean dumpResponse) {

		if (jsessionID == null || jsessionID.length() <= 0) {
			login();
		}

		try {
			formFields.clear();
			formFields.add(new NameValuePairString("jsessionid", this.jsessionID));
			formFields.add(new NameValuePairString("xml", xml));
			if (dumpRequest) log.info("before sending, form field xml content:\n" + xml);
			rw = cw.simplePOST(APIURL + ";jsessionid=" + jsessionID, formFields, ibw, 0);
			lastRespHTXML.setToken(rw.getResponseHTML());
		} catch (Exception e) {
			log.warn("post failed");
		}
		// QUI POTREI ANALIZZARE SE C'è ERRORE DOVUTO A LOGIN SCADUTO E RIFARE LOGIN
		// se lo faccio servirebbe un contatore per evitare di andare in loop 
		String success = getRespElemValue("SUCCESS").toLowerCase();
		String adjusted_body = lastRespHTXML.it().replaceAll("\n", " ").toLowerCase();
		if ((success.equals("false") && getRespElemValue("errorid").toLowerCase().equals("145"))
				|| adjusted_body.matches(".*error.*invalid_token.*")) {
			log.info("Session invalid, expire? trying to login again, response:\n" + lastRespHTXML);
			if (jsessionID != null && jsessionID.length() > 0) {
				login();
			} else {
				getOauthToken();
			}
			rw = cw.simplePOST(APIURL + ";jsessionid=" + jsessionID, formFields, ibw, 0);
			lastRespHTXML.setToken(rw.getResponseHTML());
		}

		if (dumpResponse)
			log.info(lastRespHTXML);
		else
			log.debug(lastRespHTXML);

		return lastRespHTXML.it();
	}

	public String getRespElemValue(String elementTag) {

		Document doc = Jsoup.parse(lastRespHTXML.it());
		Elements valuesList = doc.select(elementTag);
		if (valuesList != null && valuesList.size() > 0) {
			if (valuesList.size() > 1) {
				log.warn("ritornato più di un elemento");
			}
			return valuesList.get(0).text();
		}
		return "";
	}

	public List<String> getRespElemValues(String elementTag) {
		ArrayList<String> values = new ArrayList<String>();
		Document doc = Jsoup.parse(lastRespHTXML.it());
		Elements valuesList = doc.select(elementTag);
		if (valuesList != null && valuesList.size() > 0) {
			for (Element e : valuesList) {
				values.add(e.text());
			}
		}
		return values;
	}

	final static String APIURL = "http://api0.silverpop.com/XMLAPI";
	java.util.Properties properties;
	Client client;
	ClientWrapper cw;
	ResponseWrapper rw;
	InvocationBuilderWrapper ibw;
	List<NameValuePairString> formFields;


	String jsessionID;

	StringPersistent LAST_ACCESS_TOKEN = new StringPersistent("SP_oauth_token.txt");
	StringPersistent lastRespHTXML = new StringPersistent("last_response.txt");

	private static org.apache.log4j.Logger log = Logger.getLogger(Silverpop.class);

}
