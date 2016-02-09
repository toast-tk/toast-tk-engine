package com.synaptix.toast.core.runtime;

import com.synaptix.toast.core.driver.IRemoteSwingAgentDriver;

public interface IFeedableSwingPage {

	/**
	 * provide a locator object to init the web element
	 * 
	 * @param locator
	 */
	public void initElement(
		ISwingElementDescriptor locator);

	public void addElement(
		String elementName,
		String type,
		String locator);

	void setDriver(IRemoteSwingAgentDriver sDvr);
}
