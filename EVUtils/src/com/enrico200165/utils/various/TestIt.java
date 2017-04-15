package com.enrico200165.utils.various;

import com.enrico200165.utils.net.mail.SendMailSSL;
import com.enrico200165.utils.net.mail.SendMailTLS;

public class TestIt {
	public static void main(String[] args) {

		SendMailTLS smTLS = new SendMailTLS("enrico200165@gmail.com", "Ocir!?00go");
		smTLS.sendMail("enrico200165@gmail.com", "Ocir!?00go", "enrico200165@gmail.com", // from
				"enrico.yoshiko@gmail.com", // to
				null, // cc
				null, // bcc
				"TLS super successo", "Dear Mail Crawler," + "\n\n No spam to my email, please!");


	
	SendMailSSL smSSL = new SendMailSSL("enrico200165@gmail.com", "Ocir!?00go");
	smSSL.sendMail("enrico200165@gmail.com", "Ocir!?00go", "enrico200165@gmail.com", // from
			"enrico.yoshiko@gmail.com", // to
			null, // cc
			null, // bcc
			"SSL super successo", "Dear Mail Crawler," + "\n\n No spam to my email, please!");
	}

}
