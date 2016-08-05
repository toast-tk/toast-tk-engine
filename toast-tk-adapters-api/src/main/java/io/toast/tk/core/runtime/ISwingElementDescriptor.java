package io.toast.tk.core.runtime;

import io.toast.tk.core.adapter.AutoSwingType;

public interface ISwingElementDescriptor {

	String getName();

	String getLocator();

	AutoSwingType getType();
}