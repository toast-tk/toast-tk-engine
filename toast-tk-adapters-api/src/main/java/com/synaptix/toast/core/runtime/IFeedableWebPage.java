package com.synaptix.toast.core.runtime;

public interface IFeedableWebPage {

	/**
	 * provide a locator object to init the web element
	 * 
	 * @param locator
	 */
	public void initElement(IWebElement locator);

	public IWebAutoElement<?> getAutoElement(String fieldName);

	public void addElement(String elementName, String type, String method, String locator, Integer position);
}
