package com.synaptix.toast.adapter.web.component;

import com.synaptix.toast.adapter.web.HasStringValue;
import com.synaptix.toast.adapter.web.HasTextInput;
import com.synaptix.toast.automation.driver.web.SynchronizedDriver;
import com.synaptix.toast.core.report.TestResult;
import com.synaptix.toast.core.runtime.IWebElement;

public class WebInputElement extends WebAutoElement implements HasTextInput, HasStringValue {

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
	public TestResult setInput(
		String e) {
		frontEndDriver.find(wrappedElement).sendKeys(e);
		return null;
	}

	@Override
	public String getValue() {
		return frontEndDriver.find(wrappedElement).getText();
	}

}
