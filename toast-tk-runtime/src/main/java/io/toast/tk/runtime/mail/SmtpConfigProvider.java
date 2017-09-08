package io.toast.tk.runtime.mail;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.Security;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.toast.tk.runtime.constant.Property;
import io.toast.tk.runtime.utils.EncryptHelper;

public class SmtpConfigProvider {

	private static final Logger LOG = LogManager.getLogger(SmtpConfigProvider.class);

	private String proxyURL;
	private String proxyPort;
	
	private Properties properties;
	
	public static final String SMTP_PROPERTIES_FILE_NAME = "smtp.properties";
	public static final String SMTP_PROPERTIES_FILE_STREAM_PATH = Property.PATH_DELIMITER + SMTP_PROPERTIES_FILE_NAME;
	public static final String SMTP_PROPERTIES_FILE_PATH = Property.TOAST_HOME_DIR + SMTP_PROPERTIES_FILE_NAME;
	
	public static final String SMTP_PROPERTIES_HOST = "mail.smtp.host";

	public static final String SMTP_PROPERTIES_PORT = "mail.smtp.port";

	public static final String SMTP_PROPERTIES_TTLS_ENABLE = "mail.smtp.starttls.enable";

	public static final String SMTP_PROPERTIES_AUTH = "mail.smtp.auth";

	public static final String SMTP_PROPERTIES_USER = "mail.smtp.user";

	public static final String SMTP_PROPERTIES_PASSWORD_NAME = "mail.smtp.password";
	
	public SmtpConfigProvider() {
		properties = new Properties();
		this.init();
	}

	public SmtpConfigProvider(String host, String port, String user, String pswd) {
		properties = new Properties();
		this.init(host, port, user, pswd);
	}
	
	public void setProxy(String proxyURL, String proxyPort) {
		this.proxyURL = proxyURL;
		this.proxyPort = proxyPort;
	}
	
	public void init() {
		InputStream resourceAsStream = this.getClass().getResourceAsStream(SMTP_PROPERTIES_FILE_STREAM_PATH);

		if(resourceAsStream == null) {
			try {
				properties.load(new FileReader(SMTP_PROPERTIES_FILE_PATH));
			}
			catch(IOException e) {
				LOG.error(e.getMessage(), e);
			}
		} else {
			try (final Reader resourceFileReader = new InputStreamReader(resourceAsStream)) {
				properties.load(resourceFileReader);
			} catch (final IOException e) {
				LOG.error("Could not load smtp.properties file", e);
			}
		}

		properties.put(SMTP_PROPERTIES_PASSWORD_NAME, EncryptHelper.decrypt((String) properties.get(SMTP_PROPERTIES_PASSWORD_NAME)));

		setProperty(properties);
	}
	
	public void init(String host, String port, String user, String pswd) {
		properties.setProperty(SMTP_PROPERTIES_HOST, host);
		properties.setProperty(SMTP_PROPERTIES_PORT, port);
		if(user != null && !"".equals(user)) {
			properties.setProperty(SMTP_PROPERTIES_AUTH, "true");
			properties.setProperty(SMTP_PROPERTIES_USER, user);
			properties.setProperty(SMTP_PROPERTIES_PASSWORD_NAME, pswd);
		}

		setProperty(properties);
	}
	
	public final Properties get() {
		return properties;
	}
	
	@SuppressWarnings("restriction")
	private void setProperty(Properties properties) {

        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		if(proxyURL != null) {
	        properties.setProperty("proxySet","true");
	        properties.setProperty("socksProxyHost",proxyURL);
	        properties.setProperty("socksProxyPort",proxyPort);
	        properties.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
	        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
	        properties.setProperty("mail.smtp.socketFactory.port", "465");
	        properties.setProperty("mail.store.protocol", "pop3");
	        properties.setProperty("mail.transport.protocol", "smtp");
		}

        properties.put("mail.debug", "false");
	}
}
