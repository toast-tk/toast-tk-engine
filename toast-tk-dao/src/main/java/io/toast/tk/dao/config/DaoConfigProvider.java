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
		this.config = new DaoConfig("localhost", 27017);
	}

	@Override
	public DaoConfig get() {
		return config;
	}
}