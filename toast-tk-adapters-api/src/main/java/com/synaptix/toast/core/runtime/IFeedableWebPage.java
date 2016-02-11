package com.synaptix.toast.core.runtime;

public interface IFeedableWebPage extends IWebAutoElement {

	void initElement(final IWebElementDescriptor locator);

	IWebAutoElement<?> getAutoElement(final String fieldName);

	//should add an element
	void addElement(
		final String elementName, 
		final String type, 
		final String method, 
		final String locator, 
		final Integer position
	);
	
	void setDescriptor(final IWebElementDescriptor descriptor);
	
	IWebElementDescriptor getDescriptor();
}