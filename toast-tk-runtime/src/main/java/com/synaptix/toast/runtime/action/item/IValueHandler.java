package com.synaptix.toast.runtime.action.item;

import com.google.inject.Injector;
import com.synaptix.toast.runtime.bean.ArgumentDescriptor;

public interface IValueHandler {

	void setInjector(Injector injector);

	Object handle(String group, String argValue) throws Exception;

	void setArgumentDescriptor(ArgumentDescriptor descriptor);

}
