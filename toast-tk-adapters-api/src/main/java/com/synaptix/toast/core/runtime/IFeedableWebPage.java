package com.synaptix.toast.core.runtime;


public interface IFeedableWebPage extends IWebAutoElement {

	public void initElement(IWebElementDescriptor locator);

	public IWebAutoElement<?> getAutoElement(String fieldName);

	public void addElement(String elementName, String type, String method, String locator, Integer position);
	
	public void setDescriptor(IWebElementDescriptor descriptor);
	
	public IWebElementDescriptor getDescriptor();
}
