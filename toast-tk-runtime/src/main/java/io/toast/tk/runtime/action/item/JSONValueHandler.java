package io.toast.tk.runtime.action.item;

import com.google.gson.Gson;

import io.toast.tk.runtime.IActionItemRepository;
import io.toast.tk.runtime.bean.ArgumentDescriptor;

public class JSONValueHandler implements IValueHandler {

	private ArgumentDescriptor descriptor;

	private IActionItemRepository objectRepository;

	@Override
	public void setRepository(final IActionItemRepository objectRepository) {
		this.objectRepository = objectRepository;
	}

	@Override
	public Object handle(final String group, final String argValue) throws Exception {
		final Class<?> jsonClazz = Class.forName(descriptor.name);
		final Object value = new Gson().fromJson(argValue, jsonClazz);
		objectRepository.getUserVariables().put(group, value);
		return value;
	}

	@Override
	public void setArgumentDescriptor(final ArgumentDescriptor descriptor) {
		this.descriptor = descriptor;
	}
}