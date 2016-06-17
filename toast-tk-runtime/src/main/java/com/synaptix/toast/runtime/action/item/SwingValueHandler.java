package com.synaptix.toast.runtime.action.item;

import org.apache.commons.lang3.StringUtils;

import com.synaptix.toast.adapter.swing.component.DefaultSwingPage;
import com.synaptix.toast.core.runtime.ISwingAutoElement;
import com.synaptix.toast.runtime.IActionItemRepository;
import com.synaptix.toast.runtime.bean.ArgumentDescriptor;

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