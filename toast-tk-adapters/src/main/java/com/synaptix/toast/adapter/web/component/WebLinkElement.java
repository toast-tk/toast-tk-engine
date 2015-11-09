package com.synaptix.toast.adapter.web.component;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.synaptix.toast.adapter.web.HasClickAction;
import com.synaptix.toast.adapter.web.ISyncCall;
import com.synaptix.toast.automation.driver.web.SynchronizedDriver;
import com.synaptix.toast.core.runtime.IWebElement;

public class WebLinkElement extends WebAutoElement implements HasClickAction {

	public WebLinkElement(
		IWebElement element,
		SynchronizedDriver driver) {
		super(element, driver);
	}

	public WebLinkElement(
		IWebElement element) {
		super(element);
	}

	@Override
	public boolean click() {
		safeAction(new ISyncCall() {

			@Override
			public void execute(
				WebElement e) {
				e.click();
			}
		});
		return true;
	}

	@Override
	public void dbClick() {
		safeAction(new ISyncCall() {

			@Override
			public void execute(
				WebElement e) {
				Actions action = new Actions(frontEndDriver.getWebDriver());
				action.doubleClick(e);
				action.perform();
				e.click();
			}
		});
	}
}
