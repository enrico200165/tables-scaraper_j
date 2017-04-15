package com.enrico200165.utils.html;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

/**
 * Created by enrico on 28/07/2016.
 */
public class HTMLDisplayInBrowserTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testDisplayInBrowser() throws Exception {

    }

    @Test
    public void testDisplayLocFileBrowser() throws Exception {

    }

    @Test
    public void testMakeRelLinksAbsolute() throws Exception {

    }

    @Test
    public void testGetHTMLDoc() throws Exception {

    }

    @Test
    public void testWriteOut() throws Exception {

    }

    @Test
    public void testStartBrowser() throws Exception {

        HTMLDisplayInBrowser.startBrowser(new URI("http://google.com"));

        // questo fallirà
        HTMLDisplayInBrowser.startBrowser(new URI("file:///v:/concorsi2016-07-28.html"));

    }
}