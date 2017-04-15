package com.enrico200165.weblistscraper.configs;

import com.enrico200165.utils.various.PropertiesEV;
import org.apache.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;

public abstract class HostConfigABC {

    public HostConfigABC(String loginInfoPropFile) {

        if (loginInfoPropFile.equals("none")) {
            // special value for when we don't need it
        } else {
            PropertiesEV p = new PropertiesEV(loginInfoPropFile);
            this.user = p.get("user");
            this.password = p.get("password");
            this.loginFormURL = p.get("loginFormURL");
            try {
                this.baseHostURI = new URI(p.get("baseHostURI"));
            } catch (URISyntaxException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

    }


    public URI getBaseHostURI() {
        return this.baseHostURI;
    }


    public abstract String getHostNoport();

    public String getLoginPageURL() {
        return loginFormURL;
    }

    public void setLoginPageURL(String url) {
        this.loginFormURL = url;
    }


    public abstract String getHost();

    public String getUserName() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public abstract String getLoginFormAction();

    public String dump(String sep) {
        if (sep == null) sep = "";
        return getHost() + sep + getLoginFormAction() + sep + getUserName() + sep + getPassword();
    }

    public abstract String getLFormSelector();


    protected String user;
    protected String password;
    protected String loginFormURL;
    protected URI baseHostURI;

    private static org.apache.log4j.Logger log = Logger.getLogger(HostConfigABC.class);

}
