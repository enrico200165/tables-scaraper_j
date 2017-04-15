package com.enrico200165.utils.net.http;

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
	
}
