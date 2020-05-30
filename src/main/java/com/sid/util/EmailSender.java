package com.sid.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
  
import javax.mail.*;  
import javax.mail.internet.*;   


public class EmailSender {
	
	/**
	 * Send email using gmail host smtp service.
	 * FROM "sid.museu.info@gmail.com" account
	 * TO email field in config.ini file 
	 * @param subject - subject of the email 
	 * @param text - message of the email
	 */
	public static void sendEmail(String subject, String text){
		String host="smtp.gmail.com";  
		String user="sid.museu.info@gmail.com"; 
		String password="sid_2020"; 
		String to = null;
		try {
			Properties p = new Properties();
			p.load(new FileInputStream("src/main/resources/config.ini"));
			to = p.getProperty("email");
		} catch (IOException e) {
			e.printStackTrace();
		}
		sendEmail(host,user,password,to,subject,text);
	}

	/**
	 * Send email using params
	 * @param host - smtp service host, for example gmail: "smtp.gmail.com"
	 * @param user - sender email account
	 * @param password - sender email account password
	 * @param to - receiver email account
	 * @param subject - subject of the email 
	 * @param text - message of the email
	 */
	public static void sendEmail(String host,String user,String password,String to, String subject, String text){  
		Properties props = new Properties();  
		props.put("mail.smtp.host", host);  
		props.put("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {  
			protected PasswordAuthentication getPasswordAuthentication() {  
				return new PasswordAuthentication(user,password);  
			}  
		}); 
		props.put("mail.smtp.starttls.enable", "true");
		try {  
			MimeMessage message = new MimeMessage(session);  
			message.setFrom(new InternetAddress(user));  
			message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
			message.setSubject(subject);  
			message.setText(text);
			Transport.send(message);   
		} catch (MessagingException e) {
			e.printStackTrace();
			System.err.println("FAILED to send email to " + to + " with message: " + text);
		}  
	}  
}
