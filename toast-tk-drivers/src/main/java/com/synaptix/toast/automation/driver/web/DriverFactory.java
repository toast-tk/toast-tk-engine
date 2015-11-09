package com.synaptix.toast.automation.driver.web;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

public class DriverFactory {

	private static final Logger LOG = LogManager.getLogger(DriverFactory.class);
	private static final DriverFactory INSTANCE = new DriverFactory();

	private FirefoxDriver firefoxDriver;

	private DriverFactory() {
	}

	public static DriverFactory getFactory() {
		return INSTANCE;
	}

	public InternetExplorerDriver getInternetExplorerDriver() {
		InternetExplorerDriver driver = new InternetExplorerDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return driver;
	}

	public ChromeDriver getChromeDriver() {
		ChromeDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return driver;
	}

	public FirefoxDriver getFirefoxDriver() {
		if(firefoxDriver == null) {
			firefoxDriver = new FirefoxDriver();
			firefoxDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		return firefoxDriver;
	}

	public RemoteWebDriver getRemoteDriver(
		boolean canTakeScreenShots)
		throws MalformedURLException {
		RemoteWebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),
			DesiredCapabilities.firefox());
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		// if(canTakeScreenShots){
		// WebDriver augmentedDriver = new Augmenter().augment(driver);
		// File screenshot = ((TakesScreenshot)
// augmentedDriver).getScreenshotAs(OutputType.FILE);
		// }
		return driver;
	}

	public WebDriver getUiLessWebDriver() {
		MyHtmlDriver driver = new MyHtmlDriver(BrowserVersion.FIREFOX_38);
		driver.setJavascriptEnabled(true);
		driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
		return driver;
	}

	public Selenium getUnwrappedRemoteSelenium(
		String baseUrl) {
		Selenium selenium = new DefaultSelenium("localhost", 4444, "*firefox", baseUrl);
		selenium.start();
		return selenium;
	}

	static class MyHtmlDriver extends HtmlUnitDriver {

		WebClient myClient;

		public MyHtmlDriver() {
			super();
		}

		public MyHtmlDriver(
			BrowserVersion verion) {
			super(verion);
		}

		public WebClient getClient() {
			return myClient;
		}

		@Override
		protected void get(
			URL fullUrl) {
			super.get(fullUrl);
		}

		@Override
		protected WebClient modifyWebClient(
			WebClient client) {
			myClient = client;
			client.getCookieManager().setCookiesEnabled(true);
			client.getOptions().setJavaScriptEnabled(true);
			client.getOptions().setCssEnabled(true);
			client.getOptions().setPopupBlockerEnabled(false);
			client.setIncorrectnessListener(new SilentIncorrectnessListener());
			client.setCssErrorHandler(new QuietCssErrorHandler());
			client.setAjaxController(new NicelyResynchronizingAjaxController());
			return client;
		}
	}
}
