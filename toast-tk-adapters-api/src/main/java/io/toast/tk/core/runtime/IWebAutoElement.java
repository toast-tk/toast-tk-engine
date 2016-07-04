package io.toast.tk.core.runtime;

import java.util.List;

import io.toast.tk.automation.driver.web.SynchronizedDriver;

public interface IWebAutoElement<E> {
	
	E getWebElement();
	
	void setContainer(final IFeedableWebPage abstractWebPage);

	IFeedableWebPage getContainer();

	void setDriver(final SynchronizedDriver<?, ?> sDvr);

	IWebElementDescriptor getDescriptor();

	List<E> getAllWebElements();

	List<IWebElementDescriptor> getChildren();
}