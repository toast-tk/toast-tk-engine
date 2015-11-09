package com.synaptix.toast.adapter.web;

import com.synaptix.toast.automation.driver.web.SynchronizedDriver;

public interface IDrivableWebPage {

	/**
	 * set the driver that will be used by the automation elements
	 */
	public void setDriver(
		SynchronizedDriver sDvr);
}
