package io.toast.tk.agent.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Provider;



public class WebConfigProvider implements Provider<WebConfig> {
	
	private static final Logger LOG = LogManager.getLogger(WebConfigProvider.class);

	private WebConfig webConfig;

	
	public static final String TOAST_TEST_WEB_INIT_RECORDING_URL = "toast.web.recording.url";

	public static final String TOAST_CHROMEDRIVER_PATH = "toast.chromedriver.path";
	
	public static final String TOAST_TEST_WEB_APP_URL = "toast.webapp.url";

	public static final String TOAST_API_KEY = "toast.api.key";

	public static final String TOAST_PLUGIN_DIR = "toast.api.key";

	public WebConfigProvider() {
		super();
	}

	private void initConfig() throws NullPointerException {
		String userHomepath = WebConfig.getToastHome() + "/";
		Properties p = null;
		p = new Properties();
		try {
			p.load(new FileReader(userHomepath + "toast.web.properties"));
		}
		catch(IOException e) {
			LOG.error(e.getMessage(), e);
		}
		webConfig = new WebConfig();
		webConfig.setWebInitRecordingUrl(p.getProperty(TOAST_TEST_WEB_INIT_RECORDING_URL, "URL to record"));
		webConfig.setChromeDriverPath(p.getProperty(TOAST_CHROMEDRIVER_PATH, userHomepath + "chromedriver.exe"));
		webConfig.setWebAppUrl(p.getProperty(TOAST_TEST_WEB_APP_URL, "Toast WebApp url"));
		webConfig.setApiKey(p.getProperty(TOAST_API_KEY, "Web App Api Key"));
		webConfig.setApiKey(p.getProperty(TOAST_PLUGIN_DIR, webConfig.getPluginDir()));
	}

	@Override
	public WebConfig get() {
		initConfig();
		return webConfig;
	}
}
