package com.synaptix.toast.core.runtime;

import com.synaptix.toast.automation.driver.web.SynchronizedDriver;

public interface IWebAutoElement<E> {
	
	E getWebElement();

	void setFrontEndDriver(SynchronizedDriver<?, ?> sDvr);
}
