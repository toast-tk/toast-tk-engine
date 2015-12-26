package com.synaptix.toast.runtime.action.item;

import com.google.gson.Gson;
import com.google.inject.Injector;
import com.synaptix.toast.runtime.IActionItemRepository;
import com.synaptix.toast.runtime.bean.ArgumentDescriptor;

public class JSONValueHandler implements IValueHandler{

	private Injector injector;
	private ArgumentDescriptor descriptor;
	private IActionItemRepository objectRepository;

	@Override
	public void setInjector(Injector injector) {
		this.injector = injector;
		this.objectRepository = injector.getInstance(IActionItemRepository.class);
	}

	@Override
	public Object handle(String group, String argValue) throws Exception {
		Class<?> jsonClazz = Class.forName(descriptor.name);
		Object value = new Gson().fromJson(argValue, jsonClazz);
		objectRepository.getUserVariables().put(group, value);
		return value;
	}

	@Override
	public void setArgumentDescriptor(ArgumentDescriptor descriptor) {
		this.descriptor = descriptor;
	}

}
