package io.toast.tk.runtime.action.item;

import io.toast.tk.runtime.IActionItemRepository;
import io.toast.tk.runtime.bean.ArgumentDescriptor;

public interface IValueHandler {

	void setRepository(final IActionItemRepository repository);

	Object handle(final String group, final String argValue) throws Exception;

	void setArgumentDescriptor(final ArgumentDescriptor descriptor);
}