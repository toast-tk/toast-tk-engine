package com.synaptix.toast.adapter.constant;

public class AdaptersConfig {

	private final String webDriver;

	private final String webDriverPath;

	private final boolean isSSl;

	public AdaptersConfig(
		final String webDriver,
		final String webDriverPath,
		final boolean isSSl
	) {
		this.webDriver = webDriver;
		this.webDriverPath = webDriverPath;
		this.isSSl = isSSl;
	}
	
	public String getWebDriver() {
		return webDriver;
	}

	public String getWebDriverPath() {
		return webDriverPath;
	}

	public boolean getIsSSl() {
		return isSSl;
	}
}