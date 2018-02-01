package io.toast.tk.adapter.constant;


import com.google.common.base.Optional;
import com.google.inject.Provider;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import java.util.Arrays;
import java.nio.file.Paths;
import java.util.Properties;

public class AdaptersConfigProvider implements Provider<AdaptersConfig> {

	private static final Logger LOG = LogManager.getLogger(AdaptersConfigProvider.class);

	private AdaptersConfig config;

	public static final String ADAPTER_WEB_DRIVER = "web.driver";

	public static final String ADAPTER_WEB_DRIVER_PATH = "web.driver.path";

	public static final String ADAPTER_WEB_DRIVER_SSL = "web.driver.ssl";

	public static final String ADAPTER_BROWSER_PATH = "browser.path";

	public static final String ADAPTER_REPORT_FOLDER_PATH = "reports.folder.path";

	public static final String ADAPTER_MAIL_SEND = "mail.send";

	public static final String ADAPTER_MAIL_TO = "mail.to";

	public static final String ADAPTER_MAIL_FROM = "mail.from";

	public AdaptersConfigProvider() {
		initConfig();
	}

	private void initConfig() {
		LOG.info("Initialize configuration from /toast.properties");
		final Properties p = new Properties();

		Optional<InputStream> resourceAsStream = Optional.fromNullable(this.getClass().getResourceAsStream("/toast.properties"));
		if(!resourceAsStream.isPresent()){
			File translations = Paths.get(System.getProperty("user.home") + "/.toast/toast.properties").toFile();
			if(translations.exists()){

				try {
					resourceAsStream = Optional.fromNullable(new FileInputStream(translations));
				} catch (FileNotFoundException e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
		try (final Reader resourceFileReader = new InputStreamReader(resourceAsStream.get())) {
			p.load(resourceFileReader);
		} catch (final IOException e) {
			LOG.error(e.getMessage(), e);
		}
		this.config = new AdaptersConfig(
				p.getProperty(ADAPTER_WEB_DRIVER, "Chrome"),
				p.getProperty(ADAPTER_WEB_DRIVER_PATH, System.getProperty("user.home") + "/.toast/chromedriver.exe"),
				p.getProperty(ADAPTER_BROWSER_PATH),
				Boolean.parseBoolean(p.getProperty(ADAPTER_WEB_DRIVER_SSL)),
				p.getProperty(ADAPTER_REPORT_FOLDER_PATH),
				Arrays.asList(p.getProperty(ADAPTER_MAIL_TO, "test@toast-tk.io").split(";")),
				p.getProperty(ADAPTER_MAIL_FROM, "report@toast-tk.io"),
				Boolean.parseBoolean(p.getProperty(ADAPTER_MAIL_SEND, "false"))
				);
		// Mail reports configuration
		this.config.setMailFrom(p.getProperty(ADAPTER_MAIL_SEND));
		String[] mailRecipients = StringUtils.split(p.getProperty(ADAPTER_MAIL_TO), ",");
		if (mailRecipients != null) {
			this.config.setMailTo(Arrays.asList(mailRecipients));
		}
		this.config.setMailSendReport(Boolean.parseBoolean(p.getProperty(ADAPTER_MAIL_SEND)));
	}

	@Override
	public AdaptersConfig get() {
		return config;
	}
}
