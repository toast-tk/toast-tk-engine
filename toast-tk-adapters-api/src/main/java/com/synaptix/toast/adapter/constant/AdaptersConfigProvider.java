package com.synaptix.toast.adapter.constant;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import com.google.inject.Provider;

public class AdaptersConfigProvider implements Provider<AdaptersConfig> {

	private AdaptersConfig config;

	public AdaptersConfigProvider() {
		super();
		initConfig();
	}

	private void initConfig() {
		Properties p = new Properties();
		try {
			URL resource = AdaptersConfigProvider.class.getClassLoader().getResource("toast.properties");
			p.load(new FileReader(resource.getFile()));
		}
		catch(IOException e) {
		}
		config = new AdaptersConfig();
		String webDriver = p.getProperty("web.driver", "Chrome");
		config.setWebDriver(webDriver);
		String webDriverPath = p.getProperty("web.driver.path");
		config.setWebDriverPath(webDriverPath);
		boolean webDriverssl = Boolean.getBoolean("web.driver.ssl");
		config.setIsSSl(webDriverssl);
	}

	@Override
	public AdaptersConfig get() {
		return config;
	}
}
