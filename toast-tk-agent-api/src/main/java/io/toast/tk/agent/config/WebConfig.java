package io.toast.tk.agent.config;

import org.apache.commons.lang.SystemUtils;

public class WebConfig {


	public static final String TOAST_PROPERTIES_FILE = getToastHome() + SystemUtils.FILE_SEPARATOR + "toast.web.properties";

	public static final String TOAST_LOG_DIR = getToastHome() + SystemUtils.FILE_SEPARATOR + "log.web";
		
	private String webStartRecordingUrl;

	private String chromeDriverPath;

	private String webAppUrl;
	
	private String apiKey;
	
	private String pluginDir;
	
	public static String getToastHome() {
		return System.getProperty("user.home") + SystemUtils.FILE_SEPARATOR + ".toast";
	}

	public String getLogDir() {
		return getToastHome() + "log/";
	}

	public String getPluginDir() {
		return this.pluginDir == null ? getToastHome() + SystemUtils.FILE_SEPARATOR + "plugins/": this.pluginDir;
	}

	public void setWebInitRecordingUrl(
		String webStartRecordingUrl) {
		this.webStartRecordingUrl = webStartRecordingUrl;
	}

	public String getWebInitRecordingUrl() {
		return this.webStartRecordingUrl;
	}
	
	public void setChromeDriverPath(
		String chromeDriverPath) {
		this.chromeDriverPath = chromeDriverPath;
	}

	public String getChromeDriverPath() {
		return this.chromeDriverPath;
	}
	
	
	public void setWebAppUrl(
		String webAppUrl) {
		this.webAppUrl = webAppUrl;
	}

	public String getWebAppUrl() {
		return this.webAppUrl;
	}

	public void setApiKey(
		String apikey) {
		this.apiKey = apikey;
	}

	public String getApiKey() {
		return this.apiKey;
	}
	
	public void setPluginDir(String pluginDir) {
		this.pluginDir = pluginDir;
	}
}
