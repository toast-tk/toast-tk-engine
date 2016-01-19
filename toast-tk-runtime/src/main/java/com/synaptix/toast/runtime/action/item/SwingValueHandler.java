package com.synaptix.toast.runtime.action.item;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import com.google.gson.Gson;
import com.google.inject.Injector;
import com.synaptix.toast.adapter.swing.SwingAutoElement;
import com.synaptix.toast.adapter.swing.component.DefaultSwingPage;
import com.synaptix.toast.adapter.web.component.DefaultWebPage;
import com.synaptix.toast.adapter.web.component.WebAutoElement;
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

	private SwingAutoElement getPageField(String pageName, String fieldName) {
		DefaultSwingPage page = (DefaultSwingPage) objectRepository.getSwingPage(pageName);
		SwingAutoElement autoElement = page.getAutoElement(fieldName);
		return autoElement;
	}

}
