package jax_rs;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.enrico200165.weblistscraper.common.WEBUtils;

import static org.junit.Assert.assertEquals;

public class JAXRSUtils {

	@Test
	public void testIsRelative() {
		assertEquals(true,WEBUtils.isRelativeURL("/a"));
		assertEquals(true,WEBUtils.isRelativeURL("a/a/"));
		assertEquals(true,WEBUtils.isRelativeURL("a/a?par1=val1&par2=val2"));
		assertEquals(true,WEBUtils.isRelativeURL("a/a?par1=val1&par2=val2#a"));

		
		assertEquals(false,WEBUtils.isRelativeURL("http://a"));
		assertEquals(false,WEBUtils.isRelativeURL("https://a/"));
		assertEquals(false,WEBUtils.isRelativeURL("oracle.com/a/"));
		assertEquals(false,WEBUtils.isRelativeURL("host1/pippo.do"));
		
		// assertEquals(true,WEBUtils.isRelative("//"));		
	}

	private static org.apache.log4j.Logger log = Logger.getLogger(JAXRSUtils.class);

}
