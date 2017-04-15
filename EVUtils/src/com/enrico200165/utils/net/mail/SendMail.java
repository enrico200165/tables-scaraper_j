package com.enrico200165.utils.net.mail;

import java.util.Properties;

public interface SendMail {

	Properties buildProperties();

	boolean sendMail(String user, String password, String from, String to, String cc, String bcc, String subject, String body);

}