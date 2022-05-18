package com.enrico200165.weblistscraper.configs;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class HostConfig {

    public String fname;
    public String user;
    public String password;
    public String loginFormURL;
    public String loginFormAction;
    public String jsoupLoginFormSelector;
    public URI     baseHostURI;


    public HostConfig(String hostCfgFileName) {
        super();
        this.fname = hostCfgFileName;
        readCfg(fname);
    }


    public HostConfig() {
    }


    public boolean readCfg(String loginInfoPropFile) {
        if (loginInfoPropFile.equals("none")) {
            // special value for when we don't need it
            log.error("Should never pass here");
            System.exit(1);
        } else {
            InputStream input = null;
            Properties prop = new Properties();
            try {
                input = new FileInputStream(loginInfoPropFile);
                prop.load(input);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.exit(1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.user = (String)prop.get("user");
            this.password = (String)prop.get("password");
            this.loginFormURL = (String)prop.get("loginFormURL");
            this.jsoupLoginFormSelector = (String)prop.get("jsoupLoginFormSelector");
            this.loginFormAction = (String)prop.get("loginFormAction");
            setBaseHostURIStr((String)prop.get("baseHostURI"));
        }
        return true;
    }



    public URI getBaseHostURI() {
        return this.baseHostURI;
    }


    public void setBaseHostURIStr(String uriPar) {
        try {
            if (uriPar.startsWith("http://") || uriPar.startsWith("https://"))
                baseHostURI = new URI(uriPar);
            else
                baseHostURI = new URI("http://"+uriPar);
        } catch (URISyntaxException e) {
            log.error(e);
            System.exit(1);
        }
    }


    public  String getHostNoport() {
        log.error("da implementare, esco");
        System.exit(1);
        return this.getHost();
    }

    public String getLoginPageURL() {
        return loginFormURL;
    }

    public void setLoginPageURL(String url) {
        this.loginFormURL = url;
    }

    public String getHost() {
        return this.baseHostURI.toString();
    }

    public String getUserName() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getLoginFormAction() {
        return this.loginFormAction;
    }

    public String dump(String sep) {
        if (sep == null) sep = "";
        return getHost() + sep + getLoginFormAction() + sep + getUserName() + sep + getPassword();
    }

    public String getJsoupLoginFormSelector() {
        return this.jsoupLoginFormSelector;
    }


    private static org.apache.log4j.Logger log = Logger.getLogger(HostConfig.class);
}
