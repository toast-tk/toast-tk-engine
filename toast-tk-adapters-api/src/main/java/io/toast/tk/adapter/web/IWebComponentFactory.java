package io.toast.tk.adapter.web;

import io.toast.tk.core.runtime.IWebAutoElement;
import io.toast.tk.core.runtime.IWebElementDescriptor;

public interface IWebComponentFactory {

	IWebAutoElement<?> getElement(final IWebElementDescriptor iWebElement);
}