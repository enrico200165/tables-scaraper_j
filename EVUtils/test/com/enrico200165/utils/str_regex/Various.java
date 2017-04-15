package com.enrico200165.utils.str_regex;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class Various {

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        String fname = "temp2";
        StringPersistent token = new StringPersistent(fname, "ciaociao");
        token.persist();
        token.setToken("problema3");

        StringPersistent token2 = new StringPersistent(fname);

        System.out.println(token2.restore());

    }

    @Test
    public void testURL() {


        List<String> input = new ArrayList<String>();
        input.add("http://www.japan-guide.com/local/?aCAT=3&aGENDER=b&aAGE1=1&aAGE2=120&aINTEREST=0&aLANGUAGE1=0&aLANGUAGE2=0&aJOB=&aNATIONALITY=jp&aSUBCAT=&aMES=submit&aPAGE=2");


        for (String ssn : input) {
//			if (ssn.matches("http://www.japan-guide.com/local/?aCAT=.*aPAGE=[0-9]+")) {
            if (ssn.matches("http://www.japan-guide.com/local/\\?aCAT=.*aGENDER.*aAGE1.*aAGE2.*aINTEREST.*aLANGUAGE1.*aLANGUAGE2.*aJOB.*aNATIONALITY.*aSUBCAT.*aMES=submit.*aPAGE=[0-9]+")) {
                System.out.println("ok");
            } else {
                System.out.println("NOOOO");
            }
        }
    }

}
