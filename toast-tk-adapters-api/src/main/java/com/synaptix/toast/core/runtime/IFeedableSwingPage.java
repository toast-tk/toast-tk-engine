package com.synaptix.toast.core.runtime;

import com.synaptix.toast.core.driver.IRemoteSwingAgentDriver;

public interface IFeedableSwingPage {

	/**
	 * provide a locator object to init the web element
	 * 
	 * @param locator
	 */
	void initElement(final ISwingElementDescriptor locator);

	public void addElement(
		final String elementName,
		final String type,
		final String locator
	);

	void setDriver(final IRemoteSwingAgentDriver sDvr);
}