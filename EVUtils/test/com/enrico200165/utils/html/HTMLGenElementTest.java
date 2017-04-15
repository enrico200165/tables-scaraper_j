package com.enrico200165.utils.html;

import org.junit.*;

import static org.junit.Assert.assertEquals;

public class HTMLGenElementTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/*
	 * @Test public void testQuickAddChild() { HTMLGenElement e = new
	 * HTMLGenElement("enrico"); e.quickAddChild("davide",
	 * "bravissimo").quickAddChild("figlioDiDavide", "bravissimo2");
	 * e.quickAddChild("virginia", "bravissima");
	 * System.out.println(e.getHTMLMarkUp(0)); }
	 */

	/*
	 * @Test public void testQuickAddChildWithAttribute() { HTMLGenElement e =
	 * new HTMLGenElement("enrico"); e.quickAddChildWithAttrib("davide",
	 * "bravissimo","nazione","italo-giapponese")
	 * .quickAddChildWithAttrib("nipote", "ken", "diNonno", "enrico")
	 * .quickAddChildWithAttrib("pronipote", "kiotaro", "tipoNome",
	 * "romanesco"); e.quickAddChild("virginia", "bravissima");
	 * System.out.println(e.getHTMLMarkUp(0)); }
	 */

	@Test
	public void testGetHTMLMarkUp() {

		String elem = "pippo";
		String ret = "";
		String tempStr = "   contenuti dummy   ";
		String expect = "";
		int level = 0;
		HTMLGenElement e = null;

		e = new HTMLGenElement("enrico");
		for (level = 0; level < 10; level++) {
			expect = "<enrico />";
			tempStr = TagTextGen.brackets(elem) + tempStr
					+ TagTextGen.bracketsClose(elem);
			ret = e.getHTMLMarkUp(level);
			// System.out.println(ret);
			if (level == 0)
				assertEquals(ret, expect);
		}

		e = new HTMLGenElement("enrico");
		e.addAttribute("capelli", "pelato");
		for (level = 0; level < 10; level++) {
			expect = "<enrico capelli=\"pelato\" />";
			tempStr = TagTextGen.brackets(elem) + tempStr
					+ TagTextGen.bracketsClose(elem);
			ret = e.getHTMLMarkUp(level);
			// System.out.println(ret);
			if (level == 0)
				assertEquals(ret, expect);
		}

		// --- aggiungiamo figli senza contenuti
		int i = 0;
		HTMLGenElement davide = new HTMLGenElement("davide");
		e.addChild(new HTMLGenElement("virginia"));
		e.addChild(new HTMLGenElement("virginia"));
		e.addChild(new HTMLGenElement("virginia"));
		for (level = 0; level < 10; level++) {
			expect = TagTextGen.indent(i) + "<enrico capelli=\"pelato\">\n"
					+ davide.getHTMLMarkUp(i + 1) + "\n" + TagTextGen.indent(i)
					+ TagTextGen.bracketsClose("enrico");
			ret = e.getHTMLMarkUp(level);
			System.out.println(ret);
			if (level == -1)
				assertEquals(ret, expect);
		}

		// --- aggiungiamo un figlio con contenuti
		e = null;
		e = new HTMLGenElement("element");
		davide = new HTMLGenElement("davide", "contenuti");
		e.addChild(davide);
		ret = e.getHTMLMarkUp(0);
		System.out.println(ret);
		// assertEquals(ret,expect);

		// --- Add basic content, so both child and basic content
		e.addHTMLContent("contenuto di base");
		ret = e.getHTMLMarkUp(0);
		System.out.println(ret);
		// assertEquals(ret,expect);

		// add another child
		HTMLGenElement pluto = new HTMLGenElement("Pluto");
		pluto.addChild(new HTMLGenElement("giorgio", "nipote")).addChild(
				new HTMLGenElement("pronipote"));
		pluto.addChild(new HTMLGenElement("giorgio", "nipote")).addChild(
				new HTMLGenElement("pronipote"));
		e.addChild(new HTMLGenElement("virginia", "ciao"));
		e.addChild(new HTMLGenElement("giorgio", "ciao"));
		e.addChild(new HTMLGenElement("giorgio"));
		e.addChild(pluto);
		e.addChild(new HTMLGenElement("giorgio"));
		ret = e.getHTMLMarkUp(0);
		System.out.println(ret);

		/*
		 * HTMLGenElement e = new HTMLGenElement(elem);
		 * e.quickAddChildWithAttrib("davide",
		 * "bravissimo","nazione","italo-giapponese");
		 * 
		 * 
		 * 
		 * .quickAddChildWithAttrib("nipote", "ken", "diNonno", "enrico")
		 * .quickAddChildWithAttrib("pronipote", "kiotaro", "tipoNome",
		 * "romanesco"); e.quickAddChild("virginia", "bravissima");
		 * System.out.println(e.getHTMLMarkUp(0));
		 */
	}

	/*
	 * @Test public void testZZZ() { HTMLGenElement e = new
	 * HTMLGenElement("enrico"); e.addClass("fuoriclasse");
	 * e.addClass("bellissimo"); e.setID("20011965");
	 * e.addAttribute("capelli","castani"); HTMLElement davide = new
	 * HTMLElement("davide"); davide.addChild(new HTMLElement("benedetta"));
	 * davide.addChild(new HTMLElement("livia")); e.addChild(davide);
	 * HTMLElement virginia = new HTMLElement("virgina");
	 * virginia.addAttribute("aspetto", "bellissima");
	 * virginia.addValueToAttr("aspetto", "orientale");
	 * virginia.addValueToAttr("x", "orientale"); virginia.addChild(new
	 * HTMLElement("amica1")); virginia.addChild(new HTMLElement("amica2"));
	 * 
	 * HTMLElement testo = new HTMLElement("testo");
	 * testo.addHTMLContent("ecco i grandi contenuti");
	 * virginia.addChild(testo); e.addChild(virginia);
	 * System.out.println(e.getHTMLMarkUp(0)); }
	 */
}
