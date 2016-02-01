package com.synaptix.toast.core.runtime;

import java.util.List;

import com.synaptix.toast.automation.driver.web.SynchronizedDriver;

public interface IWebAutoElement<E> {
	
	E getWebElement();
	
	void setContainer(IFeedableWebPage abstractWebPage);

	IFeedableWebPage getContainer();

	void setDriver(SynchronizedDriver<?, ?> sDvr);

	IWebElementDescriptor getDescriptor();

	List<E> getAllWebElements();

}
