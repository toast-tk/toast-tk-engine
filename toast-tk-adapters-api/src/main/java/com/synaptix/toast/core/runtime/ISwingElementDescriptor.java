package com.synaptix.toast.core.runtime;

import com.synaptix.toast.core.adapter.AutoSwingType;

public interface ISwingElementDescriptor {

	String getName();

	String getLocator();

	AutoSwingType getType();
}