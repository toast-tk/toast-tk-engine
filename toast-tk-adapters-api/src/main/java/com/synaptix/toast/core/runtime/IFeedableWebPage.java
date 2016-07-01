package com.synaptix.toast.core.runtime;

public interface IFeedableWebPage extends IWebAutoElement {

	/**
	 * init current element with its repository information
	 * 
	 * @param locator
	 */
	void initElement(final IWebElementDescriptor locator);

	/**
	 * Get the selenium wrapper element for a field included in current page
	 * 
	 * @param fieldName
	 * @return
	 */
	IWebAutoElement<?> getAutoElement(final String fieldName);

	/**
	 * Add an element to the repository
	 * 
	 * @param elementName
	 * @param type
	 * @param method
	 * @param locator
	 * @param position
	 */
	void addElement(
		final String elementName, 
		final String type, 
		final String method, 
		final String locator, 
		final Integer position
	);
	
	/**
	 * set the element descriptor
	 * 
	 * @param descriptor
	 */
	void setDescriptor(final IWebElementDescriptor descriptor);
	
	/**
	 * get the element descriptor
	 * 
	 */
	IWebElementDescriptor getDescriptor();
}