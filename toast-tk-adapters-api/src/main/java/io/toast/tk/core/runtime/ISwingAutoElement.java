package io.toast.tk.core.runtime;

import io.toast.tk.core.driver.IRemoteSwingAgentDriver;

public interface ISwingAutoElement {

	/**
	 * set the remote agent driver to use for current swing element
	 * 
	 * @param sDvr
	 */
	void setFrontEndDriver(final IRemoteSwingAgentDriver sDvr);
}