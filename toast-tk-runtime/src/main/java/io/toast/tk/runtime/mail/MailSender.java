package io.toast.tk.runtime.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Send email
 */
public class MailSender {

	//private static final Logger LOG = LogManager.getLogger(MailSender.class);

	SmtpConfigProvider config;
	
	private String proxyURL;
	private String proxyPort;

	public MailSender() {
		config = new SmtpConfigProvider();
	}
	
	public MailSender(String host, String port, String user, String pswd) {
		config = new SmtpConfigProvider(host, port, user, pswd);
	}
	
	public void setProxy(String proxyURL, String proxyPort) {
		this.proxyURL = proxyURL;
		this.proxyPort = proxyPort;
	}
	
	private Session getMailSession() {
		config.setProxy(proxyURL, proxyPort);
		Properties properties = config.get();
        
		if (Boolean.parseBoolean(properties.getProperty
				(SmtpConfigProvider.SMTP_PROPERTIES_AUTH))) {
			return Session.getDefaultInstance(properties, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					String user = properties.getProperty(SmtpConfigProvider.SMTP_PROPERTIES_USER);
					String password = properties.getProperty(SmtpConfigProvider.SMTP_PROPERTIES_PASSWORD_NAME);
					return new PasswordAuthentication(user, password);
				}
			});
		} else {
			return Session.getDefaultInstance(properties);
		}
	}

	public void send(String mailSubject, String mailBody, List<String> mailTo, String mailFrom) throws MessagingException {
		send(mailSubject, mailBody,  mailTo, mailFrom, new ArrayList<String>());
	}
	public void send(String mailSubject, String mailBody, List<String> mailTo, String mailFrom, List<String> filePaths) throws MessagingException {
		Session session = getMailSession();
		
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(mailFrom));
		InternetAddress address = new InternetAddress();
		for (String recipient : mailTo) {
			address.setAddress(recipient);
			message.addRecipient(Message.RecipientType.TO, address);
		}
		message.setSubject(mailSubject);
		
		// Create the message part 
		BodyPart messageBodyPart = new MimeBodyPart();

		// Fill the message
		messageBodyPart.setText(mailBody);

		// Create a multipar message
		Multipart multipart = new MimeMultipart();

		// Set text message part
		multipart.addBodyPart(messageBodyPart);

		for(String filePath : filePaths) {
		    // Part two is attachment
		    messageBodyPart = new MimeBodyPart();
		    DataSource source = new FileDataSource(filePath);
		    messageBodyPart.setDataHandler(new DataHandler(source));
		    messageBodyPart.setFileName(filePath);
		    multipart.addBodyPart(messageBodyPart);
		}

		MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap(); 
		mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html"); 
		mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml"); 
		mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain"); 
		mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed"); 
		mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");

		// Send the complete message parts
		message.setContent(multipart);
		
		Transport.send(message);
	}

}
