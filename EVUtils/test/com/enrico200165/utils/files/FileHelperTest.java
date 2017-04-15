package com.enrico200165.utils.files;

import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by enrico on 3/12/2016.
 */
public class FileHelperTest {

    @Test
    public void testWriteTxtLinesList() throws Exception {

        List<String> lines = new ArrayList<String>();
        lines.add("riga uno");
        lines.add("riga due");

        FileHelper fh = new FileHelper(null,"testdelete","txt","UTF-8");
        fh.writeTxtLinesList(lines,"\n");
    }

}