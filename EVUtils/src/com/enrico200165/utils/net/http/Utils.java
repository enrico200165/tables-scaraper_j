package com.enrico200165.utils.net.http;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class Utils {

	public static void setFiddler(boolean active) {
		if (active) {
			System.setProperty("http.proxyHost", "127.0.0.1");
		    System.setProperty("http.proxyPort", "8888");
		    System.setProperty("https.proxyHost", "127.0.0.1");
		    System.setProperty("https.proxyPort", "8888");

			// per fiddler SSL
			System.setProperty("proxySet", "true");
			System.setProperty("javax.net.ssl.trustStore", "V:\\programs\\Java\\jdk1.7.0_79\\jre\\lib\\security\\FiddlerKeystore");
			System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
		}
	}

	public static String URLFromURI(URI uri) {
		String ret = "";

		if (uri == null) {
			log.error("");
		}

		ret += uri.getScheme() + "://" + uri.getHost() + uri.getPath();

		if (uri.getQuery() != null && uri.getQuery().length() > 0)
			ret += "?" + uri.getQuery();

		return ret;
	}


	/*
    * Sintassi s://<rel URI> per URL relative che richiedono httpS
    * Tries to build a URI
    * also managinf repetition of the host
    *
    * */
	public static URI URIFromURLString(String url, String host) {
		String myHTTPSMarker = "s://";
		boolean https = false;
		URI uri = null;

		if (url == null) {
			return null;
		}
		if (url.startsWith(myHTTPSMarker)) {
			url = url.replace(myHTTPSMarker, "");
			https = true;
		}


		try {
			if (host == null) {
				if (url == null) { //entrambi nulli
					log.error("entrambi nulli");
					uri = null;
				} else {
					// host null quindi assoluta
					if (url.startsWith("http")) {
						// ok è completa
						uri = new URI(url);
					} else {
						uri = null;
					}
				}
			} else { // host not null
				if (url == null) {   // url null
					uri = new URI("http://"+host+"/");
				} else { // entrambi non nulli
					if (url.contains(host)) {
						uri = new URI(url);
					} else {
						if (url.startsWith("http")) {
							log.warn("situazione anomala");
							uri = null;
						} else {  //situazione normale
							if (!url.startsWith("/"))
								url = "/" + url;
							if (!host.startsWith("http")) {
								if (https) {
									uri = new URI("https://" + host + url);
								} else {
									uri = new URI("http://" + host + url);
								}
							} else {
								uri = new URI(host + url);
							}
						}
					}
				}
			}
			if (uri == null)
				log.error("problem building URI from host: \"" + host + "\" and url: \"" + url + "\"");
			return uri;
		} catch (Exception e) {
			log.error("", e);
		}


		return uri;
	}


	/**
	 * Semplicistica, probabilmente fallisce in molti casi pensarci bene prima di stravolgerla dato che le URL sono difficilissime e forse
	 * impossibile da parsare ex. host1/index e /host1/idex se non si impone che comincino con / s'è ambiguita irrisolvibile
	 *
	 * @param url
	 * @return
	 */
	public static String makeUrlRelative(String url) {
		String ret = url;
		int pos;

		try {
			// deve stare prima di http altrimenti toglie tutto tranne la s
			if ((pos = ret.indexOf("https://")) != -1) {
				ret = ret.substring(pos + "https://".length());
			}
			if ((pos = ret.indexOf("http://")) != -1) {
				ret = ret.substring(pos + "http://".length());
			}

			// deve stare dopo l'esame di http:// per evitare che becchi i //
			pos = ret.indexOf("/");
			if (pos != -1) {
				ret = ret.substring(pos);
				return ret;
			}

			if (ret.matches(".*\\..*")) {
				// alta probabilità di errore qui
				ret = "/";
				return ret;
			}
		} finally {
			log.debug(url + " became " + ret);
			return ret;
		}
	}

	public static boolean isCorrectURL(String url) {
		try {
			java.net.URL u = new URL(url);
		} catch (MalformedURLException e) {
			return false;
		}
		return true;
	}


	public static boolean isUsableURI(URI uri) {
		try {
			java.net.URL u = new URL(uri.toString());
		} catch (MalformedURLException e) {
			return false;
		}
		return !(uri.getPath() == null || uri.getPath().length() == 0);
	}

	public static boolean isRelativeURL(String url) {
		boolean ret = false;

		if (!isCorrectURL(url)) {
			if (!isCorrectURL("http://google.com" + url)) {
				// rifiuta URL relative come /a
				log.error("esco, not a correct URL: " + url);
				System.exit(1);
			}
		}

		if (url.contains("http:") || url.contains("https:")) {
			return false;
		}

		// probably wrong
		// check if it contains an hostname
		return !url.matches("[\\w]+\\.?[\\w]+/[^/].*");
	}


	private static Logger log = LogManager.getLogger(Utils.class.getSimpleName());


}
