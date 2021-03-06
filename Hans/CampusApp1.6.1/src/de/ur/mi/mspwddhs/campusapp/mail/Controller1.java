package de.ur.mi.mspwddhs.campusapp.mail;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.FetchProfile.Item;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import com.sun.mail.imap.IMAPFolder.FetchProfileItem;

import android.content.Context;
import android.os.AsyncTask;

public class Controller1 extends AsyncTask<String, String, String> {
	
	private static final int MAX_NUMBER_OF_MAILS = 10;

	Folder inbox;

	private String user;
	private String password;
	private String host = "imap.uni-regensburg.de";
	private String content;
	private Multipart mp;
	private boolean textIsHtml = false;

	private int numOfUnreadMessages;

	ArrayList<Email> emailList;

	private emailListener listener;

	Email email;

	private Message message[];

	public Controller1(emailListener listener) {
		initialise(listener);
	}

	public void initialise(emailListener listener) {

		this.listener = listener;

	}

	@Override
	protected String doInBackground(String... params) {

		user = params[0];
		password = params[1];

		setup();
		return null;

	}

	private void setup() {

		handleDifferentMailTexts();

		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "imaps");
		try {

			Session session = Session.getDefaultInstance(props, null);
			Store store = session.getStore("imaps");
			store.connect(host, user, password);

			inbox = store.getFolder("INBOX");
			numOfUnreadMessages = inbox.getUnreadMessageCount();


			inbox.open(Folder.READ_ONLY);

			Message messages[] = inbox.search(new FlagTerm(
					new Flags(Flag.SEEN), false));
			message = messages;

			FetchProfile fp = new FetchProfile();
				fp.add(FetchProfile.Item.ENVELOPE);
				fp.add(FetchProfile.Item.CONTENT_INFO);
				inbox.fetch(messages, fp);
			try {
				fillMails(messages);
				inbox.close(true);
				store.close();
			} catch (Exception ex) {
				System.out.println("Mail reading error occured!");
				ex.printStackTrace();
			}
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (MessagingException e) {
			e.printStackTrace();
			System.exit(2);
		}

	}

	private void handleDifferentMailTexts() {

		MailcapCommandMap mc = (MailcapCommandMap) CommandMap
				.getDefaultCommandMap();
		mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
		mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
		mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
		mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
		mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");

	}

	@Override
	protected void onPostExecute(String result) {

		listener.onUpdateCompleted(emailList);

	}

	private void fillMails(Message[] message) throws MessagingException,
			IOException {

		emailList = new ArrayList<Email>();
		int numMails;
		if(numOfUnreadMessages>MAX_NUMBER_OF_MAILS) { 
			numMails = MAX_NUMBER_OF_MAILS;
		} else {
			numMails = numOfUnreadMessages;
		}

		for (int i = 0; i < numMails; i++) {
			content = "";
			getCompleteMail(message[i]);

		}

	}

	private void getCompleteMail(Message message) throws MessagingException,
			IOException {

		Address[] param;

		String to = "";
		String from = "";

		if ((param = message.getFrom()) != null) {
			for (int j = 0; j < param.length; j++) {
				from += param[j];
			}
		}

		if ((param = message.getRecipients(Message.RecipientType.TO)) != null) {
			for (int j = 0; j < param.length; j++) {
				to += param[j];
			}
		}
		String contentType = message.getContent().toString();

		String sender = from;
		String recipients = to;
		String subject = message.getSubject();
		Date receivedDate = message.getReceivedDate();

		String content = getText(message);

		email = new Email(from, to, receivedDate, subject, content);
		emailList.add(email);

	}

	public interface emailListener {

		public void onUpdateCompleted(ArrayList<Email> mails);

	}

	private String getText(Part p) throws MessagingException, IOException {
		if (p.isMimeType("text/*")) {
			String s = (String) p.getContent();
			textIsHtml = p.isMimeType("text/html");
			return s;
		}

		if (p.isMimeType("multipart/alternative")) {
			Multipart mp = (Multipart) p.getContent();
			String text = null;
			for (int i = 0; i < mp.getCount(); i++) {
				Part bp = mp.getBodyPart(i);
				if (bp.isMimeType("text/plain")) {
					if (text == null)
						text = getText(bp);
					continue;
				} else if (bp.isMimeType("text/html")) {
					String s = getText(bp);
					if (s != null)
						return s;
				} else {
					return getText(bp);
				}
			}
			return text;
		} else if (p.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) p.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				String s = getText(mp.getBodyPart(i));
				if (s != null)
					return s;
			}
		}

		return null;
	}

}