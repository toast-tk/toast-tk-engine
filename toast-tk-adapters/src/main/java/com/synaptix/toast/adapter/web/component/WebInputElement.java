package com.synaptix.toast.adapter.web.component;

import com.synaptix.toast.adapter.web.HasTextInput;
import com.synaptix.toast.automation.driver.web.SynchronizedDriver;
import com.synaptix.toast.core.runtime.IWebElement;

public class WebInputElement extends WebAutoElement implements HasTextInput {

	public WebInputElement(
		IWebElement element,
		SynchronizedDriver driver) {
		super(element, driver);
	}

	public WebInputElement(
		IWebElement element) {
		super(element);
	}

	@Override
	public void setInput(
		String e) {
		frontEndDriver.find(wrappedElement).sendKeys(e);
	}

	@Override
	public String getValue() {
		return frontEndDriver.find(wrappedElement).getText();
	}
}
