package com.synaptix.toast.adapter.constant;

public class AdaptersConfig {

	private final String webDriver;

	private final String webDriverPath;
	
	private final String browserPath;

	private final boolean isSSl;

	public AdaptersConfig(
		final String webDriver,
		final String webDriverPath,
		final String browserPath,
		final boolean isSSl
	) {
		this.webDriver = webDriver;
		this.webDriverPath = webDriverPath;
		this.browserPath = browserPath;
		this.isSSl = isSSl;
	}
	
	public String getWebDriver() {
		return webDriver;
	}

	public String getWebDriverPath() {
		return webDriverPath;
	}
	
	public String getBrowserPath() {
		return browserPath;
	}

	public boolean getIsSSl() {
		return isSSl;
	}
}