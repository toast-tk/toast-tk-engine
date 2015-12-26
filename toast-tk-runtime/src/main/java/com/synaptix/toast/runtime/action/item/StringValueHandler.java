package com.synaptix.toast.runtime.action.item;

import com.google.inject.Injector;
import com.synaptix.toast.runtime.bean.ArgumentDescriptor;

public class StringValueHandler implements IValueHandler{

	private Injector injector;
	private ArgumentDescriptor descriptor;


	@Override
	public Object handle(String group, String argValue) {
		return argValue;
	}

	@Override
	public void setArgumentDescriptor(ArgumentDescriptor descriptor) {
		this.descriptor = descriptor;
	}
	
	@Override
	public void setInjector(Injector injector) {
		this.injector = injector;
	}
}
