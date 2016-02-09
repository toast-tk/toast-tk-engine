package com.synaptix.toast.adapter.web;

import com.synaptix.toast.core.runtime.IWebAutoElement;
import com.synaptix.toast.core.runtime.IWebElementDescriptor;

public interface IWebComponentFactory {

	IWebAutoElement<?> getElement(IWebElementDescriptor iWebElement);

}
