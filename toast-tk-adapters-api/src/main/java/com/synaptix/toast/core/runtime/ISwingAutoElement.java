package com.synaptix.toast.core.runtime;

import com.synaptix.toast.core.driver.IRemoteSwingAgentDriver;

public interface ISwingAutoElement {

	/**
	 * set the remote agent driver to use for current swing element
	 * 
	 * @param sDvr
	 */
	void setFrontEndDriver(final IRemoteSwingAgentDriver sDvr);
}