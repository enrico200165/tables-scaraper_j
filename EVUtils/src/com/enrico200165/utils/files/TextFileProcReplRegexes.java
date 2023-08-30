package com.enrico200165.utils.files;

import com.enrico200165.utils.str_regex.NameValuePairString;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by enrico on 3/12/2016.
 */
public class TextFileProcReplRegexes implements TextLineProcessorI {

    public TextFileProcReplRegexes(List<NameValuePairString> replaces) {
        this.replaces = replaces;


    }

    @Override
    public String processLine(String in) {
        if (replaces == null) {
            log.error("replaces is null,esco");
            System.exit(1);
        }

        String ret = in;
        for (NameValuePairString replace :  replaces) {
            String toReplace = replace.getKey(); //RgH.j()
            String replacement = replace.getValue();
            ret = ret.replaceAll(toReplace,replacement);
        }
        return ret;
    }

    List<NameValuePairString> replaces;
    private static Logger log = LogManager.getLogger(TextFileProcReplRegexes.class);

}
