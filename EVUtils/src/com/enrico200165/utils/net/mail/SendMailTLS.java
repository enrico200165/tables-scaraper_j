package com.enrico200165.utils.net.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailTLS implements SendMail {

	public SendMailTLS(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}

	public  Properties buildProperties() {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		return props;
	}

	public boolean sendMail(String user, String password, String from, String to, String cc, String bcc, String subject, String body) {

		EVAuthenticator auth = new EVAuthenticator(this.getUserName(), this.getPassword());

		Session session = Session.getInstance(buildProperties(), auth);

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			message.setText(body);

			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			return false;
		}
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	String userName;
	String password;
	
}