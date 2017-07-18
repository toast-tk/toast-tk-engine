package io.toast.tk.runtime.mail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Send email
 */
public class MailSender {

	private static final Logger LOG = LogManager.getLogger(MailSender.class);

	private static final String SMTP_PROPERTIES_FILE_PATH = "/smtp.properties";

	private Session getMailSession() {
		InputStream resourceAsStream = this.getClass().getResourceAsStream(SMTP_PROPERTIES_FILE_PATH);

		final Properties properties = new Properties();
		try (final Reader resourceFileReader = new InputStreamReader(resourceAsStream)) {
			properties.load(resourceFileReader);
		} catch (final IOException e) {
			LOG.error("Could not load smtp.properties file", e);
		}

		if (Boolean.parseBoolean(properties.getProperty
				("mail.smtp.auth"))) {
			return Session.getDefaultInstance(properties, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					String user = properties.getProperty("mail.smtp.user");
					String password = properties.getProperty("mail.smtp.password");
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
