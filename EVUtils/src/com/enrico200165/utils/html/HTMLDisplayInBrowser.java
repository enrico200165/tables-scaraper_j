package com.enrico200165.utils.html;

import com.enrico200165.utils.files.FileHelper;
import java.util.logging.Logger;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;

public class HTMLDisplayInBrowser {



	static public void displayInBrowser(String fname, String bodyContent, boolean isFragment, String hostForAbsLinks) {
		
		
		if (hostForAbsLinks != null)
			bodyContent = makeRelLinksAbsolute(bodyContent,hostForAbsLinks);

		// writes string to file
		HTMLDisplayInBrowser.writeOut(fname, bodyContent, isFragment);

		Runtime rt = Runtime.getRuntime();
		try {
			if(Desktop.isDesktopSupported())  {
				Desktop.getDesktop().browse(new URI(fname));
			}
			// rt.exec(new String[] { "C:/Program Files (x86)/Mozilla Firefox/firefox.exe ", fname });

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	static public void displayLocFileBrowser(String fname) {
		try {
			if(Desktop.isDesktopSupported())  {
				String url = "file:///"+ FileHelper.adjustPath(fname);
				URI uri = new URI(url);
				Desktop.getDesktop().browse(uri);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	static public void startBrowser(URI uri) {
		try {
			if(Desktop.isDesktopSupported())  {
				Desktop.getDesktop().browse(uri);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	static public String makeRelLinksAbsolute(String page, String host) {
		page = page.replaceAll("href *= * */" , "href=http://"+host+"/");
		// page = page.replaceAll("src *= * */" , "src=http://"+host+"/");
		return page;
	}
	
	
	static String getHTMLDoc(String bodyContent) {
		String s = "";
		String spazioLinee = "<br><br>";
		String fontSize = "150%";
		
//		s += "<html><head><meta content=\"text/html; charset=ISO-8859-1\"http-equiv=\"content-type\"><title>Energy&Utilities Model</title></head>";
		s += "<html><head><meta content=\"text/html; charset=UTF-8\"http-equiv=\"content-type\"><title>Energy&Utilities Model</title></head>";

		
		s += "<body>";
		s += bodyContent;
		s += "</body></html>";

		return s;
	}

	static public void writeOut(String fname, String html, boolean isFragment) {
		
		String contents = isFragment ? getHTMLDoc(html) : html;
		

		FileOutputStream fos = null;
		OutputStreamWriter outwriter = null;
		try {
			fos = new FileOutputStream(fname);
			outwriter = new OutputStreamWriter(fos);

			outwriter.write(contents);

			outwriter.flush();
			outwriter.close();
			outwriter = null;
			fos.flush();
			fos.close();
			fos = null;
			log.info("in "+System.getProperty("user.dir")+" written: "+fname);
			
		} catch (IOException e) {
			System.err.print("Eccezione di IO\n" + e.toString());
		}

	}

	private static Logger log = Logger.getLogger(HTMLDisplayInBrowser.class.getSimpleName());
}
