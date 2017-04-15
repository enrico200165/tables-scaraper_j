package com.enrico200165.utils.html;

import org.apache.log4j.*;

import java.util.ArrayList;



/**
 * @author enrico SPORCO HACK PER CONVENZIONE (non controllata nel codice): Il contenuto o è una stringa o una serie di sottolementi, mai
 *         l'uno e l'altro
 * 
 */
public class HTMLGenElement implements IHTMLContent {

	public HTMLGenElement(String name) {
		super();
		this.name = name;
		this.stringContent = "";
		this.attributoids = new ArrayList<Attributoid>();
		this.subElements = new ArrayList<HTMLGenElement>();
	}

	public HTMLGenElement(String name, String content) {
		this.name = name;
		this.stringContent = content;
		this.attributoids = new ArrayList<Attributoid>();
		this.subElements = new ArrayList<HTMLGenElement>();
	}

	@Override
	public String getHTMLMarkUp(int level) {
		String contentFromSubEls = "";
		String attribs = "";
		String markup = "";

		// --- check ---
		if ((stringContent != null) && (stringContent.length() > 0) && this.subElements.size() > 0) {
			log.warn("element: <" + this.name + "> with both content and children: " + stringContent);
		}

		// --- prepare ---
		if (this.attributoids.size() > 0) {
			for (Attributoid a : this.attributoids) {
				attribs += a.getHTMLMarkUp(level) + " ";
			}
			attribs = attribs.trim(); // remove trailing whitespace
		}

		// --- produce ---
		if (this.subElements.size() > 0) {
			for (int i = 0; i < this.subElements.size(); i++) {
				if (i != 0) contentFromSubEls += "\n";
				contentFromSubEls += subElements.get(i).getHTMLMarkUp(level + 1);
			}
			if (this.stringContent != null && this.stringContent.length() > 0) {
				markup = TagTextGen.genHTMLWithChAndBasic(this.name, attribs, contentFromSubEls, this.stringContent, level);
			} else {
				markup = TagTextGen.genHTMLWithChildren(this.name, attribs, contentFromSubEls, level);
			}
		} else {
			if (stringContent != null && this.stringContent.trim().length() > 0) {
				markup = TagTextGen.genHTMLNoChildren(this.name, attribs, this.stringContent, level);
			} else {
				markup = TagTextGen.genHTMLNoContent(this.name, attribs, this.stringContent, level);
			}
		}

		return markup;
	}

	public HTMLGenElement addAttribute(String name, String value) {
		attributoids.add(new Attributoid(name, value));
		return this;
	}

	public boolean addValueToAttr(String name, String value) {
		if (stringContent.length() > 0) {
			log.error("mischiati due tipi di contenuto");
		}
		for (Attributoid a : this.attributoids) {
			if (a.name.equals(name)) {
				a.addValue(" " + value);
				return true;
			}
		}
		log.error("attempt to add value <" + value + "> to non existing attribute: " + name);
		return false;
	}

	public boolean addClass(String value) {
		for (Attributoid a : this.attributoids) {
			if (a.name.equals("class")) {
				a.addValue(" " + value);
				return true;
			}
		}
		this.attributoids.add(new Attributoid("class", value));
		return false;
	}

	public boolean setID(String value) {
		for (Attributoid a : this.attributoids) {
			if (a.name.equals("ID")) {
				log.error("id already exists" + a.values.get(0));
				return false;
			}
		}
		this.attributoids.add(new Attributoid("ID", value));
		return false;
	}

	public HTMLGenElement addChild(HTMLGenElement se) {
		subElements.add(se);
		return this;
	}

	public HTMLGenElement removeChild(HTMLGenElement child) {
		subElements.remove(child);
		return this;
	}

	public HTMLGenElement addChildQuick(String element, String value) {
		HTMLGenElement child = new HTMLGenElement(element);
		if (value == null) value = "";
		child.addHTMLContent(value);
		this.addChild(child);
		return child; // allow chaining to the child
	}

	public HTMLGenElement quickAddChildWithAttrib(String element, String value, String attrName, String attrValue) {
		return this.addChildQuick(element, value).addAttribute(attrName, attrValue);
	}

	public HTMLGenElement addHTMLContent(String c) {
		if (c == null || c.equals("null")) {
			// log.warn("adding null string, ignore it");
		} else {
			if (c.length() == 0) {
				// log.debug("passing an empty string");
			}
			stringContent += c;
		}
		return this;
	}

	String name;
	String stringContent;
	ArrayList<Attributoid> attributoids;
	ArrayList<HTMLGenElement> subElements;

	private static org.apache.log4j.Logger log = Logger.getLogger(HTMLGenElement.class);
}
