package com.enrico200165.weblistscraper.configs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LoginConfig {

    public String user_field;
    public String user_value;

    public String password_value;
    public String password_field;

    public String loginFormURL;
    public String loginFormAction;

    public String jsoupLoginFormSelector;

    private static Logger log = LogManager.getLogger(LoginConfig.class.getName());
}
