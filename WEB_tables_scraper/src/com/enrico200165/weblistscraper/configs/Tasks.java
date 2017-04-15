package com.enrico200165.weblistscraper.configs;

import java.util.List;

/**
 * Created by enrico on 15/04/2017.
 */
public final class  Tasks {

    public String toString() {
        String ret = "";
        for (int i = 0; i < tasks.size();i++) {
            ret += "["+i+"]"+tasks.get(i);
        }
        return ret;
    }
    public List< String > tasks;
}
