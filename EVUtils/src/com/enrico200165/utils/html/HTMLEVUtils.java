package com.enrico200165.utils.html;

import java.util.ArrayList;

public class HTMLEVUtils {

	static final String LinePrefix = "<div class='{class}' id='{id}'>";
	static final String LineSuffix = "</div>";
	static final String fieldPrefix = "<span class='{class}' id='{id}'>";
	static final String fieldSuffix = "</span>";

	public static final String br = "<br />\n";
	public static final String HTMLEnd = "</body></html>";

	public HTMLEVUtils() {
	}

	public static String anchor(String url, String text) {
		String ret = "<a href=\"http://" + url + "\">";
		ret += text + "</a>";
		return ret;
	}

	public static String HTMLDocStart(String title, String cssStyleSheet) {
		String HTMLStart;
		HTMLStart = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTMLEVUtils 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">";
		HTMLStart += "\n" + "<html><head>";
		HTMLStart += "\n"
				+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">";
		HTMLStart += "\n" + "<title>" + title + "</title>";
		if (cssStyleSheet != null && cssStyleSheet.length() > 0) {
			HTMLStart += "\n" + "<link  rel=\"stylesheet\" type=\"text/css\"";
			HTMLStart += "href=\"" + cssStyleSheet + "\"/>";
		}
		HTMLStart += "</head><body>";
		return HTMLStart;
	}

	static String htmlClass(String c) {
		return " class=\"" + c + "\"";
	}

	static String htmlID(String c) {
		return " id=\"" + c + "\"";
	}

	static public String tableOpen(int border, String cssClass, String cssID, String style) {
		String s = "\n";
		s += "<table";
		if (style == null) {
			s += " style=\"text-align:left;table-layout:fixed;\"";
		} else {
			s+= " "+style;
		}
		if (cssClass != null && cssClass.length() > 0)
			s += htmlClass(cssClass);
		if (cssID != null && cssID.length() > 0)
			s += htmlID(cssID);
		if (border > 0)
			s += " border=\"" + border + "\"";
		s += ">";
		return s;
	}

	static public String tableHeader(ArrayList<TableHeaderCell> colHeaders) {
		String s = "\n";
		s += "<tr>";
		for (HTMLGenElement h : colHeaders) {
			s += h.getHTMLMarkUp(0);
		}
		s += "</tr>";
		return s;
	}

	static public String tableClose() {
		return "</table>";
	}

	static public String div(String cssKanjiClass, String ID, String content) {
		String s = "\n";
		s += LinePrefix.replaceFirst("\\{class\\}", cssKanjiClass);

		if (ID != null && ID.length() > 0)
			s = s.replaceFirst("\\{id\\}", ID);
		else
			s = s.replaceFirst(" id='\\{id\\}'", "");

		s += content + LineSuffix;
		return s;
	}

	/*
	 * Probabilmente dovrei trattarla come generico elemento html
	 */
	public static String tableRow(String cssClass, String cssID, String data) {
		String ret = "\n";
		ret += "<tr";
		if (cssClass != null && cssClass.length() > 0) {
			ret += " class=\"" + cssClass + "\"";
		}
		ret += ">";
		ret += data + "</tr>";
		return ret;
	}

}
