package com.synaptix.toast.adapter.web;

import com.synaptix.toast.core.runtime.IWebAutoElement;
import com.synaptix.toast.core.runtime.IWebElement;

public interface IWebElementFactory {

	IWebAutoElement<?> getElement(IWebElement iWebElement);

}
