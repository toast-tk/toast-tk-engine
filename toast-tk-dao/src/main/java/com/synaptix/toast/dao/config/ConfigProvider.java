package com.synaptix.toast.dao.config;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import com.google.inject.Provider;

public class ConfigProvider implements Provider<Config> {

	private Config config;

	public ConfigProvider() {
		super();
		initConfig();
	}

	private void initConfig() {
		Properties p = new Properties();
		try {
			URL resource = ConfigProvider.class.getClassLoader().getResource("config.properties");
			p.load(new FileReader(resource.getFile()));
		}
		catch(IOException e) {
		}
		config = new Config();
		String mongDbPortProperty = p.getProperty("config.mongo.port", "27017");
		config.setMongoPort(Integer.valueOf(27017));
		String mongDbHostProperty = p.getProperty("config.mongo.host", "localhost");
		config.setMongoServer("localhost");
	}

	@Override
	public Config get() {
		return config;
	}
}
