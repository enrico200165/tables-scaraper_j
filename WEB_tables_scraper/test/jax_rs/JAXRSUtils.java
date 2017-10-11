package jax_rs;

import com.enrico200165.utils.net.http.Utils;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.enrico200165.weblistscraper.common.WEBUtils;

import static org.junit.Assert.assertEquals;

public class JAXRSUtils {

	@Test
	public void testIsRelative() {
		assertEquals(true,Utils.isRelativeURL("/a"));
		assertEquals(true,Utils.isRelativeURL("a/a/"));
		assertEquals(true,Utils.isRelativeURL("a/a?par1=val1&par2=val2"));
		assertEquals(true,Utils.isRelativeURL("a/a?par1=val1&par2=val2#a"));

		
		assertEquals(false,Utils.isRelativeURL("http://a"));
		assertEquals(false,Utils.isRelativeURL("https://a/"));
		assertEquals(false,Utils.isRelativeURL("oracle.com/a/"));
		assertEquals(false, Utils.isRelativeURL("host1/pippo.do"));
		
		// assertEquals(true,WEBUtils.isRelative("//"));		
	}

	private static org.apache.log4j.Logger log = Logger.getLogger(JAXRSUtils.class);

}
