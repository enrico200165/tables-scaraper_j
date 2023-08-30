package com.enrico200165.weblistscraper.tools;

import com.enrico200165.utils.str_regex.NameValuePairString;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import java.util.List;


public class Experiment  {


		
	/*
	 * Gets the page containing the login form and extracts hidden fields and
	 * values
	 */
	boolean prova(String pageURI, List<NameValuePairString> hiddenFormFields, String name, String passwd) {
		Client client = ClientBuilder.newClient();
		
		// bind to URL
		log.info("getting login page to examine form: "+ client.target(pageURI).path("/").getUri());
		WebTarget target = client.target(pageURI).path("/");
		
	
		
		Invocation.Builder invocationBuilder = target.request();
		ResponseWrapper rw = new ResponseWrapper(invocationBuilder.get());
		if (rw.it().getStatus() != 200) {
			log.warn("some problem, maybe not blocking, getting page with login form: " + rw.it().getStatus());
		}
		Document doc = rw.getJSoupDocument();
		Element loginForm = doc.select("#user-login-form").get(0);
		if (loginForm == null) {
			log.error("failed to get form to analyze at: "+ target.getUri());
			System.exit(1);
		}
		// log.info("login form OUTER HTML\n" + loginForm.outerHtml());
		Elements inputFields = loginForm.select("input");
		// display all
		for (Element e : inputFields) {
			String attrName = e.attr("name");
			// log.info(e.attr("name") + " = " + e.val());
			if (attrName.equals("name")) {
				hiddenFormFields.add(new NameValuePairString(attrName, name));
			} else if (attrName.equals("pass")) {
				hiddenFormFields.add(new NameValuePairString(attrName, passwd));
			} else { // campi che riportiamo tali e quali
				hiddenFormFields.add(new NameValuePairString(attrName, e.val()));
				log.debug("captured form input: " + attrName + " = " + e.val());
				continue;
			}
		}

		String s = "\n------------- login form fields from login page --------------------";
		for (NameValuePairString nvp : hiddenFormFields) {
			s += "\nhidden form fields: " + nvp.getKey() + " = " + nvp.getValue();
		}
		// log.info(s);

		return false;
	}


	private static Logger log = LogManager.getLogger(Experiment.class.getSimpleName());
}
