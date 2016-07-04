package io.toast.tk.adapter.constant;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Provider;

public class AdaptersConfigProvider implements Provider<AdaptersConfig> {

	private static final Logger LOG = LogManager.getLogger(AdaptersConfigProvider.class);
	
	private AdaptersConfig config;

	public AdaptersConfigProvider() {
		initConfig();
	}

	private void initConfig() {
		final Properties p = new Properties();
		final URL resource = AdaptersConfigProvider.class.getClassLoader().getResource("toast.properties");
		try(final FileReader resourceFileReader = new FileReader(resource.getFile());) {
			p.load(resourceFileReader);
		}
		catch(final IOException e) {
			LOG.error(e.getMessage(), e);
		}
		this.config = new AdaptersConfig(p.getProperty("web.driver", "Chrome"), p.getProperty("web.driver.path"), p.getProperty("browser.path"), Boolean.getBoolean("web.driver.ssl"));
	}

	@Override
	public AdaptersConfig get() {
		return config;
	}
}