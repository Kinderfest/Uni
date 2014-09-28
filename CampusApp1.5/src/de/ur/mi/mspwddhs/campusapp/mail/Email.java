package de.ur.mi.mspwddhs.campusapp.mail;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Email {
	
	private String from;
	private String to;
	private Date date;
	private String subject;
	private String content;
	
	public Email (String from, String to, Date date, String subject, String content) {
		
		this.from= from;
		this.to = to;
		this.date = date;
		this.subject = subject;
		this.content = content;
				
	}

	public String getFrom() {
		return from;
	}
	
	public String getTo() {
		return to;
	}
	
	public String getDate() {
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		String currentDateString = df.format(date);
		return currentDateString;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public String getContent() {
		return content;
	}

	
}
