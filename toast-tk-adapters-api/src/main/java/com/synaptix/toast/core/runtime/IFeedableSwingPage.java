package com.synaptix.toast.core.runtime;

public interface IFeedableSwingPage {

	/**
	 * provide a locator object to init the web element
	 * 
	 * @param locator
	 */
	public void initElement(
		ISwingElement locator);

	public void addElement(
		String elementName,
		String type,
		String locator);
}
