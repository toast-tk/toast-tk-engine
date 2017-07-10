package io.toast.tk.runtime.mail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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

	public void send(String mailSubject, String mailBody, List<String> mailTo, String mailFrom) {
		Session session = getMailSession();
		
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailFrom));
			InternetAddress address = new InternetAddress();
			for (String recipient : mailTo) {
				address.setAddress(recipient);
				message.addRecipient(Message.RecipientType.TO, address);
			}
			message.setSubject(mailSubject);
			message.setText(mailBody);

			Transport.send(message);
		} catch (MessagingException e) {
			LOG.error(e.getMessage(), e);
		}
	}

}
