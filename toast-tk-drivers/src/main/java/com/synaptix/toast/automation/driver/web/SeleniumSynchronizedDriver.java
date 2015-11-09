package com.synaptix.toast.automation.driver.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.synaptix.toast.automation.api.IMiniResult;
import com.synaptix.toast.core.runtime.IWebElement;

public class SeleniumSynchronizedDriver implements SynchronizedDriver {

	private final WebDriver driver;

	private int defaultRepeat = 5;

	private int defaultTimeout = 1000;

	public SeleniumSynchronizedDriver() {
		this.driver = DriverFactory.getFactory().getFirefoxDriver();
		// GetEval("selenium.browserbot.setShouldHighlightElement(true)");
	}

	public SeleniumSynchronizedDriver(
		WebDriver driver,
		int defaultRepeat,
		int defaultTimeout) {
		this.driver = driver;
		if(defaultRepeat > -1) {
			this.defaultRepeat = defaultRepeat;
		}
		if(defaultTimeout > -1) {
			this.defaultTimeout = defaultTimeout;
		}
	}

	public SeleniumSynchronizedDriver(
		WebDriver driver) {
		this.driver = driver;
	}

	public WebElement doSynchronizedSelection(
		final IWebElement element) {
		SeleniumHelper.waitCondition(defaultRepeat, defaultTimeout, new IMiniResult() {

			@Override
			public boolean result() {
				return SeleniumHelper.positionSelect(driver, element) != null;
			}
		});
		WebElement positionSelect = SeleniumHelper.positionSelect(driver, element);
		return positionSelect;
	}

	public void open(
		String demoLoginUrl) {
		driver.get(demoLoginUrl);
	}

	@Override
	public WebDriver getWebDriver() {
		return driver;
	}

	public void doSynchronizedOnUrl(
		final String urlFragment) {
		SeleniumHelper.waitCondition(defaultRepeat, defaultTimeout, new IMiniResult() {

			@Override
			public boolean result() {
				return driver.getCurrentUrl().contains(urlFragment);
			}
		});
	}

	// In Selenium RC, you can use Highlight(locator),
	// which will locate the element and highlight it,
	// or you can use
// GetEval("selenium.browserbot.setShouldHighlightElement(true)"),
	// which turns on the automatic highlighting every time an element is
// located
	// (the behavior you know from Selenium IDE).
	@Override
	public WebElement find(
		IWebElement element) {
		WebElement doSynchronizedSelection = doSynchronizedSelection(element);
		return doSynchronizedSelection;
	}
}
