package io.toast.tk.adapter.swing;

import io.toast.tk.core.runtime.ISwingAutoElement;
import io.toast.tk.core.runtime.ISwingElementDescriptor;

public interface ISwingComponentFactory {

	ISwingAutoElement getElement(final ISwingElementDescriptor iSwingComponent);
}