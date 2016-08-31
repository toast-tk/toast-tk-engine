package io.toast.tk.adapter.constant;

public class AdaptersConfig {

	private final String webDriver;

	private final String webDriverPath;

	private final String browserPath;

	private String reportsFolderPath;

	private final boolean isSSl;

	public AdaptersConfig(
			final String webDriver,
			final String webDriverPath,
			final String browserPath,
			final boolean isSSl,
			final String reportsFolderPath
	) {
		this.webDriver = webDriver;
		this.webDriverPath = webDriverPath;
		this.browserPath = browserPath;
		this.isSSl = isSSl;
		this.reportsFolderPath = reportsFolderPath;
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

	public String getReportsFolderPath() {
		return reportsFolderPath;
	}
}