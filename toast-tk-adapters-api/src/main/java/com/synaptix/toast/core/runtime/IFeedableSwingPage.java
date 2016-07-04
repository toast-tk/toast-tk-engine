package com.synaptix.toast.core.runtime;

import com.synaptix.toast.core.driver.IRemoteSwingAgentDriver;

public interface IFeedableSwingPage {

	/**
	 * provide a locator object to init the web element
	 * 
	 * @param locator the element descriptor
	 */
	void initElement(final ISwingElementDescriptor locator);

	/**
	 * Add an element to the repository
	 * 
	 * @param elementName element name
	 * @param type element type (button, link,..)
	 * @param locator html locator
	 */
	public void addElement(
		final String elementName,
		final String type,
		final String locator
	);

	/**
	 * set the driver for current page
	 * 
	 * @param driver the driver
	 */
	void setDriver(final IRemoteSwingAgentDriver driver);
}