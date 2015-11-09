package com.synaptix.toast.adapter.web.component;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.synaptix.toast.adapter.web.HasSelect;
import com.synaptix.toast.automation.driver.web.SynchronizedDriver;
import com.synaptix.toast.core.runtime.IWebElement;

public class WebSelectElement extends WebAutoElement implements HasSelect {

	public WebSelectElement(
		IWebElement element,
		SynchronizedDriver driver) {
		super(element, driver);
	}

	public WebSelectElement(
		IWebElement element) {
		super(element);
	}

	@Override
	public void selectByValue(
		String value) {
		WebElement find = frontEndDriver.find(wrappedElement);
		Select realSelect = new Select(find);
		realSelect.selectByValue(value);
	}

	@Override
	public void selectByIndex(
		int index) {
		WebElement find = frontEndDriver.find(wrappedElement);
		Select realSelect = new Select(find);
		realSelect.selectByIndex(index);
	}
}
