package com.synaptix.toast.runtime.action.item;

import com.google.inject.Injector;
import com.synaptix.toast.runtime.IActionItemRepository;
import com.synaptix.toast.runtime.bean.ArgumentDescriptor;

public class StringValueHandler implements IValueHandler{

	private Injector injector;

	private ArgumentDescriptor descriptor;

	private IActionItemRepository objectRepository;


	@Override
	public Object handle(final String group, final String argValue) {
		return objectRepository.getUserVariables().getOrDefault(group, argValue);
	}

	@Override
	public void setArgumentDescriptor(final ArgumentDescriptor descriptor) {
		this.descriptor = descriptor;
	}
	
	@Override
	public void setInjector(final Injector injector) {
		this.injector = injector;
		this.objectRepository = injector.getInstance(IActionItemRepository.class);
	}
}