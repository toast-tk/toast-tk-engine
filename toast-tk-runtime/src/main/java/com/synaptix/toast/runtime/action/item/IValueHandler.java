package com.synaptix.toast.runtime.action.item;

import com.synaptix.toast.runtime.IActionItemRepository;
import com.synaptix.toast.runtime.bean.ArgumentDescriptor;

public interface IValueHandler {

	void setRepository(final IActionItemRepository repository);

	Object handle(final String group, final String argValue) throws Exception;

	void setArgumentDescriptor(final ArgumentDescriptor descriptor);
}