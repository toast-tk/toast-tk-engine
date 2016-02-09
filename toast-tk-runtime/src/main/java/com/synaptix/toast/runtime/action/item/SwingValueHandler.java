package com.synaptix.toast.runtime.action.item;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Injector;
import com.synaptix.toast.adapter.swing.component.DefaultSwingPage;
import com.synaptix.toast.core.runtime.ISwingAutoElement;
import com.synaptix.toast.runtime.IActionItemRepository;
import com.synaptix.toast.runtime.bean.ArgumentDescriptor;

public class SwingValueHandler implements IValueHandler{

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
		String[] components = StringUtils.split(group, ".");
		return getPageField(components[0], components[1]);
	}

	@Override
	public void setArgumentDescriptor(ArgumentDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	private ISwingAutoElement getPageField(String pageName, String fieldName) {
		DefaultSwingPage page = (DefaultSwingPage) objectRepository.getSwingPage(pageName);
		ISwingAutoElement autoElement = page.getAutoElement(fieldName);
		return autoElement;
	}

}
