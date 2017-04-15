package com.enrico200165.utils.net.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class EVAuthenticator extends Authenticator {

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(user, password);
	}

	public EVAuthenticator(String user, String password) {
		super();
		this.user = user;
		this.password = password;
	}

	String user;
	String password;
}
