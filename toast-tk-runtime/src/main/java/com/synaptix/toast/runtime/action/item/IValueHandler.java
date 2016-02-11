package com.synaptix.toast.runtime.action.item;

import com.google.inject.Injector;
import com.synaptix.toast.runtime.bean.ArgumentDescriptor;

public interface IValueHandler {

	void setInjector(final Injector injector);

	Object handle(final String group, final String argValue) throws Exception;

	void setArgumentDescriptor(final ArgumentDescriptor descriptor);
}