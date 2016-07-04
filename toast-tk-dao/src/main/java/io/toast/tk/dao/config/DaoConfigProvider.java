package io.toast.tk.dao.config;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Provider;

public class DaoConfigProvider implements Provider<DaoConfig> {

	private static final Logger LOG = LogManager.getLogger(DaoConfigProvider.class);
	
	private DaoConfig config;

	public DaoConfigProvider() {
		initConfig();
	}

	private void initConfig() {
		final Properties p = new Properties();
		final URL resource = DaoConfigProvider.class.getClassLoader().getResource("config.properties");
		try(final FileReader fileReader = new FileReader(resource.getFile());) {
			p.load(fileReader);
		}
		catch(final IOException e) {
			LOG.error(e.getMessage(), e);
		}
		this.config = new DaoConfig("localhost", 27017);
	}

	@Override
	public DaoConfig get() {
		return config;
	}
}