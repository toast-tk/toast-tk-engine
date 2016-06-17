package com.synaptix.toast.runtime.action.item;


import com.synaptix.toast.runtime.IActionItemRepository;
import com.synaptix.toast.runtime.bean.ArgumentDescriptor;

public class StringValueHandler implements IValueHandler{

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
	public void setRepository(final IActionItemRepository objectRepository) {
		this.objectRepository = objectRepository;
	}
}