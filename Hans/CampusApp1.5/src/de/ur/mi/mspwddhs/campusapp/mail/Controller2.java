package de.ur.mi.mspwddhs.campusapp.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.os.AsyncTask;
import android.util.Log;

public class Controller2 extends AsyncTask<String, String, String> {

	String from;
	String to;
	String user;
	String password;
	String mailText;
	String mailSubject;
	
	String host = "mail.uni-regensburg.de";

	@Override
	protected String doInBackground(String... params) {

		from = params[0];
		user = params[1];
		password = params[2];
		mailText = params[3];
		mailSubject = params[4];
		to = params[5];
							
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtps.ssl.trust", host);
		
		props.setProperty("mail.smtp.host", host);

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(user, password);
					}
				});

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));

			// Set Subject: header field
			message.setSubject(mailSubject);

			// Now set the actual message
			message.setText(mailText);

			// Send message
			Transport.send(message);
			// System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
		return null;
	}

}