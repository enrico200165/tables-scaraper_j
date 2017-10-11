package com.enrico200165.utils.net.http;

import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.net.URL;


import static org.junit.Assert.*;

public class UtilsTest {

     static Utils u = null;
    // Run once, e.g. Database connection, connection pool
    @BeforeClass
    public static void runOnceBeforeClass() {
    }


    @Test
    public void setFiddler() throws Exception {
    }

    @Test
    public void URLFromURI() throws Exception {
    }

    @Test
    public void URIFromURLString() throws Exception {

        URI u = Utils.URIFromURLString("user","http://host.com");
        assertEquals(u.toString(),"http://host.com/user");
    }

    @Test
    public void makeUrlRelative() throws Exception {
    }

    @Test
    public void isCorrectURL() throws Exception {
    }

    @Test
    public void isUsableURI() throws Exception {
    }

    @Test
    public void isRelativeURL() throws Exception {
    }

}