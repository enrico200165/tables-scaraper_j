package com.enrico200165.utils.html;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;
public class TagTextGen {
	final String indent = "  ";

	
	public static String brackets(String s) {
		return "<"+s+">";
	}

	public static String bracketsClose(String s) {
		return "</"+s+">";
	}


	public static String bracketsShort(String s) {
		return "<"+s+" />";
	}

	
	public static String genHTMLNoChildren(String tag, String attributes,
			String content, int level) {

		String paddAttrib = attributes.trim().length() > 0 ? " " : "";
		attributes = (attributes == null) ? "" : attributes;
		String ret = indent(level) + brackets(tag + paddAttrib + attributes);

		ret += content.trim();

		// --- tag finale ---
		ret += bracketsClose(tag);

		return ret;
	}

	public static String genHTMLNoContent(String tag, String attributes,
			String content, int level) {

		if (content != null && content.trim().length() >0 ) {
			log.log(Level.SEVERE, "element <"+tag+"> has contents, it should not when calling this");
		}
		
		String pad = indent(level);		
		
		String paddAttrib = (attributes == null || attributes.trim().length() <= 0) ? "" : " ";
		attributes = (attributes == null) ? "" : attributes.trim();
		String ret = pad + bracketsShort(tag + paddAttrib + attributes);
		
		return ret;
	}

	public static String genHTMLWithChildren(String tag,
			String attributes, String content, int level) {
		// -- open tag ---
		String paddAttrib = (attributes != null && attributes.trim().length() > 0) ? " " : "";
		attributes = (attributes == null) ? "" : attributes;
		String ret = indent(level) + brackets(tag + paddAttrib + attributes);

		// --- contenuti ---
		if (content != null && content.length() > 0) {
			ret += "\n";
			ret += content;
		}

		// --- tag finale ---
		ret += "\n"+indent(level)+bracketsClose(tag);

		return ret;
	}

	
	public static String genHTMLWithChAndBasic(String tag,
			String attributes, String elemContent, String basicCont, int level) {

		String pad = indent(level);

		// -- open tag ---
		String paddAttrib = (attributes != null && attributes.trim().length() > 0) ? " " : "";
		attributes = (attributes == null) ? "" : attributes;
		String ret = pad + brackets(tag + paddAttrib + attributes);

		// --- contenuto base , su linea dedicata
		if (basicCont != null && basicCont.length() > 0) {
			ret += "\n" + indent(level+1);
			ret += basicCont;
		}				
		// --- contenuti children ---
		if (elemContent != null && elemContent.length() > 0) {
			ret += "\n" + indent(level); // ... al livello +1 
			ret += elemContent;
		}

		// --- tag finale ---
		ret += "\n"+pad+bracketsClose(tag);

		return ret;
	}

	
	public static String indent(int level) {
		String indent = "";
		for (int i = 0; i < level; i++)
			indent += "    ";
		return indent;
	}

	static Logger log=Logger.getLogger(TagTextGen.class.getSimpleName());

}
