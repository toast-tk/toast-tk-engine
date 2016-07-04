package io.toast.tk.runtime.action.item;

import org.apache.commons.lang3.StringUtils;

import io.toast.tk.adapter.swing.component.DefaultSwingPage;
import io.toast.tk.core.runtime.ISwingAutoElement;
import io.toast.tk.runtime.IActionItemRepository;
import io.toast.tk.runtime.bean.ArgumentDescriptor;

public class SwingValueHandler implements IValueHandler{

	private ArgumentDescriptor descriptor;

	private IActionItemRepository objectRepository;

	@Override
	public void setRepository(final IActionItemRepository objectRepository) {
		this.objectRepository = objectRepository;
	}

	@Override
	public Object handle(final String group, final String argValue) throws Exception {
		String[] components = StringUtils.split(group, ".");
		return getPageField(components[0], components[1]);
	}

	@Override
	public void setArgumentDescriptor(final ArgumentDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	private ISwingAutoElement getPageField(final String pageName, final String fieldName) {
		final DefaultSwingPage page = (DefaultSwingPage) objectRepository.getSwingPage(pageName);
		final ISwingAutoElement autoElement = page.getAutoElement(fieldName);
		return autoElement;
	}
}