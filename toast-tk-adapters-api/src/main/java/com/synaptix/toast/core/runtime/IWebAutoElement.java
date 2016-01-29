package com.synaptix.toast.core.runtime;

import java.util.List;

import com.synaptix.toast.automation.driver.web.SynchronizedDriver;

public interface IWebAutoElement<E> {
	
	E getWebElement();
	
	void setContainer(IFeedableWebPage abstractWebPage);

	IFeedableWebPage getContainer();

	void setFrontEndDriver(SynchronizedDriver<?, ?> sDvr);

	IWebElement getWrappedElement();

	List<E> getAllWebElements();

}
