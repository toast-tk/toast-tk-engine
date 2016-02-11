package com.synaptix.toast.adapter.swing;

import com.synaptix.toast.core.runtime.ISwingAutoElement;
import com.synaptix.toast.core.runtime.ISwingElementDescriptor;

public interface ISwingComponentFactory {

	ISwingAutoElement getElement(final ISwingElementDescriptor iSwingComponent);
}