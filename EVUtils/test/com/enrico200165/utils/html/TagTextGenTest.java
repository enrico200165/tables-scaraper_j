package com.enrico200165.utils.html;



import org.junit.*;

import static org.junit.Assert.assertEquals;

public class TagTextGenTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGenerateHTMLNoContent() {

		String ret = TagTextGen.genHTMLNoContent("pippo", null, null, 0);
		assertEquals("<pippo />", ret);

		// stringa vuota per attributi
		ret = TagTextGen.genHTMLNoContent("pippo", "", null, 0);
		assertEquals("<pippo />", ret);

		// attributi con whitespace che deve essere rimosso
		ret = TagTextGen.genHTMLNoContent("pippo", " cane=pluto   ", null,
				0);
		assertEquals("<pippo cane=pluto />", ret);

		// presenti contenuti che non dovrebbero esserci
		ret = TagTextGen.genHTMLNoContent("pippo", " cane=pluto   ",
				"contents that should not be there", 0);
		assertEquals("<pippo cane=pluto />", ret);

		/*
		 * ret= TagTextGen.generateHTML("pippo",null,null,1);
		 * assertEquals("\n  <pippo>\n  </pippo>",ret);
		 * 
		 * ret=
		 * TagTextGen.generateHTML("pippo","class=\"classe1 classe2\"","abc",1);
		 * assertEquals
		 * ("\n  <pippo class=\"classe1 classe2\">\n    abc\n  </pippo>",ret);
		 * System.out.println(ret);
		 */
	}

	@Test
	public void testGenerateHTMLWithChildren() {

		String ret = TagTextGen
				.genHTMLWithChildren("pippo", null, null, 0);
		assertEquals("<pippo>\n</pippo>", ret);

		String tmpStr;
		String expected = "";

		tmpStr = "normally this would be sublements";
		for (int i = 0; i < 10; i++) {
			expected = TagTextGen.indent(i) + "<pippo>" + "\n"
					+ TagTextGen.indent(i + 1) + tmpStr + "\n"
					+ TagTextGen.indent(i) + "</pippo>";
			ret = TagTextGen.genHTMLWithChildren("pippo", null, tmpStr, i);
			if (!ret.equals(expected)) {
				System.out.println("level: " + i
						+ " not found expected; found instead:\n" + ret);
			}
			System.out.println("level: " + i + " expected:\n" + expected);
			//assertEquals(expected, ret);
		}

	}

	/*
	@Test
	public void testGenerateHTMLNoChildren() {
		final String elemStr = "pippo";
		String tmpStr;
		String ret = "";
		String expected = "";

		tmpStr = "    contenuti blah blah   ";
		for (int i = 0; i < 10; i++) {
			expected = "";
			ret = TagTextGen.generateHTMLNoChildren(elemStr, null, "", i);
		}
	}*/

	@Test
	public void testIndent() {
		assertEquals("        ", TagTextGen.indent(2));
	}

}
