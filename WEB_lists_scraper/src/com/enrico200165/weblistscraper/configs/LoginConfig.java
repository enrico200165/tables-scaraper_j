package com.enrico200165.weblistscraper.configs;

import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.Level;

public class LoginConfig {

    public String user_field;
    public String user_value;

    public String password_value;
    public String password_field;

    public String loginFormURL;
    public String loginFormAction;

    public String jsoupLoginFormSelector;

    static Logger log=Logger.getLogger(LoginConfig.class.getSimpleName());
}
